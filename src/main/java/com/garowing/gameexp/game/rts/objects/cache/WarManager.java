package com.garowing.gameexp.game.rts.objects.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 战争管理器
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月14日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public final class WarManager 
{
	
	// 所有战争集合
	private final Map<Integer, WarInstance> warCollection = new ConcurrentHashMap<>();

	public int gainWarCollCount ()
	{
		return this.warCollection.size();
	}
	
	public WarInstance getWar(int id)
	{
		return warCollection.get(id);
	}
	
	public void registWar(WarInstance war)
	{
		if(warCollection.putIfAbsent(war.getObjectId(), war) != null)
		{
			throw new DuplicateFormatFlagsException("战争[" + war.getModel().getId() + "]实例ID[" + war.getObjectId() + "]已经存在");
		}
	}
	
	public WarInstance unRegistWar(WarInstance object)
	{
		return warCollection.remove(object.getObjectId());
	}
	
	public void doOnAllWars(BiConsumer<Integer, WarInstance> action)
	{
		warCollection.forEach(action);
	}
	
	public List<WarInstance> findWars(Predicate<WarInstance> conditions)
	{
		return warCollection.values().stream().filter(conditions).collect(Collectors.toList());
	}
	
	public Collection<WarInstance> unmodifiableWars()
	{
		return Collections.unmodifiableCollection(warCollection.values());
	}
	
	public Collection<WarInstance> copyWars()
	{
		return new ArrayList<>(warCollection.values());
	}
	
	public void clearWars ()
	{
		warCollection.clear();
	}
}
