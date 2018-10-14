package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

/**
 * 施法距离目标选择器，超出施法距离则排除
 * @author seg
 *
 */
public class CastRangeTargetSelector extends TargetSelector
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
		
		float range = 0f;
		if(((GameObject)data).getObjectType() == GameObjectType.SKILL_EFFECT)
		{
			SkillEntity skill = ((EffectEntity)data).getSkill();
			if(skill != null)
				range = skill.getCastRange();
		}
		else if(((GameObject)data).getObjectType() == GameObjectType.SKILL)
		{
			range = ((SkillEntity)data).getCastRange();
		}
		
		GameObject caster = data.getCaster();
		
		List<WarObjectInstance> troopTargets = new ArrayList<WarObjectInstance>();
		for(GameObject target : initTargets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(range > 0 && caster != null && caster.getObjectType() == GameObjectType.TROOP)
			{
				float distance = MathUtil.getDistance((WarObjectInstance)caster, troopTarget);
				if(distance > range)
					continue;
			}
			
			troopTargets.add((WarObjectInstance) target);
		}
		
		return troopTargets;
	}

}
