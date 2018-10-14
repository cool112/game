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
 * 中毒buff
 * @author seg
 *
 */
public class PoisonBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 最大生命百分比
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float mxaHpPercent;
	
	/**
	 * 固定伤害（最小伤害）
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float fixDamage;
	
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
			
			if((troopTarget.getFightStates().getMetaStates() & FightStateEnum.IMMUNE_POISON.getStates()) > 0)
			{
				HpAction action = new HpAction(troopTarget, caster, 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.IMMUSE);
				troopTarget.addAction(action);
				continue;
			}
			
			FightStateService.addStateEffect(troopTarget, FightStateEnum.POISON, effect);
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
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP || !((Troop)target).isLive())
				continue;
			
			Troop troopTarget = (Troop) target;
			float poisonModified = 0f;
			float poisonedModified = 0f;
			if(caster != null && caster.getObjectType() == GameObjectType.TROOP)
			{
				Troop troopCaster = (Troop) caster;
				poisonModified = troopCaster.getAttrModList().getUnitModifiedTotal(ModifiedType.POISON_HARM_ADD, troopTarget, effect);
				poisonedModified = troopTarget.getAttrModList().getUnitModifiedTotal(ModifiedType.POISONED_HARM_ADD, troopCaster, effect);
			}
			else
				poisonedModified = troopTarget.getAttrModList().getEffectModifiedTotal(ModifiedType.POISONED_HARM_ADD, effect);
			
			float damage = troopTarget.getMaxHp() * mxaHpPercent;
			damage *= (1 + poisonModified + poisonedModified);
			FightEngine.reduceHP(caster, troopTarget, effect.getObjectId(), effect.getPrototypeId(), (int)damage, HpUpdateType.POISON);
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
			FightStateService.removeStateEffect(troop, FightStateEnum.POISON, effect);
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
