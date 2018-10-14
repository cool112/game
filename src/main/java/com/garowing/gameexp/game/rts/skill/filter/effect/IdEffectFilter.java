package com.garowing.gameexp.game.rts.skill.filter.effect;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;

/**
 * id效果过滤器
 * @author seg
 *
 */
public class IdEffectFilter implements EffectFilter
{
	/**
	 * 免疫效果ID集合
	 */
	private List<Integer> effectIds;
	
	public IdEffectFilter(List<Integer> effectIds)
	{
		super();
		this.effectIds = effectIds;
	}

	@Override
	public boolean isValid(Projectile effect)
	{
		int id = 0;
		if(((GameObject)effect).getObjectType() == GameObjectType.SKILL_EFFECT)
			id = ((EffectEntity)effect).getPrototypeId();
		else if(((GameObject)effect).getObjectType() == GameObjectType.SKILL)
			id = ((SkillEntity)effect).getModelId();
		
		if(effectIds != null && effectIds.contains(id))
			return false;
		
		return true;
	}

}
