package com.garowing.gameexp.game.rts.base.xml;

import javax.xml.bind.Unmarshaller;

/**
 * data元素数据接口
 */
public abstract class AbstractData 
{
	
	protected final void afterUnmarshal(Unmarshaller u, Object parent)
	{
		loadData();
	}
	
	/**
	 * 数据加载
	 */
	protected abstract void loadData(); 
	
	/**
	 * 初始化数据
	 */
	protected abstract void initData ();
}

