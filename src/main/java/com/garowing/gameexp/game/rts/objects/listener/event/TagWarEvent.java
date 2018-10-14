package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 标记战争事件
 * @author 		John.张建
 * @date		2015年11月6日
 * @version 	1.0
 */
public class TagWarEvent extends WarEvent
{
	
	public TagWarEvent (WarInstance war)
	{
		super(war);
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
