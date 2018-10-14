package com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants;

/**
 * 条件
 * @author seg
 *
 */
public enum Condition
{
	/**
	 * 小于等于
	 */
	LESS_EQUEL("<="),
	
	/**
	 * 大于
	 */
	MORE_THAN(">");
	
	private String code;

	private Condition(String code)
	{
		this.code = code;
	}
	
	public static Condition getConditionByCode(String code)
	{
		Condition[] values = Condition.values();
		for(Condition value : values)
		{
			if(value.code.equals(code))
				return value;
		}
		
		return null;
	}
}
