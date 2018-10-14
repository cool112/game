package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.DefultWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 战场定时监听
 * @author 		John.张建
 * @date		2016年2月2日
 * @version		1.0
 */
public interface WarQuartzListener extends WarListener
{
	
	public void eventQuartz (DefultWarEvent event);
	
}
