package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * 整型集合变掩码,不填等于0
 * @author seg
 *
 */
public class ListIntMaskPropertyTransformer implements PropertyTransformer<Integer>
{

	@Override
	public Integer transform(String value, Field field) throws TransformationException
	{
		int mask = 0;
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null") || value.equals("0"))
			return mask;
		
		String[] parts = value.split(" ");

		if (parts.length <= 0)
		{
			throw new TransformationException("Can't transform property, must be in format \"1 2 3\"");
		}
		
		for (String part : parts)
		{
			int data = Integer.valueOf(part);
			mask |= 1 << data;
		}
		
		return mask;
	}

}
