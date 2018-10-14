package com.garowing.gameexp.game.rts.ai.behaviac.constants;

/**
 * 节点返回状态
 * @author seg
 * 2017年1月4日
 */
public enum EBTStatus
{
	/**
	 * 无，在结果类型中使用，使用某个函数的返回结果
	 */
	BT_INVALID,
	
	/**
	 * 成功
	 */
    BT_SUCCESS,
    
    /**
     * 失败
     */
    BT_FAILURE,
    
    /**
     * 运行中
     */
    BT_RUNNING;
}
