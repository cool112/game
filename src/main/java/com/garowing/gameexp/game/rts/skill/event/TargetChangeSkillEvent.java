package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 目标改变事件
 * @author seg
 *
 */
public class TargetChangeSkillEvent extends AbstractSkillEvent
{
	/**
	 * 来源
	 */
	private Troop source;
	
	/**
	 * 目标
	 */
	private Troop target;
	
	public TargetChangeSkillEvent(WarInstance war, Troop source, Troop target)
	{
		super(war);
		this.source = source;
		this.target = target;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.TARGET_CHANGE;
	}

	@Override
	public GameObject getSource()
	{
		return source;
	}

	@Override
	public GameObject getTarget()
	{
		return target;
	}

	@Override
	public String getName()
	{
		return "target change event. source[" + source + "] target[" + target + "]";
	}

}
