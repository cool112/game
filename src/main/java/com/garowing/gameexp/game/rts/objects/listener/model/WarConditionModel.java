package com.garowing.gameexp.game.rts.listener.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 战争条件Model
 * @author John.zero
 */
@XmlRootElement(name = "condition")
@XmlAccessorType(XmlAccessType.NONE)
public class WarConditionModel 
{
	/**
	 * 脚本ID
	 */
	@XmlAttribute(name="condition_script")
	private int scriptId;
	
	/**
	 * 数据
	 */
	@XmlAttribute(name="data")
	private String data;
	
	/**
	 * 描述
	 */
	@XmlAttribute(name="info")
	private String info;

	public int getScriptId() 
	{
		return scriptId;
	}

	public String getData() 
	{
		return data;
	}

}
