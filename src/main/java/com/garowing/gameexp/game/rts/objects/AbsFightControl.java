package com.garowing.gameexp.game.rts.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.garowing.gameexp.game.rts.objects.model.WarControlModel;
//import com.yeto.war.datastatic.StaticDataManager;
//import com.yeto.war.fightcore.behaviac.agent.BaseControlAIObj;
//import com.yeto.war.fightcore.skill.entity.SkillEntity;
//import com.yeto.war.module.ai.entity.EmojiEntity;
//import com.yeto.war.module.ai.model.StrategyEmojiModel;
//import com.yeto.war.module.card.CardObject;
//import com.yeto.war.module.strategos.StrategosObject;
//import com.yeto.war.module.strategos.StrategosSkillModel;

/**
 * 可战斗的控制器
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年5月18日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class AbsFightControl extends WarControlInstance
{
//	/* 当前粮草点 */
//	private float food;
//	
//	// 粮草恢复速度, 每秒恢复粮草数
//	private float foodSD;
//	
//	/* 手牌 */
//	private final Map<Integer, CardObject> handCards;
//	
//	/* 抽取卡牌的列表 */
//	private final List<CardObject> extractCards;
//	
//	/* 将军技能 */
//	private final List<StrategosSkillModel> strategosSkills;
//	
//	/**
//	 * 已使用卡牌
//	 */
//	private final List<CardObject> historyCards;
//	
//	/**
//	 * 最大历史卡牌记录
//	 */
//	public static final int MAX_HISTORY_CARDS = 5;
//	
//	/**
//	 * 额外初始能量
//	 */
//	private int extraInitPower;
//	
//	/**
//	 * 表情集合
//	 */
//	private Map<Integer, EmojiEntity> emojiMap;
//	
//	/**
//	 * 控制器代理
//	 */
//	private BaseControlAIObj agent;
//	
//	public AbsFightControl(WarInstance war, WarControlModel controlModel)
//	{
//		super(war, controlModel);
//		
//		this.strategosSkills 	= new ArrayList<>();
//		this.handCards 				= new HashMap<>();
//		this.extractCards 		= new ArrayList<>();
//		this.historyCards 		= new ArrayList<>();
//		this.emojiMap			= new HashMap<Integer, EmojiEntity>();
//		Map<Integer, StrategyEmojiModel> emojiModelMap = StaticDataManager.STRATEGY_EMOJI_DATA.getMap();
//		if(emojiModelMap != null)
//		{
//			for(StrategyEmojiModel model : emojiModelMap.values())
//			{
//				EmojiEntity emojiEntity = new EmojiEntity(war);
//				emojiEntity.setFastChatModelId(model.getFastChatModelId());
//				this.emojiMap.put(emojiEntity.getFastChatModelId(), emojiEntity);
//			}
//		}
//			
//	}
//	
//	/**
//	 * 获得拥有者ID
//	 * @return
//	 */
//	public abstract long gainOwnerID ();
//	
//	/**
//	 * 获得拥有者牌组内容
//	 * @return
//	 */
//	public abstract List<CardObject> getDeck();
//	
//	
//	/**
//	 * 获得拥有者战斗者出战的将军
//	 * @return
//	 */
//	public abstract StrategosObject getStrategos ();
//	
//	@Override
//	public float getAttrAttack() {
//		StrategosObject strategos = getStrategos();
//		if(strategos != null)
//			return strategos.getLegAttack();
//		return 0;
//	}
//	
//	@Override
//	public float getAttrDefense() {
//		StrategosObject strategos = getStrategos();
//		if(strategos != null)
//			return strategos.getLegDef();
//		return 0;
//	}
//	
//	@Override
//	public int getHomeHp()
//	{
//		StrategosObject strategos = getStrategos();
//		if(strategos != null)
//			return strategos.getHomeHp();
//		return 0;
//	}
//	
//	@Override
//	public int getStrategosLevel()
//	{
//		StrategosObject strategos = getStrategos();
//		if(strategos != null)
//			return strategos.getLevel();
//		return 0;
//	}
//	
//	@Override
//	public String getName() 
//	{
//		return "战斗控制器-" + getObjectId();
//	}
//	
//	/******************** 卡牌操作方法 ********************/
//	
//	public CardObject gainCard (int cardId)
//	{
//		return handCards.get(cardId);
//	}
//	
//	public CardObject removeCard (int cardId)
//	{
//		return handCards.remove(cardId);
//	}
//	
//	public boolean checkCard (int cardId)
//	{
//		return handCards.containsKey(cardId);
//	}
//	
//	public int gainCardSize ()
//	{
//		return handCards.size();
//	}
//	
//	public void clearCards ()
//	{
//		handCards.clear();
//		extractCards.clear();
//	}
//	
//	/**
//	 * 获取额外卡牌数量
//	 * @return
//	 */
//	public int gainExtractSize ()
//	{
//		return extractCards.size();
//	}
//	
//	/**
//	 * 增加历史卡牌
//	 * @param card
//	 */
//	public void addHistoryCard(CardObject card)
//	{
//		historyCards.add(card);
//		if(historyCards.size() > MAX_HISTORY_CARDS)
//			historyCards.remove(0);
//	}
//	
//	public Collection<CardObject> copyCards ()
//	{
//		return new ArrayList<>(handCards.values());
//	}
//
//	public Map<Integer, CardObject> getCards() {
//		return handCards;
//	}
//
//	public List<CardObject> getExtractCards() {
//		return extractCards;
//	}
//	
//	/******************** 将军操作方法 ********************/
//
//	public List<StrategosSkillModel> copyStrategosSkills() 
//	{
//		return new ArrayList<>(strategosSkills);
//	}
//	
//	public List<SkillEntity> getStrategosSkills() 
//	{
//		StrategosObject general = getStrategos();
//		List<SkillEntity> skills = general.getBoundSkills();
//		return skills;
//	}
//	
//	public void clearStrategosSkills ()
//	{
//		strategosSkills.clear();
//	}
//
//
//	public float getFood() {
//		return food;
//	}
//
//	public void setFood(float food) {
//		this.food = food;
//	}
//
//	public float getFoodSD() {
//		return foodSD;
//	}
//
//	public void setFoodSD(float foodSD) {
//		this.foodSD = foodSD;
//	}
//
//	public List<CardObject> getHistoryCards()
//	{
//		return historyCards;
//	}
//
//	public int getExtraInitPower()
//	{
//		return extraInitPower;
//	}
//
//	public void setExtraInitPower(int extraInitPower)
//	{
//		this.extraInitPower = extraInitPower;
//	}
//
//	public Map<Integer, EmojiEntity> getEmojiMap()
//	{
//		return emojiMap;
//	}
//
//	public BaseControlAIObj getAgent()
//	{
//		return agent;
//	}
//
//	public void setAgent(BaseControlAIObj agent)
//	{
//		this.agent = agent;
//	}
//	
	
}

