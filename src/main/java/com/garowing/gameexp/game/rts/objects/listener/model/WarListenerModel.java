package com.garowing.gameexp.game.rts.listener.model;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.garowing.gameexp.game.rts.listener.objects.WarListenerConditionType;
import com.garowing.gameexp.game.rts.listener.objects.WarListenerType;

/**
 * 战争监听器Model
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月29日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@XmlRootElement(name = "linstener")
@XmlAccessorType(XmlAccessType.NONE)
public class WarListenerModel 
{
	
	@XmlAttribute(name="dun_linstener_script")
	private int scriptId;
	
	@XmlAttribute(name="data")
	private String data;
	
	@XmlAttribute(name="info")
	private String info;
	
	@XmlAttribute(name="type")
	private WarListenerType type;
	
	@XmlAttribute(name = "condition_type")
	private WarListenerConditionType conditionType;

	@XmlElement(name="action")
	private List<WarActionModel> actionModels;
	
	@XmlElement(name = "condition")
	private List<WarConditionModel> conditionModels;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		if(conditionType == null)
			conditionType = WarListenerConditionType.AND;
	}

	public int getScriptId() {
		return scriptId;
	}

	public String getData() {
		return data;
	}

	public String getInfo() {
		return info;
	}

	public List<WarActionModel> getActions() {
		return actionModels;
	}

	public WarListenerType getType() {
		return type;
	}

	public WarListenerConditionType getConditionType(){
		return conditionType;
	}

	public List<WarConditionModel> getConditionModels() {
		return conditionModels;
	}
}
