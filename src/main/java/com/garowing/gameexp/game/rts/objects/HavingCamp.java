package com.garowing.gameexp.game.rts.objects;

/**
 * 有阵营的
 * @author seg
 * 2017年2月3日
 */
public interface HavingCamp
{
	/**
	 * 获取阵营id
	 * @return
	 */
	public int getCampId();

	/**
	 * 获取控制器
	 * @return
	 */
	public WarControlInstance getControl();
	
	/**
	 * 设置控制器
	 * @param control
	 */
	public void setControl(WarControlInstance control);
}
