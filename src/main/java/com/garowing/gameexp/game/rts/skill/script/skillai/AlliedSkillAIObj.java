package com.garowing.gameexp.game.rts.skill.script.skillai;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 友军技能ai
 * @author seg
 *
 */
@ScriptName(name = "alliedAI")
public class AlliedSkillAIObj extends SkillAIObj
{

	@Override
	public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY)
	{
		HavingCamp caster = skill.getCaster();
		int casterCampId = caster.getCampId();
		WarInstance war = skill.getWar();
		GameObject target = war.getObject(targetId);
		if(target == null)
			return false;
		
		HavingCamp campTarget = (HavingCamp) target;
		if(casterCampId != campTarget.getCampId())
			return false;
		
		return true;
	}

	@Override
	public int getModifiedPriority(SkillEntity skill)
	{
		return 0;
	}

}
