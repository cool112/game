package com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter;

import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants.AttrType;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants.Condition;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;;

/**
 * 生命值条件过滤器
 * @author seg
 *
 */
public class HpConditionFilter extends AbstractAttrConditionFilter
{

	public HpConditionFilter(Condition condition, boolean isPercent, float value)
	{
		super(condition, isPercent, value);
	}

	@Override
	public boolean filter(WarObjectInstance unit)
	{
		float currVal = 0f;
		if(isPercent)
			currVal = unit.getHp() * 1f / unit.getMaxHp();
		else
			currVal = unit.getHp();
		
		switch (condition)
		{
		case LESS_EQUEL:
			return currVal <= value;
		case MORE_THAN:
			return currVal > value;
		default:
			break;
		}
		
		return false;
	}

	@Override
	public AttrType getAttrType()
	{
		return AttrType.HP;
	}

}
