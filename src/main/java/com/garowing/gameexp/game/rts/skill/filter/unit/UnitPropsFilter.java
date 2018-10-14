package com.garowing.gameexp.game.rts.skill.filter.unit;

import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 单位属性过滤器
 * @author seg
 *
 */
public interface UnitPropsFilter
{
	int CAMP_ALL = 0;
	
	int CAMP_FRIEND = 1;
	
	int CAMP_ENEMY = 2;
	/**
	 * 获取对象集合
	 * @param list
	 * @return
	 */
	public List<WarObjectInstance> filter(Projectile data, GameObject caster, Collection<WarObjectInstance> list); 
	
	/**
	 * 获取过滤器类型
	 * @return
	 */
	public int getType();
}
