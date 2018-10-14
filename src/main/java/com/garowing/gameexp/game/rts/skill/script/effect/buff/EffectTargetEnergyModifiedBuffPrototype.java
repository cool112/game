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
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

import commons.configuration.Property;

/**
 * 效果-目标能量点修正值buff
 * @author seg
 *
 */
public class EffectTargetEnergyModifiedBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 能量点列表
	 */
	@Property(key = EffectKey.ENERGY, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	private List<Integer> energies;
	
	/**
	 * 修正类型
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float modifiedType;
	
	/**
	 * 百分比
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float percent;
	
	/**
	 * 计算类型
	 */
	@Property(key = EffectKey.CALCULATE_TYPE, defaultValue = "0")
	private int calType;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		int energyMask = 0;
		for(Integer energyPoint : energies)
		{
			energyMask |= 1 << energyPoint;
		}
		
		if(energyMask == 0)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
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
			
			if(subEffectIds == null || subEffectIds.isEmpty())
				troopTarget.getAttrModList().addModifiedValue(type, effect, new ModifiedValueKey(ModifiedValueKey.DEFAULT_EFFECT_KEY, 0, 0, energyMask), FixValue.valueOf(calType, percent));
			else
			{
				for(Integer effectId : subEffectIds)
				{
					troopTarget.getAttrModList().addModifiedValue(type, effect, new ModifiedValueKey(MathUtil.getHashCode(ModifiedValueKey.EFFECT_TYPE, effectId), 0, 0, energyMask), FixValue.valueOf(calType, percent));
				}
			}
			
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
