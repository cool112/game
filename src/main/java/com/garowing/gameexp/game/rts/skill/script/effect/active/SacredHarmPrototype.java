package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.SkillHarmSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 神圣伤害，无视伤害免疫
 * @author seg
 *
 */
public class SacredHarmPrototype extends NormalHarmPrototype
{
	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		
		WarInstance war = effect.getWar();
		GameObject caster = effect.getCaster();
		if(caster.getObjectType() == GameObjectType.TROOP && !effect.isTemporary())
		{
			Troop troopCaster = (Troop) caster;
			if(!troopCaster.canAttack())
			{
				war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, caster, effect, true));
				return SkillError.ERROR_EFFECT_NO_ATTACK;
			}
		}
			
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			int damage = calculateDamage(effect, caster, troopTarget);
			war.getSkillManager().addSkillEvent(new SkillHarmSkillEvent(war, effect, troopTarget, damage));
		}
		
		return SkillError.SUCCESS;
	}
}
