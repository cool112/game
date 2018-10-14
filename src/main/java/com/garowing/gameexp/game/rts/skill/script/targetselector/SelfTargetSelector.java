package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 施法者作为目标选择器
 * @author seg
 *
 */
public class SelfTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile data)
	{
		GameObject caster = data.getCaster();
		if(caster == null)
			return false;
		
		return true;
	}

	@Override
	public List<GameObject> findTarget(Projectile data)
	{
		List<GameObject> targetList = new ArrayList<GameObject>();
		targetList.add(data.getCaster());
		return targetList;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		return null;
	}
	
}
