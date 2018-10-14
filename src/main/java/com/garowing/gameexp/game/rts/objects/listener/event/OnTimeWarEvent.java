package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 时间到达事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月28日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class OnTimeWarEvent extends WarEvent
{
	/**
	 * 当前时间
	 */
	private long time;
	
	public OnTimeWarEvent(WarInstance war, long time)
	{
		super(war);
		this.time = time;
	}

	public long getTime() 
	{
		return time;
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