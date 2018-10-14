package com.yeto.war.fightcore;

import com.yeto.war.datacenter.CenterDataManager;
import com.yeto.war.fightcore.war.objects.WarInstance;

/**
 * 游戏对象
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月13日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class GameObject 
{
	// 对象ID
	private final int objectId;
	
	/**
	 * 战场id
	 */
	private final int warId;
	
	public GameObject(WarInstance war)
	{
		this.objectId = war.nextInstanceId();
		this.warId = war.getObjectId();
		war.addObject(this);
	}

	public GameObject(int objectId)
	{
		this.objectId = objectId;
		this.warId = objectId;
	}

	/**
	 * 获得对象ID
	 * @return
	 */
	public final int getObjectId()
	{
		return objectId;
	}
	
	/**
	 * 获取战场实体
	 * @return
	 */
	public WarInstance getWar()
	{
		return CenterDataManager.WAR.getWar(warId);
	}

	/**
	 * 获得对象名称
	 */
	public abstract String getName();
	
	/**
	 * 获得对象类型
	 */
	public abstract int getObjectType();
	
	/**
	 * 销毁对象
	 */
	public void destroy()
	{
		WarInstance war = getWar();
		if(war == null)
			return;
		
		war.removeObject(this);
	}
	
	@Override
	public String toString() 
	{
		return getName() + "uid-" + objectId;
	}

	public int getWarId()
	{
		return warId;
	}
}
