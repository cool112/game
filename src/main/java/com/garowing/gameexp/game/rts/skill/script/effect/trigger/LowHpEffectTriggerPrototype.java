package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.UnitHpChangeSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 低血量效果触发器
 * @author seg
 *
 */
public class LowHpEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 低血量阈限
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float lowPercent;
	
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.HP_CHANGE)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		UnitHpChangeSkillEvent hpChangeEvent = (UnitHpChangeSkillEvent) event;
		Troop target = (Troop) hpChangeEvent.getTarget();
		int beforeHp = hpChangeEvent.getBeforeHp();
		int currentHp = hpChangeEvent.getCurrentHp();
		int maxHp = target.getMaxHp();
		if(beforeHp * 1.0f / maxHp <= lowPercent || currentHp * 1.0f / maxHp > lowPercent)
			return SkillError.ERROR_INVALID_STATE;
		
		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, target, effectModelId, currTime, null, false);
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.HP_CHANGE;
	}

}
