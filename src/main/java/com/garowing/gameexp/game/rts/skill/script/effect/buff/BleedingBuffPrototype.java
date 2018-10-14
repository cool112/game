package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.yeto.war.fightcore.fight.state.FightStateService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 流血buff
 * @author seg
 *
 */
public class BleedingBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 攻击系数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float attackCoe;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		GameObject caster = effect.getCaster();
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			if((troopTarget.getFightStates().getMetaStates() & FightStateEnum.IMMUNE_BLEEDING.getStates()) > 0)
			{
				HpAction action = new HpAction(troopTarget, caster, 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.IMMUSE);
				troopTarget.addAction(action);
				continue;
			}
			
			FightStateService.addStateEffect(troopTarget, FightStateEnum.BLEEDING, effect);
			newTargetIds.add(target.getObjectId());
			sendAddBuffMsg(target, effect);
		}
		
		effect.setTargetIds(newTargetIds);
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		GameObject caster = effect.getCaster();
		int attack = 0;
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP || !((Troop)target).isLive())
				continue;
			
			Troop troopTarget = (Troop) target;
			float bleedModified = 0f;
			float bleededModified = 0f;
			if(caster.getObjectType() == GameObjectType.TROOP)
			{
				Troop troopCaster = (Troop) caster;
				attack = troopCaster.getAttack();
				bleedModified = troopCaster.getAttrModList().getUnitModifiedTotal(ModifiedType.BLEED_HARM_ADD, troopTarget, effect);
				bleededModified = troopTarget.getAttrModList().getUnitModifiedTotal(ModifiedType.BLEEDED_HARM_ADD, troopCaster, effect);
			}
			else
				bleededModified = troopTarget.getAttrModList().getEffectModifiedTotal(ModifiedType.BLEEDED_HARM_ADD, effect);
			
			float damage = attack * attackCoe;
			damage *= (1 + bleedModified + bleededModified);
			FightEngine.reduceHP(caster, troopTarget, effect.getObjectId(), effect.getPrototypeId(), (int)damage, HpUpdateType.BLEEDING);
		}
		
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
			
			Troop troop = (Troop) target;
			FightStateService.removeStateEffect(troop, FightStateEnum.BLEEDING, effect);
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
