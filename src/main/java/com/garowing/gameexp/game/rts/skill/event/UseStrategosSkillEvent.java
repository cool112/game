package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 施放将军技能事件
 * @author seg
 * 2017年3月25日
 */
public class UseStrategosSkillEvent extends AbstractSkillEvent
{
	/**
	 * 技能实例
	 */
	SkillEntity skill;
	
	public UseStrategosSkillEvent(WarInstance war, SkillEntity skill)
	{
		super(war);
		this.skill = skill;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.USE_GENERAL_SKILL;
	}

	@Override
	public GameObject getSource()
	{
		return skill;
	}

	@Override
	public GameObject getTarget()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "使用将军技能事件 skill:[" + skill + "]";
	}

}
