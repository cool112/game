package com.garowing.gameexp.game.rts.skill.script.effect.active;

import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;

import commons.configuration.Property;

/**
 * 抽象召唤技能
 * @author seg
 *
 */
abstract public class AbstractSummonPrototype extends EffectPrototype
{
	/**
	 * 最大保持数量
	 */
	@Property(key = EffectKey.NUMBER, defaultValue = "1")
	protected int maxNum;
	
	/**
	 * 召唤单位存活时间，单位毫秒，0表示无限
	 */
	@Property(key = EffectKey.LIVE_TIME, defaultValue = "0")
	protected int liveTime;
	
	/**
	 * 自定义ai延迟
	 */
	@Property(key = EffectKey.FACTOR_E, defaultValue = "0")
	protected float aiDelay;
	
	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

	public int getMaxNum()
	{
		return maxNum;
	}
}
