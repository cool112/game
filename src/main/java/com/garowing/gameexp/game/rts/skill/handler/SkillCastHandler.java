package com.garowing.gameexp.game.rts.skill.handler;

import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;

/**
 * 技能施放处理
 * @author seg
 * 2017年3月24日
 */
public interface SkillCastHandler
{
	/**
	 * 释放前处理
	 * @param skill
	 */
	public SkillError beforeCast(SkillEntity skill, long currTime);
	
	/**
	 * 成功
	 * @param skill
	 */
	public void onSuc(SkillEntity skill, long currTime);
	
	/**
	 * 失败
	 * @param skill
	 */
	public void onFail(SkillError error, SkillEntity skill);
}
