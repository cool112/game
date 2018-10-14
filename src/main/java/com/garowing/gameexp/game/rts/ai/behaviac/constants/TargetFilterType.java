package com.garowing.gameexp.game.rts.ai.behaviac.constants;

/**
 * 过滤器类型
 * @author seg
 * 2017年1月5日
 */
public enum TargetFilterType
{
	/**
	 * 最近的敌人
	 */
	NEAREST_ENEMY(1);
	
	/**
	 * 代码
	 */
	private int code;
	
	private TargetFilterType(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}
}
