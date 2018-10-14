package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 单位血量改变事件
 * @author seg
 *
 */
public class UnitHpChangeSkillEvent extends AbstractSkillEvent
{
	/**
	 * 血量改变来源
	 */
	private GameObject source;
	
	/**
	 * 血量改变目标
	 */
	private Troop target;
	
	/**
	 * 此前hp
	 */
	private int beforeHp;
	
	/**
	 * 当前hp
	 */
	private int currentHp;
	
	public UnitHpChangeSkillEvent(WarInstance war, GameObject source, Troop target, int beforeHp, int currentHp)
	{
		super(war);
		this.source = source;
		this.target = target;
		this.beforeHp = beforeHp;
		this.currentHp = currentHp;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.HP_CHANGE;
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
		return "unit hp change event";
	}

	public int getBeforeHp()
	{
		return beforeHp;
	}

	public int getCurrentHp()
	{
		return currentHp;
	}

}
