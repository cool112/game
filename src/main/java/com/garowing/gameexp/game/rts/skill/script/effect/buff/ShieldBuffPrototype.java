package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.config.FightConfig;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_ADD_SHIELD;
import com.yeto.war.network.gs.sendable.fight.GC_REMOVE_SHIELD;

import commons.configuration.Property;

/**
 * 护盾buff
 * @author seg
 *
 */
public class ShieldBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 攻击力转化系数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float transforEfficient;
	
	/**
	 * 固定护盾值
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float fixShield;
	
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		GameObject caster = effect.getCaster();
		int ap = 0;
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
			Troop troopCaster = (Troop) caster;
			ap = troopCaster.getAttack();
			float targetControlAttack = troopCaster.getControl().getAttrAttack();
			ap *= (1 + targetControlAttack * FightConfig.FIGHT_ATTACK_COEFFICIENT);
		}
		
		int shieldVal = (int) (ap * transforEfficient + fixShield);
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			troopTarget.getAttachEffects().addEffecShield(effect.getObjectId(), (int) shieldVal);
			newTargetIds.add(target.getObjectId());
			sendAddShieldMsg(target, effect, shieldVal);
		}
		
		effect.setTargetIds(newTargetIds);
		return SkillError.SUCCESS;
	}
	
	/**
	 * 发送增加护盾
	 * @param target
	 * @param effect
	 * @param shieldVal
	 */
	protected void sendAddShieldMsg(GameObject target, EffectEntity effect, int shieldVal)
	{
		WarInstance war = effect.getWar();
		int effectId = effect.getObjectId();
		int skillModelId = effect.getSkillModelId();
		int effectModelId = effect.getPrototypeId();
		
		SendPacketUtil.sendWarPacket(war, (sender)->{
			sender.sendPacket(new GC_ADD_SHIELD(sender.getId(), 0, effectId, skillModelId, 
					effectModelId, target.getObjectId(), (int)shieldVal));
		});
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
			
			Troop troop = (Troop) target;
			troop.getAttachEffects().removeEffectShield(effect.getObjectId());
			sendDelShieldMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

	/**
	 * 发送删除护盾
	 * @param target
	 * @param effect
	 */
	protected void sendDelShieldMsg(GameObject target, EffectEntity effect)
	{
		WarInstance war = effect.getWar();
		int effectId = effect.getObjectId();
		int skillModelId = effect.getSkillModelId();
		int effectModelId = effect.getPrototypeId();
		
		SendPacketUtil.sendWarPacket(war, (sender)->{
			sender.sendPacket(new GC_REMOVE_SHIELD(sender.getId(), 0, effectId, skillModelId, 
					effectModelId, target.getObjectId()));
		});
	}
}
