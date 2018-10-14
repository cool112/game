package com.garowing.gameexp.game.rts.skill.dynamic;

import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 战斗饥渴(攻击者生命越低,造成的额外伤害越高)
 * @author zhouxiaofeng
 * 2015年8月5日
 */
public class FightHungryDynamic implements ModifiedValue{
	
	/**
	 * 拥有者
	 */
	private WarObjectInstance owner;
	
	/**
	 * 伤害加成百分比
	 */
	private float harmPercent;
	
	/**
	 * 计算类型
	 */
	private int calculType;
	
	public FightHungryDynamic(WarObjectInstance owner, float harmPercent, int calculType){
		this.owner = owner;
		this.harmPercent = harmPercent;
		this.calculType = calculType;
	}
	
	@Override
	public float getValue(Troop target) {
		if(owner == null){
			return 0;
		}
		float maxHp = owner.getMaxHp();
		float hp = owner.getHp();
		return harmPercent * (maxHp - hp) / maxHp;
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
