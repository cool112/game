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

/**
 * 阵营部队数量比较触发器
 * @author seg
 *
 */
public class CampCompareEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 运算符，0-小于，1-大于
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float operator;
	
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
		int leftOp = troopNumEvent.getSelfCount(campId, controllorId);
		int rightOp = troopNumEvent.getEnemyCount(campId);
		
		boolean reachCondition = false;
		if(operator == 0)
			reachCondition = leftOp < rightOp;
		else
			reachCondition = leftOp > rightOp;
		
		if(!reachCondition)
			return SkillError.ERROR_INVALID_STATE;
		
		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, caster, effectModelId, currTime, null, true);
		}
		return SkillError.SUCCESS;
	}


	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.TROOP_COUNT;
	}

}
