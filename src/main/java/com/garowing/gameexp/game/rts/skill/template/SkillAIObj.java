package com.garowing.gameexp.game.rts.skill.template;

import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;

/**
 * 技能ai
 * @author seg
 *
 */
abstract public class SkillAIObj
{
	/**
	 * 技能是否有效
	 * @param skill
	 * @return
	 */
	abstract public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY);
	
	/**
	 * 获取修正的优先级
	 * @param skill
	 * @return
	 */
	abstract public int getModifiedPriority(SkillEntity skill);
}
