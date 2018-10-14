package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 圆形区域目标选择器,目前只支持一个圆
 * @author seg
 *
 */
public class CircleTargetSelector extends TargetSelector
{
	private static final Logger mLogger = Logger.getLogger(CircleTargetSelector.class);
	
	@Override
	public boolean check(Projectile data)
	{
		List<GameObject> targets = data.getTargets();
		if(targets == null || targets.isEmpty() || targets.get(0).getObjectType() != GameObjectType.TROOP)
		{
			float x = data.getTargetX();
			float y = data.getTargetY();
			if(x == 0f && y == 0f)
				return false;
		}
		
		if(this.radius <= 0)
			return false;
		
		return true;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		float x = data.getTargetX();
		float y = data.getTargetY();
		List<GameObject> targets = data.getTargets();
		if(x == 0f && y == 0f && targets != null && !targets.isEmpty())
		{
			GameObject target = targets.get(0);
			if(target != null && target.getObjectType() == GameObjectType.TROOP)
			{
				Troop troop = (Troop) target; 
				x = troop.getX();
				y = troop.getY();
			}
		}
		
		if(x == 0f && y == 0f)
			return null;
		
		if(!(data instanceof GameObject))
			return null;
		
		WarInstance war = ((GameObject)data).getWar();
		
		Collection<WarObjectInstance> visibles = null;
		if(radius > 0)
		{
			float modifiedRadius = radius;
			GameObject caster = data.getCaster();
			if(caster.getObjectType() == GameObjectType.TROOP && ((GameObject)data).getObjectType() == GameObjectType.SKILL_EFFECT)
			{
				modifiedRadius += ((Troop)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.EFFECT_RADIUS, (EffectEntity) data);
			}
			visibles = WarUtils.getLiveObjectsByRadius(war, x, y, modifiedRadius);
		}else{
			visibles = WarUtils.getObjects(war);
		}
		
		return visibles;
	}
	
}
