package com.garowing.gameexp.game.rts.skill.script.playControl.skillplay;

import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.SkillPlayControllor;

import commons.configuration.Property;

/**
 * 固定时间播放控制器
 * @author seg
 * 2017年3月15日
 */
@ScriptName(name = "fixTimeSp")
public class FixTimeSkillPlayControllor extends SkillPlayControllor
{
	/**
	 * 初始延迟时间，单位毫秒
	 */
	@Property(key="initDelay", defaultValue="0")
	private long initDelay;
	
	@Property(key="waitTime", defaultValue="0")
	private long waitTime;
	
	@Override
	public long getWaitTime(SkillEntity skill, long currTime)
	{
		long remainTime = skill.getStartTime() + waitTime - currTime;
		if(remainTime < 0)
			remainTime = 0;
		
		return remainTime;
	}

	@Override
	public long getInitDelay(SkillEntity skill, long currTime)
	{
		return initDelay;
	}

}
