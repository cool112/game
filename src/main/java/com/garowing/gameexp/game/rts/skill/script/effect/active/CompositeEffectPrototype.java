package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.RestartEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 组合效果
 * @author seg
 *
 */
public class CompositeEffectPrototype extends EffectPrototype
{
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		List<Integer> subEffects = effect.getSubEffectIds();
		if(subEffects == null || subEffects.isEmpty())
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		if(effect.getTargetIds().isEmpty() && effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		effect.setTargets(targets);
		WarInstance war = effect.getWar();
		List<Integer> subEffectIds = effect.getSubEffectIds();
		for(Integer subEffectId : subEffectIds)
		{
			GameObject obj = war.getObject(subEffectId);
			if(obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity subEffect = (EffectEntity) obj;
			if(subEffect.getState() != SkillManager.IDLE)
			{
				war.getSkillManager().addSkillEvent(new RestartEffectSkillEvent(war, effect, subEffect));
			}
			else
			{
				subEffect.setState(SkillManager.START);
				subEffect.setTargetIds(effect.getTargetIds());
				subEffect.setStartTime(currTime);
				war.getSkillManager().addEffect(subEffect);
				SkillEngine.sendSkillMsg(subEffect);
			}
		}
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		WarInstance war = effect.getWar();
		List<Integer> subEffectIds = effect.getSubEffectIds();
		for(Integer subEffectId : subEffectIds)
		{
			GameObject obj = war.getObject(subEffectId);
			if(obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity subEffect = (EffectEntity) obj;
			if(subEffect.getState() != SkillManager.IDLE)
				war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, effect, subEffect, true));
		}
		return SkillError.SUCCESS;
	}

}
