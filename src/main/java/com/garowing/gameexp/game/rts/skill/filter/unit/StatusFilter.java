package com.garowing.gameexp.game.rts.skill.filter.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.fight.state.FightStateList;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterKey;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterType;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

import commons.configuration.Property;

/**
 * 状态过滤器
 * @author seg
 *
 */
public class StatusFilter implements UnitPropsFilter
{
	/**
	 * 状态掩码
	 */
	@Property(key = PropsFilterKey.TROOP_STATUS_MASK, defaultValue = "0")
	private int statusMask;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WarObjectInstance> filter(Projectile data, GameObject caster, Collection<WarObjectInstance> list)
	{
		if(list == null)
			return Collections.EMPTY_LIST;
		
		List<WarObjectInstance> newList = new ArrayList<WarObjectInstance>();
		for(WarObjectInstance target : list)
		{
			FightStateList fightStateList = target.getFightStates();
			int metaStates = fightStateList.getMetaStates();
			if((metaStates & statusMask) > 0 && (metaStates & ~statusMask) == 0)
				newList.add(target);
		}
		
		return newList;
	}

	@Override
	public int getType()
	{
		return PropsFilterType.STATUS.getCode();
	}

}
