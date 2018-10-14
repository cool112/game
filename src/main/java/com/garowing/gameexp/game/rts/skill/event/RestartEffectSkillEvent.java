package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 重置效果事件
 * @author seg
 *
 */
public class RestartEffectSkillEvent extends AbstractSkillEvent
{
	/**
	 * 重置源
	 */
	private GameObject source;
	
	/**
	 * 目标效果
	 */
	private EffectEntity target;
	
	public RestartEffectSkillEvent(WarInstance war, GameObject source, EffectEntity target)
	{
		super(war);
		this.source = source;
		this.target = target;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.RESTART_EFFECT;
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
		return "restart effect event source[" + source + "] target[" + target + "]";
	}

}
