package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * 点集转换器0,0 1,2
 * @author seg
 *
 */
public class ListPointPropertyTransformer implements PropertyTransformer<List<float[]>>
{

	@SuppressWarnings("unchecked")
	@Override
	public List<float[]> transform(String value, Field field) throws TransformationException
	{
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null"))
			return Collections.EMPTY_LIST;
		
		value = value.replaceAll("\\(|\\)", "");
		String[] parts = value.split(" ");
		if (parts.length <= 0)
		{
			throw new TransformationException("Can't transform property, must be in format (1,1) (2,2)");
		}
		
		List<float[]> result = new ArrayList<float[]>();;
		for (String part : parts)
		{
			String[] values = part.split(",");
			float x = Float.valueOf(values[0]);
			float y = Float.valueOf(values[1]);
			result.add(new float[]{x, y});
		}
		return result;
	}

}
