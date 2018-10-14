package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 攻击技能事件
 * @author seg
 * 2017年3月24日
 */
public class AttackSkillEvent extends AbstractSkillEvent
{
	/**
	 * 攻击者
	 */
	private EffectEntity source;
	
	/**
	 * 目标
	 */
	private Troop target;
	
	public AttackSkillEvent(WarInstance war, EffectEntity source, Troop target)
	{
		super(war);
		this.source = source;
		this.target = target;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.ATTACK;
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
		return "攻击事件 source:[" + source + "] target:[" + target + "]";
	}

}
