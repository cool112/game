package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.GuidanceWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 引导结束监听器接口
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月20日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public interface GuidanceEndDunListener extends WarListener
{
	public void eventEnd (GuidanceWarEvent event);
}
