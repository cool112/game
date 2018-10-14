package com.garowing.gameexp.game.rts.skill.script.effect.active;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.EffectType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.model.SkillModel;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarInstance;

import commons.configuration.Property;

/**
 * 替换技能效果
 * @author seg
 *
 */
public class ReplaceSkillPrototype extends EffectPrototype
{
	/**
	 * 旧技能id
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float oldSkillId;
	
	/**
	 * 新技能id
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float newSkillId;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		SkillModel model = StaticDataManager.SKILL_DATA.getModel((int) oldSkillId);
		if(model == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		model = StaticDataManager.SKILL_DATA.getModel((int) newSkillId);
		if(model == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		HavingSkill caster = effect.getCaster();
		WarInstance war = effect.getWar();
		if(war == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		int removeSkillId = (int) this.oldSkillId;
		int addSkillId = (int)this.newSkillId;
		for(SkillEntity skill : caster.getBoundSkills())
		{
			if(skill.getModelId() == removeSkillId)
			{
				caster.unbindSkill(skill.getObjectId());
				if(skill.getState() != SkillManager.IDLE)
				{
					for(Integer effectId : skill.getEffectEntityList())
					{
						GameObject subEffect = war.getObject(effectId);
						if(subEffect == null || subEffect.getObjectType() != GameObjectType.SKILL_EFFECT)
							continue;
						
						EffectEntity subEffectEntity = (EffectEntity) subEffect;
						if(EffectType.getTypeById(subEffectEntity.getEffectPrototype().getTemplateId()) == EffectType.ENTER_SCENE_TRIGGER)
						{
							subEffectEntity.setState(SkillManager.END);
							subEffectEntity.setEndTime(currTime);
							subEffectEntity.setForceEnd(true);
							subEffectEntity.getEffectPrototype().onExit(subEffectEntity);
						}
						else
							war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, effect, (EffectEntity) subEffect, true));
					}
				}
				SkillEngine.bindSkill(caster, addSkillId, skill.getCastHandler());
				break;	
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
		return SkillError.SUCCESS;
	}

}
