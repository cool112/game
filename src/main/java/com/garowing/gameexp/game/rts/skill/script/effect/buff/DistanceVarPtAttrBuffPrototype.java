package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.attr.AttrHelp;
import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.yeto.war.fightcore.attr.entity.ValueEntity;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_TROOP_ATTR;
import com.yeto.war.utils.MathUtil;

import commons.configuration.Property;

/**
 * 距离变量百分比属性buff
 * @author seg
 *
 */
public class DistanceVarPtAttrBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 基础属性
	 */
	@Property(key = EffectKey.ATTRIBUTES, defaultValue = "")
	private String attributes;
	
	/**
	 * 乘数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0.0")
	private float multiplier;
	
	/**
	 * 除数
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0.0")
	private float divider;
	
	/**
	 * 最大值
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0.0")
	private float maxValue;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		Troop caster = effect.getCaster();
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			AttrEntity attrEntity = AttrHelp.parseAttr(attributes);
			float distance = MathUtil.getDistance(caster.getX(), caster.getY(), troopTarget.getX(), troopTarget.getY());
			double curreValue = maxValue - Math.ceil(distance / divider) * multiplier;
			curreValue = curreValue < 0 ? 0 : curreValue;
			curreValue = curreValue > maxValue ? maxValue : curreValue;
			for(ValueEntity value : attrEntity.getAttrMap().values())
			{
				System.err.println("see start value " + curreValue);
				value.setPt((float) curreValue);
			}
			
			newTargetIds.add(troopTarget.getObjectId());
			troopTarget.getAttrEntity().add(attrEntity);
			troopTarget.getAttachEffects().setEffectAttr(effect.getObjectId(), attrEntity);
			sendAddBuffMsg(target, effect);
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), troopTarget.getAttrEntity().getAttrMap()));
			});
		}
		
		effect.setTargetIds(newTargetIds);
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		Troop caster = effect.getCaster();
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troop = (Troop) target;
			if(!troop.isLive())
				continue;
			
			AttrEntity effectAttr = troop.getAttachEffects().getEffectAttr(effect.getObjectId());
			if(effectAttr != null)
				troop.getAttrEntity().reduce(effectAttr);
			else
			{
				effectAttr = AttrHelp.parseAttr(attributes);
				troop.getAttachEffects().setEffectAttr(effect.getObjectId(), effectAttr);
			}
				
			float distance = MathUtil.getDistance(caster.getX(), caster.getY(), troop.getX(), troop.getY());
			double curreValue = maxValue - Math.ceil(distance / divider) * multiplier;
			curreValue = curreValue < 0 ? 0 : curreValue;
			curreValue = curreValue > maxValue ? maxValue : curreValue;
			for(ValueEntity value : effectAttr.getAttrMap().values())
			{
				System.err.println("see activate value " + curreValue);
				value.setPt((float) curreValue ); 
			}
			
			troop.getAttrEntity().add(effectAttr);
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
			if(!troop.isLive())
				continue;
			
			AttrEntity effectAttr = troop.getAttachEffects().getEffectAttr(effect.getObjectId());
			if(effectAttr != null)
			{
				troop.getAttrEntity().reduce(effectAttr);
				troop.getAttachEffects().removeEffectAttr(effect.getObjectId());
			}
			
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), troop.getAttrEntity().getAttrMap()));
			});
			
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
