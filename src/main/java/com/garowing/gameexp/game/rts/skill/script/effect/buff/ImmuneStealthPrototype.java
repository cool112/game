package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.yeto.war.fightcore.fight.state.FightStateService;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

/**
 * 驱散潜行效果
 * @author seg
 *
 */
public class ImmuneStealthPrototype extends AbstractBuffPrototype
{
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		List<Integer> newTargets = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			FightStateService.addStateEffect((Troop)target, FightStateEnum.IMMUNE_STEALTH, effect);
			sendAddBuffMsg(target, effect);
			newTargets.add(target.getObjectId());
		}
		
		effect.setTargetIds(newTargets);
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
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			FightStateService.removeStateEffect(troopTarget, FightStateEnum.IMMUNE_STEALTH, effect);
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
