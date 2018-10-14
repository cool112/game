package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * list<Intger> 转换器
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月24日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class ListIntPropertyTransformer implements PropertyTransformer<List<Integer>>
{

	private final static List<Integer> EMPTY_LIST = new ArrayList<Integer>(0);
	
	@Override
	public List<Integer> transform(String value, Field field) throws TransformationException 
	{
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null"))
			return EMPTY_LIST;
		
		String[] parts = value.split(" ");

		if (parts.length <= 0)
		{
			throw new TransformationException("Can't transform property, must be in format \"1001 1002 1003\"");
		}
		
		List<Integer> list = new ArrayList<>();
		for (String part : parts)
		{
			int data = Integer.valueOf(part);
			list.add(data);
		}
		
		return list;
	}

}
