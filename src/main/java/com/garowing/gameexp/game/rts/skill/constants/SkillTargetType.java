package com.garowing.gameexp.game.rts.skill.constants;

/**
 * 技能目标类型
 * @author seg
 *
 */
public interface SkillTargetType
{
	/**
	 * 无需目标
	 */
	int NO_NEED_TARGET = 0;
	
	/**
	 * 需要单位或坐标
	 */
	int NEED_UNIT_OR_POINT = 1;
	
	/**
	 * 需要单位
	 */
	int NEED_UNIT = 2;
}
