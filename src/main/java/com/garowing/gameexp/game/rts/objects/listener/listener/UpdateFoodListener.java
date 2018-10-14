package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.FoodWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 粮草点监听
 * @author zhouxiaofeng
 * 2015年11月13日
 */
public interface UpdateFoodListener extends WarListener{


	/**
	 * 粮草点
	 * @author zhouxiaofeng
	 * 2015年11月13日
	 */
	public void eventFood(FoodWarEvent event);
	
}
