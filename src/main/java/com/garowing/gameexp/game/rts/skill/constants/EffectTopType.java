package com.garowing.gameexp.game.rts.skill.constants;

/**
 * 效果一级类型
 * @author seg
 *
 */
public interface EffectTopType
{
	/**
	 * 主动效果
	 */
	int ACTIVE = 1;
	
	/**
	 * buff
	 */
	int BUFF = 2;
	
	/**
	 * 触发
	 */
	int TRIGGER = 3;
	
	/**
	 * 召唤效果
	 */
	int SUMMON = 4;
	
	/**
	 * 光环，效果生成器
	 */
	int AUREOLE = 5;
}
