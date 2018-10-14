package com.garowing.gameexp.game.rts.skill.dynamic;

import java.util.Collection;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 战意(战场上部队数量越多,攻击造成额外伤害越高)
 * 
 * @author zhouxiaofeng 2015年8月5日
 */
public class UnitCountDynamic implements ModifiedValue
{

	/**
	 * 拥有者
	 */
	private HavingCamp owner;

	/**
	 * 每个单位增加值
	 */
	private float eachValue;

	/**
	 * 检查目标(0:全部,1:自己,2:敌方)
	 */
	private int cheackTarget;
	
	/**
	 * 计算类型
	 */
	private int calculType;
	
	/**
	 * 部队类型掩码
	 */
	private int troopTypeMask;

	public UnitCountDynamic(HavingCamp troopTarget, float percent, int campType, int calType, int troopTypeMask)
	{
		this.owner = troopTarget;
		this.eachValue = percent;
		this.cheackTarget = campType;
		this.calculType = calType;
		this.troopTypeMask = troopTypeMask;
	}

	@Override
	public float getValue(Troop target)
	{
		return getValue();
	}

	@Override
	public int getCalculType()
	{
		return calculType;
	}

	@Override
	public float getValue()
	{
		if (owner == null)
			return 0;

		WarInstance war = ((GameObject) owner).getWar();
		if (war == null)
			return 0;
		
		Collection<Troop> list = null;
		
		switch (cheackTarget)
		{
		case 0:
			list = WarUtils.getTroopsByCamp(war, 0, troopTypeMask);
			break;
		case 1:
			list = WarUtils.getTroopsByCamp(war, owner.getCampId(), troopTypeMask);
			break;
		case 2:
			int enemyCampId = WarUtils.getEnemyCampId(owner.getControl());
			list = WarUtils.getTroopsByCamp(war, enemyCampId, troopTypeMask);
			break;
		default:
			break;
		}
		
		if(list == null)
			return 0;
		
		int count = list.size();
		count = count < 1 ? 1 : count;
		return (count - 1) * eachValue;
	}

}
