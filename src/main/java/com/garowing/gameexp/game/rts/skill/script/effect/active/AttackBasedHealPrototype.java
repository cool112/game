package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.config.FightConfig;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.strategos.StrategosObject;
import com.yeto.war.module.troop.Troop;
import commons.configuration.Property;

/**
 * 基于攻击力的治疗效果
 * @author seg
 *
 */
public class AttackBasedHealPrototype extends EffectPrototype
{
	/**
	 * 转化效率
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "1.0")
	private float transforEfficient;
	
	/**
	 * 固定治疗值
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float fixHeal;
	
	/**
	 * 最大血量百分比
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0")
	private float maxHpPercent;
	
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
		
		int ap = 0;
		float healModified = 0;
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
			Troop troopCaster = (Troop) caster;
			ap = troopCaster.getAttack();
			float targetControlAttack = troopCaster.getControl().getAttrAttack();
			ap *= (1 + targetControlAttack * FightConfig.FIGHT_ATTACK_COEFFICIENT);
			healModified = troopCaster.getAttrModList().getEffectModifiedTotal(ModifiedType.HEAL, effect);
		}
		else if(caster.getObjectType() == GameObjectType.STRATEGOS)
		{
			StrategosObject general = (StrategosObject) caster;
			ap = general.getAttack();
		}
		
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			float targetHealMod = troopTarget.getAttrModList().getEffectModifiedTotal(ModifiedType.BE_HEAlED, effect);
			int heal = (int) (ap * transforEfficient + fixHeal);
			heal *= (1 + healModified + targetHealMod);
			heal += troopTarget.getMaxHp() * maxHpPercent;
			FightEngine.addHP(troopTarget, effect.getObjectId(), effect.getPrototypeId(), heal, HpUpdateType.ADD_HP);
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

}
