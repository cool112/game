package com.garowing.gameexp.game.rts.skill.filter.effect;

import com.garowing.gameexp.game.rts.skill.Projectile;

/**
 * 效果过滤器
 * @author seg
 *
 */
public interface EffectFilter
{
	/**
	 * 是否有效
	 * @param skill
	 * @return
	 */
	public boolean isValid(Projectile effect);
}
