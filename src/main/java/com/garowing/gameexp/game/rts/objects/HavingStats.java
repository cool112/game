package com.garowing.gameexp.game.rts.objects;

import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.yeto.war.fightcore.fight.attr.FightAddList;

/**
 * 有战斗属性的
 * @author seg
 * 2017年2月3日
 */
public interface HavingStats
{
	/**
	 * 获取攻击力
	 * @return
	 */
	public int getAttack();
	
	/**
	 * 获取自身属性
	 * @return
	 */
	public AttrEntity getAttrEntity();
	
	/**
	 * 获取属性修正列表
	 * @return
	 */
	public FightAddList getAttrModList();
}
