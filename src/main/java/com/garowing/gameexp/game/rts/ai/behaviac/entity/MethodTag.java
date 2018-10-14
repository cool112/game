package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.cache.AccessibleCache;
import com.garowing.gameexp.game.rts.ai.behaviac.utils.TagUtils;


/**
 * 方法标签，从cpp格式转化而来
 * @author seg
 * 2017年1月5日
 */
public class MethodTag extends AbstractTag
{
	private static final Logger mLogger = LogManager.getLogger(MethodTag.class);
	
	/**
	 * 参数列表
	 */
	private List<AbstractTag> params = new ArrayList<AbstractTag>();
	
	/**
	 * 返回类型
	 */
	private Class<?> returnType;
	
	/**
	 * 方法调用
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T invoke(Object obj)
	{
		Method method = AccessibleCache.getMethod(this);
		if(method == null)
			return null;
		
		T rt = null;
		
		if(!"Self".equals(this.instance))
		{
			obj = AccessibleCache.getClass(this.instance);
			if(obj == null)
				return null;
		}
			
		returnType = getReturnType();
		try
		{
		
			if(returnType == void.class)
			{
				method.invoke(obj, TagUtils.getParams(params, obj));
				return null;
			}
			
			rt =  (T) method.invoke(obj, TagUtils.getParams(params, obj));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			mLogger.warn("method invoke exception! methodTag: " + this.toString(), e);
		}
		
		return rt;
	}

	@Override
	public <T> T rt(Object obj)
	{
		return invoke(obj);
	}
	
	@Override
	public String toString()
	{
		if(returnType == null)
		{
			Method method = AccessibleCache.getMethod(this);
			if(method != null)
				returnType = method.getReturnType();
		}
		
		StringBuilder sb = new StringBuilder(returnType == null ? "" : returnType.getSimpleName());
		sb.append(" ").append(instance).append('.').append(className).append('.').append(name)
		.append('(');
		boolean first = true;
		for(AbstractTag param : params)
		{
			if(first)
				first = false;
			else
				sb.append(',');
			
			sb.append(param.toString());
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	/**
	 * 增加参数
	 * @param paramTag
	 */
	public void addParam(AbstractTag paramTag)
	{
		params.add(paramTag);
	}
	

	public List<AbstractTag> getParams()
	{
		return params;
	}

	public void setParams(List<AbstractTag> params)
	{
		this.params = params;
	}

	public Class<?> getReturnType()
	{
		if(returnType == null)
		{
			Method method = AccessibleCache.getMethod(this);
			if(method != null)
				returnType = method.getReturnType();
		}
		
		return returnType;
	}
	
	@Override
	public String getRtType()
	{
		Class<?> clazz = getReturnType();
		if(clazz == null || clazz == Void.class)
			return "void";
		
		return clazz.getSimpleName().toLowerCase();
	}

	public void setReturnType(Class<?> returnType)
	{
		this.returnType = returnType;
	}

	
}
