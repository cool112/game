package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 引导战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月20日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class GuidanceWarEvent extends WarEvent
{
	
	/**
	 * 引导ID
	 */
	private int guidanceId; 
	
	public GuidanceWarEvent (WarInstance war, int guidanceId)
	{
		super(war);
		this.guidanceId = guidanceId;
	}

	public int getGuidanceId() {
		return guidanceId;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.WAR_EVENT;
	}
	
}