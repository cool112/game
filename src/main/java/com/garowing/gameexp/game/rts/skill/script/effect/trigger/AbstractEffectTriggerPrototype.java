package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.manager.ListIntTroopTypeMaskPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.RandomUtil;
import com.yeto.war.utils.RatioUitl;

import commons.configuration.Property;

/**
 * 抽象效果触发器,需要注意e、f系数已被占用，子类不可使用
 * @author seg
 *
 */
abstract public class AbstractEffectTriggerPrototype extends EffectPrototype
{
	/**
	 * 触发几率
	 */
	@Property(key = EffectKey.PROBABILITY, defaultValue = "0")
	protected float probability;
	
	/**
	 * 触发cd
	 */
	@Property(key = EffectKey.CD, defaultValue = "0")
	protected int cd;
	
	/**
	 * 伪随机容量
	 */
	@Property(key = EffectKey.FACTOR_E, defaultValue = "0")
	protected float pseudorandomSize;
	
	/**
	 * 伪随机次数
	 */
	@Property(key = EffectKey.FACTOR_F, defaultValue = "0")
	protected float pseudorandomNum;
	
	/**
	 * 部队类型掩码
	 */
	@Property(key = EffectKey.TROOP_TYPES, defaultValue = "", propertyTransformer = ListIntTroopTypeMaskPropertyTransformer.class)
	protected int troopTypeMask;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
//		if(effect.getPrototypeId() == 1010102)
//			System.err.println("die bomb");
		
		for(Integer effectId : subEffectIds)
		{
			EffectPrototype effectPrototype = StaticDataManager.EFFECT_DATA.getEffectPrototype(effectId);
			if(effectPrototype == null)
				return SkillError.ERROR_BASE_DATA_ERROR;
		}
		
		return SkillError.SUCCESS;
	}
	
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
			
			troopTarget.getAttachEffects().addTriggerEffect(getEventType(), effect);
			newTargetIds.add(troopTarget.getObjectId());
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
			((Troop)target).getAttachEffects().removeTriggerEffect(getEventType(), effect.getObjectId());
		}
		return SkillError.SUCCESS;
	}
	
	/**
	 * 处理事件实现
	 * @param event
	 * @param effect
	 * @param currTime
	 * @return
	 */
	abstract public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime);
	
	/**
	 * 获取事件类型
	 * @return
	 */
	abstract public TriggerEventType getEventType();
	
	/**
	 * 处理事件
	 * @param event
	 * @param effect
	 * @param currTime
	 * @return
	 */
	public SkillError handleEvent(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(!onTriggerCheck(event, effect, currTime))
			return SkillError.SUCCESS;
		
		SkillError result = handleEventImpl(event, effect, currTime);
		if(result != SkillError.SUCCESS)
			return result;
		
		afterTrigger(effect, currTime);
		return result;
	}
	
	/**
	 * 触发检查
	 * @param event 
	 * @param effect
	 * @return
	 */
	protected boolean onTriggerCheck(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(cd > 0 && effect.getNextActivateTime() - currTime > 0 )
			return false;
		
		if(!targetCheck(event))
			return false;
		
		if(probability > 0)
		{
			if(!RatioUitl.isSuccess(probability))
				return false;
			else
				return true;
		}
		
		if(pseudorandomNum > 0 && pseudorandomSize >0)
		{
			Integer num = effect.getNextPseudoNum();
			if(num == null || num == -1)
			{
				int[] triggerIndex = RandomUtil.newInstance().noRepeatXNum(0, (int)pseudorandomSize, (int)pseudorandomNum);
				int[] emptyArray = new int[(int) pseudorandomSize];
				for(Integer index : triggerIndex)
				{
					emptyArray[index] = 1;
				}
				LinkedList<Integer> pseudorandomQueue = new LinkedList<Integer>();
				for(int i : emptyArray)
				{
					pseudorandomQueue.add(i);
				}
				effect.setPseudoRamdon(pseudorandomQueue);
				num = effect.getNextPseudoNum();
			}
			
			if(num == 0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * 触发成功处理
	 * @param effect
	 * @param currTime
	 */
	protected void afterTrigger(EffectEntity effect, long currTime)
	{
		effect.addActivateCount();
		effect.setLastActivateTime(currTime);
		effect.setNextActivateTime(currTime + cd); 
	}
	
	/**
	 * 目标检查
	 * @param event
	 * @return
	 */
	protected boolean targetCheck(AbstractSkillEvent event)
	{
		if(troopTypeMask > 0)
		{
			GameObject target = event.getTarget();
			if(target != null && target.getObjectType() == GameObjectType.TROOP)
			{
				int troopType = ((Troop)target).getModelType();
				if((troopType & troopTypeMask) == 0 || (troopType & ~troopTypeMask) > 0)
					return false;
			}
		}
		
		return true;
	}

}
