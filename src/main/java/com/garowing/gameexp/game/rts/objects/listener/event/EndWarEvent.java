package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 结束战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年6月12日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class EndWarEvent extends WarEvent
{
	private final int winCampID;
	
	public EndWarEvent(WarInstance war, int winCampID)
	{
		super(war);
		this.winCampID = winCampID;
	}

	public int getWinCampID() {
		return winCampID;
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