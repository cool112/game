package com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants;

import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter.AbstractAttrConditionFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter.HpConditionFilter;

/**
 * 属性类型
 * @author seg
 *
 */
public enum AttrType
{
	/**
	 * 生命条件
	 */
	HP("hp", HpConditionFilter.class);
	
	/**
	 * 编号
	 */
	private String code;
	
	/**
	 * 类型
	 */
	private Class<? extends AbstractAttrConditionFilter> handlerClass;

	private AttrType(String code, Class<? extends AbstractAttrConditionFilter> handlerClass)
	{
		this.code = code;
		this.handlerClass = handlerClass;
	}

	/**
	 * 获取类型by编号
	 * @param type
	 * @return
	 */
	public static AttrType getTypeByCode(String code)
	{
		AttrType[] types = AttrType.values();
		for(AttrType type : types)
		{
			if(type.code.equals(code))
				return type;
		}
		return null;
	}

	public String getCode()
	{
		return code;
	}

	public Class<? extends AbstractAttrConditionFilter> getHandlerClass()
	{
		return handlerClass;
	}
	
	
	
}
