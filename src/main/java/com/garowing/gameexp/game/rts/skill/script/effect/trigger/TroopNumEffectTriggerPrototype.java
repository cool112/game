package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.TroopNumSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingCamp;

import commons.configuration.Property;

public class TroopNumEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 运算符，0-小于等于，1-大于等于
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float operator;
	
	/**
	 * 阵营类型，0-全部 1-己方 2-敌方 3-盟军
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float campType;
	
	/**
	 * 数量条件
	 */
	@Property(key = EffectKey.NUMBER, defaultValue = "0")
	private int number;
	
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.TROOP_COUNT_CHANGE)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		GameObject caster = effect.getCaster();
		if(!(caster instanceof HavingCamp))
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		int campId = ((HavingCamp)caster).getCampId();
		int controllorId = ((HavingCamp)caster).getControl().getObjectId();
		
		TroopNumSkillEvent troopNumEvent = (TroopNumSkillEvent) event;
		int leftOpNum = 0;
		switch ((int)campType)
		{
		case 0:
			leftOpNum = troopNumEvent.getAllCount();
			break;
		case 1:
			leftOpNum = troopNumEvent.getSelfCount(campId, controllorId);
			break;
		case 2:
			leftOpNum = troopNumEvent.getEnemyCount(campId);
			break;
		case 3:
			leftOpNum = troopNumEvent.getFriendlyCount(campId, controllorId);
			break;
		default:
			break;
		}
		
		boolean reachCondition = false;
		if(operator == 0)
		{
			reachCondition = leftOpNum <= number;
		}
		else if(operator == 1)
		{
			reachCondition = leftOpNum >= number;
		}
		
		if(!reachCondition)
			return SkillError.ERROR_INVALID_STATE;
		
		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, caster, effectModelId, currTime, null, false);
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.TROOP_COUNT;
	}
}
