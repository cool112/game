package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 技能触发事件
 * @author seg
 * 2017年3月24日
 */
public abstract class AbstractSkillEvent extends GameObject
{
	public AbstractSkillEvent(WarInstance war)
	{
		super(war);
	}

	/**
	 * 获取事件类型
	 * @return
	 */
	abstract public SkillEventType getEventType();
	
	/**
	 * 获取发起者
	 * @return
	 */
	abstract public GameObject getSource();
	
	/**
	 * 获取目标
	 * @return
	 */
	abstract public GameObject getTarget();
	
	@Override
	public int getObjectType()
	{
		return GameObjectType.SKILL_EVENT;
	}
}
