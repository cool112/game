package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AbstractEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 部队死亡事件处理
 * @author seg
 *
 */
public class TroopDieSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.TROOP_DIE)
			return;
		
		GameObject attackObject = event.getSource();
		Troop dieObject = (Troop) event.getTarget();
		List<EffectEntity> dieEffects = dieObject.getAttachEffects().getTriggerEffect(TriggerEventType.DIE_EVENT);
		for(EffectEntity effect : dieEffects)
		{
			EffectPrototype prototype = effect.getEffectPrototype();
			if(prototype instanceof AbstractEffectTriggerPrototype)
			{
				AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
				triggerPrototype.handleEvent(event, effect, currTime);
			}
		}
		
		if (attackObject != null)
		{
			int type = attackObject.getObjectType();
			List<EffectEntity> attackEffects = null;
			if(type == GameObjectType.TROOP)
			{
				Troop attack = (Troop)attackObject;
				attackEffects = attack.getAttachEffects().getTriggerEffect(TriggerEventType.KILL_EVENT);
			}
			else
			{
				if(attackObject instanceof HavingCamp)
				{
					WarControlInstance control = ((HavingCamp)attackObject).getControl();
					if(control instanceof AbsFightControl)
						attackEffects = ((AbsFightControl)control).getAttachEffects().getTriggerEffect(TriggerEventType.KILL_EVENT);
				}
			}
			
			if(attackEffects != null)
			{
				for (EffectEntity effect : attackEffects)
				{
					EffectPrototype prototype = effect.getEffectPrototype();
					if(prototype instanceof AbstractEffectTriggerPrototype)
					{
						AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
						triggerPrototype.handleEvent(event, effect, currTime);
					}
				}
			}
		}
		
		dieListener(event, dieObject, currTime);
	}
	
	/**
	 * 其他死亡监听
	 * @param event
	 * @param dieObject
	 * @param currTime
	 */
	private final static void dieListener(AbstractSkillEvent event, Troop dieObject, long currTime)
	{
		WarInstance war = dieObject.getWar();
		if(war == null)
			return;
		
		Collection<WarObjectInstance> troops = WarUtils.getObjects(war);
		if(troops == null || troops.isEmpty())
			return;
		
		for(WarObjectInstance warObj : troops)
		{
			if(dieObject.getObjectId() == warObj.getObjectId())
				continue;
			
			List<EffectEntity> otherDieEffects = warObj.getAttachEffects().getTriggerEffect(TriggerEventType.DIE_LISTENER_EVENT);
			if(otherDieEffects == null || otherDieEffects.isEmpty())
				continue;
			
			for(EffectEntity effect : otherDieEffects)
			{
				EffectPrototype prototype = effect.getEffectPrototype();
				if(prototype instanceof AbstractEffectTriggerPrototype)
				{
					AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
					triggerPrototype.handleEvent(event, effect, currTime);
				}
			}
		}
	}

}
