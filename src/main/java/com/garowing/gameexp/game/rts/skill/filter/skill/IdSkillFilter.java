package com.garowing.gameexp.game.rts.skill.filter.skill;

import java.util.List;

import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;

/**
 * id技能过滤器
 * @author seg
 *
 */
public class IdSkillFilter implements SkillFilter
{
	/**
	 * 技能ID集合
	 */
	private List<Integer> skillIds;
	
	public IdSkillFilter(List<Integer> skillIds){
		this.skillIds = skillIds;
	}
	
	@Override
	public boolean isValid(SkillEntity skill)
	{
		int skillId = skill.getModelId();
		if(skillIds != null && skillIds.contains(skillId)){
			return false;
		}
		
		return true;
	}

}
