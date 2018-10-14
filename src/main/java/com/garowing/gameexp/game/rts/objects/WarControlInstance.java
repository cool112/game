package com.garowing.gameexp.game.rts.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.garowing.gameexp.game.rts.objects.model.WarControlModel;
import com.yeto.war.fightcore.skill.entity.AttachEffectsList;
import com.yeto.war.fightcore.skill.entity.SkillEntity;

import io.netty.util.internal.ConcurrentSet;

/**
 * 战争控制器对象
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月10日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class WarControlInstance extends GameObject implements HavingCamp, HavingSkill
{

	// 战争控制者Model
	private final WarControlModel model;
	
	/**
	 * 阵营Id
	 */
	private int campId = -1;
	
	/**
	 * 控制的对象集合
	 */
	private Set<Integer> troopSet;
	
	/**
	 * 附加效果集合
	 */
	private AttachEffectsList attachEffects;
	
	/**
	 * 绑定的技能实体id
	 */
	private Set<Integer> boundSkillIds = new HashSet<Integer>();
	
	public WarControlInstance (WarInstance war, WarControlModel model)
	{
		super(war);
		this.model = model;
		this.troopSet = new ConcurrentSet<Integer>();
		this.attachEffects = new AttachEffectsList(war);
	}
	
	/**
	 * 控制器 攻击力
	 * @create	2015年6月9日	darren.ouyang
	 */
	public abstract float getAttrAttack();
	
	/**
	 * 控制器 防御力
	 * @create	2015年6月9日	darren.ouyang
	 */
	public abstract float getAttrDefense();
	
	/**
	 * 获取基地血量
	 * @return
	 */
	public abstract int getHomeHp();
	
	/**
	 * 获取将军技能等级
	 * @return
	 */
	public abstract int getStrategosLevel();
	
	/**
	 * 获得所属阵营ID
	 * @return
	 * @create	2015年4月13日	darren.ouyang
	 */
	public int gainCampID ()
	{
		return this.campId;
	}
	
	/**
	 * 增加归属对象
	 * @param obj
	 */
	public void addObject(GameObject obj)
	{
		this.troopSet.add(obj.getObjectId());
	}
	
	/**
	 * 移除归属对象
	 * @param obj
	 */
	public void removeObject(GameObject obj)
	{
		this.troopSet.remove(obj.getObjectId());
	}
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public GameObject getObject(int id)
	{
		if(!troopSet.contains(id))
			return null;
		
		return getWar().getObject(id);
	}

	@Override
	public AttachEffectsList getAttachEffects()
	{
		return attachEffects;
	}
	
	/******************** get/set方法 ********************/
	
	public WarControlModel getModel() {
		return model;
	}

	public WarCampInstance getCamp()
	{
		return getWar().getCamp(campId);
	}

	public void setCamp(WarCampInstance camp) 
	{
		if(camp == null)
			this.campId = -1;
		else
			this.campId = camp.getObjectId();
	}
	
	@Override
	public int getObjectType()
	{
		return GameObjectType.CONTROLLOR;
	}
	
	@Override
	public List<SkillEntity> getBoundSkills()
	{
		WarInstance war = getWar();
		List<SkillEntity> skills = new ArrayList<SkillEntity>();
		for(Integer skillId : this.boundSkillIds)
		{
			GameObject skill = war.getObject(skillId);
			if(skill == null || skill.getObjectType() != GameObjectType.SKILL)
				continue;
			
			skills.add((SkillEntity) skill);
		}
		
		return skills;
	}

	@Override
	public SkillEntity chooseSkill(long currTime, int targetId, float targetX, float targetY)
	{
		return null;
	}

	@Override
	public void bindNewSkill(SkillEntity skill)
	{
		boundSkillIds.add(skill.getObjectId());
	}

	@Override
	public void unbindSkill(int objectId)
	{
		boundSkillIds.remove(objectId);
	}

	@Override
	public void cleanSkill()
	{
		if(boundSkillIds != null)
			boundSkillIds.clear();
	}
	
}
