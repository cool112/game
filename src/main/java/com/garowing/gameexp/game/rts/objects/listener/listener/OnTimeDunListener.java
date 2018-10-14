package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.OnTimeWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 时间到达监听器接口
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月28日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public interface OnTimeDunListener extends WarListener
{
	public void eventTime (OnTimeWarEvent event);
}
