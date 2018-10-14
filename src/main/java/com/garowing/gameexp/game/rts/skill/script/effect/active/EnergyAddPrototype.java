package com.garowing.gameexp.game.rts.skill.script.effect.active;

import com.yeto.war.fightcore.fight.FightEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;

import commons.configuration.Property;

/**
 * 能量增加效果
 * @author seg
 *
 */
public class EnergyAddPrototype extends EffectPrototype
{
	/**
	 * 能量点数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float energy;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		SkillError result = onActivate(effect, currTime);
		if(result == SkillError.SUCCESS)
		{
			effect.addActivateCount();
			effect.setLastActivateTime(currTime);
			effect.setNextActivateTime(currTime + effect.getInterval());
		}
		
		return result;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		HavingCamp caster = effect.getCaster();
		if(caster == null)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		WarControlInstance controllor = caster.getControl();
		if(!(controllor instanceof AbsFightControl))
			return SkillError.SUCCESS;
		
		FightEngine.addFood((AbsFightControl) controllor, energy, currTime);
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

}
