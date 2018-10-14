//package com.garowing.gameexp.game.rts.ai.behaviac.agent;
//
//import java.util.Collections;
//import java.util.List;
//
//import com.yeto.war.datastatic.StaticDataManager;
//import com.garowing.gameexp.game.rts.ai.GameObject;
//import com.garowing.gameexp.game.rts.ai.GameObjectType;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.StrategyOptionType;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.StrategyOption;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.WarStatisData;
//import com.garowing.gameexp.game.rts.ai.skill.entity.GeneralSkillEntity;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarInstance;
//import com.yeto.war.module.ai.entity.EmojiEntity;
//import com.yeto.war.module.ai.model.StrategyCardModel;
//import com.yeto.war.module.ai.model.StrategyEmojiModel;
//import com.yeto.war.module.ai.model.StrategyGeneralSkillModel;
//import com.yeto.war.module.ai.template.condition.StrategyCondition;
//import com.yeto.war.module.card.CardObject;
//
///**
// * 基础策略ai对象
// * @author seg
// *
// */
//public class BaseStrategyOptionAIObj extends Agent
//{
//	/**
//	 * 卡牌id
//	 */
//	private int ownerId;
//	
//	/**
//	 * 权重值
//	 */
//	private float weight;
//	
//	/**
//	 * 选项类型
//	 */
//	private int optionType;
//	
//	public BaseStrategyOptionAIObj(WarInstance war)
//	{
//		super(war);
//	}
//
//	@Override
//	public String getName()
//	{
//		return "card ai obj";
//	}
//
//	@Override
//	public int getObjectType()
//	{
//		return GameObjectType.AI;
//	}
//	
//	public static BaseStrategyOptionAIObj valueOf(GameObject option)
//	{
//		return valueOf(option, option.getWar());
//	}
//	
//	public static BaseStrategyOptionAIObj valueOf(GameObject option, WarInstance war)
//	{
//		BaseStrategyOptionAIObj optionAiObj = new BaseStrategyOptionAIObj(war);
//		optionAiObj.ownerId = option.getObjectId();
//		if(option instanceof StrategyOption)
//			optionAiObj.optionType = ((StrategyOption)option).getOptionType();
//		
//		return optionAiObj;
//	}
//	
//	/**
//	 * 更新权重,非bt方法
//	 * @param data
//	 * @param currTime
//	 */
//	public void updateWeight(WarStatisData data)
//	{
//		List<Integer> conditionIds = getConditionIds();
//		
//		weight = getInitWeight(data);
//		for(Integer conditionId : conditionIds)
//		{
//			StrategyCondition contidition = StaticDataManager.STRATEGY_CONDITION_DATA.getCondition(conditionId);
//			if(contidition != null)
//				weight += contidition.getWeight(data);
//		}
//	}
//
//	/**
//	 * 获取初始权重值
//	 * @param data
//	 * @return
//	 */
//	private float getInitWeight(WarStatisData data)
//	{
//		WarInstance war = getWar();
//		int energy = data.getSelfEnergy();
//		int costEnergy = 0;
//		int energyDiff = 0;
//		float initWeight = 0;
//		switch (optionType)
//		{
//		case StrategyOptionType.CARD:
//			CardObject card = (CardObject) war.getObject(ownerId);
//			costEnergy = card.getCostEnergy();
//			energyDiff = energy - costEnergy;
//			if(energyDiff < 0)
//				initWeight = energyDiff * StaticDataManager.STRATEGY_CONFIG_DATA.getEnergyOffset();
//			
//			break;
//		case StrategyOptionType.GENERAL_SKILL:
//			GeneralSkillEntity generalSkill = (GeneralSkillEntity) war.getObject(ownerId);
//			costEnergy = generalSkill.getCostEnergy();
//			energyDiff = energy - costEnergy;
//			long remainCd = generalSkill.getRemainCd(data.getStatisTime());
//			if(energyDiff < 0)
//				initWeight += energyDiff * StaticDataManager.STRATEGY_CONFIG_DATA.getEnergyOffset();
//			
//			if(remainCd > 0)
//				initWeight += (remainCd / 1000f) * StaticDataManager.STRATEGY_CONFIG_DATA.getCdOffset() ;
//			
//			break;
//		default:
//			break;
//		}
//		return initWeight;
//	}
//
//	/**
//	 * 获取条件ids
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Integer> getConditionIds()
//	{
//		List<Integer> conditionIds = Collections.EMPTY_LIST;
//		WarInstance war = getWar();
//		switch (optionType)
//		{
//		case StrategyOptionType.CARD:
//			CardObject card = (CardObject) war.getObject(ownerId);
//			StrategyCardModel cardModel = StaticDataManager.STRATEGY_CARD_DATA.getModel(card.getCardModelId());
//			if(cardModel == null)
//				return Collections.EMPTY_LIST;
//			
//			conditionIds = cardModel.getConditions();
//			break;
//		case StrategyOptionType.GENERAL_SKILL:
//			GeneralSkillEntity generalSkill = (GeneralSkillEntity) war.getObject(ownerId);
//			StrategyGeneralSkillModel generalSkillModel = StaticDataManager.STRATEGY_GENERAL_SKILL_DATA.getModel(generalSkill.getStrategosSkillId());
//			if(generalSkillModel == null)
//				return Collections.EMPTY_LIST;
//			
//			conditionIds = generalSkillModel.getConditions();
//			break;
//		case StrategyOptionType.EMOJI:
//			EmojiEntity emoji = (EmojiEntity) war.getObject(ownerId);
//			StrategyEmojiModel emojiModel = StaticDataManager.STRATEGY_EMOJI_DATA.getModel(emoji.getFastChatModelId());
//			if(emojiModel == null)
//				return Collections.EMPTY_LIST;
//			
//			conditionIds = emojiModel.getConditions();
//			break;
//		default:
//			break;
//		}
//		return conditionIds;
//	}
//
//	public float getWeight()
//	{
//		return weight;
//	}
//
//}
