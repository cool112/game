package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.yeto.war.fightcore.fight.state.FightMetaState;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.SkillHarmSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;

/**
 * 即死效果
 * @author seg
 *
 */
public class InstantKillPrototype extends EffectPrototype
{

	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		if(effect.getTargetIds().isEmpty() && effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
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
			
			if((troopTarget.getFightStates().getMetaStates() & FightMetaState.IMMUNE_INSTANT_SKILL) > 0)
			{
				HpAction action = new HpAction(troopTarget, caster, 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.IMMUSE);
				troopTarget.addAction(action);
				continue;
			}
			
			FightEngine.troopDie(troopTarget, caster, effect.getObjectId(), effect.getWrapperEffectId(), HpUpdateType.SKILL_DIE);
		}
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

}
