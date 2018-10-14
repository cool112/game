//package com.garowing.gameexp.game.rts.ai.behaviac.agent;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.apache.log4j.Logger;
//
//import com.yeto.war.datastatic.StaticDataManager;
//import com.garowing.gameexp.game.rts.ai.GameObject;
//import com.garowing.gameexp.game.rts.ai.GameObjectType;
//import com.garowing.gameexp.game.rts.ai.SendPacketUtil;
//import com.garowing.gameexp.game.rts.ai.attr.constants.UnitType;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentClass;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentMethod;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.StrategyOptionType;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.StrategyOption;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.WarStatisData;
//import com.garowing.gameexp.game.rts.ai.fight.FightEngine;
//import com.garowing.gameexp.game.rts.ai.skill.SkillEngine;
//import com.garowing.gameexp.game.rts.ai.skill.constants.SkillFuncType;
//import com.garowing.gameexp.game.rts.ai.skill.entity.GeneralSkillEntity;
//import com.garowing.gameexp.game.rts.ai.skill.entity.SkillEntity;
//import com.garowing.gameexp.game.rts.ai.war.WarUtils;
//import com.garowing.gameexp.game.rts.ai.war.objects.AbsFightControl;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarControlInstance;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarInstance;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarObjectInstance;
//import com.yeto.war.module.ErrorCode;
//import com.yeto.war.module.ai.entity.EmojiEntity;
//import com.yeto.war.module.ai.skilltype.AiSkillPosType;
//import com.yeto.war.module.ai.template.condition.StrategyCondition;
//import com.yeto.war.module.ai.template.position.StrategyPosition;
//import com.yeto.war.module.card.CardObject;
//import com.yeto.war.module.fight.objects.PlayerControl;
//import com.yeto.war.module.player.PlayerEntity;
//import com.yeto.war.module.strategos.StrategosObject;
//import com.yeto.war.module.troop.Troop;
//import com.yeto.war.network.gs.sendable.fight.GC_SEND_FAST_CHAT;
//import com.yeto.war.utils.MathUtil;
//import com.yeto.war.utils.RatioUitl;
//
///**
// * 控制器ai
// * @author seg
// * 2017年5月2日
// */
//@AgentClass
//public class BaseControlAIObj extends Agent
//{
//	/**
//	 * 日志
//	 */
//	private static final Logger mLogger = Logger.getLogger(BaseControlAIObj.class);
//	
//	/**
//	 * 策略，进攻
//	 */
//	private static final int STRATEGY_ATTACK = 1;
//	
//	/**
//	 * 策略防守
//	 */
//	private static final int STRATEGY_DEFENCE = 2;
//	
//	/**
//	 * 策略等待
//	 */
//	private static final int STRATEGY_WAIT = 3;
//	
//	/**
//	 * 策略，杀死某个单位
//	 */
//	private static final int STRATEGY_KILL = 4;
//	
//	/**
//	 * 忽视一切权重阈值
//	 */
//	private static float ignoreOtherThreshold = 0;
//	
//	/**
//	 * 消耗品功能map
//	 * consumedFuncMap.cardOrGeneralSkillId = funcTypes
//	 */
//	private Map<Integer, List<SkillFuncType>> consumedFuncMap = new HashMap<Integer, List<SkillFuncType>>();
//	
//	/**
//	 * 功能物品map
//	 * funcConsumedMap.funcType = cardOrGeneralSkillIds
//	 */
//	private Map<SkillFuncType, Set<Integer>> funcConsumedMap = new HashMap<SkillFuncType, Set<Integer>>();
//	
//	/**
//	 * 策略对应的消耗品id
//	 * strategyConsumedMap.strategy = cardOrGeneralSkillIds
//	 */
//	private Map<Integer, Set<Integer>> strategyConsumedMap = new HashMap<Integer, Set<Integer>>();
//	
//	/**
//	 * 选择的策略
//	 */
//	private int selectedStrategy;
//	
//	/**
//	 * 控制器引用
//	 */
//	private WarControlInstance control;
//	
//	/**
//	 * 战场统计数据
//	 */
//	private WarStatisData warData;
//
//	public BaseControlAIObj(WarInstance war)
//	{
//		super(war);
//	}
//	
//	public static BaseControlAIObj valueOf(WarControlInstance control, WarInstance war)
//	{
//		BaseControlAIObj controlAi = new BaseControlAIObj(war);
//		controlAi.control = control;
//		return controlAi;
//	}
//	
//	/**
//	 * 施法，卡牌或将军技能
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus cast()
//	{
//		System.err.println("cast objId");
//		if(selectedStrategy == 0)
//			return EBTStatus.BT_SUCCESS;
//		
//		GameObject strategyOption = getWar().getObject(selectedStrategy);
//		if(strategyOption.getObjectType() == GameObjectType.CARD)
//		{
//			CardObject cardOption = (CardObject) strategyOption;
////			AiSkillPosType positionAi = cardOption.getAiPosType();
////			if(positionAi == null)
////				return EBTStatus.BT_FAILURE;
////			
////			AbsFightControl fightControl = (AbsFightControl) cardOption.getControl();
////			if(!positionAi.handler.check(fightControl))
////				return EBTStatus.BT_SUCCESS;
////			
////			float[] xy = positionAi.handler.getPos(fightControl, cardOption.chooseSkill(0, 0, 0, 0));
////			WarObjectInstance target = positionAi.handler.getTarget(fightControl);
////			int targetId = 0;
////			if(target != null)
////				targetId = target.getObjectId();
//			
//			StrategyPosition positionAi = cardOption.getAiPosition();
//			if(positionAi == null)
//				return EBTStatus.BT_FAILURE;
//			
//			AbsFightControl fightControl = (AbsFightControl) cardOption.getControl();
//			if(!positionAi.check(fightControl))
//				return EBTStatus.BT_SUCCESS;
//			
//			float[] xy = positionAi.getPosition(warData, fightControl, cardOption.chooseSkill(0, 0, 0, 0));
////			WarObjectInstance target = positionAi.getTarget(warData, fightControl);
//			int targetId = 0;
////			if(target != null)
////				targetId = target.getObjectId();
//			
//			SkillEngine.castSkillByControllor(cardOption, targetId, xy[0], xy[1]);
//		}
//		else if(strategyOption.getObjectType() == GameObjectType.SKILL)
//		{
//			GeneralSkillEntity skillOption = (GeneralSkillEntity) strategyOption;
////			AiSkillPosType positionAi = skillOption.getAiPosType();
////			if(positionAi == null)
////				return EBTStatus.BT_FAILURE;
////			
////			AbsFightControl fightControl = (AbsFightControl) ((StrategosObject)skillOption.getCaster()).getControl();
////			if(!positionAi.handler.check(fightControl))
////				return EBTStatus.BT_SUCCESS;
////			
////			float[] xy = positionAi.handler.getPos(fightControl, skillOption);
////			WarObjectInstance target = positionAi.handler.getTarget(fightControl);
//			
//			StrategyPosition positionAi = skillOption.getAiPosition();
//			if(positionAi == null)
//				return EBTStatus.BT_FAILURE;
//			
//			AbsFightControl fightControl = (AbsFightControl) ((StrategosObject)skillOption.getCaster()).getControl();
//			if(!positionAi.check(fightControl))
//				return EBTStatus.BT_SUCCESS;
//			
//			float[] xy = positionAi.getPosition(warData, fightControl, skillOption);
//			WarObjectInstance target = null;
//			SkillEngine.castSkill(skillOption, target, xy[0], xy[1]);
//		}
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 发送表情
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus sendEmoji()
//	{
//		if(!(control instanceof AbsFightControl))
//			return EBTStatus.BT_FAILURE;
//		
//		AbsFightControl fightControl = (AbsFightControl) control;
//		for(EmojiEntity emoji : fightControl.getEmojiMap().values())
//		{
//			if(emoji.getSelectCount() > 0)
//				continue;
//			
//			emoji.updateWeight(warData);
//			if(emoji.getWeight() <= 0)
//				continue;
//			
//			emoji.addSelectedCount();
//			WarInstance war = getWar();
//			PlayerEntity player = (fightControl instanceof PlayerControl) ? ((PlayerControl) fightControl).getPlayer() : null;
//			
//			SendPacketUtil.sendWarPacket(war, (sender)->{
//				sender.sendPacket(new GC_SEND_FAST_CHAT(ErrorCode.SUCCESS.code(), sender.getId(), player, 0, emoji.getFastChatModelId(), null));
//				}
//			);
//			break;
//		}
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 施放可以战斗的卡牌到血量最低的敌人身旁
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus castFighterCardNearLowestEnemy()
//	{
/////<<< BEGIN WRITING YOUR CODE castFighterCardNearLowestEnemy
//		return EBTStatus.BT_INVALID;
/////<<< END WRITING YOUR CODE
//	}
//
//	/**
//	 * 释放伤害技能到血量最低的敌人上
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus castSkillOnLowestEnemy()
//	{
/////<<< BEGIN WRITING YOUR CODE castSkillOnLowestEnemy
//		return EBTStatus.BT_INVALID;
/////<<< END WRITING YOUR CODE
//	}
//
//	/**
//	 * 选择策略，从现有策略中选择使用的卡牌策略以及施放位置策略
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus chooseStrategy()
//	{
//		int selfTroopCount = WarUtils.getObjectsByCamp(getWar(), control.gainCampID()).size();
//		int enemyTroopCount = WarUtils.getObjectsByEnemy(getWar(), control.gainCampID()).size();
//		if(selfTroopCount >= enemyTroopCount)
//			selectedStrategy = STRATEGY_ATTACK;
//		else
//			selectedStrategy = STRATEGY_DEFENCE;
//		
//		return EBTStatus.BT_INVALID;
//	}
//
//	/**
//	 * 根据使用卡牌策略选择一张卡牌或技能
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus findCardOrSkill()
//	{
//		if(!(control instanceof AbsFightControl))
//			return EBTStatus.BT_FAILURE;
//		
//		
//		WarInstance war = getWar();
//		Set<Integer> conditionIds = warData.getValidConditionIds();
//		int selfCampId = control.getCampId();
//		warData.setSelfCampId(selfCampId);
//		Collection<WarObjectInstance> objects = WarUtils.getObjects(war);
//		sort((List<WarObjectInstance>) objects);
//		WarObjectInstance enemyBase = warData.getEnemyBase();
//		WarObjectInstance selfBase = warData.getSelfBase();
//		if(enemyBase != null)
//			warData.setEnemyEnergy((int) ((AbsFightControl)enemyBase.getControl()).getFood());
//		
//		if(selfBase != null)
//		{
//			float hpPct = selfBase.getHp() * 1.0f /selfBase.getMaxHp();
//			ignoreOtherThreshold += hpPct > 0.5 ? 0 : (hpPct - 0.5) * StaticDataManager.STRATEGY_CONFIG_DATA.getBaseHpPer();
//		}
//			
//		selectedStrategy = 0;
//		float friendUnitsScore = 0;
//		float enemyUnitsScore = 0;
//		for(WarObjectInstance obj : objects)
//		{
////			warData.addTotal();
////			if((obj.getModelType() & UnitType.GENERAL.getMask()) > 0)
////			{
////				if(obj.getCampId() == selfCampId)
////				{
////					selfBase = (Troop) obj;
////					warData.setSelfBaseHpPercent(selfBase.getHp() * 1.0f /selfBase.getMaxHp());
////				}
////				else
////				{
////					enemyBase = (Troop) obj;
////					warData.setEnemyBaseHpPercent(enemyBase.getHp() * 1.0f /enemyBase.getMaxHp());
////				}
////			}
////			
////			if(obj.getCampId() == selfCampId)
////			{
////				statisFriendUnit(obj, warData, selfBase);
////			}
////			else
////			{
////				statisEnemyUnit(obj, warData, selfBase);
////			}
//			for(Integer conditionId : conditionIds)
//			{
//				StrategyCondition condition = StaticDataManager.STRATEGY_CONDITION_DATA.getCondition(conditionId);
//				if(condition != null)
//					condition.count(warData, obj);
//			}
//			
//			if(obj.getCampId() == selfCampId)
//				friendUnitsScore += ((Troop)obj).getUnitScore();
//			else
//			{
//				float score = ((Troop)obj).getUnitScore();
//				if(MathUtil.isInRange(obj, selfBase, 15))
//					score *= 2;
//				
//				enemyUnitsScore += score;
//			}
//				
//		}
//		
//		ignoreOtherThreshold += (friendUnitsScore - enemyUnitsScore) * StaticDataManager.STRATEGY_CONFIG_DATA.getForcePer();
////		StringBuilder logInfo = new StringBuilder();
////		logInfo.append("机器人决策日志：").append(" 当前用时:").append(FightEngine.getFightTime(war)/1000).append("s\n")
////		.append("我方势力：").append(friendUnitsScore).append(" 敌方势力：").append(enemyUnitsScore).append(" 剩余能量：").append(warData.getSelfEnergy());
////		logInfo.append(" 权重阈值：").append(ignoreOtherThreshold).append("\n").append("选项权重：[");
//		StrategyOption maxWeightOption = null;
//		boolean beyongThreshold = false;
//		boolean maxOptionValid = false;
//		
//		for(Entry<Integer, StrategyOption> validOptionEntry : warData.getValidMap().entrySet())
//		{
//			StrategyOption validOption = validOptionEntry.getValue();
//			validOption.updateWeight(warData);
//			float weight = validOption.getWeight();
////			appendLog(logInfo, validOption, weight);
//			if(maxWeightOption == null || maxWeightOption.getWeight() < weight)
//			{
//				maxWeightOption = validOption;
//				maxOptionValid = true;
//				if(weight > ignoreOtherThreshold)
//				{
////					selectedStrategy = validOptionEntry.getKey();
////					return EBTStatus.BT_SUCCESS;
//					beyongThreshold = true;
//				}
//			}
//		}
//		
//		for(Entry<Integer, StrategyOption> invalidOptionEntry : warData.getInvalidMap().entrySet())
//		{
//			StrategyOption invalidOption = invalidOptionEntry.getValue();
//			invalidOption.updateWeight(warData);
//			float weight = invalidOption.getWeight();
////			appendLog(logInfo, invalidOption, weight);
//			if(maxWeightOption == null || maxWeightOption.getWeight() < weight)
//			{
//				maxWeightOption = invalidOption;
//				maxOptionValid = false;
//				if(weight > ignoreOtherThreshold)
//				{
////					selectedStrategy = invalidOptionEntry.getKey();
////					return EBTStatus.BT_FAILURE;
//					beyongThreshold = true;
//				}
//			}
//			
//		}
//		
////		logInfo.append("]");
////		mLogger.info(logInfo.toString());
//		
//		if(beyongThreshold && maxOptionValid)
//		{
//			selectedStrategy = ((GameObject)maxWeightOption).getObjectId();
//			return EBTStatus.BT_SUCCESS;
//		}
//			
//		
////		int total = warData.getValidMap().size() + warData.getInvalidMap().size();
////		if(warData.getValidMap().size() >= total -1)
////		{
////			StrategyOption option = RatioUitl.random(new ArrayList<StrategyOption>(warData.getValidMap().values()));
////			if(option != null)
////			{
////				selectedStrategy = ((GameObject)option).getObjectId();
////				return EBTStatus.BT_SUCCESS;
////			}
////		}
//			
//		return EBTStatus.BT_FAILURE;
//	}
//
//	/**
//	 * @param logInfo
//	 * @param validOption
//	 * @param weight
//	 */
//	private void appendLog(StringBuilder logInfo, StrategyOption validOption, float weight)
//	{
//		if(validOption.getOptionType() == StrategyOptionType.GENERAL_SKILL)
//			logInfo.append("将军技能：").append(weight).append("|");
//		else if(validOption.getOptionType() == StrategyOptionType.CARD)
//			logInfo.append(((CardObject)validOption).getCardModelId()).append(":").append(weight).append("|");
//	}
//	
//	/**
//	 * 统计敌人单位
//	 * @param obj
//	 * @param warData2
//	 * @param selfBase
//	 */
//	private void statisEnemyUnit(WarObjectInstance obj, WarStatisData warData, Troop selfBase)
//	{
//		warData.addEnemyTotal();
//		if(selfBase != null && MathUtil.isInRange(obj, selfBase, 10))
//			warData.addEnemyIn10();
//		
//		int unitType = obj.getModelType();
//		if((unitType & UnitType.BUILDING.getMask()) > 0)
//			warData.addEnemyBuildingCount();
//		
//		if((unitType & UnitType.ATTCK_AIR.getMask()) > 0)
//			warData.addEnemyAtkAirCount();
//		
//		if((unitType & UnitType.RANGE.getMask()) > 0)
//			warData.addEnemyRangeCount();
//	}
//
//	/**
//	 * 如有需要排序，简单统计
//	 * @param objects
//	 */
//	private void sort(List<WarObjectInstance> objects)
//	{
//		for(int i = 0; i < objects.size() ; i++)
//		{
//			warData.addTotal();
//			WarObjectInstance obj = objects.get(i);
//			if(obj.getCampId() == warData.getSelfCampId())
//				warData.addFriendTotal();
//			else
//				warData.addEnemyTotal();
//			
//			if((obj.getModelType() & UnitType.GENERAL.getMask()) > 0)
//			{
//				if(obj.getCampId() == warData.getSelfCampId())
//					warData.setSelfBase(obj);
//				else
//					warData.setEnemyBase(obj);
//					
//			}
//		}
//	}
//
//	/**
//	 * 统计友军数据
//	 * @param obj
//	 * @param warData
//	 * @param selfBase
//	 */
//	private void statisFriendUnit(WarObjectInstance obj, WarStatisData warData, Troop selfBase)
//	{
//		warData.addFriendTotal();
//		if(selfBase != null && obj.getObjectId() == selfBase.getObjectId())
//			return;
//		
//		int modelType = obj.getModelType();
//		if(selfBase != null && MathUtil.isInRange(obj, selfBase, 15))
//		{
//			warData.addFriendIn15();
//			if((modelType & UnitType.LIVING_THING.getMask()) > 0)
//				warData.addFriendLivingIn15();
//		}
//			
//	}
//
//	/**
//	 * 根据施放策略找到施法位置
//	 * @return
//	 */
//	public EBTStatus findCastPosition()
//	{
/////<<< BEGIN WRITING YOUR CODE findCastPosition
//		return EBTStatus.BT_INVALID;
/////<<< END WRITING YOUR CODE
//	}
//
//	/**
//	 * 寻找一张有输出的卡牌
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus findFighterCard()
//	{
/////<<< BEGIN WRITING YOUR CODE findFighterCard
//		return EBTStatus.BT_INVALID;
/////<<< END WRITING YOUR CODE
//	}
//
//	/**
//	 * 寻找一个有伤害的技能
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus findHarmSkill()
//	{
/////<<< BEGIN WRITING YOUR CODE findHarmSkill
//		return EBTStatus.BT_INVALID;
/////<<< END WRITING YOUR CODE
//	}
//
//	/**
//	 * 此刻，是否拥有策略，没有策略等于等待机会策略
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus hasStrategies()
//	{
////		if(strategyConsumedMap.isEmpty())
////			return EBTStatus.BT_FAILURE;
//		
//		long currTime = System.currentTimeMillis();
//		warData = new WarStatisData();
//		warData.setStatisTime(currTime);
//		warData.setSelfEnergy((int) ((AbsFightControl)control).getFood());
//		ignoreOtherThreshold = 0;
//		
//		int energyFactor = (int) (StaticDataManager.STRATEGY_CONFIG_DATA.getEnergyCriticality() + (warData.getSelfEnergy() * StaticDataManager.STRATEGY_CONFIG_DATA.getEnergyPer()));
////		int energyFactor = 0;
//		if(energyFactor <= 0)
//			ignoreOtherThreshold += -99999;
//		else
//			ignoreOtherThreshold += energyFactor;
//		
//		Map<Integer, CardObject> currentCards = ((AbsFightControl)control).getCards();
//		float energy = ((AbsFightControl)control).getFood();
//		for(CardObject card : currentCards.values())
//		{
//			warData.getValidConditionIds().addAll(card.getAgent().getConditionIds());
//			if(card.getProvision() <= energy)
//				warData.addValidOption(card);
//			else
//				warData.addInvalidOption(card);
//		}
//		
//		
//		List<SkillEntity> strategosSkills = ((AbsFightControl)control).getStrategosSkills();
//		for(SkillEntity skill : strategosSkills)
//		{
//			if(!(skill instanceof GeneralSkillEntity))
//				continue;
//			
//			GeneralSkillEntity generalSkill = (GeneralSkillEntity) skill;
//				
//			if(generalSkill.getModel().isPassive())
//				continue;
//			
//			warData.getValidConditionIds().addAll(generalSkill.getAgent().getConditionIds());
//			if(generalSkill.getEnergy() <= energy && !generalSkill.isInCD(currTime))
//				warData.addValidOption(generalSkill);
//			else
//				warData.addInvalidOption(generalSkill);
//			
//		}
//		
//		for(EmojiEntity emojiEntity : ((AbsFightControl)control).getEmojiMap().values())
//		{
//			warData.getValidConditionIds().addAll(emojiEntity.getAgent().getConditionIds());
//		}
//		
//		if(!warData.getValidMap().isEmpty())
//		{
//			return EBTStatus.BT_SUCCESS;
//		}
//		
//		return EBTStatus.BT_FAILURE;
//	}
//
//	/**
//	 * 等待机会，或无计可施
//	 * @return
//	 */
//	public EBTStatus endTurn()
//	{
/////<<< BEGIN WRITING YOUR CODE wait
//		return EBTStatus.BT_INVALID;
/////<<< END WRITING YOUR CODE
//	}
//
//	@Override
//	public String getName()
//	{
//		return "BaseControlAIObj";
//	}
//
//	@Override
//	public int getObjectType()
//	{
//		return GameObjectType.AI;
//	}
//
//}
