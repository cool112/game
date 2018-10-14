package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import java.util.Collection;
import java.util.List;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AbstractEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 部队数改变事件处理
 * @author seg
 *
 */
public class TroopNumSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.TROOP_COUNT_CHANGE)
			return;
		
		WarInstance war = event.getWar();
		if(war == null)
			return;
		
		Collection<WarObjectInstance> troops = WarUtils.getObjects(war);
		if(troops == null || troops.isEmpty())
			return;
		
		for(WarObjectInstance warObj : troops)
		{
			List<EffectEntity> otherDieEffects = warObj.getAttachEffects().getTriggerEffect(TriggerEventType.TROOP_COUNT);
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
