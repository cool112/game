package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import java.util.List;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AbstractEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.yeto.war.module.troop.Troop;

/**
 * 单位血量改变事件处理
 * @author seg
 *
 */
public class UnitHpChangeSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.HP_CHANGE)
			return;
		
		Troop target = (Troop) event.getTarget();
		List<EffectEntity> effects = target.getAttachEffects().getTriggerEffect(TriggerEventType.HP_CHANGE);
		if(effects == null || effects.isEmpty())
			return;
		
		for(EffectEntity effect : effects)
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
