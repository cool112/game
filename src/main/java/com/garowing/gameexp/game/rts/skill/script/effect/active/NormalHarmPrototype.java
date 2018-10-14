package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.config.FightConfig;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.state.FightMetaState;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.GeneralSkillEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.SkillHarmSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.card.CardObject;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.strategos.StrategosObject;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 普通伤害效果
 * @author seg
 *
 */
public class NormalHarmPrototype extends EffectPrototype
{
	/**
	 * 被攻击方血量百分比加成
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "1.0")
	private float factorA;
	
	/**
	 * 攻击方攻击百分比加成
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "1.0")
	private float factorB;
	
	/**
	 * 被攻击方绝对值加成
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0")
	private float factorC;
	
	/**
	 * 攻击方攻击绝对值加成
	 */
	@Property(key = EffectKey.FACTOR_D, defaultValue = "0")
	private float factorD;
	
	/**
	 * 攻方将军加成系数
	 */
	@Property(key = EffectKey.FACTOR_E, defaultValue = "0")
	private float factorE;
	
	/**
	 * 被攻击方将军加成系数
	 */
	@Property(key = EffectKey.FACTOR_F, defaultValue = "0")
	private float factorF;

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
		SkillError result = onActivate(effect, currTime);
		if(result == SkillError.SUCCESS)
		{
			effect.addActivateCount();
			effect.setLastActivateTime(currTime);
			effect.setNextActivateTime(currTime + effect.getInterval());
		}
			
		return result;
	}

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
			
			if((troopTarget.getFightStates().getMetaStates() & FightMetaState.IMMUNE_DAMAGE) > 0)
			{
				HpAction action = new HpAction(troopTarget, caster, 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.IMMUSE);
				troopTarget.addAction(action);
				continue;
			}
			
			int damage = calculateDamage(effect, caster, troopTarget);
			war.getSkillManager().addSkillEvent(new SkillHarmSkillEvent(war, effect, troopTarget, damage));
		}
		
		return SkillError.SUCCESS;
	}
	
	/**
	 * 计算伤害
	 * @param effect
	 * @param caster
	 * @param target
	 * @return
	 */
	protected int calculateDamage(EffectEntity effect, GameObject caster, GameObject target)
	{
		float attackAck = ((HavingCamp)caster).getControl().getAttrAttack();
		float targetDef = ((HavingCamp)target).getControl().getAttrDefense();
		float drpt = 0f;
		float harm = 0f;
		
		if(target.getObjectType() == GameObjectType.TROOP && caster.getObjectType() == GameObjectType.TROOP)
		{
			Troop targetTroop = (Troop) target;
			Troop attackerTroop = (Troop) caster;
			float damageOneFlag = attackerTroop.getAttrModList().getUnitModifiedTotal(ModifiedType.DAMAGE_TO_ONE, targetTroop, effect);
			if(damageOneFlag > 0)
				harm = 1;
			else
			{
				float attrAttack = attackerTroop.getControl().getAttrAttack();
				float attrDefense = targetTroop.getControl().getAttrDefense();
				float attackValue = attackerTroop.getAttack() * (1f+ attrAttack*FightConfig.FIGHT_ATTACK_COEFFICIENT)/
						(1f+attrDefense*FightConfig.FIGHT_DEFENSE_COEFFICIENT);
				drpt = ((Troop)target).getDRPT();
				drpt = drpt > 0.95f ? 0.95f : drpt;
				harm = (targetTroop.getMaxHp() * factorA) + factorC + (attackValue * factorB) + factorD;
				harm *= 1 + targetTroop.getAttrModList().getUnitModifiedTotal(ModifiedType.BE_HARM_ADD, attackerTroop, effect);
				harm *= (attackAck * factorE + 1) / (targetDef * factorF + 1);
				harm *= 1 + attackerTroop.getAttrModList().getUnitModifiedTotal(ModifiedType.HARM_ADD, targetTroop, effect);
				harm *= 1.0f - drpt; 
			}
		}
		else if(target.getObjectType() == GameObjectType.TROOP && caster.getObjectType() == GameObjectType.STRATEGOS)
		{
			Troop targetTroop = (Troop) target;
			StrategosObject general = (StrategosObject) caster;
			
			GeneralSkillEntity skill = (GeneralSkillEntity) effect.getSkill();
			drpt = ((Troop)target).getDRPT();
			drpt = drpt > 0.95f ? 0.95f : drpt;
			harm = (targetTroop.getMaxHp() * factorA) + factorC + (skill.getLevel() * factorB) + factorD;
			harm *= 1 + targetTroop.getAttrModList().getEffectModifiedTotal(ModifiedType.BE_HARM_ADD, effect);
			harm *= (attackAck * factorE + 1) / (targetDef * factorF + 1);
			harm *= 1 + general.getAttrModList().getUnitModifiedTotal(ModifiedType.HARM_ADD, targetTroop, effect);
			harm *= 1.0f - drpt; 
		}
		else if(target.getObjectType() == GameObjectType.TROOP && caster.getObjectType() == GameObjectType.CARD)
		{
			Troop targetTroop = (Troop) target;
			CardObject card = (CardObject) caster;
			drpt = ((Troop)target).getDRPT();
			drpt = drpt > 0.95f ? 0.95f : drpt;
			harm = (targetTroop.getMaxHp() * factorA) + factorC + factorD;
			harm *= 1 + targetTroop.getAttrModList().getEffectModifiedTotal(ModifiedType.BE_HARM_ADD, effect);
			harm *= (attackAck * factorE + 1) / (targetDef * factorF + 1);
			harm *= 1.0f - drpt; 
		}
		
		int finalHarm = (int)(harm);
		if (finalHarm <= 0)
			finalHarm = 1;
		
		return finalHarm;
	}
	
	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}
	
}
