package com.garowing.gameexp.game.rts.skill.filter.skill;

import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;

/**
 * 技能过滤器
 * @author seg
 *
 */
public interface SkillFilter
{
	/**
	 * 是否有效
	 * @param skill
	 * @return
	 */
	public boolean isValid(SkillEntity skill);
}
