package com.garowing.gameexp.game.rts.skill.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 场上怪物数技能事件
 * @author seg
 *
 */
public class TroopNumSkillEvent extends AbstractSkillEvent
{
	/**
	 * 场上目标数集合
	 * troopCountMap.campId.controlId = count
	 */
	private Map<Integer, Map<Integer, Integer>> troopCountMap = new HashMap<Integer, Map<Integer,Integer>>();
	
	public TroopNumSkillEvent(WarInstance war)
	{
		super(war);
		Collection<WarObjectInstance> objs = WarUtils.getObjects(war);
		for(WarObjectInstance obj : objs)
		{
			Map<Integer, Integer> campMap = troopCountMap.get(obj.getCampId());
			if(campMap == null)
			{
				campMap = new HashMap<Integer, Integer>();
				troopCountMap.put(obj.getCampId(), campMap);
			}
			
			Integer count = campMap.get(obj.getControl().getObjectId());
			if(count == null)
			{
				count = 0;
				campMap.put(obj.getControl().getObjectId(), count);
			}
			
			campMap.put(obj.getControl().getObjectId(), count + 1);
		}
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.TROOP_COUNT_CHANGE;
	}
	
	/**
	 * 获取总数
	 * @return
	 */
	public int getAllCount()
	{
		int total = 0;
		for(Map<Integer, Integer> controlMap : troopCountMap.values())
		{
			for(Integer count : controlMap.values())
				total += count;
		}
		return total;
	}

	@Override
	public GameObject getSource()
	{
		return null;
	}

	@Override
	public GameObject getTarget()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "troop count change event countMap[" + troopCountMap + "]";
	}

	/**
	 * 获取自己方数量
	 * @param campId
	 * @param controllorId
	 * @return
	 */
	public int getSelfCount(int campId, int controllorId)
	{
		Map<Integer, Integer> controlMap = troopCountMap.get(campId);
		if(controlMap == null)
			return 0;
		
		Integer total = controlMap.get(controllorId);
		if(total == null)
			return 0;
		
		return total;
	}

	/**
	 * 获取友方数量
	 * @param campId
	 * @param controllorId
	 * @return
	 */
	public int getFriendlyCount(int campId, int controllorId)
	{
		Map<Integer, Integer> controlMap = troopCountMap.get(campId);
		if(controlMap == null)
			return 0;
		
		int total = 0;
		for(Entry<Integer, Integer> entry : controlMap.entrySet())
		{
			if(entry.getKey() != controllorId)
				total += entry.getValue();
		}
		
		return total;
	}
	
	/**
	 * 获取敌方数量
	 * @param campId
	 * @return
	 */
	public int getEnemyCount(int campId)
	{
		int total = 0;
		for(Entry<Integer, Map<Integer, Integer>> controlMap : troopCountMap.entrySet())
		{
			if(controlMap.getKey() == campId)
				continue;
			
			for(Integer count : controlMap.getValue().values())
				total += count;
		}
		return total;
	}

}
