package com.garowing.gameexp.game.rts.skill.dynamic;

import com.yeto.war.module.troop.Troop;

/**
 * 修正值
 * @author zhouxiaofeng
 * 2015年7月31日
 */
public interface ModifiedValue {
	
	/**
	 * 获取加成类型
	 * @return
	 */
	public int getCalculType();
	
	/**
	 * 根据目标计算值
	 * @param target 攻击的目标
	 * @return
	 */
	public float getValue(Troop target);
	
	/**
	 * 获取值,无目标情况
	 * @return
	 */
	public float getValue();
	
}
