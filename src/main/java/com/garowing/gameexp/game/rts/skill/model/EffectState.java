package com.garowing.gameexp.game.rts.skill.model;

/**
 * 效果状态
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月24日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public enum EffectState 
{
	CREATED		("创建"),
	START		("开始"),
	ACTING		("活跃"),
	FINISHING	("完成"),
	END			("结束");
	
	public final String info;
	
	EffectState (String info)
	{
		this.info = info;
	}
}
