package com.garowing.gameexp.game.rts.skill.manager;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillCastHandler;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.model.SkillModel;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 技能管理器
 * @author seg
 * 2017年2月7日
 */
public class SkillManager extends GameObject
{
	/**
	 * 执行间隔 毫秒
	 */
	public static final int TASK_INTERVAL = 100;
	
	/**
	 * 技能空闲
	 */
	public static final int IDLE = 5;
	
	/**
	 * 技能创建
	 */
	public static final int CREATE = 1;
	
	/**
	 * 技能开始
	 */
	public static final int START = 2;
	
	/**
	 * 技能激活状态
	 */
	public static final int ACTIVATE = 3;
	
	/**
	 * 技能结束状态
	 */
	public static final int END = 4;
	
	public static final Logger mLogger = LogManager.getLogger(SkillManager.class);
	
	/**
	 * 技能缓存,包含技能及技能事件
	 */
	private ConcurrentLinkedQueue<Integer> skillCache = new ConcurrentLinkedQueue<Integer>();

	public SkillManager(WarInstance war)
	{
		super(war);
	}

	@Override
	public String getName()
	{
		return "Skill manager";
	}
	
	/**
	 * 增加场上技能
	 * @param skill
	 */
	public void addSkill(SkillEntity skill)
	{
		if(skill == null)
			return;
		
		skillCache.add(skill.getObjectId());
	}
	
	/**
	 * 增加场上技能事件
	 * @param event
	 */
	public void addSkillEvent(AbstractSkillEvent event)
	{
		if(event == null)
			return;
		
		skillCache.add(event.getObjectId());
	}
	
	
	/**
	 * 更新技能状态
	 */
	public void updateSkills(long currTime)
	{
//		System.err.println("======================"+ getWar().getName()+"=======skillMgr============");
		Iterator<Integer> iterator = skillCache.iterator();
		while(iterator.hasNext())
		{
			GameObject obj = null;
			try
			{
				int objId = iterator.next();
				WarInstance war = getWar();
				if(war == null)
					return;
				
				obj = war.getObject(objId);
				if(obj == null)
				{
					iterator.remove();
					continue;
				}
			
				int type = obj.getObjectType();
				if(type == GameObjectType.SKILL)
				{
					SkillEntity skill = (SkillEntity) obj;
					handleSkillEntity(currTime, iterator, skill);
				}
				else if(type == GameObjectType.SKILL_EVENT)
				{
					AbstractSkillEvent skillEvent = (AbstractSkillEvent) obj;
					SkillEventHandler handler = skillEvent.getEventType().getHandler();
					if(handler == null)
						continue;
					
//					System.err.println("skill event:" + skillEvent);
					handler.handle(skillEvent, currTime);
					iterator.remove();
					getWar().removeObject(skillEvent);
				}
				else if(type == GameObjectType.SKILL_EFFECT)
				{
					EffectEntity effect = (EffectEntity) obj;
					handleEffect(currTime, iterator, effect);
				}
				
			}
			catch (Throwable e)
			{
				mLogger.error("skill update fail! :[" + obj + "]", e);
				if(obj != null)
				{
					if(obj.getObjectType() == GameObjectType.SKILL)
					{
						((SkillEntity)obj).setState(END);
					}
					else if(obj.getObjectType() == GameObjectType.SKILL_EVENT)
					{
						iterator.remove();
						getWar().removeObject(obj);
					}
					else if(obj.getObjectType() == GameObjectType.SKILL_EFFECT)
					{
						((EffectEntity) obj).setState(END);
						((EffectEntity) obj).setForceEnd(true);
					}
				}
			}
		}
	}

	
	/**
	 * 处理技能实体
	 * @param currTime
	 * @param queue
	 * @param skill
	 */
	private void handleSkillEntity(long currTime, Iterator<Integer> queue, SkillEntity skill)
	{
		switch(skill.getState())
		{
		case CREATE:
			//TODO 判断是否到达开始时间，然后开始执行技能
			SkillModel model = skill.getModel();
			long startTime = skill.getCreateTime() + model.getPlayControllor().getInitDelay(skill, currTime);
			if(model.getId() == 410108)
				System.err.println("create current["+currTime+"] start["+startTime+"] skillId[" + model.getId()+"]");
			if(currTime >= startTime)
			{
				//发送首层技能
//				if(skill.getCastHandler() == null)
//				{
//					SkillEngine.sendSkillMsg(skill);
//				}
				TargetSelector selector = skill.getTargetSelector();
				if(selector != null)
					skill.setTargets(selector.findTarget(skill));
				
				skill.setState(START);
				skill.setStartTime(currTime);
			}
			break;
		case START:
			// 计算触发时间，如果到达触发时机，执行各种效果，进入激活状态
//			if(skill.getModelId() == 200007)
//				System.err.println("start current["+currTime+"] skill[" +skill +"]");
			long waitTime = skill.getModel().getPlayControllor().getWaitTime(skill, currTime);
			if(skill.getModelId() == 410108)
				System.err.println("start current["+currTime+"] wait["+waitTime+"] skill[" +skill +"]");
			if(waitTime < TASK_INTERVAL)
			{
				SkillCastHandler castHandler = skill.getCastHandler();
				if(castHandler != null)
				{
					SkillError result = castHandler.beforeCast(skill, currTime);
					if(result != SkillError.SUCCESS)
					{
						castHandler.onFail(result, skill);
						skill.setState(END);
						break;
					}
					else
						castHandler.onSuc(skill, currTime);
				}
				
				if(skill.getModelId() == 2020)
					System.err.println("start current["+currTime+"] wait["+waitTime+"] skill[" +skill +"]");
				
				skill.setState(ACTIVATE);
				SkillError errorCode = skill.executeCurrentEffect(currTime);
				if(errorCode != SkillError.SUCCESS)
					mLogger.warn("skill execute fail! ec:[" + errorCode.info + "] skillId:["+skill.getModelId() +"]");
			}
			break;
		case ACTIVATE:
			//TODO 判断是否达到结束条件，各效果结束、目标死亡、施法者死亡等，进入结束
			if(skill.getModelId() == 101081)
				System.err.println("active current["+currTime+"] "+" skill[" +skill +"]");
			boolean isEnd = skill.getModel().getPlayControllor().isSkillEnd(skill, currTime);
			if(isEnd)
			{
//				System.err.println("skill end skillid[" + skill + "]");
				skill.setState(END);
			}
			break;
		case END:
			//TODO 取消效果任务，从技能管理器和根节点清除实例
			if(skill.getModelId() == 101081)
				System.err.println("end current["+currTime+"] "+" skill[" +skill +"]");
			skill.onExit();
			queue.remove();
			break;
		default:
			mLogger.warn("invailid skill state :[" + skill.getState() + "]");
		}
	}
	
	/**
	 * 处理效果实体
	 * @param currTime
	 * @param queue
	 * @param effect
	 */
	private void handleEffect(long currTime, Iterator<Integer> queue, EffectEntity effect)
	{
		switch(effect.getState())
		{
		case CREATE:
			//TODO 判断是否到达开始时间，然后开始执行技能
			long startTime = effect.getCreateTime() + effect.getEffectPrototype().getPlayControllor().getInitDelay(effect, currTime);
			if(effect.getPrototypeId() == 1010701)
				System.err.println(currTime + ": effect create " + effect);
			
			if(currTime >= startTime)
			{
				if(effect.getEffectPrototype().onCheck(effect) == SkillError.SUCCESS)
				{
					SkillEngine.sendSkillMsg(effect);
					effect.setState(START);
					effect.setStartTime(currTime);
				}
				else
				{
					effect.setState(END);
					effect.setEndTime(currTime);
				}
				
			}
			break;
		case START:
			//计算触发时间，如果到达触发时机，执行效果，进入激活状态
//			if(effect.getPrototypeId() == 1000309)
//				System.err.println("wait start");
			long waitTime = effect.getEffectPrototype().getPlayControllor().getWaitTime(effect, currTime);
//			if(effect.getPrototypeId() == 105)
//				System.err.println("wait" + waitTime);
			
			if(waitTime < TASK_INTERVAL)
			{
				if(effect.getPrototypeId() == 4101082)
					System.err.println(currTime + ": effect start " + effect + " targets:" + effect.getTargetIds());
				
				SkillError errorCode = effect.getEffectPrototype().onStart(effect, currTime);
				if(errorCode != SkillError.SUCCESS)
				{
					effect.getEffectHandler().onStartFail(effect, currTime);
					mLogger.warn("effect execute fail! ec:[" + errorCode.info + "] effectId:["+effect.getEffectPrototype().getModelId() +"]");
				}
				else
				{
					effect.getEffectHandler().onStartSuc(effect, currTime);
				}
				
				SkillEntity skill = effect.getSkill();
				if(skill != null && skill.getCurrentEffectIndex() == 0)
				{
					skill.setLastCastTime(currTime);
					skill.setNextCastTime(currTime + skill.getModel().getRealCd()); 
				}
			}
			break;
		case ACTIVATE:
			//TODO 判断是否达到结束条件，一般用次数和间隔时间控制
//			if(effect.getPrototypeId() == 1030205)
//				System.err.println("effect activate enter " + effect + " count " + effect.getActivateCount());
			boolean isEnd = effect.getEffectPrototype().getPlayControllor().isEffectEnd(effect, currTime);
			if(isEnd)
			{
				if(effect.getPrototypeId() == 3301)
					System.err.println("effect activate end " + effect + " count " + effect.getActivateCount());
				
				effect.setState(END);
				effect.setEndTime(currTime);
			}
			else
			{
				if(!effect.isTrigger() && (effect.getNextActivateTime() - currTime) < TASK_INTERVAL)
				{
//					if(effect.getPrototypeId() == 3301)
//						System.err.println(currTime + ": effect activate " + effect);
					
					SkillError result = effect.getEffectPrototype().onActivate(effect, currTime);
					if(result != SkillError.SUCCESS)
					{
//						mLogger.warn("effect activate fail! ec:[" + result.info + "] effectId:["+effect.getEffectPrototype().getModelId() +"]");
						effect.getEffectHandler().onActivateFail(effect, currTime);
					}
					else
					{
						effect.getEffectHandler().onActivateSuc(effect, currTime);
					}
					
					effect.addActivateCount();
					effect.setLastActivateTime(currTime);
					effect.setNextActivateTime(currTime + effect.getInterval());
				}
					
			}
			break;
		case END:
			//TODO 取消效果任务，执行退出行为，从队列移除
//			if(effect.getPrototypeId() == 101)
//				System.err.println(currTime + ": effect wait end " + effect);
			long endWaitTime = effect.getEffectPrototype().getPlayControllor().getEndWait(effect, currTime);
			if(endWaitTime < TASK_INTERVAL)
			{
				if(effect.getPrototypeId() == 1201)
					System.err.println(currTime + ": effect end " + effect);
				effect.getEffectPrototype().onExit(effect);
				effect.clean();
				queue.remove();
				if(effect.isTemporary())
					effect.destroy();
			}
			break;
		default:
			mLogger.warn("invailid effect state :[" + effect.getState() + "]");
		}
	}
	

	/**
	 * 增加效果
	 * @param effect
	 */
	public void addEffect(EffectEntity effect)
	{
		if(effect == null)
			return;
		
		skillCache.add(effect.getObjectId());
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.SKILL_MGR;
	}



}
