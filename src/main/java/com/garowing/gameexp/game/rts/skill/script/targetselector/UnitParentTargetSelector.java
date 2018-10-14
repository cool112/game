package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 获取单位的母体召唤者（除卡牌、将军）为目标
 * @author seg
 *
 */
public class UnitParentTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile data)
	{
		GameObject caster = data.getCaster();
		if(caster == null || caster.getObjectType() != GameObjectType.TROOP)
			return false;
		
		Troop troopCaster = (Troop) caster;
		int callerId = troopCaster.getCallerId();
		GameObject caller = troopCaster.getWar().getObject(callerId);
		if(caller == null || caller.getObjectType() != GameObjectType.TROOP)
			return false;
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GameObject> findTarget(Projectile data)
	{
		GameObject caster = data.getCaster();
		if(caster == null || caster.getObjectType() != GameObjectType.TROOP)
			return Collections.EMPTY_LIST;
		
		Troop troopCaster = (Troop) caster;
		int callerId = troopCaster.getCallerId();
		GameObject caller = troopCaster.getWar().getObject(callerId);
		if(caller == null || caller.getObjectType() != GameObjectType.TROOP)
			return Collections.EMPTY_LIST;
		
		List<GameObject> targetList = new ArrayList<GameObject>();
		targetList.add(caller);
		return targetList;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		return null;
	}

}
