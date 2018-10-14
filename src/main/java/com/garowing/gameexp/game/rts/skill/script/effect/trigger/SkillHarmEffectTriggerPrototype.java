package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 伤害技能效果触发器
 * @author seg
 *
 */
public class SkillHarmEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 目标类型,0-被攻击者，1-攻击者
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float targetType;
	
	/**
	 * 限制效果触发
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float effectId;
	
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.SKILL_HARM)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		EffectEntity sourceEffect = (EffectEntity) event.getSource();
//		System.err.println("cond:" + effectId + " target:" + sourceEffect.getPrototypeId());
		if(effectId > 0 && sourceEffect.getPrototypeId() != effectId)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		Troop target = null;
		if(targetType == 0)
			target = (Troop)event.getTarget();
		else
			target = sourceEffect.getCaster();
		
		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, target, effectModelId, currTime, null, false);
		}
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.SKILL_HARM;
	}

}
