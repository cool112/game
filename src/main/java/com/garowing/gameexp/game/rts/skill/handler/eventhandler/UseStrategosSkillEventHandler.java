package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import java.util.Collection;
import java.util.List;

import com.garowing.gameexp.game.rts.skill.Skill2TriggerFactory;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AbstractEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.strategos.StrategosSkillModel;

/**
 * 使用将军技能事件处理
 * @author seg
 * 2017年3月24日
 */
public class UseStrategosSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.USE_GENERAL_SKILL)
			return;
		
		WarInstance war = event.getWar();
		if(war == null)
			return;
		
		Collection<WarObjectInstance> troops = WarUtils.getObjects(war);
		if(troops == null || troops.isEmpty())
			return;
		
		for(WarObjectInstance warObj : troops)
		{
			List<EffectEntity> generalEffects = warObj.getAttachEffects().getTriggerEffect(TriggerEventType.USE_GENERAL_SKILL);
			if(generalEffects == null || generalEffects.isEmpty())
				continue;
			
			for(EffectEntity effect : generalEffects)
			{
				EffectPrototype prototype = effect.getEffectPrototype();
				if(prototype instanceof AbstractEffectTriggerPrototype)
				{
					AbstractEffectTriggerPrototype triggerPrototype = (AbstractEffectTriggerPrototype) prototype;
					triggerPrototype.handleEvent(event, effect, currTime);
				}
			}
		}
		
		SkillEntity skill = (SkillEntity) event.getSource();
		WarControlInstance control = skill.getOwnerControl();
		StrategosSkillModel strategosSkill = skill.getSourceStrategosSkill();
		
		Skill2TriggerFactory.onCastStrategosSkill((AbsFightControl) control, strategosSkill);
	}

}
