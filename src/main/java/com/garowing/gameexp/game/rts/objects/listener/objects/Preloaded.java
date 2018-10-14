package com.garowing.gameexp.game.rts.listener.objects;

import java.util.Set;

/**
 * 可预先加载的,可视化对象
 * @author seg
 * 2017年1月14日
 */
public interface Preloaded
{
	/**
	 * 获取可视化对象id
	 * @return
	 */
	public Set<Integer> getVisualObjId();
	
	/**
	 * 获取可视化对象类型
	 * @return
	 */
	public int getVisualObjType();
}
