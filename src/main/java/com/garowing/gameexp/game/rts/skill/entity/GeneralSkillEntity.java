package com.garowing.gameexp.game.rts.skill.entity;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.behaviac.agent.BaseStrategyOptionAIObj;
import com.yeto.war.fightcore.behaviac.constants.StrategyOptionType;
import com.yeto.war.fightcore.behaviac.entity.StrategyOption;
import com.yeto.war.fightcore.behaviac.entity.WarStatisData;
import com.garowing.gameexp.game.rts.skill.handler.SkillCastHandler;
import com.garowing.gameexp.game.rts.skill.model.SkillModel;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.ai.skilltype.AiSkillPosType;
import com.yeto.war.module.ai.template.position.StrategyPosition;
import com.yeto.war.module.fight.model.StrategosSkillType;
import com.yeto.war.module.strategos.StrategosSkillModel;

/**
 * 将军技能实体
 * @author seg
 *
 */
public class GeneralSkillEntity extends SkillEntity implements StrategyOption
{
	/**
	 * 将军技能id
	 */
	private int strategosSkillId;
	
	/**
	 * 技能等级id
	 */
	private int skillLevelId;
	
	/**
	 * 等级
	 */
	private int level;
	
	/**
	 * 消耗能量
	 */
	private int energy;
	
	/**
	 * 技能类型
	 */
	private StrategosSkillType skillType;
	
	// ai使用数据
	private AiSkillPosType aiPosType;
	
	/**
	 * ai代理
	 */
	private BaseStrategyOptionAIObj agent;
	
	/**
	 * ai定位id
	 */
	private int aiPosId;
	
	public GeneralSkillEntity(WarInstance war, StrategosSkillModel strategosModel, SkillModel skillModel, GameObject caster, SkillCastHandler castHandler)
	{
		super(war, skillModel, caster, castHandler);
		this.strategosSkillId = strategosModel.getId();
		this.skillLevelId = strategosModel.getLevelSkillId();
		this.level = strategosModel.getLevel();
		this.energy = strategosModel.getProvision();
		this.skillType = strategosModel.getSkillType();
		this.aiPosType = strategosModel.getAiPosType();
		this.aiPosId = strategosModel.getAiPosId();
		this.agent = BaseStrategyOptionAIObj.valueOf(this);
	}
	
	@Override
	public StrategyPosition getAiPosition()
	{
		return StaticDataManager.STRATEGY_POSITION_DATA.getPositionAi(aiPosId);
	}


	public int getStrategosSkillId()
	{
		return strategosSkillId;
	}

	public void setStrategosSkillId(int strategosSkillId)
	{
		this.strategosSkillId = strategosSkillId;
	}

	public int getSkillLevelId()
	{
		return skillLevelId;
	}

	public void setSkillLevelId(int skillLevelId)
	{
		this.skillLevelId = skillLevelId;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getEnergy()
	{
		return energy;
	}

	public void setEnergy(int energy)
	{
		this.energy = energy;
	}

	public StrategosSkillType getSkillType()
	{
		return skillType;
	}

	public void setSkillType(StrategosSkillType skillType)
	{
		this.skillType = skillType;
	}

	public AiSkillPosType getAiPosType()
	{
		return aiPosType;
	}

	public void setAiPosType(AiSkillPosType aiPosType)
	{
		this.aiPosType = aiPosType;
	}

	@Override
	public int getCostEnergy()
	{
		return energy;
	}

	@Override
	public boolean isCd(long currTime)
	{
		return isInCD(currTime);
	}

	@Override
	public void updateWeight(WarStatisData data)
	{
		agent.updateWeight(data);
	}

	@Override
	public int getSelectCount()
	{
		return 0;
	}
	
	@Override
	public float getWeight()
	{
		return agent.getWeight();
	}

	public BaseStrategyOptionAIObj getAgent()
	{
		return agent;
	}

	public void setAgent(BaseStrategyOptionAIObj agent)
	{
		this.agent = agent;
	}

	@Override
	public int getOptionType()
	{
		return StrategyOptionType.GENERAL_SKILL;
	}

}
