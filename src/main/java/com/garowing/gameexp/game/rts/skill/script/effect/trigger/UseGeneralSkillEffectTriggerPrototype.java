package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.GeneralSkillEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.yeto.war.module.strategos.StrategosObject;

import commons.configuration.Property;

/**
 * 将军技能使用触发
 * @author seg
 *
 */
public class UseGeneralSkillEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 阵营类型,0-全部 1-己方 2-敌方 3-盟军
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0.0")
	private float campType;
	
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.USE_GENERAL_SKILL)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		GeneralSkillEntity generalSkill = (GeneralSkillEntity) event.getSource();
		StrategosObject general = generalSkill.getCaster();
		HavingCamp caster = effect.getCaster();
		if(caster == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		switch ((int)campType)
		{
		case 0:
			break;
		case 1:
			if(general.getCampId() != caster.getCampId())
				return SkillError.ERROR_INVALID_STATE;
			
			break;
		case 2:
			if(general.getCampId() == caster.getCampId())
				return SkillError.ERROR_INVALID_STATE;
			
			break;

		default:
			return SkillError.ERROR_BASE_DATA_ERROR;
		}
		
		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, (GameObject) caster, effectModelId, currTime, null, false);
		}
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.USE_GENERAL_SKILL;
	}

}
