package com.garowing.gameexp.game.rts.skill.script.effect.aureole;

import java.util.List;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillParams;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.yeto.war.module.troop.Troop;

/**
 * 客户端通知触发器
 * @author seg
 *
 */
public class ClientNotifyGeneratorAureolePrototype extends EffectPrototype
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
		effect.setNextActivateTime(Long.MAX_VALUE);
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		SkillParams params = effect.getClientNotificationMap().getCurrParams();
		if(params == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		List<Integer> targetIds = params.getTargetIds();
		if(targetIds == null || targetIds.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		effect.setTargetIds(targetIds);
		effect.setTargetX(params.getTargetX());
		effect.setTargetY(params.getTargetY());
		
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
				SkillEngine.castTmpEffect(effect, troopTarget, prototypeEffectId, currTime, null, false);
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
