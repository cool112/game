package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.SkillHarmSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AbstractEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.RatioUitl;

/**
 * 技能伤害事件处理
 * @author seg
 *
 */
public class SkillHarmSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.SKILL_HARM)
			return;
		if (event.getSource() == null || event.getSource().getObjectType() != GameObjectType.SKILL_EFFECT)
			return;
		EffectEntity attackEffect = (EffectEntity) event.getSource();
		HavingSkill attacker = attackEffect.getCaster();
		Troop target = (Troop) event.getTarget();
		int damage = ((SkillHarmSkillEvent)event).getDamage();
		
		List<EffectEntity> attackEffects = attacker.getAttachEffects().getTriggerEffect(TriggerEventType.SKILL_HARM);
		for(EffectEntity effect : attackEffects)
		{
			EffectPrototype prototype = effect.getEffectPrototype();
			if(prototype instanceof AbstractEffectTriggerPrototype)
			{
				AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
				triggerPrototype.handleEvent(event, effect, currTime);
			}
		}
		
		calculateTargetState((GameObject) attacker, target, event, currTime);
		doDamage(attackEffect, (GameObject) attacker, target, damage, currTime);

	}
	
	/**
	 * 造成伤害
	 * @param attackEffect
	 * @param attacker
	 * @param target
	 * @param damage
	 * @param currTime
	 */
	private void doDamage(EffectEntity attackEffect, GameObject attacker, Troop target, int damage, long currTime)
	{
		HpUpdateType updateType = HpUpdateType.SKILL_ATTACK;
		
		// 当次攻击 目标是否 闪避, 格挡
		boolean isDodge = target.getFightStates().isAttackDodge();
		
		if(isDodge)
		{
			updateType = HpUpdateType.AVOID;
			HpAction action = new HpAction(target, attacker, (int) 0, attackEffect.getObjectId(), attackEffect.getPrototypeId(), updateType);
			target.addAction(action);
			return;
		}
		
		FightEngine.reduceHP(attacker, target, attackEffect.getObjectId(), attackEffect.getPrototypeId(), damage, updateType);
	}

	/**
	 * 计算下次目标的状态
	 * @param attack
	 * @param target
	 * @param currTime 
	 * @param event 
	 * @create	2015年8月13日	darren.ouyang
	 */
	private void calculateTargetState (GameObject attack, Troop target, AbstractSkillEvent event, long currTime)
	{
		// 清理之前状态
		target.getFightStates().setAttackDodge(false);
		
		List<EffectEntity> targetEffects = target.getAttachEffects().getTriggerEffect(TriggerEventType.BE_SKILL_HARM);
		for(EffectEntity effect : targetEffects)
		{
			EffectPrototype prototype = effect.getEffectPrototype();
			if(prototype instanceof AbstractEffectTriggerPrototype)
			{
				AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
				triggerPrototype.handleEvent(event, effect, currTime);
			}
		}
			
		float attrDodge = target.getDodge();
		attrDodge	= (attrDodge > 0.8f) ? 0.8f : attrDodge;
		float hitRT =  1f - attrDodge;
		if (hitRT <= 0f || !RatioUitl.isSuccess(hitRT))
			target.getFightStates().setAttackDodge(true);
		
	}
	
}
