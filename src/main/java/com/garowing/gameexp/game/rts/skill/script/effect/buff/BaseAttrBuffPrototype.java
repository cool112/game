package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.yeto.war.fightcore.attr.model.AttrKey;
import com.yeto.war.fightcore.fight.FightEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.AttributesPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_TROOP_ATTR;

import commons.configuration.Property;

/**
 * 基础属性buff
 * @author seg
 *
 */
public class BaseAttrBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 基础属性
	 */
	@Property(key = EffectKey.ATTRIBUTES, defaultValue = "", propertyTransformer = AttributesPropertyTransformer.class)
	private AttrEntity attributes;
	
	/**
	 * 是否修改当前生命值，0-不修改 1-修改
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float changeCurrHp;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			AttrEntity attrEntity = troopTarget.getAttrEntity();
			float oldMaxHp = attrEntity.getValue(AttrKey.HP);
			attrEntity.add(attributes);
			
			
			int maxHp = troopTarget.getMaxHp();
			int hp = troopTarget.getHp();
			int skillInstance = effect.getObjectId();
			int skillModelId = effect.getPrototypeId();
			if(hp < maxHp && changeCurrHp == 1)
			{
				float changeVal = attrEntity.getValue(AttrKey.HP) - oldMaxHp;
				hp += changeVal;
				hp = Math.min(Math.max(1, hp), maxHp);
				FightEngine.updateHP(troopTarget, skillInstance, skillModelId, hp, HpUpdateType.BUFF);
			}
			
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), attrEntity.getAttrMap()));
			});
			
			sendAddBuffMsg(target, effect);
			newTargetIds.add(target.getObjectId());
		}
		
		effect.setTargetIds(newTargetIds);
		
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
			
			Troop troop = (Troop) target;
			AttrEntity attrEntity = troop.getAttrEntity();
			attrEntity.reduce(attributes);
			
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), attrEntity.getAttrMap()));
			});
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
