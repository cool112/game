package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 全局目标选择器
 * @author seg
 *
 */
public class GlobalTargetSelector extends TargetSelector
{
	private static final Logger mLogger = Logger.getLogger(GlobalTargetSelector.class);
	
	@Override
	public boolean check(Projectile data)
	{
		return true;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		GameObject caster = data.getCaster();
		WarInstance war = caster.getWar();
		Collection<WarObjectInstance> visibles = WarUtils.getObjects(war);
		
		return visibles;
	}

}
