package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * map<int,int> 属性解析former
 * @author zhouxiaofeng
 */
public class MapIntPropertyTransformer implements PropertyTransformer<Map<Integer, Integer>>
{

	@Override
	public Map<Integer, Integer> transform(String value, Field field) throws TransformationException
	{
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null"))
			return new HashMap<Integer, Integer>(0);
		
		String[] parts = value.split(" ");
		if (parts.length <= 0)
		{
			throw new TransformationException("Can't transform property, must be in format 1_1,2_2");
		}
		
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (String part : parts)
		{
			String[] values = part.split("_");
			int key = Integer.valueOf(values[0]);
			int val = Integer.valueOf(values[1]);
			result.put(key, val);
		}
		return result;
	}

}
