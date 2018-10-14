package com.garowing.gameexp.game.rts.skill.filter.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterKey;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterType;
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

import commons.configuration.Property;

/**
 * 队伍id过滤器
 * @author seg
 *
 */
public class TroopIdFilter implements UnitPropsFilter
{
	/**
	 * 允许的部队id
	 */
	@Property(key = PropsFilterKey.TROOP_IDS, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	private List<Integer> troopIds;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WarObjectInstance> filter(Projectile data, GameObject caster, Collection<WarObjectInstance> list)
	{
		if(list == null || list.isEmpty())
			return Collections.EMPTY_LIST;
		
		List<WarObjectInstance> newList = new ArrayList<WarObjectInstance>();
		for(WarObjectInstance target : list)
		{
			if(isInList(target.getModelID()))
				newList.add(target);
		}
		
		return newList;
	}
	
	/**
	 * 是否在列表中
	 * @param id
	 * @return
	 */
	private boolean isInList(int id)
	{
		if(troopIds == null)
			return true;
		
		for(Integer troopId : troopIds)
		{
			if(troopId.equals(id))
				return true;
		}
		
		return false;
	}

	@Override
	public int getType()
	{
		return PropsFilterType.TROOP_ID.getCode();
	}

}
