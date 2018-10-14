package com.garowing.gameexp.game.rts.listener.objects;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.listener.model.WarActionModel;

/**
 * 战争行为
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月28日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class WarAction
{
	
	public abstract boolean initData(WarActionModel model);
	
	public abstract void execute (GameObject event);
	
}
