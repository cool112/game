package com.garowing.gameexp.game.rts.skill.entity;

import java.util.Map;

/**
 * 护盾减少结果
 * @author seg
 *
 */
public class ShieldReduceResult
{
	/**
	 * 剩余伤害
	 */
	private float damage;
	
	/**
	 * 护盾减少集合
	 */
	private Map<Integer, Integer> shieldReduceMap;
	
	public ShieldReduceResult(float damage, Map<Integer, Integer> shieldReduceMap)
	{
		super();
		this.damage = damage;
		this.shieldReduceMap = shieldReduceMap;
	}

	public float getDamage()
	{
		return damage;
	}

	public void setDamage(float damage)
	{
		this.damage = damage;
	}

	public Map<Integer, Integer> getShieldReduceMap()
	{
		return shieldReduceMap;
	}

	public void setShieldReduceMap(Map<Integer, Integer> shieldReduceMap)
	{
		this.shieldReduceMap = shieldReduceMap;
	}
	
	
	
}
