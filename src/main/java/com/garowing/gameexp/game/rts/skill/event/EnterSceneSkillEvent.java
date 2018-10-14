package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 进入战场事件
 * @author seg
 * 2017年3月25日
 */
public class EnterSceneSkillEvent extends AbstractSkillEvent
{
	/**
	 * 本体
	 */
	private Troop source;
	
	public EnterSceneSkillEvent(WarInstance war, Troop source)
	{
		super(war);
		this.source = source;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.ENTER_SCENE;
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
		return "进入战场事件 source:[" + source + "]";
	}

}
