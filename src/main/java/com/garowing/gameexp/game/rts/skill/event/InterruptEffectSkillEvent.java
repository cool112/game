package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 中断效果事件
 * @author seg
 *
 */
public class InterruptEffectSkillEvent extends AbstractSkillEvent
{
	/**
	 * 强制结束来源
	 */
	private GameObject source;
	
	/**
	 * 目标效果
	 */
	private EffectEntity target;
	
	/**
	 * 是否强制中断
	 */
	private boolean isForce;
	
	public InterruptEffectSkillEvent(WarInstance war, GameObject source, EffectEntity target, boolean isForce)
	{
		super(war);
		this.source = source;
		this.target = target;
		this.isForce = isForce;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.INTERRUPT;
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
		return "skill force end event, source[" + source + "] target[" + target + "]";
	}

	public boolean isForce()
	{
		return isForce;
	}

	
}
