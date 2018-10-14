package com.garowing.gameexp.game.rts.skill.handler;

import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;

/**
 * 技能事件处理器
 * @author seg
 * 2017年3月24日
 */
public interface SkillEventHandler
{
	/**
	 * 处理事件
	 * @param event
	 * @param currTime 
	 */
	public void handle(AbstractSkillEvent event, long currTime);
}
