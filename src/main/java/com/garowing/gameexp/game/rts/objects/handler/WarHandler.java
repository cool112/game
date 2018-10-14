package com.garowing.gameexp.game.rts.objects.handler;

//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.log4j.Logger;
//
//import com.yeto.war.fightcore.attr.constants.UnitType;
//import com.yeto.war.fightcore.attr.entity.AttrEntity;
//import com.yeto.war.fightcore.attr.model.AttrKey;
//import com.yeto.war.fightcore.attr.model.ValueType;
//import com.yeto.war.fightcore.fight.food.FoodAddType;
//import com.yeto.war.fightcore.skill.entity.GeneralSkillEntity;
//import com.yeto.war.fightcore.skill.entity.SkillEntity;
//import com.garowing.gameexp.game.rts.WarEngine;
//import com.garowing.gameexp.game.rts.WarUtils;
//import com.garowing.gameexp.game.rts.listener.WarListenerFactory;
//import com.garowing.gameexp.game.rts.objects.AbsFightControl;
//import com.garowing.gameexp.game.rts.objects.WarControlInstance;
//import com.garowing.gameexp.game.rts.objects.WarInstance;
//import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
//import com.yeto.war.module.card.CardObject;
//import com.yeto.war.module.fight.objects.PlayerControl;
//import com.yeto.war.module.player.PlayerEntity;
//import com.yeto.war.module.troop.Troop;
//import com.yeto.war.network.gs.sendable.fight.GC_GET_BATTLE_INFO;

/**
 * 战争处理类
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月10日
 * @version 	1.0
 */
public abstract class WarHandler 
{
//	static final Logger log = Logger.getLogger(WarHandler.class);
//	
//	/**
//	 * 获得战斗类型
//	 * @return
//	 */
//	public abstract int getBattleType();
//	/**
//	 * 创建战斗
//	 */
//	public  boolean onWarCreate(WarInstance war){return true;}
//
//	/**
//	 * 当所有玩家都连接完毕时
//	 * @param war
//	 */
//	public boolean onWarLinked (WarInstance war){return true;}
//	
//	/**
//	 * 时间到
//	 * @param war
//	 */
//	public void timeOut(WarInstance war){}
//	
//	/**
//	 * 链接超时
//	 * @param war
//	 */
//	public void linkTimeOut(WarInstance war){};
//	
//	/**
//	 * 玩家连接游戏战斗
//	 * @param object
//	 * @param player
//	 */
//	public boolean onPlayerLink (WarInstance war, PlayerEntity player){return true;} 
//	
//	/**
//	 * 玩家下线
//	 * @param battle
//	 * @param player
//	 */
//	public void onPlayerOffine (WarInstance war, PlayerEntity player){}
//	
//	/**
//	 * 获得回调的数据
//	 * @param war
//	 * @param player
//	 * @return
//	 */
//	public Map<String, String> getPlayerCallBackDatas (WarInstance war, PlayerEntity player)
//	{
//		return player.getDatas();
//	}
//	
//	/**
//	 * 死亡
//	 * @param object
//	 */
//	public void onDie(WarObjectInstance die){}
//	
//	/**
//	 * 初始化一个战斗控制器处理(包括玩家)
//	 * @param control
//	 */
//	public void onFightControlInit (AbsFightControl control){}
//	
//	/**
//	 * 使用卡牌
//	 * @param player
//	 * @param card
//	 */
//	public void onUseCard (AbsFightControl control, CardObject card){}
//	
//	/**
//	 * 使用将军技能
//	 * @param player
//	 * @param skill
//	 */
//	public void onUseStrategosSkill (AbsFightControl control, GeneralSkillEntity skill){}
//	
//	/**
//	 * on food add
//	 * @param control
//	 * @param food
//	 * @param delta
//	 * @param type
//	 */
//	public void onFoodAddEvent(AbsFightControl control, int food, int delta, FoodAddType type, long time) {}
//	
//	/**
//	 * init food
//	 * @param control
//	 */
//	public void initFood(AbsFightControl control) 
//	{
//		WarEngine.initFood(control);
//	}
//	
//	/**
//	 * 定时器
//	 * @param time
//	 */
//	public void onTimer(WarInstance war, long time){}
//	
//	/**
//	 * 军队加载基础属性时调用
//	 * @param control
//	 * @param troop
//	 */
//	public AttrEntity onTroopLoadBaseAttrs (WarInstance war, Troop troop, AttrEntity attrEntity)
//	{
//		if ((troop.getModelType() & UnitType.GENERAL.getMask()) == 0)
//			return attrEntity;
//		
//		WarControlInstance owner = troop.getControl();
//		if(owner instanceof PlayerControl)
//		{
//			int homeHp = owner.getHomeHp();
//			attrEntity.setAttrValue(AttrKey.HP, ValueType.BASE, homeHp);
//			return attrEntity;
//		}
//	
//		int level = war.getModel().getLevel();
//		int baseValue = getModelGeneralHp(level);
//		int hp = (int) ((1.0f * level * level / 500 + 1) * baseValue);
//		attrEntity.setAttrValue(AttrKey.HP, ValueType.BASE, hp);
//		return attrEntity;
//	}
//	
//	/**
//	 * 计算副本控制器的将军血量
//	 * @param level
//	 * @return
//	 */
//	protected int getModelGeneralHp(int level)
//	{
//		int baseValue = 20000;
//		if(level < 50)
//		{
//			baseValue = 14000;
//		}
//		if(level < 40)
//		{
//			baseValue = 11600;
//		}
//		if(level < 15)
//		{
//			baseValue = 5000;
//		}
//		if(level < 2)
//		{
//			baseValue = 1200;
//		}
//		return baseValue;
//	}
//	
//	/**
//	 * 获取中转数据
//	 * @param player
//	 * @param battleKey
//	 * @return
//	 */
//	public String getData(PlayerEntity player, String battleKey)
//	{
//		Map<String, String> datas = player.getDatas();
//		if(datas == null)
//			return null;
//		
//		return datas.get(battleKey);
//	}
//	
//	/**
//	 * 获取中转数据int值
//	 * @param player
//	 * @param battleKey
//	 * @return
//	 */
//	public int getDataInt(PlayerEntity player, String battleKey)
//	{
//		String data = getData(player, battleKey);
//		if(data == null)
//			return 0;
//		
//		return Integer.valueOf(data);
//	}
//	
//	/**
//	 * 获取中转数据long值
//	 * @param player
//	 * @param battleKey
//	 * @return
//	 */
//	public long getDataLong(PlayerEntity player, String battleKey)
//	{
//		String data = getData(player, battleKey);
//		if(data == null)
//			return 0;
//		
//		return Long.valueOf(data);
//	}
//
//	/**
//	 * 获取战场快照
//	 * @return
//	 */
//	public void getWarSnapShoot(WarInstance war, PlayerControl control)
//	{
//		//引导临时处理，完成引导，解除暂停
//		if(war.isSuspend())
//			WarEngine.getInstance().suspendEnd(war);
//				
//		PlayerEntity player = control.getPlayer();
//		Set<Integer> guideIds = new HashSet<Integer>(war.getGuideIds());
//		for(Integer guideId : guideIds)
//		{
//			WarListenerFactory.onGuidanceEnd(player, guideId);
//		}
//		war.getGuideIds().clear();
//				
//		Collection<WarObjectInstance> troops = WarUtils.getObjects(war);
//		List<SkillEntity> strategosSkills = control.getStrategosSkills();
//		Collection<CardObject> handCards = control.getCards().values();
//		int food = (int) control.getFood();
//		player.sendPacket(new GC_GET_BATTLE_INFO(0, player, troops, handCards, strategosSkills, food, getBattleType()));
//	}
}
