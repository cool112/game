package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 默认的事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年6月12日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class DefultWarEvent extends WarEvent
{
	public DefultWarEvent(WarInstance war)
	{
		super(war);
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.WAR_EVENT;
	}
}