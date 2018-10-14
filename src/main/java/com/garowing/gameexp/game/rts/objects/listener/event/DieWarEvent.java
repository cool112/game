package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 部队死亡战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月28日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class DieWarEvent extends WarEvent
{
	
	/**
	 * 死亡部队
	 */
	private WarObjectInstance visible; 
	
	public DieWarEvent (WarObjectInstance visible)
	{
		super(visible.getWar());
		this.visible = visible;
	}

	public WarObjectInstance getVisible() {
		return visible;
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
