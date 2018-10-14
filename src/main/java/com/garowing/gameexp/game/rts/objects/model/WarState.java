package com.garowing.gameexp.game.rts.objects.model;

/**
 * 战争状态
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月10日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public enum WarState 
{
	CREATE	("创建"),
	READY	("准备"),
	SUSPEND	("暂停"),
	IN_FIGHT("战斗中"),
	END		("结束"),
;
	
	public final String info;
	
	WarState (String info)
	{
		this.info = info;
	}
}
