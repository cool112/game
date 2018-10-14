package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.filter.skill.IdSkillFilter;
import com.garowing.gameexp.game.rts.skill.filter.skill.SkillFilter;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingSkill;

/**
 * 技能免疫buff
 * @author seg
 *
 */
public class ImmuneSkillBuffPrototype extends AbstractBuffPrototype
{
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		if (subEffectIds == null || subEffectIds.isEmpty())
		{
			return SkillError.ERROR_BASE_DATA_ERROR;
		}
		
		SkillFilter skillFilter = new IdSkillFilter(subEffectIds);
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			((HavingSkill)target).getAttachEffects().addSkillFilter(effect, skillFilter);
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
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			((HavingSkill)target).getAttachEffects().rmoveSkillFilter(effect);
		}
		
		return SkillError.SUCCESS;
	}

}
