package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.manager.ListIntTroopTypeMaskPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 其他单位死亡监听
 * @author seg
 *
 */
public class OtherDieEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 阵营类型,0-全 1-自己 2-敌
	 */
	@Property(key = EffectKey.FACTOR_A)
	private float campType;
	
	/**
	 * 忽略部队id
	 */
	@Property(key = EffectKey.ATTR_ID, defaultValue = "0")
	private float ignoreTroopId;
	
	/**
	 * 目标类型，0-触发器携带者，1-死者，2-攻击者
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float targetType;
	
	/**
	 * 部队类型掩码
	 */
	@Property(key = EffectKey.TROOP_TYPES, defaultValue = "", propertyTransformer = ListIntTroopTypeMaskPropertyTransformer.class)
	private int troopTypeMask;
	
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.TROOP_DIE)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		HavingCamp caster = effect.getCaster();
		Troop dead = (Troop) event.getTarget();
		GameObject target = null;
		switch ((int)targetType)
		{
		case 0:
			target = (GameObject) caster;
			break;
		case 1:
			target = dead;
			break;
		case 2:
			target = event.getSource();
			break;

		default:
			break;
		}
		
		int troopType = dead.getModelType();
		if((troopTypeMask & troopType) == 0 || (~troopTypeMask & troopType) > 0)
			return SkillError.ERROR_INVALID_STATE;
		
		if(ignoreTroopId > 0 && dead.getModelID() == ignoreTroopId)
			return SkillError.ERROR_INVALID_STATE;
		
		switch ((int)campType)
		{
		case 1:
			if(dead.getCampId() != caster.getCampId())
				return SkillError.ERROR_INVALID_STATE;
			
			break;
		case 2:
			if(dead.getCampId() == caster.getCampId())
				return SkillError.ERROR_INVALID_STATE;
			
			break;
		default:
			break;
		}

		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, target, effectModelId, currTime, null, false);
		}
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.DIE_LISTENER_EVENT;
	}

}
