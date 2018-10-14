package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
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
 * 攻击效果触发器
 * 
 * @author seg
 *
 */
public class AttackEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 目标类型,0-被攻击者，1-攻击者
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float	targetType;

	/**
	 * 独立效果，不依附任何单位，单位死亡不会销毁该效果,0-依附 1-独立
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float	isolated;

	/**
	 * 条件类型:<br>
	 * 0-无 1-目标血量低于百分比
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0")
	private float	conditionType;

	/**
	 * 条件值
	 */
	@Property(key = EffectKey.FACTOR_D, defaultValue = "0")
	private float	conditionValue;

	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if (event.getEventType() != SkillEventType.ATTACK)
			return SkillError.ERROR_BASE_DATA_ERROR;

		Troop target = null;
		if (targetType == 0)
			target = (Troop) event.getTarget();
		else
			target = ((EffectEntity) event.getSource()).getCaster();

		for (Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, target, effectModelId, currTime, null, isolated != 0);
		}
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.ATTACK_EVENT;
	}

	@Override
	protected boolean targetCheck(AbstractSkillEvent event)
	{
		GameObject target = event.getTarget();
		if (target == null || target.getObjectType() != GameObjectType.TROOP)
			return false;

		Troop troopTarget = (Troop) target;

		if (troopTypeMask > 0)
		{
			int troopType = troopTarget.getModelType();
			if ((troopType & troopTypeMask) == 0 || (troopType & ~troopTypeMask) > 0)
				return false;
		}

		if (conditionType > 0)
		{
			switch ((int) conditionType)
			{
			case 1:
				float hpPercent = troopTarget.getHp() * 1.f / troopTarget.getMaxHp();
				if (hpPercent >= conditionValue)
					return false;
				break;

			default:
				break;
			}
		}

		return true;
	}

}
