package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.Collection;
import java.util.Collections;

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
 * 以自身为圆心目标选择器
 * @author seg
 *
 */
public class SelfCircleTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile data)
	{
		GameObject caster = data.getCaster();
		if(caster == null)
			return false;
		
		if(this.radius <= 0)
			return false;
		
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		Troop caster = data.getCaster();
		if(caster == null)
			return Collections.EMPTY_LIST;
		
		float x = caster.getX();
		float y = caster.getY();
		
		if(x == 0f && y == 0f)
			return Collections.EMPTY_LIST;
		
		if(!(data instanceof GameObject))
			return Collections.EMPTY_LIST;
		
		WarInstance war = caster.getWar();
		
		Collection<WarObjectInstance> visibles = null;
		if(radius > 0)
		{
			float modifiedRadius = radius;
			if(((GameObject)data).getObjectType() == GameObjectType.SKILL_EFFECT)
			{
				modifiedRadius += ((Troop)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.EFFECT_RADIUS, (EffectEntity) data);
			}
			visibles = WarUtils.getLiveObjectsByRadius(war, x, y, modifiedRadius);
		}else
		{
			visibles = WarUtils.getObjects(war);
		}
		
		if(visibles == null)
			return Collections.EMPTY_LIST;
		
		return visibles;
	}

}
