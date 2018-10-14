package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;

import commons.configuration.Property;

/**
 * 修改控制器初始能量
 * @author seg
 *
 */
public class ModInitEnergyPrototype extends EffectPrototype
{
	/**
	 * 修正值
	 */
	@Property(key = EffectKey.NUMBER)
	private int value;
	
	/**
	 * 阵营类型，0-仅自己 1-自己和友军 2-敌方
	 */
	@Property(key = EffectKey.FACTOR_A)
	private float campType;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		HavingCamp caster = effect.getCaster();
		if(caster == null)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		HavingCamp caster = effect.getCaster();
		if(caster == null)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		WarControlInstance casterControl = caster.getControl();
		if(campType == 0)
		{
			if(!(casterControl instanceof AbsFightControl))
				return SkillError.ERROR_EFFECT_NO_ATTACK;
			
			AbsFightControl fightControl = (AbsFightControl) casterControl;
			int oldValue = fightControl.getExtraInitPower();
			fightControl.setExtraInitPower(oldValue + value);
		}
		else
		{
			int campId = caster.getCampId();
			if(campType == 2)
				campId = WarUtils.getEnemyCampId(casterControl);
			
			WarInstance war = effect.getWar();
			List<AbsFightControl> controls = WarUtils.getFightControlByCamp(war, campId);
			for(AbsFightControl control : controls)
			{
				int oldValue = control.getExtraInitPower();
				control.setExtraInitPower(oldValue + value);
			}
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

}
