package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.garowing.gameexp.game.rts.ai.GameObject;
import com.garowing.gameexp.game.rts.ai.war.objects.WarObjectInstance;

/**
 * 战场统计数据
 * @author seg
 *
 */
public class WarStatisData
{
	/**
	 * ai控制器的阵营id
	 */
	private int selfCampId;
	
	/**
	 * 统计时间
	 */
	private long statisTime;
	
	/**
	 * 总数
	 */
	private int total;
	
	/**
	 * 我方总数
	 */
	private int friendTotal;
	
	/**
	 * 敌方总数
	 */
	private int enemyTotal;
	
	/**
	 * 敌方建筑数
	 */
	private int enemyBuildingCount;
	
	/**
	 * 敌方可攻击空中数
	 */
	private int enemyAtkAirCount;
	
	/**
	 * 敌方远程数
	 */
	private int enemyRangeCount;
	
	/**
	 * 我方能量
	 */
	private int selfEnergy;
	
	/**
	 * 我方基地十格内敌军数
	 */
	private int enemyIn10Count;
	
	/**
	 * 自己基地血量百分比
	 */
	private float selfBaseHpPercent;
	
	/**
	 * 敌方基地血量百分比
	 */
	private float enemyBaseHpPercent;
	
	/**
	 * 我方基地15格内友军数
	 */
	private int friendIn15Count;
	
	/**
	 * 我方基地15格内生物数
	 */
	private int friendLivingIn15Count;
	
	/**
	 * 有效选项集合
	 * validMap.objectId = option
	 */
	private Map<Integer, StrategyOption> validMap = new HashMap<Integer, StrategyOption>();
	
	/**
	 * 无效选项集合
	 * invalidMap.objectId = option
	 */
	private Map<Integer, StrategyOption> invalidMap = new HashMap<Integer, StrategyOption>();
	
	/**
	 * 各条件结果集合
	 */
	private Map<Integer, Float> conditionCounterMap = new HashMap<Integer, Float>();
	
	/**
	 * 我方基地
	 */
	private WarObjectInstance selfBase;
	
	/**
	 * 敌方基地
	 */
	private WarObjectInstance enemyBase;
	
	/**
	 * 敌方能量
	 */
	private int enemyEnergy;
	
	/**
	 * 有效条件集合
	 */
	private Set<Integer> validConditionIds = new HashSet<Integer>();
	
	/**
	 * 增加总数 
	 */
	public void addTotal()
	{
		this.total++;
	}
	
	/**
	 * 友军数量
	 */
	public void addFriendTotal()
	{
		this.friendTotal++;
	}
	
	/**
	 * 敌方数量
	 */
	public void addEnemyTotal()
	{
		this.enemyTotal++;
	}
	
	/**
	 * 增加基地15码内友军
	 */
	public void addFriendIn15()
	{
		this.friendIn15Count++;
	}
	
	/**
	 * 增加10码内敌人数
	 */
	public void addEnemyIn10()
	{
		this.enemyIn10Count++;
	}
	
	/**
	 * 增加敌人建筑数
	 */
	public void addEnemyBuildingCount()
	{
		this.enemyBuildingCount++;
	}
	
	/**
	 * 增敌军加对空数
	 */
	public void addEnemyAtkAirCount()
	{
		this.enemyAtkAirCount++;
	}

	/**
	 * 增加敌人远程数
	 */
	public void addEnemyRangeCount()
	{
		this.enemyRangeCount++;
	}
	
	/**
	 * 增加有效选项
	 * @param option
	 */
	public void addValidOption(StrategyOption option)
	{
		validMap.put(((GameObject)option).getObjectId(), option);
	}
	
	/**
	 * 增加无效选项
	 * @param option
	 */
	public void addInvalidOption(StrategyOption option)
	{
		invalidMap.put(((GameObject)option).getObjectId(), option);
	}
	
	/**
	 * 增加基地范围内友方生物数
	 */
	public void addFriendLivingIn15()
	{
		friendLivingIn15Count++;
	}
	
	/**
	 * 为条件值计数
	 * @param modelId
	 */
	public void addConditionCounter(int modelId)
	{
		Float preVal = conditionCounterMap.get(modelId);
		if(preVal == null)
		{
			preVal = 0f;
		}
		
		conditionCounterMap.put(modelId, preVal + 1); 
	}
	
	/**
	 * 获取条件值
	 * @param modelId
	 * @return
	 */
	public float getConditionCounter(int modelId)
	{
		Float count = conditionCounterMap.get(modelId);
		if(count == null)
			return 0;
		
		return count;
	}
	
	/**
	 * 设置条件值
	 * @param modelId
	 * @param energy
	 */
	public void setConditionCounter(int modelId, float value)
	{
		conditionCounterMap.put(modelId, value);
	}
	
	/**
	 * 是否已包含条件值
	 * @param modelId
	 * @return
	 */
	public boolean containsConditionId(int modelId)
	{
		Float value = conditionCounterMap.get(modelId);
		return !(value == null);
	}
	
	public int getEnemyEnergy()
	{
		return enemyEnergy;
	}
	
	public int getTotal()
	{
		return total;
	}

	public void setTotal(int total)
	{
		this.total = total;
	}

	public int getFriendTotal()
	{
		return friendTotal;
	}

	public void setFriendTotal(int friendTotal)
	{
		this.friendTotal = friendTotal;
	}

	public int getEnemyTotal()
	{
		return enemyTotal;
	}

	public void setEnemyTotal(int enemyTotal)
	{
		this.enemyTotal = enemyTotal;
	}

	public int getEnemyBuildingCount()
	{
		return enemyBuildingCount;
	}

	public void setEnemyBuildingCount(int enemyBuildingCount)
	{
		this.enemyBuildingCount = enemyBuildingCount;
	}

	public int getEnemyAtkAirCount()
	{
		return enemyAtkAirCount;
	}

	public void setEnemyAtkAirCount(int enemyAtkAirCount)
	{
		this.enemyAtkAirCount = enemyAtkAirCount;
	}

	public int getEnemyRangeCount()
	{
		return enemyRangeCount;
	}

	public void setEnemyRangeCount(int enemyRangeCount)
	{
		this.enemyRangeCount = enemyRangeCount;
	}

	public int getSelfEnergy()
	{
		return selfEnergy;
	}

	public void setSelfEnergy(int selfEnergy)
	{
		this.selfEnergy = selfEnergy;
	}

	public int getEnemyIn10Count()
	{
		return enemyIn10Count;
	}

	public void setEnemyIn10Count(int enemyIn10Count)
	{
		this.enemyIn10Count = enemyIn10Count;
	}

	public float getSelfBaseHpPercent()
	{
		return selfBaseHpPercent;
	}

	public void setSelfBaseHpPercent(float selfBaseHpPercent)
	{
		this.selfBaseHpPercent = selfBaseHpPercent;
	}

	public float getEnemyBaseHpPercent()
	{
		return enemyBaseHpPercent;
	}

	public void setEnemyBaseHpPercent(float enemyBaseHpPercent)
	{
		this.enemyBaseHpPercent = enemyBaseHpPercent;
	}

	public int getFriendIn15Count()
	{
		return friendIn15Count;
	}

	public void setFriendIn15Count(int friendIn15Count)
	{
		this.friendIn15Count = friendIn15Count;
	}

	public long getStatisTime()
	{
		return statisTime;
	}

	public void setStatisTime(long statisTime)
	{
		this.statisTime = statisTime;
	}

	public Map<Integer, StrategyOption> getValidMap()
	{
		return validMap;
	}

	public Map<Integer, StrategyOption> getInvalidMap()
	{
		return invalidMap;
	}

	public int getFriendLivingIn15Count()
	{
		return friendLivingIn15Count;
	}

	public void setFriendLivingIn15Count(int friendLivingIn15Count)
	{
		this.friendLivingIn15Count = friendLivingIn15Count;
	}

	public WarObjectInstance getSelfBase()
	{
		return selfBase;
	}

	public void setSelfBase(WarObjectInstance selfBase)
	{
		this.selfBase = selfBase;
	}

	public WarObjectInstance getEnemyBase()
	{
		return enemyBase;
	}

	public void setEnemyBase(WarObjectInstance enemyBase)
	{
		this.enemyBase = enemyBase;
	}

	public int getSelfCampId()
	{
		return selfCampId;
	}

	public void setSelfCampId(int selfCampId)
	{
		this.selfCampId = selfCampId;
	}

	public Set<Integer> getValidConditionIds()
	{
		return validConditionIds;
	}

	public void setValidConditionIds(Set<Integer> validConditionIds)
	{
		this.validConditionIds = validConditionIds;
	}

	public void setEnemyEnergy(int enemyEnergy)
	{
		this.enemyEnergy = enemyEnergy;
	}


}
