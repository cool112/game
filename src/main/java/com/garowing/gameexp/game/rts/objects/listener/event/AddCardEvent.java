package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 增加手牌事件
 * @author zhouxiaofeng
 * 2015年11月13日
 */
public class AddCardEvent extends WarEvent
{

	public AddCardEvent(WarInstance war) {
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
