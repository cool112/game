package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;

/**
 * 使用将军技能战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月30日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class UseGeneralSkillWarEvent extends WarEvent
{
	private AbsFightControl control;
	private int skillId;
	private int level;
	
	public UseGeneralSkillWarEvent(AbsFightControl control, int skillId, int level)
	{
		super(control.getWar());
		this.control = control;
		this.skillId = skillId;
		this.level = level;
	}

	public AbsFightControl getCaseer() {
		return control;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.WAR_EVENT;
	}

}
