package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import java.lang.reflect.Field;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.cache.AccessibleCache;

/**
 * 属性标签
 * @author seg
 * 2017年1月6日
 */
public class PropertyTag extends AbstractTag
{
	private static final Logger mLogger = LogManager.getLogger(PropertyTag.class);
	
	/**
	 * 类型描述
	 */
	private String type;
	
	@Override
	public <T> T rt(Object obj)
	{
		return getProperty(obj);
	}
	
	/**
	 * 获取对象属性值
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getProperty(Object obj)
	{
		Field field = AccessibleCache.getField(this);
		if(field == null)
			return null;
		
		if(!"Self".equals(this.instance))
		{
			obj = AccessibleCache.getClass(this.instance);
			if(obj == null)
				return null;
		}
		
		T value = null;
		try
		{
			value = (T) field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			mLogger.warn("field extract exception! propertyTag: " + this.toString(), e);
		}
			
		return value;
	}
	
	@Override
	public String getRtType()
	{
		return getType().toLowerCase();
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * 设置属性
	 * @param obj
	 * @param value
	 */
	public void setValue(Object obj, Object value)
	{
		Field field = AccessibleCache.getField(this);
		if(field == null)
			return;
		
		if(!"Self".equals(this.instance))
		{
			obj = AccessibleCache.getClass(this.instance);
			if(obj == null)
				return;
		}
		
		try
		{
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			mLogger.warn("field assign exception! propertyTag: " + this.toString(), e);
		}
			
	}

}
