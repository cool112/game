package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 更新血量战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月30日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class ChangeHpWarEvent extends WarEvent
{
	
	private WarObjectInstance visible; 
	private int beforeHp; 
	private int afterHp;
	
	public ChangeHpWarEvent (WarObjectInstance visible, int beforeHp, int afterHp)
	{
		super(visible.getWar());
		this.visible = visible;
		this.beforeHp = beforeHp;
		this.afterHp = afterHp;
	}

	public WarObjectInstance getVisible() {
		return visible;
	}

	public int getBeforeHp() {
		return beforeHp;
	}

	public int getAfterHp() {
		return afterHp;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.WAR_EVENT;
	}

	
}
