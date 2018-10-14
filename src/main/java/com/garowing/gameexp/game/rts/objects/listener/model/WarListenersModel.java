package com.garowing.gameexp.game.rts.listener.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 战争Model
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月29日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@XmlRootElement(name = "dungeon")
@XmlAccessorType(XmlAccessType.NONE)
public class WarListenersModel
{

	@XmlAttribute(name = "dungeon_id")
	private int dungeonModelId;

	@XmlAttribute(name = "info")
	private String info;

	@XmlElementWrapper(name = "listeners")
	@XmlElement(name = "linstener")
	private List<WarListenerModel> listeners;

	public Integer gainKey()
	{
		return dungeonModelId;
	}

	public int getDungeonModelId()
	{
		return dungeonModelId;
	}

	public String getInfo()
	{
		return info;
	}

	public List<WarListenerModel> getListeners()
	{
		return listeners;
	}

}
