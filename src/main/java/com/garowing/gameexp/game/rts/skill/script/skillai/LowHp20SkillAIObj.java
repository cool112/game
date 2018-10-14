package com.garowing.gameexp.game.rts.skill.script.skillai;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.yeto.war.module.troop.Troop;

/**
 * 20%低血量技能ai
 * @author seg
 *
 */
@ScriptName(name = "lowHp20AI")
public class LowHp20SkillAIObj extends SkillAIObj
{
	/**
	 * 低血量阈限
	 */
	private static final float LOW_HP_THRESHOLD = 0.2f;
	
	@Override
	public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY)
	{
		GameObject caster = skill.getCaster();
		if(caster == null || caster.getObjectType() != GameObjectType.TROOP)
			return false;
		
		Troop troopCaster = (Troop) caster;
		int hp = troopCaster.getHp();
		int maxHp = troopCaster.getMaxHp();
		if(hp * 1.0f / maxHp > LOW_HP_THRESHOLD)
			return false;
		
		return true;
	}

	@Override
	public int getModifiedPriority(SkillEntity skill)
	{
		return 0;
	}
}
