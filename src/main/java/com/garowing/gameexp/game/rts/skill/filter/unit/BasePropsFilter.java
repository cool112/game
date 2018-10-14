package com.garowing.gameexp.game.rts.skill.filter.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.behaviac.agent.BaseAIObj;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterKey;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterType;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter.IAttrConditionFilter;
import com.garowing.gameexp.game.rts.skill.manager.ListAttrCondPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.manager.ListIntMaskPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 基础属性过滤器
 * @author seg
 *
 */
public class BasePropsFilter implements UnitPropsFilter
{
	/**
	 * 阵营类型
	 */
	@Property(key = PropsFilterKey.CAMP_TYPE, defaultValue = "0")
	protected int campType;
	
	/**
	 * 允许类型掩码
	 */
	@Property(key = PropsFilterKey.TROOP_TYPE_MASK, defaultValue = "0")
	protected int allowTypeMask;
	
	/**
	 * 排序类型列表
	 */
	@Property(key = PropsFilterKey.ALLOW_TROOP_TYPES, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	protected List<Integer> sortedTypes;
	
	/**
	 * 是否包含自己
	 */
	@Property(key = PropsFilterKey.INCLUDE_SELF, defaultValue = "false")
	protected boolean includeSelf;
	
	/**
	 * 属性条件过滤
	 */
	@Property(key = PropsFilterKey.ATTRIBUTES, defaultValue = "", propertyTransformer = ListAttrCondPropertyTransformer.class)
	protected List<IAttrConditionFilter> attrConditions;
	
	/**
	 * 禁止的召唤者类型掩码
	 */
	@Property(key = PropsFilterKey.CALLER_TYPE, defaultValue = "", propertyTransformer = ListIntMaskPropertyTransformer.class)
	protected int forbidCallerTypeMask;
	
	public BasePropsFilter()
	{
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WarObjectInstance> filter(Projectile data, GameObject caster, Collection<WarObjectInstance> list)
	{
		if(list == null)
			return Collections.EMPTY_LIST;
		
		int campId = 0;
		if(caster instanceof HavingCamp)
			campId = ((HavingCamp)caster).getCampId();
		
		int selfId = caster.getObjectId();
		
		List<WarObjectInstance> newList = new ArrayList<WarObjectInstance>();
		for(WarObjectInstance obj : list)
		{
			if(checkBaseProps(data, obj, selfId, campId))
				newList.add(obj);
		}
		
		return newList;
	}
	
	/**
	 * 检查基础信息
	 * @param data 
	 * @param obj
	 * @param casterId
	 * @param casterCamp
	 * @return
	 */
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


	@Override
	public int getType()
	{
		return PropsFilterType.BASE.getCode();
	}


	public int getCampType()
	{
		return campType;
	}


	public void setCampType(int campType)
	{
		this.campType = campType;
	}


	public int getAllowTypeMask()
	{
		return allowTypeMask;
	}


	public void setAllowTypeMask(int allowTypeMask)
	{
		this.allowTypeMask = allowTypeMask;
	}


	public List<Integer> getSortedTypes()
	{
		return sortedTypes;
	}


	public void setSortedTypes(List<Integer> sortedTypes)
	{
		this.sortedTypes = sortedTypes;
	}


	public boolean isIncludeSelf()
	{
		return includeSelf;
	}


	public void setIncludeSelf(boolean includeSelf)
	{
		this.includeSelf = includeSelf;
	}
	
}
