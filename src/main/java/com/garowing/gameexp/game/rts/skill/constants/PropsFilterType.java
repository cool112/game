package com.garowing.gameexp.game.rts.skill.constants;

import com.garowing.gameexp.game.rts.skill.filter.unit.BasePropsFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.CompositeFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.EnergyFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.StatusFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.TroopIdFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.UnitPropsFilter;

/**
 * 单位属性过滤器类型
 * @author seg
 *
 */
public enum PropsFilterType
{
	/**
	 * 基础过滤器
	 */
	BASE		(0, BasePropsFilter.class),
	
	/**
	 * 组合过滤器
	 */
	COMPOSITE	(1, CompositeFilter.class),
	
	/**
	 * 状态过滤器
	 */
	STATUS		(2, StatusFilter.class),
	
	/**
	 * 部队id过滤器
	 */
	TROOP_ID	(3, TroopIdFilter.class),
	
	/**
	 * 部队基础能量过滤器
	 */
	ENERGY		(4, EnergyFilter.class);
	
	
	private int code;
	
	private Class<? extends UnitPropsFilter> implClass;
	

	private PropsFilterType(int code, Class<? extends UnitPropsFilter> filterClass)
	{
		this.code = code;
		this.implClass = filterClass;
	}
	
	/**
	 * 获取类型
	 * @param id
	 * @return
	 */
	public static PropsFilterType getTypeById(int id)
	{
		PropsFilterType[] types = PropsFilterType.values();
		for(PropsFilterType type : types)
		{
			if(type.code == id)
				return type;
		}
		return null;
	}

	public int getCode()
	{
		return code;
	}

	public Class<? extends UnitPropsFilter> getFilterClass()
	{
		return implClass;
	}
	
}
