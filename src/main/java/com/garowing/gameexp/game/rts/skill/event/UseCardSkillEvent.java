package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.card.CardObject;

/**
 * 使用卡牌事件
 * @author seg
 *
 */
public class UseCardSkillEvent extends AbstractSkillEvent
{
	/**
	 * 来源卡牌
	 */
	private CardObject source;
	
	public UseCardSkillEvent(WarInstance war, CardObject card)
	{
		super(war);
		this.source = card;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.USE_CARD;
	}

	@Override
	public GameObject getSource()
	{
		return source;
	}

	@Override
	public GameObject getTarget()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "use card event[" + source + "]";
	}

}
