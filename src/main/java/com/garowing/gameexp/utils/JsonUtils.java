package com.garowing.gameexp.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * object对象和json字符串互转
 */
public class JsonUtils 
{
	
	
	/**
	 * 对象转json数据
	 * @param object		对象
	 * @return
	 */
	public static <T>String objectToJson(T object)
	{
		return objectToJson(object, false);
	}
	
	
	/**
	 * 对象转json数据
	 * @param object		对象
	 * @param writeClass	是否写入class
	 * @return
	 */
	public static <T>String objectToJson(T object, boolean writeClass)
	{
		if(object == null)
		{
			return "";
		}
		if(writeClass)
		{
			return JSON.toJSONString(object, SerializerFeature.WriteClassName);
		}
		else
		{
			return JSON.toJSONString(object);
		}
	}
	
	public static <T> String objectToJson(T object, SerializerFeature... features)
	{
		if (object == null)
			return "";
		return JSONObject.toJSONString(object, features);
	}
	
	/**
	 * json数据转对象
	 * @param str			json
	 * @param clazz			对象class
	 * @return
	 */
	public static <T> T jsonToObject(String str, Class<T> clazz)
	{
		if(str == null || str.isEmpty())
			return null;
		
		return JSON.parseObject(str, clazz);
	}
	
	/**
	 * 将字符串转换成json对象
	 * @param str
	 * @return
	 */
	public static JSONObject jsonToJsonobject(String str)
	{
		if(str == null || str.isEmpty())
			return null;
		
		return JSON.parseObject(str);
	}
	
	/**
	 * 将JSON对象准换成string
	 * @param object
	 * @return
	 */
	public static String jsonobjectToJson(JSONObject object)
	{
		if(object == null)
		{
			return "";
		}
		return JSON.toJSONString(object);
	}
	
	/**
	 * json数据转对象
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T jsonToObject(String str, TypeReference<T> type)
	{
		if(str == null || str.isEmpty())
			return null;
		
		return JSON.parseObject(str, type);
	}
	
	/**
	 * json数据转换list
	 * @param str
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonToArray(String str, Class<T> clazz)
	{
		if(str == null || str.isEmpty())
			return null;
		
		return JSON.parseArray(str, clazz);
	}
}

