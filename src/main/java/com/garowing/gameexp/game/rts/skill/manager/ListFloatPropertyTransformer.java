package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * 浮点数集合转换
 * @author seg
 *
 */
public class ListFloatPropertyTransformer implements PropertyTransformer<List<Float>>
{

	@SuppressWarnings("unchecked")
	@Override
	public List<Float> transform(String value, Field field) throws TransformationException
	{
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null"))
			return Collections.EMPTY_LIST;
		
		String[] parts = value.split(" ");

		if (parts.length <= 0)
		{
			throw new TransformationException("Can't transform property, must be in format \"0.0 0.5 0.8\"");
		}
		
		List<Float> list = new ArrayList<>();
		for (String part : parts)
		{
			float data = Float.valueOf(part);
			list.add(data);
		}
		
		return list;
	}

}
