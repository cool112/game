package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 产生一个战争代码事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月16日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class WarCodeEvent extends WarEvent
{
	private final int code;
	
	public WarCodeEvent(WarInstance war, int code)
	{
		super(war);
		this.code = code;
	}

	public int getCode() {
		return code;
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
