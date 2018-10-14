package com.garowing.gameexp.game.rts.skill.constants;

/**
 * 属性过滤器属性名
 * @author seg
 *
 */
public interface PropsFilterKey
{
	/**
	 * 阵营类型
	 */
	String CAMP_TYPE = "campType";
	
	/**
	 * 包含自己
	 */
	String INCLUDE_SELF = "includeSelf";
	
	/**
	 * 部队类型掩码
	 */
	String TROOP_TYPE_MASK = "troopTypeMask";
	
	/**
	 * 允许部队类型序列，顺序信息
	 */
	String ALLOW_TROOP_TYPES = "allowTroopTypes";
	
	/**
	 * 消耗能量
	 */
	String CONSUME_ENERGY = "consumeEnergy";

	/**
	 * 部队状态掩码
	 */
	String TROOP_STATUS_MASK = "troopStatusMask";

	/**
	 * 部队id集合
	 */
	String TROOP_IDS = "troopIds";
	
	/**
	 * 属性条件
	 */
	String ATTRIBUTES = "attributes";
	
	/**
	 * 召唤者类型
	 */
	String CALLER_TYPE = "callerType";
}
