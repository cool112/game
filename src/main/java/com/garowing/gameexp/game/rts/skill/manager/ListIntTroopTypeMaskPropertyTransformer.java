package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;

import com.yeto.war.fightcore.attr.constants.UnitType;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * 整形数组变部队类型掩码
 * @author seg
 *
 */
public class ListIntTroopTypeMaskPropertyTransformer implements PropertyTransformer<Integer>
{

	@Override
	public Integer transform(String value, Field field) throws TransformationException
	{
		int mask = 0;
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null") || value.equals("0"))
			return 0xffffffff;
		
		String[] parts = value.split(" ");

		if (parts.length <= 0)
		{
			throw new TransformationException("Can't transform property, must be in format \"1 2 3\"");
		}
		
		for (String part : parts)
		{
			int data = Integer.valueOf(part);
			UnitType type = UnitType.getUnitType(data);
			if(type != null)
				mask |= type.getMask();
		}
		
		return mask;
	}

}
