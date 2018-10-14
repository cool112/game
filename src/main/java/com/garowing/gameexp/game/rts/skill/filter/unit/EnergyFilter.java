package com.garowing.gameexp.game.rts.skill.filter.unit;

import java.util.List;

import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.behaviac.agent.BaseAIObj;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterKey;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterType;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter.IAttrConditionFilter;
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 部队基础能量过滤器
 * @author seg
 *
 */
public class EnergyFilter extends BasePropsFilter
{
	/**
	 * 能量列表
	 */
	@Property(key = PropsFilterKey.CONSUME_ENERGY, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	private List<Integer> energyList;
	
	@Override
	public int getType()
	{
		return PropsFilterType.ENERGY.getCode();
	}
	
	/**
	 * 检查基础信息
	 * @param obj
	 * @param casterId
	 * @param casterCamp
	 * @return
	 */
	@Override
	protected boolean checkBaseProps(Projectile data, WarObjectInstance obj, int casterId, int casterCamp)
	{
		if(obj == null || obj.getObjectType() != GameObjectType.TROOP)
			return false;
		
		if(!this.includeSelf && obj.getObjectId() == casterId)
			return false;
		
		Troop troop = (Troop) obj;
		
		if(!troop.isLive())
			return false;
		
		if(data instanceof SkillEntity)
		{
			if(!troop.getAttachEffects().isValidSkill(data))
				return false;
		}
		
		if(campType == BaseAIObj.CAMP_ENEMY && (troop.getCampId() == casterCamp || !troop.canAttacked()))
			return false;
		
		if(campType == BaseAIObj.CAMP_FRIEND && troop.getCampId() != casterCamp)
			return false;
		
		if((forbidCallerTypeMask & (1 << troop.getCallerType())) > 0)
			return false;
		
		if((obj.getModelType() & allowTypeMask) == 0 || (obj.getModelType() & ~allowTypeMask) > 0)
			return false;
		
		if(!energyList.contains(((Troop)obj).getModel().getFood()))
			return false;
		
		if(attrConditions != null && !attrConditions.isEmpty())
		{
			for(IAttrConditionFilter filter : attrConditions)
			{
				if(!filter.filter(troop))
					return false;
			}
		}
		
		return true;
	}
	
}
