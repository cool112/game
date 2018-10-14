package com.garowing.gameexp.game.rts.listener.event;

import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;

/**
 * 更新粮草点事件
 * @author      John_zero
 */
public class FoodWarEvent extends WarEvent
{
	
	private AbsFightControl control; 
	private int food;
	
	public FoodWarEvent (AbsFightControl control, int food)
	{
		super(control.getWar());
		this.control = control;
		this.food = food;
	}

	public AbsFightControl getControl() 
	{
		return control;
	}

	public int getFood() 
	{
		return food;
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
