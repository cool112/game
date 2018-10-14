package com.garowing.gameexp.game.rts.skill.constants;

/**
 * 效果属性
 * @author seg
 *
 */
public interface EffectKey
{
	/**
	 * id
	 */
	String ID = "id";
	
	/**
	 * 部队类型
	 */
	String TROOP_TYPES = "troopTypes";
	
	/**
	 * cd
	 */
	String CD = "cd";
	
	/**
	 * 间隔
	 */
	String INTERVAL = "interval";
	
	/**
	 * 计算类型
	 */
	String CALCULATE_TYPE = "calculateType";
	
	/**
	 * 子效果集
	 */
	String SUB_EFFECTS = "subEffects";
	
	/**
	 * 播放控制
	 */
	String PLAY_CONTROL = "playControl";
	
	/**
	 * 因数A，公式使用
	 */
	String FACTOR_A = "factorA";
	
	/**
	 * 因数B，公式使用
	 */
	String FACTOR_B = "factorB";
	
	/**
	 * 因数C，公式使用
	 */
	String FACTOR_C = "factorC";
	
	/**
	 * 因数D，公式使用
	 */
	String FACTOR_D = "factorD";
	
	/**
	 * 因数E，公式使用
	 */
	String FACTOR_E = "factorE";
	
	/**
	 * 因数F，公式使用
	 */
	String FACTOR_F = "factorF";
	
	/**
	 * 属性串
	 */
	String ATTRIBUTES = "attributes";
	
	/**
	 * 概率
	 */
	String PROBABILITY = "probability";
	
	/**
	 * 目标选择器id
	 */
	String TARGET_SELECTOR_ID =  "targetSelectorId";
	
	/**
	 * 激活次数，持续性效果使用
	 */
	String ACTIVATE_COUNT = "activateCount";
	
	/**
	 * 是否需要目标，除了少量地面技能不需要
	 */
	String NEED_TARGET = "needTarget";
	
	/**
	 * 状态列表, buff或debuff的状态，多作为条件判断使用
	 */
	String STATUS = "status";
	
	/**
	 * 初始化延迟,毫秒
	 */
	String INIT_DELAY = "initDelay";
	
	/**
	 * 执行延迟，毫秒
	 */
	String EXEC_DELAY = "execDelay";
	
	/**
	 * 飞行速度
	 */
	String FLY_SPEED = "flySpeed";
	
	/**
	 * 单位id,attr表定义
	 */
	String ATTR_ID = "attrId";
	
	/**
	 * 数量
	 */
	String NUMBER = "number";

	/**
	 * 模板id
	 */
	String TEMPLATE = "template";

	/**
	 * 存活时间
	 */
	String LIVE_TIME = "liveTime";

	/**
	 * 结束延迟
	 */
	String END_DELAY = "endDelay";

	/**
	 * 通知类型
	 */
	String NOTIFY_TYPE = "notifyType";

	/**
	 * 能量点
	 */
	String ENERGY = "energy";
}
