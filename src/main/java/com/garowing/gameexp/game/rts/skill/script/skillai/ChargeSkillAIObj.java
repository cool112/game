package com.garowing.gameexp.game.rts.skill.script.skillai;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.script.effect.active.ChargePrototype;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

/**
 * 冲锋技能ai
 * @author seg
 *
 */
@ScriptName(name = "chargeAI")
public class ChargeSkillAIObj extends SkillAIObj
{

	@Override
	public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY)
	{
		List<Integer> effectIds = skill.getEffectEntityList();
		WarInstance war = skill.getWar();
		GameObject target = war.getObject(targetId);
		if(target == null || target.getObjectType() != GameObjectType.TROOP)
			return false;
		
		Troop troopTarget = (Troop) target;
		Troop caster = skill.getCaster();
		float maxDistance = skill.getCastRange();
		float distance = MathUtil.getDistance(caster, (WarObjectInstance) target);
		for(Integer effectId : effectIds)
		{
			GameObject obj = war.getObject(effectId);
			if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity effect = (EffectEntity) obj;
			if(!(effect.getEffectPrototype() instanceof ChargePrototype))
				continue;
			
			ChargePrototype effectPrototype = (ChargePrototype) effect.getEffectPrototype();
			float minDistance = effectPrototype.getMinDistance();
//			System.err.println("dis :" + distance + " min:" + minDistance + " max:" + maxDistance);
			if(distance <= minDistance || distance > maxDistance)
				return false;
			
			float dx = troopTarget.getX() - caster.getX();
			float dy = troopTarget.getY() - caster.getY();
			
			float[] unitVector = MathUtil.getUnitVector(new float[]{dx, dy});
			if(isWayBlock(troopTarget, caster, unitVector, distance))
				return false;
		}
		return true;
	}

	/**
	 * 路径是否有障碍物,主要是排除场景障碍
	 * @param troopTarget
	 * @param caster
	 * @param unitVector
	 * @param distance
	 * @return
	 */
	private boolean isWayBlock(Troop troopTarget, Troop caster, float[] unitVector, float distance)
	{
		float x = caster.getX();
		float y = caster.getY();
		float targetX = troopTarget.getX();
		float targetY = troopTarget.getY();
		
		for (float dis = 1; dis<=distance; ++dis)
		{
			float currentX = x + unitVector[0];
			float currentY = y + unitVector[1];
			
			// 不可以移动到达的点
			if((int)currentX != (int)targetX || (int)currentY != (int)targetY)
			{
				if (!WarSceneService.getInstance().isMovePostion(caster.getWar(), currentX, currentY, caster.getModelType()))
				{
					return true;
				}
			}
			
			x = currentX;
			y = currentY; 
		}
		return false;
	}

	@Override
	public int getModifiedPriority(SkillEntity skill)
	{
		return 0;
	}

}
