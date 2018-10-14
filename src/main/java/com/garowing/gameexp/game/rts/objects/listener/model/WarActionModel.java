package com.garowing.gameexp.game.rts.listener.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.yeto.war.module.troop.model.TroopBornModel;

/**
 * 战争行为Model
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月29日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@XmlRootElement(name = "action")
@XmlAccessorType(XmlAccessType.NONE)
public class WarActionModel 
{
	@XmlAttribute(name="dun_action_script")
	private int scriptId;
	
	@XmlAttribute(name="data")
	private String data;
	
	@XmlAttribute(name="info")
	private String info;
	
	@XmlElement(name="troop_born")
	private List<TroopBornModel> borns;
	
	@XmlElement(name="linstener")
	private List<WarListenerModel> linstenerModels;

	public int getScriptId() {
		return scriptId;
	}

	public String getData() {
		return data;
	}

	public String getInfo() {
		return info;
	}

	public List<TroopBornModel> getBorns() {
		return borns;
	}
	
	public List<WarListenerModel> getLinstenerModels() {
		return linstenerModels;
	}

}
