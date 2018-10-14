package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 部队死亡事件
 * @author seg
 *
 */
public class TroopDieSkillEvent extends AbstractSkillEvent
{
	/**
	 * 死亡来源
	 */
	private GameObject source;
	
	/**
	 * 死亡目标
	 */
	private Troop target;
	
	public TroopDieSkillEvent(WarInstance war, GameObject source, Troop target)
	{
		super(war);
		this.source = source;
		this.target = target;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.TROOP_DIE;
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
		return "troop die event source[" + source + "] target[" + target + "]";
	}

}
