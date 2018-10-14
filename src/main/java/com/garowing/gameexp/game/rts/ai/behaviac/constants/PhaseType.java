package com.garowing.gameexp.game.rts.ai.behaviac.constants;

/**
 * 执行条件类型，为了和配置一致方便转换，没使用全大写格式
 * @author seg
 *
 */
public interface PhaseType
{
	/**
	 * 当结果失败
	 */
	String FAILURE = "Failure";
	
	/**
	 * 当执行成功
	 */
	String SUCCESS = "Success";
	
	/**
	 * 无论成功失败
	 */
	String BOTH = "Both";
}
