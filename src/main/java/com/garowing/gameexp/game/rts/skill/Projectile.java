package com.garowing.gameexp.game.rts.skill;

import java.util.List;

import com.garowing.gameexp.game.rts.objects.GameObject;
import com.garowing.gameexp.game.rts.skill.entity.ProjectileAttrEntity;

/**
 * 投射物
 * @author seg
 *
 */
public interface Projectile
{
	/**
	 * 获取目标
	 * @return
	 */
	public List<GameObject> getTargets();
	
	/**
	 * 获取目标坐标x
	 * @return
	 */
	public float getTargetX();
	
	/**
	 * 获取目标坐标y
	 * @return
	 */
	public float getTargetY();
	
	/**
	 * 获取施法者
	 * @return
	 */
	public <T extends GameObject> T getCaster();
	
	/**
	 * 获取投射物属性
	 * @return
	 */
	public ProjectileAttrEntity getProjectileAttr();
}
