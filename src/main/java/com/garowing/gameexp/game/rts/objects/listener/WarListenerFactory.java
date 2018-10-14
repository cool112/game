package com.garowing.gameexp.game.rts.listener;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.fight.food.FoodAddType;
import com.yeto.war.fightcore.skill.entity.GeneralSkillEntity;
import com.yeto.war.fightcore.skill.event.UseCardSkillEvent;
import com.yeto.war.fightcore.skill.event.UseStrategosSkillEvent;
import com.garowing.gameexp.game.rts.listener.event.AddCardEvent;
import com.garowing.gameexp.game.rts.listener.event.CallTroopWarEvent;
import com.garowing.gameexp.game.rts.listener.event.ChangeHpWarEvent;
import com.garowing.gameexp.game.rts.listener.event.DefultWarEvent;
import com.garowing.gameexp.game.rts.listener.event.DieWarEvent;
import com.garowing.gameexp.game.rts.listener.event.EndWarEvent;
import com.garowing.gameexp.game.rts.listener.event.FoodWarEvent;
import com.garowing.gameexp.game.rts.listener.event.GuidanceWarEvent;
import com.garowing.gameexp.game.rts.listener.event.OnTimeWarEvent;
import com.garowing.gameexp.game.rts.listener.event.UseCardWarEvent;
import com.garowing.gameexp.game.rts.listener.event.UseGeneralSkillWarEvent;
import com.garowing.gameexp.game.rts.listener.listener.AddCardListener;
import com.garowing.gameexp.game.rts.listener.listener.CallTroopListener;
import com.garowing.gameexp.game.rts.listener.listener.ChangeHpListener;
import com.garowing.gameexp.game.rts.listener.listener.DieDunListener;
import com.garowing.gameexp.game.rts.listener.listener.GuidanceEndDunListener;
import com.garowing.gameexp.game.rts.listener.listener.OnTimeDunListener;
import com.garowing.gameexp.game.rts.listener.listener.UpdateFoodListener;
import com.garowing.gameexp.game.rts.listener.listener.UseCardDunListener;
import com.garowing.gameexp.game.rts.listener.listener.UseGeneralSkillDunListener;
import com.garowing.gameexp.game.rts.listener.listener.WarEndListener;
import com.garowing.gameexp.game.rts.listener.listener.WarQuartzListener;
import com.garowing.gameexp.game.rts.listener.listener.WarStartListener;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.card.CardObject;
import com.yeto.war.module.player.PlayerEntity;
import com.yeto.war.module.troop.Troop;

/**
 * 战争事件监听工厂
 * 所有战争事件都继承这个工厂类,并无实质性用处,方便管理所有监听
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月2日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class WarListenerFactory
{
	
	/**
	 * 战争开始监听
	 * @param war
	 */
	public static void onStartWar (WarInstance war)
	{
		// 战争监听器处理
		DefultWarEvent event = new DefultWarEvent(war);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof WarStartListener))
				continue;
			
			WarStartListener action = (WarStartListener)listener;
			action.eventStart(event);
		}
	}
	
	/**
	 * 战争结束监听
	 * @param war
	 * @param winCampID
	 */
	public static void onEndWar (WarInstance war, int winCampID)
	{
		// 战争监听器处理
		EndWarEvent event = new EndWarEvent(war, winCampID);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof WarEndListener))
				continue;
			
			WarEndListener action = (WarEndListener)listener;
			action.eventEnd(event);
		}
	}
	
	/**
	 * 定时任务执行 
	 * @param war
	 */
	public static void onWarQuartz (WarInstance war)
	{
		DefultWarEvent event = new DefultWarEvent(war);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof WarQuartzListener))
				continue;
			
			WarQuartzListener action = (WarQuartzListener)listener;
			action.eventQuartz(event);
		}
	}
	
	/**
	 * 定时执行
	 * @param time
	 */
	public static void onTime (WarInstance war, long time)
	{
		// 战争处理器相关处理
		war.handler().onTimer(war, time);
		
		// 战争监听器处理
		OnTimeWarEvent event = new OnTimeWarEvent(war, time);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof OnTimeDunListener))
				continue;
			
			OnTimeDunListener action = (OnTimeDunListener)listener;
			action.eventTime(event);
		}
		
	}
	
	/**
	 * 部队死亡
	 * @param visible	部队
	 */
	public static void onDie (WarObjectInstance visible)
	{
		WarInstance war = visible.getWar();
		
		// 战争处理器相关处理
		war.handler().onDie(visible);
		
		// 战争监听器处理
		DieWarEvent event = new DieWarEvent(visible);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof DieDunListener))
				continue;
			
			DieDunListener action = (DieDunListener)listener;
			action.eventDie(event);
		}
	}
	
	/**
	 * 使用卡牌
	 * @param player
	 * @param card
	 */
	public static void onUseCard (AbsFightControl control, CardObject card)
	{
		WarInstance war = control.getWar();
		
		// 战争处理器相关处理
		war.handler().onUseCard(control, card);
		
		// 战争监听器处理
		UseCardWarEvent event = new UseCardWarEvent(war, card);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof UseCardDunListener))
				continue;
			
			UseCardDunListener action = (UseCardDunListener)listener;
			action.eventUseCard(event);
		}
		
		war.getSkillManager().addSkillEvent(new UseCardSkillEvent(war, card));
	}
	
	/**
	 * 使用将军技能
	 * @param player
	 * @param skill
	 */
	public static void onUseGeneralSkill (AbsFightControl control, GeneralSkillEntity skill)
	{
		WarInstance war = control.getWar();
		
		// 战争处理器相关处理
		war.handler().onUseStrategosSkill(control, skill);
		
		// 战争监听器处理
		UseGeneralSkillWarEvent event = new UseGeneralSkillWarEvent(control, skill.getSkillLevelId(), skill.getLevel());
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof UseGeneralSkillDunListener))
				continue;
			
			UseGeneralSkillDunListener action = (UseGeneralSkillDunListener)listener;
			action.eventUseGeneralSkill(event);
		}
		
		war.getSkillManager().addSkillEvent(new UseStrategosSkillEvent(war, skill));
	}
	
	/**
	 * 改变血量触发
	 * @param attackObject 
	 * @param visible
	 * @param beforeHp
	 * @param afterHp
	 */
	public static void onChangeHp (GameObject attackObject, WarObjectInstance visible, int beforeHp, int afterHp)
	{
		WarInstance war = visible.getWar();
		
		// 战争监听器处理
		ChangeHpWarEvent event = null;
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof ChangeHpListener))
				continue;
			
			if(event == null)
				event = new ChangeHpWarEvent(visible, beforeHp, afterHp);
			
			ChangeHpListener action = (ChangeHpListener)listener;
			action.eventChangeHp(event);
		}
	}

	/**
	 * 召唤军队触发
	 * @param player
	 * @param troop
	 */
	public static void onCallTroop (WarControlInstance control, Troop troop)
	{
		WarInstance war = control.getWar();
		
		CallTroopWarEvent event = new CallTroopWarEvent(control, troop);
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof CallTroopListener))
				continue;
			
			CallTroopListener listen = (CallTroopListener)listener;
			listen.eventCallTroop(event);
		}
	}
	
	/**
	 * 引导结束触发
	 * @param player
	 * @param guidance
	 */
	public static void onGuidanceEnd (PlayerEntity player, int guidance)
	{
		WarInstance war = player.getWar();
		GuidanceWarEvent event = new GuidanceWarEvent(war, guidance);
		
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof GuidanceEndDunListener))
				continue;
			
			GuidanceEndDunListener action = (GuidanceEndDunListener)listener;
			action.eventEnd(event);
		}
	}
	
	/**
	 * 粮草点更新增加
	 */
	public static void onFoodAddEvent(AbsFightControl control, int food, int delta, FoodAddType type, long time)
	{
		WarInstance war = control.getWar();
		FoodWarEvent event = new FoodWarEvent(control, food);
		
		war.handler().onFoodAddEvent(control, food, delta, type, time);
		
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof UpdateFoodListener))
				continue;
			
			UpdateFoodListener action = (UpdateFoodListener)listener;
			action.eventFood(event);
		}
	}
	
	/**
	 * 卡牌增加事件
	 * @param control
	 */
	public static void cardAddEvent(WarInstance war)
	{
		AddCardEvent event = new AddCardEvent(war);
		
		List<WarListener> list = war.getWarListenerList().getListeners();
		for (WarListener listener : list)
		{
			if (!(listener instanceof AddCardListener))
				continue;
			
			AddCardListener action = (AddCardListener)listener;
			action.eventAddCard(event);
		}
	}
}
