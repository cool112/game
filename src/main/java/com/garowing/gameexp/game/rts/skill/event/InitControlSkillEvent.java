package com.garowing.gameexp.game.rts.skill.event;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 初始化控制器事件
 * @author seg
 * 2017年3月25日
 */
public class InitControlSkillEvent extends AbstractSkillEvent
{
	/**
	 * 控制器
	 */
	private AbsFightControl control;
	
	public InitControlSkillEvent(WarInstance war, AbsFightControl control)
	{
		super(war);
		this.control = control;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.INIT_CONTROL;
	}

	@Override
	public GameObject getSource()
	{
		return control;
	}

	@Override
	public GameObject getTarget()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "初始化控制器事件 control:[" + control + "]";
	}

}
