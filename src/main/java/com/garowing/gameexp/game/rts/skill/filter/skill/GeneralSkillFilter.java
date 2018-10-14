package com.garowing.gameexp.game.rts.skill.filter.skill;

import com.garowing.gameexp.game.rts.skill.entity.GeneralSkillEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;

/**
 * 将军技能过滤器
 * @author seg
 *
 */
public class GeneralSkillFilter implements SkillFilter
{

	@Override
	public boolean isValid(SkillEntity skill)
	{
		if(skill instanceof GeneralSkillEntity)
			return false;
		
		return true;
	}

}
