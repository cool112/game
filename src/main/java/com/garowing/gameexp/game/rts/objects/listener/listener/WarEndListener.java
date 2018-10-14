package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.EndWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 结束战场监听
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年6月12日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public interface WarEndListener extends WarListener
{
	public void eventEnd (EndWarEvent event);
}
