package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.attr.constants.UnitType;
import com.yeto.war.fightcore.fight.attr.FixValue;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.attr.ModifiedValueKey;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 单位数量固定修正值
 * @author seg
 *
 */
public class UnitCountFixModifiedBuffPrototype extends AbstractBuffPrototype
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
	
	/**
	 * 阵营类型, 0-全部，1-自己，2-敌方
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0")
	private float campType;
	
	/**
	 * 部队类型掩码
	 */
	@Property(key = EffectKey.TROOP_TYPES, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	private List<Integer> troopTypes;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		int troopTypeMask = 0;
		for(Integer troopType : troopTypes)
		{
			UnitType type = UnitType.getUnitType(troopType);
			if(type != null)
				troopTypeMask |= type.getMask();
		}
		
		ModifiedType type = ModifiedType.getTypeByCode((int) modifiedType);
		if(type == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		WarInstance war = effect.getWar();
		if (war == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		HavingCamp caster = effect.getCaster();
		
		Collection<Troop> list = null;
		
		switch ((int)campType)
		{
		case 0:
			list = WarUtils.getTroopsByCamp(war, 0, troopTypeMask);
			break;
		case 1:
			list = WarUtils.getTroopsByCamp(war, caster.getCampId(), troopTypeMask);
			break;
		case 2:
			int enemyCampId = WarUtils.getEnemyCampId(caster.getControl());
			list = WarUtils.getTroopsByCamp(war, enemyCampId, troopTypeMask);
			break;
		default:
			break;
		}
		
		if(list == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		int count = list.size();
		count = count < 1 ? 1 : count;
		float totalValue =  (count - 1) * value;
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			troopTarget.getAttrModList().addModifiedValue(type, effect, ModifiedValueKey.DEFAULT_KEY, FixValue.valueOf(calType, totalValue));
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
