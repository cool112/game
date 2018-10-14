package com.garowing.gameexp.game.rts.skill.constants;

/**
 * 触发事件类型
 * 
 * @author zhouxiaofeng 2015年8月14日
 */
public enum TriggerEventType
{
	/**
	 * 攻击事件
	 */
	ATTACK_EVENT,

	/**
	 * 被攻击事件
	 */
	BE_ATTACK_EVENT,

	/**
	 * 死亡事件(自己死亡)
	 */
	DIE_EVENT,

	/**
	 * 死亡监听事件
	 */
	DIE_LISTENER_EVENT,

	/**
	 * 开始战斗事件
	 */
	START_FIGTH_EVENT,

	/**
	 * 杀死事件
	 */
	KILL_EVENT,

	/**
	 * 部队数量满足指定条件事件
	 */
	TROOP_COUNT,
	
	/**
	 * 使用卡牌
	 */
	USE_CARD,
	
	/**
	 * 目标改变
	 */
	TARGET_CHANGE, 
	
	/**
	 * 血量改变
	 */
	HP_CHANGE,
	
	/**
	 * 技能伤害
	 */
	SKILL_HARM,
	
	/**
	 * 被技能伤害
	 */
	BE_SKILL_HARM, 
	
	/**
	 * 使用将军技能
	 */
	USE_GENERAL_SKILL
}
