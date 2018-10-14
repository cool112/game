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
 * 目标改变事件处理，需要注意目标可能为空
 * @author seg
 *
 */
public class TargetChangeSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.TARGET_CHANGE)
			return;
		
		Troop source = (Troop) event.getSource();
		List<EffectEntity> dieEffects = source.getAttachEffects().getTriggerEffect(TriggerEventType.TARGET_CHANGE);
		for(EffectEntity effect : dieEffects)
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
