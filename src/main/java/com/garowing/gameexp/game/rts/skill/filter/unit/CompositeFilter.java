package com.garowing.gameexp.game.rts.skill.filter.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterType;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 组合filter
 * @author seg
 *
 */
public class CompositeFilter implements UnitPropsFilter
{
	/**
	 * 过滤器id集合
	 */
	private List<Integer> filterIds;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WarObjectInstance> filter(Projectile data, GameObject caster, Collection<WarObjectInstance> list)
	{
		if(caster == null || list == null)
			return Collections.EMPTY_LIST;
		
		List<WarObjectInstance> newList = new ArrayList<WarObjectInstance>(list);
		for(Integer filterId : filterIds)
		{
			UnitPropsFilter filter = StaticDataManager.PROPS_FILTER_DATA.getFilter(filterId);
			if(filter == null)
				continue;
			
			newList = filter.filter(data, caster, newList);
		}
		
		return newList;
	}

	@Override
	public int getType()
	{
		return PropsFilterType.COMPOSITE.getCode();
	}

	public List<Integer> getFilterIds()
	{
		return filterIds;
	}

	public void setFilterIds(List<Integer> filterIds)
	{
		this.filterIds = filterIds;
	}

}
