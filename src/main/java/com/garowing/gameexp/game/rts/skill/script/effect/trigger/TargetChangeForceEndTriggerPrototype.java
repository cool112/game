package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 目标改变强制结束效果 触发器，如移动解隐
 * @author seg
 *
 */
public class TargetChangeForceEndTriggerPrototype extends AbstractEffectTriggerPrototype
{

	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.TARGET_CHANGE)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		WarInstance war = effect.getWar();
		HavingSkill caster = effect.getCaster();
		List<SkillEntity> skills = caster.getBoundSkills();
		for(SkillEntity skill : skills)
		{
			for(Integer effectId : skill.getEffectEntityList())
			{
				GameObject obj = war.getObject(effectId);
				if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
					continue;
				
				EffectEntity subEffect = (EffectEntity) obj;
				if(!subEffectIds.contains(subEffect.getPrototypeId()))
					continue;
				
				if(subEffect.getState() < SkillManager.END)
					war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, effect, subEffect, true));
			}
		}
		
		for(Integer effectId : caster.getAttachEffects().getTempEffects())
		{
			GameObject obj = war.getObject(effectId);
			if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity subEffect = (EffectEntity) obj;
			if(!subEffectIds.contains(subEffect.getPrototypeId()))
				continue;
			
			if(subEffect.getState() < SkillManager.END)
				war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, effect, subEffect, true));
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.TARGET_CHANGE;
	}

}
