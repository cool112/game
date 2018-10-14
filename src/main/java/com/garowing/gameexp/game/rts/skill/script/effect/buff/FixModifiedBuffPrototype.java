package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.attr.FixValue;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.attr.ModifiedValueKey;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 固定修正值buff
 * @author seg
 *
 */
public class FixModifiedBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 修正类型
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float modifiedType;
	
	/**
	 * 修正值
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float value;
	
	/**
	 * 计算类型
	 */
	@Property(key = EffectKey.CALCULATE_TYPE, defaultValue = "0")
	private int calType;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		ModifiedType type = ModifiedType.getTypeByCode((int) modifiedType);
		if(type == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			troopTarget.getAttrModList().addModifiedValue(type, effect, ModifiedValueKey.DEFAULT_KEY, FixValue.valueOf(calType, value));
			sendAddBuffMsg(target, effect);
			newTargetIds.add(target.getObjectId());
		}
		
		effect.setTargetIds(newTargetIds);
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
		ModifiedType type = ModifiedType.getTypeByCode((int) modifiedType);
		if(type == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isLive())
				continue;
			
			troopTarget.getAttrModList().removeModifiedValue(type, effect);
			sendDelBuffMsg(target, effect);
		}
		
		return SkillError.SUCCESS;
	}

}
