package com.garowing.gameexp.game.rts.skill.template;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_END_SKILL;

/**
 * 效果播放控制器
 * @author seg
 * 2017年4月1日
 */
public abstract class EffectPlayControllor
{
	/**
	 * 获取播放时间
	 * @param skill
	 * @param currTime 
	 * @return
	 */
	public abstract long getWaitTime(EffectEntity effect, long currTime);
	
	/**
	 * 获取初始化时间
	 * @param skill
	 * @param currTime 
	 * @return
	 */
	public abstract long getInitDelay(EffectEntity effect, long currTime);

	/**
	 * 是否效果结束
	 * @param effect
	 * @param currTime
	 * @return
	 */
	public boolean isEffectEnd(EffectEntity effect, long currTime)
	{
		
		GameObject caster = effect.getCaster();
		if(!effect.isTemporary() && caster == null)
			return true;
//		
//		if(caster.getObjectType() == GameObjectType.TROOP && !((Troop)caster).isLive())
//			return true;
		
		SkillEntity skill = effect.getSkill();
		
		WarInstance war = effect.getWar();
		for(Integer subEffectId : effect.getSubEffectIds())
		{
			GameObject obj = war.getObject(subEffectId);
			if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity subEffect = (EffectEntity) obj;
			if(subEffect.getState() < SkillManager.END)
				return false;
		}
		
		if(effect.getActivateCount() >= effect.getMaxActivateCount())
		{
			if(effect.getWrapperEffectId() == effect.getPrototypeId())
			{
				if(skill.getEffectEntityList().size() > 1)
				{
					skill.addEffectIndex();
					if(skill.getCurrentEffectIndex() != 0)
					{
						System.err.println("multiple effect skill cast:" + skill.getModelId());
						skill.executeCurrentEffect(currTime);
					}
				}
			}
			
			return true;
		}
		
		//针对多次触发的主动技能，优化中断
		if(!effect.isTemporary() && !skill.getModel().isPassive())
		{
			if(skill != null)
			{
				boolean forceEnd = false;
				if(effect.getEffectPrototype().needTarget)
				{
					List<GameObject> targets = effect.getEffectPrototype().getTargets(effect);
					if(targets == null || targets.isEmpty())
						forceEnd = true; 
				}
				
				if(!skill.isParallel())
				{
					if(caster.getObjectType() == GameObjectType.TROOP && !((Troop)caster).canAttack())
						forceEnd = true;
				}
				
				if(forceEnd)
				{
					SendPacketUtil.sendWarPacket(skill.getWar(), (sender)->{
						sender.sendPacket(new GC_END_SKILL(sender.getId(), 0, skill.getObjectId(), skill.getModelId(), caster.getObjectId()));
					});
					return true;
				}
			}
		}
		
		
		return false;
	}
	
	/**
	 * 是否效果结束
	 * @param effect
	 * @param currTime
	 * @return
	 */
	public long getEndWait(EffectEntity effect, long currTime)
	{
		
		GameObject caster = effect.getCaster();
		if(caster == null)
			return 0;
		
		if(effect.isForceEnd())
			return 0;
		
		long endDelay = effect.getEffectPrototype().getEndDelay();
		if(endDelay > 0)
		{
			float reduceCoe = 0f;
			if(caster.getObjectType() == GameObjectType.TROOP)
			{
				reduceCoe  = ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.CD_ADD, effect.getSkillModelId());
			}
			endDelay = (long) (endDelay * (1 + reduceCoe));
			long waitTime = effect.getEndTime() + endDelay - currTime;
			return waitTime;
		}
		
		return endDelay;
	}
}
