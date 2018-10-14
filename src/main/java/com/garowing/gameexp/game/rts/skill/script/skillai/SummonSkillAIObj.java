package com.garowing.gameexp.game.rts.skill.script.skillai;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.constants.EffectTopType;
import com.garowing.gameexp.game.rts.skill.constants.EffectType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.script.effect.active.AbstractSummonPrototype;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 召唤技能ai
 * @author seg
 *
 */
@ScriptName(name = "summonAI")
public class SummonSkillAIObj extends SkillAIObj
{

	@Override
	public boolean isValid(SkillEntity skill, int targetId, float targetX, float targetY)
	{
		List<Integer> effectIds = skill.getEffectEntityList();
		WarInstance war = skill.getWar();
		for(Integer effectId : effectIds)
		{
			GameObject obj = war.getObject(effectId);
			if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity effect = (EffectEntity) obj;
			EffectType effectType = EffectType.getTypeById(effect.getEffectPrototype().getTemplateId());
			if(effectType == null)
				continue;
			
			if(effectType.getTopType() != EffectTopType.SUMMON)
				continue;
			
			AbstractSummonPrototype summonEffect = (AbstractSummonPrototype) effect.getEffectPrototype();
			GameObject caster = skill.getCaster();
			if(caster.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troop = (Troop) caster;
			int maxSummmonNum = summonEffect.getMaxNum();
			maxSummmonNum += troop.getAttrModList().getEffectModifiedTotal(ModifiedType.SUMMON_NUM_ADD, effect);
			
			int petCount = troop.getAttachEffects().getChildrenCount(summonEffect.getModelId());
			if(petCount >= maxSummmonNum)
				return false;
		}
		
		return true;
	}

	@Override
	public int getModifiedPriority(SkillEntity skill)
	{
		return 0;
	}

}
