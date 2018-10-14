package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 默认目标选择器
 * @author seg
 *
 */
public class DefaultTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile currentSkillData)
	{
		return true;
	}

	@Override
	public List<GameObject> findTarget(Projectile data)
	{
		List<GameObject> targets = data.getTargets();
		if(targets != null)
		{
			return targets;
		}
		return Collections.emptyList();
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
