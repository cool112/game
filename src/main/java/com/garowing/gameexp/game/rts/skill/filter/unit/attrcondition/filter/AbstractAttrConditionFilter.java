package com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter;

import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants.Condition;

/**
 * 抽象条件处理
 * @author seg
 *
 */
public abstract class AbstractAttrConditionFilter implements IAttrConditionFilter
{
	/**
	 * 条件
	 */
	protected Condition condition;
	
	/**
	 * 是否百分比
	 */
	protected boolean isPercent;
	
	/**
	 * 条件值
	 */
	protected float value;

	public AbstractAttrConditionFilter(Condition condition, boolean isPercent, float value)
	{
		super();
		this.condition = condition;
		this.isPercent = isPercent;
		this.value = value;
	}

	public Condition getCondition()
	{
		return condition;
	}

	public boolean isPercent()
	{
		return isPercent;
	}

	public float getValue()
	{
		return value;
	}

}
