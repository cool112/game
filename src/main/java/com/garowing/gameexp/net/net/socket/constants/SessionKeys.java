/**
 * 
 */
package com.garowing.gameexp.net.net.socket.constants;

/**
 * @author gjs
 * 2018年7月26日
 */
public interface SessionKeys {
	/** 用户账号 */
	String ACCOUNT_ID = "ACCOUNT_ID";
	/** 绑定的游戏服 */
	String SESSION_TRANSFER_NAME = "SESSION_TRANSFER_NAME";
	/** 绑定状态 0-预备 1-成功 */
	String SESSION_TRANSFER_STATE = "SESSION_TRANSFER_STATE";
	/** 延迟关闭连接 */
	String DELAY_CLOSE = "DELAY_CLOSE";
}
