package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.card.CardObject;

/**
 * 使用卡牌战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月30日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class UseCardWarEvent extends WarEvent
{
	private CardObject card;
	
	public UseCardWarEvent(WarInstance war, CardObject card)
	{
		super(war);
		this.card = card;
	}

	public int getCardId() {
		return card.getCardModelId();
	}

	public int getLevel() {
		return card.getStar();
	}

	public int getCardQuality() {
		return card.getCardQuality();
	}

	@Override
	public String getName()
	{
		return "card event";
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.WAR_EVENT;
	}

	public CardObject getCard()
	{
		return card;
	}

	
}
