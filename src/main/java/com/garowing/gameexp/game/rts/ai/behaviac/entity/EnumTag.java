package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;

/**
 * 枚举标签
 * @author seg
 * 2017年1月9日
 */
public class EnumTag extends AbstractTag
{
	private static final Logger mLogger = LogManager.getLogger(EnumTag.class);
	
	/**
	 *字符串值 
	 */
	private String value;
	
	/**
	 * 当前值
	 */
	private Object curValue;
	
	/**
	 * 类型，boolean 、 ebtstatus、phase
	 */
	private String type;
	
	/**
	 * obj传入枚举类可以获得对应枚举常量<br/>
	 * 注意false/true归于枚举并且不需要输入参数(与const bool true这种常量本质没有区别，格式差异)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T rt(Object obj)
	{
		if(curValue != null)
			return (T) curValue;
		
		curValue = getEnumConstant(obj);
		
		return (T) curValue;
	}
	
	/**
	 * 获取枚举常量, 如果并非枚举则返回value字符串
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getEnumConstant(Object obj)
	{
		if("true".equals(value))
			return (T) Boolean.TRUE;
		
		if("false".equals(value))
			return (T) Boolean.FALSE;
		
		if("ebtstatus".equals(getRtType()))
			return (T) EBTStatus.valueOf(value);
		
		if(obj instanceof Class)
		try
		{
			T enumConst = (T)Enum.valueOf((Class) obj, value);
			return enumConst;
		} catch (Exception e)
		{
			mLogger.warn("EnumTag rt fail! value:["+ value +"] class:["+obj+"]",e);
		}
		
		return (T) value;
	}

	@Override
	public String getRtType()
	{
		if(type != null)
			return type;
		if("true".equals(value) || "false".equals(value))
		{
			type = "boolean";
			return type;
		}
		else if(value.startsWith("BT_"))
		{
			type = "ebtstatus";
			return type;
		}
		return null;
	}
	
	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
}
