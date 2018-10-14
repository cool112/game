package com.garowing.gameexp.game.rts.listener.objects;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 战争事件监听集合
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月10日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public final class WarListenerList 
{
	// 战争事件集合
	private final List<WarListener> listenerCollection;
	
	public WarListenerList ()
	{
		this.listenerCollection = new CopyOnWriteArrayList<WarListener>();
	}

	/**
	 * 增加一个监听器
	 * @param listener
	 * @create	2015年4月10日	darren.ouyang
	 */
	public void addListener(WarListener listener)
	{
		this.listenerCollection.add(listener);
	}
	
	/**
	 * 移除一个监听器
	 * @param listener
	 * @create	2015年4月10日	darren.ouyang
	 */
	public final void removeListener(WarListener listener)
	{
		this.listenerCollection.remove(listener);
	}
	
	/**
	 * 获取监听器集合
	 * @return
	 * @create	2015年4月10日	darren.ouyang
	 */
	public List<WarListener> getListeners ()
	{
		return this.listenerCollection;
	}

}
