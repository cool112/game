package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 技能伤害事件
 * @author seg
 *
 */
public class SkillHarmSkillEvent extends AbstractSkillEvent
{
	/**
	 * 来源
	 */
	private EffectEntity source;
	
	/**
	 * 目标
	 */
	private GameObject target;
	
	/**
	 * 伤害数值
	 */
	private int damage;
	
	public SkillHarmSkillEvent(WarInstance war, EffectEntity source, GameObject target, int damage)
	{
		super(war);
		this.source = source;
		this.target = target;
		this.damage = damage;
	}
	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.SKILL_HARM;
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
		return "skill harm event. source[" + source + "] target[" + target + "]";
	}
	public int getDamage()
	{
		return damage;
	}

}
