package com.garowing.gameexp.game.rts.skill.script.skillai;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 目标血量低于20%技能ai
 * @author seg
 *
 */
@ScriptName(name = "targetLowHp20AI")
public class TargetLowHp20SkillAIObj extends SkillAIObj
{
	/**
	 * 低血量阈限
	 */
	private static final float LOW_HP_THRESHOLD = 0.2f;
	
	@Override
	public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY)
	{
		WarInstance war = skill.getWar();
		GameObject target = war.getObject(targetId);
		if(target == null || target.getObjectType() != GameObjectType.TROOP)
			return false;
		
		Troop troopCaster = (Troop) target;
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
