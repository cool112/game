package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.filter.effect.IdEffectFilter;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingSkill;

/**
 * 效果免疫buff
 * @author seg
 *
 */
public class ImmuneEffectBuffPrototype extends AbstractBuffPrototype
{
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		if (subEffectIds == null || subEffectIds.isEmpty())
		{
			return SkillError.ERROR_BASE_DATA_ERROR;
		}
		
		IdEffectFilter effectFilter = new IdEffectFilter(subEffectIds);
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			((HavingSkill)target).getAttachEffects().addEffectFilter(effect, effectFilter);
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
			((HavingSkill)target).getAttachEffects().rmoveEffectFilter(effect);
		}
		
		return SkillError.SUCCESS;
	}

}
