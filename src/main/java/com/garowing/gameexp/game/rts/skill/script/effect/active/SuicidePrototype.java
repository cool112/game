package com.garowing.gameexp.game.rts.skill.script.effect.active;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.fight.model.HpUpdateType;

/**
 * 自杀效果
 * @author seg
 *
 */
public class SuicidePrototype extends EffectPrototype
{

	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		GameObject caster = effect.getCaster();
		if(caster.getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		GameObject caster = effect.getCaster();
		FightEngine.troopDie((WarObjectInstance) caster, caster, effect.getObjectId(), effect.getWrapperEffectId(), HpUpdateType.SKILL_DIE);
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
