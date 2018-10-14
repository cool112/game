package com.garowing.gameexp.game.rts.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import commons.utils.JsonUtils;

/**
 * 伤害计数器
 * @author LuMingyi
 * @date 2017年10月31日
 */
public class DamageCounter
{
	/**
	 * map.campId.objectId = damage
	 */
	private Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
	
	/**
	 * 战场实体
	 */
	private WarInstance war;
	
	public DamageCounter(WarInstance war)
	{
		this.war = war;
	}
	
	/**
	 * 增加伤害
	 * @return 是否是这个单位第一次造成伤害
	 */
	public boolean add(int campId, int objectId, int damage)
	{
		return add(map, campId, objectId, damage);
	}

	/**
	 * 增加伤害
	 * @return 是否是这个单位第一次造成伤害
	 */
	private static boolean add(Map<Integer, Map<Integer, Integer>> map, int campId, int objectId, int damage)
	{
		Map<Integer, Integer> subMap = map.get(campId);
		if (subMap == null)
		{
			subMap = new HashMap<>();
			map.put(campId, subMap);
		}
		return add(subMap, objectId, damage);
	}

	/**
	 * 增加伤害
	 * @return 是否是这个单位第一次造成伤害
	 */
	private static boolean add(Map<Integer, Integer> map, int objectId, int damage)
	{
		if (!map.containsKey(objectId))
		{
			map.put(objectId, damage);
			return true;
		}
		map.put(objectId, map.get(objectId) + damage);
		return false;
	}
	
	/**
	 * 获取一个对象造成的总伤害
	 */
	public int get(int campId, int objectId)
	{
		Map<Integer, Integer> subMap = map.get(campId);
		if (subMap == null)
			return 0;
		Integer value = subMap.get(objectId);
		return value == null ? 0 : value;
	}

	/**
	 * 获取map.objectId = damage
	 */
	public Map<Integer, Integer> getMapByCamp(int campId)
	{
		return map.get(campId);
	}
	
	/**
	 * 合并伤害
	 */
	private static void murge(Map<Integer, Integer> oriMap, WarInstance war)
	{
		Map<Integer, Integer> map = new HashMap<>();
		for (Entry<Integer, Integer> entry : oriMap.entrySet())
		{
			int objectId = entry.getKey();
			int damage = entry.getValue();
			add(map, objectId, damage);
		}
		oriMap.clear();
		oriMap.putAll(map);
	}

	/**
	 * 合并伤害
	 */
	public void murge()
	{
		for (Map<Integer, Integer> subMap : map.values())
			murge(subMap, war);
	}
	
	public String toString(int camp)
	{
		Map<Integer, Integer> subMap = map.get(camp);
		return subMap == null ? "{}" : JsonUtils.objectToJson(subMap);
	}
}
