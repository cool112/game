package com.garowing.gameexp.game.rts.skill.script.effect.aureole;

import java.util.List;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.yeto.war.module.troop.Troop;

/**
 * 固定效果光环
 * @author seg
 *
 */
public class FixEffectAureolePrototype extends EffectPrototype
{
	@Override
 	public SkillError onCheck(EffectEntity effect)
	{
		if(subEffectIds == null || subEffectIds.isEmpty())
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		for(Integer effectId : subEffectIds)
		{
			EffectPrototype effectPrototype = StaticDataManager.EFFECT_DATA.getEffectPrototype(effectId);
			if(effectPrototype == null)
				return SkillError.ERROR_BASE_DATA_ERROR;
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
		{
			if(needTarget)
				return SkillError.ERROR_EFFECT_NO_TARGET;
			else
				return SkillError.SUCCESS;
		}
			
		
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			for(Integer prototypeEffectId : subEffectIds)
			{
				SkillEngine.castOrRefreshTempEffect(effect, troopTarget, prototypeEffectId, currTime);
			}
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

}
