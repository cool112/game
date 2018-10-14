package com.garowing.gameexp.game.rts.skill.script.skillai;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;

/**
 * 范围技能ai
 * @author seg
 *
 */
@ScriptName(name = "aoeAI")
public class AoeSkillAIObj extends SkillAIObj
{

	@Override
	public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY)
	{
		skill.getTargetIds().clear();
		skill.getTargetIds().add(targetId);
		skill.setTargetX(targetX);
		skill.setTargetY(targetY);
		EffectEntity firstEffect = null;
		try
		{
			TargetSelector selector = skill.getTargetSelector();
			if (selector == null)
				return false;
			
			List<GameObject> skillTargets = selector.findTarget(skill);
			firstEffect = skill.getCurrentEffect();
			firstEffect.setTargets(skillTargets);
			List<GameObject> targets = firstEffect.getEffectPrototype().getTargets(firstEffect);
			firstEffect.getTargetIds().clear();
			if (targets == null || targets.isEmpty())
			{
				return false;
			}
			
			return true;
		} finally
		{
			if(firstEffect != null)
				firstEffect.getTargetIds().clear();
			
			skill.getTargetIds().clear();
			skill.setTargetX(0);
			skill.setTargetY(0);
		}
	}

	@Override
	public int getModifiedPriority(SkillEntity skill)
	{
		return 0;
	}

}
