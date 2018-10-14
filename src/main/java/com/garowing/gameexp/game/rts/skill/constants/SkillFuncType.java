package com.garowing.gameexp.game.rts.skill.constants;

/**
 * 技能功能类型
 * @author seg
 * 2017年5月8日
 */
public enum SkillFuncType
{
	/**
	 * 无类型
	 */
	NONE(0),
	
	/**
	 * 伤害
	 */
	DAMAGE(1),
	
	/**
	 * 治疗
	 */
	HEAL(2),
	
	/**
	 * 保护
	 */
	PROTECTED(3),
	
	/**
	 * 控场，增强和弱化也包含
	 */
	CONTROL(4),
	
	/**
	 * 强化
	 */
	ENHANCE(5),
	
	/**
	 * 召唤
	 */
	SUMMON(6),
	
	/**
	 * 弱化
	 */
	WEAKEN(7),
	
	/**
	 * 回复法力
	 */
	MANA(8),
	
	/**
	 * 区域
	 */
	AREA(9),
	
	/**
	 * 全体
	 */
	GLOBLE(10);
	
	
	/**
	 * 编号
	 */
	private int code;

	private SkillFuncType(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

}
