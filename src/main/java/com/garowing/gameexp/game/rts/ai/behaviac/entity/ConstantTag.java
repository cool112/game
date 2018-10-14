package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.constants.BehaviacConstantType;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;

/**
 * 常量标签
 * @author seg
 * 2017年1月9日
 */
public class ConstantTag extends AbstractTag
{
	private static final Logger mLogger = LogManager.getLogger(ConstantTag.class);

	/**
	 * 值
	 */
	private String value;
	
	/**
	 * 类型描述
	 */
	private String type;
	
	/**
	 * 常量值
	 */
	private Object constantValue;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T rt(Object obj)
	{
		return (T) getConstantValue();
	}
	
	/**
	 * 获取常量值
	 * @return
	 */
	public Object getConstantValue()
	{
		if(this.constantValue != null)
			return constantValue;
		
		try
		{
			switch(type)
			{
			case BehaviacConstantType.BOOL:
				this.constantValue = Boolean.parseBoolean(value);
				return constantValue;
			case BehaviacConstantType.INT:
				this.constantValue = Integer.parseInt(value);
				return constantValue;
			case BehaviacConstantType.LONG:
				this.constantValue = Long.parseLong(value);
				return constantValue;
			case BehaviacConstantType.FLOAT:
				this.constantValue = Float.parseFloat(value);
				return constantValue;
			case BehaviacConstantType.STRING:
				constantValue = value.replaceAll("\"", "").replaceAll("\\\\", "/");
				return constantValue;
			case BehaviacConstantType.EBTSTATUS:
				constantValue = EBTStatus.valueOf(value);
				return constantValue;
			default:
				return null;
			}
		} catch (NumberFormatException e)
		{
			mLogger.warn("const value parse fail! value:["+value+"] type:["+type+"]");
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("const").append(' ').append(type).append(' ').append(value);
		return sb.toString();
	}
	

	@Override
	public String getRtType()
	{
		return getType().toLowerCase();
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

	public void setConstantValue(Object constantValue)
	{
		this.constantValue = constantValue;
	}

}
