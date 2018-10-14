package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 怪物目标选择器
 * @author seg
 *
 */
public class CurrentUnitTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile data)
	{
		return true;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		List<GameObject> initTargets = data.getTargets();
		if(initTargets == null || initTargets.isEmpty())
			return null;
		
		List<WarObjectInstance> troopTargets = new ArrayList<WarObjectInstance>();
		for(GameObject target : initTargets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			troopTargets.add((WarObjectInstance) target);
		}
		
		return troopTargets;
	}

}
