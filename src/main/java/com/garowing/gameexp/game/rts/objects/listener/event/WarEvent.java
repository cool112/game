package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 战争监听事件父类
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月24日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class WarEvent  extends GameObject
{
	public WarEvent(WarInstance war)
	{
		super(war);
	}
}
