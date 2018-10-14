package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 召唤部队战争事件
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月30日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class CallTroopWarEvent extends WarEvent
{
	
	private WarControlInstance control;
	private Troop troop; 
	
	public CallTroopWarEvent (WarControlInstance control, Troop troop)
	{
		super(control.getWar());
		this.control = control;
		this.troop = troop;
	}

	public WarControlInstance getControl() {
		return control;
	}

	public Troop getTroop() {
		return troop;
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
