package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import com.yeto.war.module.ai.template.position.StrategyPosition;
import com.yeto.war.utils.RatioUitl.IWeight;

/**
 * 策略选项
 * @author seg
 *
 */
public interface StrategyOption extends IWeight
{
	/**
	 * 消耗能量
	 * @return
	 */
	public int getCostEnergy();
	
	/**
	 * 是否在cd中
	 * @return
	 */
	public boolean isCd(long currTime);
	
	/**
	 * 更新权重
	 * @param data
	 */
	public void updateWeight(WarStatisData data);
	
	/**
	 * 获取已选择数
	 * @return
	 */
	public int getSelectCount();
	
	/**
	 * 获取类型
	 * @return
	 */
	public int getOptionType();
	
	/**
	 * 获取定位ai
	 * @return
	 */
	public StrategyPosition getAiPosition();
}
