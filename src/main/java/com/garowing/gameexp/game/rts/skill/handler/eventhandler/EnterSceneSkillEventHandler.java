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
 * 进入战场技能事件
 * @author seg
 * 2017年3月24日
 */
public class EnterSceneSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.ENTER_SCENE)
			return;
		
		Troop source = (Troop) event.getSource();
//		System.err.println("enter scene:[" +source+ "]");
//		Skill2TriggerFactory.onStartFight(source);
		List<EffectEntity> casterEffects = source.getAttachEffects().getTriggerEffect(TriggerEventType.START_FIGTH_EVENT);
		for(EffectEntity effect : casterEffects)
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
