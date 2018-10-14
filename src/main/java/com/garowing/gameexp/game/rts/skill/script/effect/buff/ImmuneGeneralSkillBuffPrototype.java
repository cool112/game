package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.filter.skill.GeneralSkillFilter;
import com.garowing.gameexp.game.rts.skill.filter.skill.SkillFilter;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingSkill;

/**
 * 免疫将军技能buff
 * @author seg
 *
 */
public class ImmuneGeneralSkillBuffPrototype extends AbstractBuffPrototype
{

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		SkillFilter skillFilter = new GeneralSkillFilter();
		
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
