package com.garowing.gameexp.game.rts.skill.dynamic;

import com.yeto.war.module.troop.Troop;


/**
 * 目标hp检查
 * @author zhouxiaofeng
 * 2015年8月5日
 */
public class CheckHpDynamic implements ModifiedValue{
	
	/**
	 * 额外伤害百分比
	 */
	private float harmPercent;
	
	/**
	 * 触发效果的HP百分比
	 */
	private float hpPercent;
	
	/**
	 * 是否大于(true:大于 false:小于)
	 */
	private boolean isBigger;
	
	/**
	 * 加成类型
	 */
	private int calculType;
	

	public CheckHpDynamic(float harmPercent, float hpPercent, boolean isBigger, int calculType){
		this.harmPercent = harmPercent;
		this.hpPercent = hpPercent;
		this.isBigger = isBigger;
		this.calculType = calculType;
	}
	
	@Override
	public float getValue(Troop target) {
		if(target == null){
			return 0;
		}
		float maxHp = target.getMaxHp();
		float percent = target.getHp() / maxHp;
		boolean isTrigger = false;
		if(isBigger){
			isTrigger = percent >= hpPercent;
		}else{
			isTrigger = percent <= hpPercent;
		}
		
		if(isTrigger){
			return harmPercent;
		}
		return 0;
	}

	@Override
	public int getCalculType() {
		return calculType;
	}

	@Override
	public float getValue()
	{
		return 0;
	}
	
}
