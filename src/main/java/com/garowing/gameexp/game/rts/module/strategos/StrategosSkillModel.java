package com.garowing.gameexp.game.rts.module.strategos;

import com.yeto.war.module.ai.skilltype.AiSkillPosType;
import com.yeto.war.module.fight.model.StrategosSkillType;


/**
 * 将军技能战斗对象
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月4日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class StrategosSkillModel
{
	/* 主将技能ID */
	private int id;
	
	/* 等级 */
	private int level;
	
	private int levelSkillId;
	
	private int quality;
	
	private int baseSkillId;

	/*粮草点 */
	private int provision;
	
	/* 是否开启*/
	private boolean isOpen;
	
	private StrategosSkillType skillType;

	// ai使用数据
	private AiSkillPosType aiPosType;
	
	/**
	 * ai定位id
	 */
	private int aiPosId;
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("主将技能");
		sb
			.append("[ID:").append(this.id).append("]")
			.append("[level:").append(this.level).append("]")
			.append("[levelSkillId:").append(this.levelSkillId).append("]")
			.append("[quality:").append(this.quality).append("]")
			.append("[baseSkillId:").append(this.baseSkillId).append("]")
			.append("[isOpen:").append(this.isOpen).append("]")
		;
		return sb.toString();
	}
	
	/**
	 * 检测是否存在技能
	 * @return
	 * @create	2015年1月5日	darren.ouyang
	 */
	public boolean checkSkill ()
	{
		if (baseSkillId <= 0)
			return false;
		
		return true;
	}
	
	public int getProvision() {
		return provision;
	}

	public void setProvision(int provision) {
		this.provision = provision;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getBaseSkillId() {
		return baseSkillId;
	}

	public void setBaseSkillId(int baseSkillId) {
		this.baseSkillId = baseSkillId;
	}

	public int getLevelSkillId() {
		return levelSkillId;
	}

	public void setLevelSkillId(int levelSkillId) {
		this.levelSkillId = levelSkillId;
	}
	
	public StrategosSkillType getSkillType() {
		return skillType;
	}

	public void setSkillType(StrategosSkillType skillType) {
		this.skillType = skillType;
	}
	
	public AiSkillPosType getAiPosType() {
		return aiPosType;
	}

	public void setAiPosType(AiSkillPosType aiPosType) {
		this.aiPosType = aiPosType;
	}

	public int getAiPosId()
	{
		return aiPosId;
	}

	public void setAiPosId(int aiPosId)
	{
		this.aiPosId = aiPosId;
	}

}
