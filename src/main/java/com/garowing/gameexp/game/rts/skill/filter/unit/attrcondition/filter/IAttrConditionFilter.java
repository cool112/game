package com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter;

import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants.AttrType;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 属性条件过滤器
 * @author seg
 *
 */
public interface IAttrConditionFilter
{
	/**
	 * 过滤
	 * @param unit
	 * @return true满足条件
	 */
	public boolean filter(WarObjectInstance unit);
	
	/**
	 * 获取类型
	 * @return
	 */
	public AttrType getAttrType();
}
