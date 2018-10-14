package com.garowing.gameexp.game.rts.objects;

import java.util.List;

import com.yeto.war.fightcore.skill.entity.AttachEffectsList;
import com.yeto.war.fightcore.skill.entity.SkillEntity;

/**
 * 有技能的
 * @author seg
 * 2017年3月25日
 */
public interface HavingSkill
{
	/**
	 * 获取附加效果列表
	 * @return
	 */
	public AttachEffectsList getAttachEffects();
	
	/**
	 * 获取绑定的技能
	 * @return
	 */
	public List<SkillEntity> getBoundSkills();
	
	/**
	 * 选择技能
	 * @return
	 */
	public SkillEntity chooseSkill(long currTime, int targetId, float targetX, float targetY);
	
	/**
	 * 绑定新技能
	 * @param skill
	 */
	public void bindNewSkill(SkillEntity skill);

	/**
	 * 解绑技能
	 * @param objectId
	 */
	public void unbindSkill(int objectId);
	
	/**
	 * 清除技能
	 */
	public void cleanSkill();
}
