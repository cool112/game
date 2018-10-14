package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import java.util.List;

import com.yeto.war.config.FightConfig;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.state.FightMetaState;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AbstractEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.RatioUitl;

/**
 * 普通攻击事件处理
 * @author seg
 * 2017年3月24日
 */
public class AttackSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.ATTACK)
			return;
		if (event.getSource() == null || event.getSource().getObjectType() != GameObjectType.SKILL_EFFECT)
			return;
		EffectEntity attackEffect = (EffectEntity) event.getSource();
		Troop attacker = attackEffect.getCaster();
		Troop target = (Troop) event.getTarget();
		
		List<EffectEntity> attackEffects = attacker.getAttachEffects().getTriggerEffect(TriggerEventType.ATTACK_EVENT);
		for(EffectEntity effect : attackEffects)
		{
			EffectPrototype prototype = effect.getEffectPrototype();
			if(prototype instanceof AbstractEffectTriggerPrototype)
			{
				AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
				triggerPrototype.handleEvent(event, effect, currTime);
			}
		}
		
		calculateNextTargetState(attacker, target, event, currTime);
		boolean isCritical = attacker.getFightStates().isAttackCritical();
		hurtTarget(attackEffect, attacker, target, isCritical, currTime);
	}

	/**
	 * 计算下次目标的状态
	 * @param attack
	 * @param target
	 * @param currTime 
	 * @param event 
	 * @create	2015年8月13日	darren.ouyang
	 */
	private void calculateNextTargetState (Troop attack, Troop target, AbstractSkillEvent event, long currTime)
	{
		// 清理之前状态
		target.getFightStates().setAttackDodge(false);
		target.getFightStates().setAttackBlock(false);
		target.getFightStates().setEffectBlock(false);
		
		List<EffectEntity> targetEffects = target.getAttachEffects().getTriggerEffect(TriggerEventType.BE_ATTACK_EVENT);
		for(EffectEntity effect : targetEffects)
		{
			EffectPrototype prototype = effect.getEffectPrototype();
			if(prototype instanceof AbstractEffectTriggerPrototype)
			{
				AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
				triggerPrototype.handleEvent(event, effect, currTime);
			}
		}
			
		// 计算下次目标是否闪避 命中率=额外命中率+0.6-闪避率（命中率≤100%）
		float attrDodge = target.getDodge();
		attrDodge	= (attrDodge > 0.8f) ? 0.8f : attrDodge;
		float hitRT =  1f - attrDodge;
		if (hitRT <= 0f || !RatioUitl.isSuccess(hitRT))
			target.getFightStates().setAttackDodge(true);
		
		// 计算下次目标者是否格挡
		float blockRT = target.getBlock();
		blockRT = (blockRT > 0.8f) ? 0.8f : blockRT;	// 极限几率
		if (blockRT >=1f || (blockRT>0f && RatioUitl.isSuccess(blockRT)))
			target.getFightStates().setAttackBlock(true);
		
	}

	/**
	 * 伤害单个对象
	 * @param attack
	 * @param target
	 * @param physicalInstanceID
	 * @param attackEffectId
	 * @param isCritical
	 * @return
	 */
	private void hurtTarget (EffectEntity effect, Troop attack, Troop target, boolean isCritical, long currTime)
	{
//		if(attack.getModelID() == 10206)
//			System.err.println("troop attack");
		
		int metaState = target.getFightStates().getMetaStates();
		HpUpdateType updateType = HpUpdateType.SKILL_ATTACK;
		if((metaState & FightMetaState.IMMUNE_DAMAGE) > 0)
			return;
		
		// 当次攻击 目标是否 闪避, 格挡
		boolean isDodge = target.getFightStates().isAttackDodge();
		
		if(isDodge)
		{
			HpAction action = new HpAction(target, attack, (int) 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.AVOID);
			target.addAction(action);
			return;
		}
		
		float reduceHp = 0;
		float damageOneFlag = attack.getAttrModList().getUnitModifiedTotal(ModifiedType.DAMAGE_TO_ONE, target, effect);
		if(damageOneFlag > 0)
		{
			reduceHp = 1;
		}
		else
		{
			//计算出伤害修正与正常伤害值
			float attackValue = physicalAttackColculate(attack, target);
			
			// 暴击伤害  暴击伤害=攻击力*伤害修正*暴击伤害加成系数
			float critValue = 0f;
			if (isCritical)
			{
				float critHarm = target.getCritHarm();
				critValue = attackValue * critHarm;
				updateType = HpUpdateType.CRITICAL;
			}
			
			// 是否格挡 格挡伤害=（攻击力*伤害修正+暴击伤害）*格挡伤害减免系数[被攻击者]
			float blockValue = 0f;
			float blockHarm = 0f;
			boolean isBlock = target.getFightStates().isAttackBlock();
			if (isBlock)
			{
				// 技能对格挡伤害加成
				float skillBlockHarm = target.getAttrModList().getUnitModifiedTotal(ModifiedType.BLOCK_ADD, attack, effect);
				blockHarm = target.getBlockHarm() + skillBlockHarm;
				updateType = HpUpdateType.BLOCK;
			}
			
			boolean isEffectBlock = target.getFightStates().isEffectBlock();
			if (isEffectBlock)
			{
				float skillBlockHarm = target.getAttrModList().getUnitModifiedTotal(ModifiedType.EFFECT_BLOCK_ADD, attack, effect);
				blockHarm += skillBlockHarm;
				updateType = HpUpdateType.BLOCK;
			}
			
			blockHarm = (blockHarm >= 1f) ? 1f : blockHarm;
			blockValue = (attackValue + critValue) * blockHarm;

			// 攻击指定对象伤害加成
			float harmAddition = attack.getAttrModList().getUnitModifiedTotal(ModifiedType.HARM_ADD, target, effect);
			// 被攻击时受到伤害加成(如:诅咒)
			float beAttackHarmAdd =target.getAttrModList().getUnitModifiedTotal(ModifiedType.BE_HARM_ADD, attack, effect);
//			if(target.getModelID() == 20006)
//				System.err.println("be harmd percent : " + beAttackHarmAdd);
			
			//计算免伤, 没有免伤值 总伤害*(1-免伤百分比)
			float drpt = target.getDRPT();
			drpt = (drpt>=1f) ? 1f : drpt;
			reduceHp = (attackValue + critValue - blockValue) * (1f - drpt ) * (1 + harmAddition) * (1 + beAttackHarmAdd);
		}
			
		//大于0则扣血
		if(reduceHp < 0f)
			reduceHp = 0f;
		
		FightEngine.reduceHP(attack, target, effect.getObjectId(), effect.getPrototypeId(), (int) reduceHp, updateType);
		
	}

	/**
	 * 计算物理伤害值
	 * @param attack
	 * @param target
	 * @return
	 */
	private float physicalAttackColculate (Troop attack, Troop target)
	{
		float attackAp = (float)attack.getAttack();
		
		float attrAttack = attack.getControl().getAttrAttack();
		float attrDefense = target.getControl().getAttrDefense();
		
		float attackValue = attackAp * (1f+ attrAttack*FightConfig.FIGHT_ATTACK_COEFFICIENT)/
				(1f+attrDefense*FightConfig.FIGHT_DEFENSE_COEFFICIENT);
		
		return attackValue;
	}
}
