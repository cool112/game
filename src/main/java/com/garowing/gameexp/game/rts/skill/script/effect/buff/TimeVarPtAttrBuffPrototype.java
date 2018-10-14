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

import commons.configuration.Property;

/**
 * 时间变量百分比属性加成buff
 * @author seg
 *
 */
public class TimeVarPtAttrBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 基础属性
	 */
	@Property(key = EffectKey.ATTRIBUTES, defaultValue = "")
	private String attributes;
	
	/**
	 * 指数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0.0")
	private float pow;
	
	/**
	 * 乘数
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0.0")
	private float multipier;
	
	/**
	 * 常数
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0.0")
	private float constant;
	
	/**
	 * 最小值
	 */
	@Property(key = EffectKey.FACTOR_D, defaultValue = "0")
	private float min;
	
	/**
	 * 最大值
	 */
	@Property(key = EffectKey.FACTOR_E, defaultValue = "0")
	private float max;

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		AttrEntity attrEntity = AttrHelp.parseAttr(attributes);
		float addValue = constant / 100 - 1;
		for(ValueEntity value : attrEntity.getAttrMap().values())
		{
			value.setPt(addValue);
		}
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			newTargetIds.add(troopTarget.getObjectId());
			troopTarget.getAttrEntity().add(attrEntity);
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
		long lastTime = effect.getLastActivateTime();
		if(lastTime == 0)
			lastTime = effect.getStartTime();
		
		if(lastTime == 0)
			return SkillError.ERROR_ATTR_BUFF_NO_DATA;
		
		long startTime = effect.getStartTime();
		long lastDuration = lastTime - startTime;
		long currDuration = currTime - startTime;
		AttrEntity attrEntity = AttrHelp.parseAttr(attributes);
		for(ValueEntity value : attrEntity.getAttrMap().values())
		{
			double curreValue = Math.pow(currDuration, pow) + multipier * currDuration / 100;
			curreValue = curreValue < min ? min : curreValue;
			curreValue = curreValue > max ? max : curreValue;
			double lastValue = Math.pow(lastDuration, pow) + multipier * lastDuration / 100;
			lastValue = lastValue < min ? min : lastValue;
			lastValue = lastValue > max ? max : lastValue;
			value.setPt((float) (curreValue - lastValue)); 
		}
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troop = (Troop) target;
			if(!troop.isLive())
				continue;
			
			troop.getAttrEntity().add(attrEntity);
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		long startTime = effect.getStartTime();
		long lastTime = effect.getLastActivateTime();
		long lastDuration = lastTime - startTime;
		AttrEntity attrEntity = AttrHelp.parseAttr(attributes);
		for(ValueEntity value : attrEntity.getAttrMap().values())
		{
			double lastValue = Math.pow(lastDuration, pow) + multipier * lastDuration / 100 + constant / 100 - 1;
			lastValue = lastValue < min ? min : lastValue;
			lastValue = lastValue > max ? max : lastValue;
			value.setPt((float) lastValue); 
		}
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troop = (Troop) target;
			if(!troop.isLive())
				continue;
			
			troop.getAttrEntity().reduce(attrEntity);
			
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), troop.getAttrEntity().getAttrMap()));
			});
			
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
