package com.garowing.gameexp.net.net.socket.constants;

/**
 * 包类型,由于使用相同的id,需要通过类型来区分通道
 * @author gjs
 *
 */
public enum PacketType {
	/** 客户端到网关 */
	CLIENT_GATEWAY,
	/** 网关到大厅 */
	GATEWAY_HALL,
	/** 大厅到网关 */
	HALL_GATEWAY,
	/** 网关到战斗服 */
	GATEWAY_BATTLE,
	/** 战斗服到网关 */
	BATTLE_GATEWAY,
	/** 网关到活动 */
	GATEWAY_ACTIVITY,
	/** 活动到网关 */
	ACTIVITY_GATEWAY,
	/** 网关到聊天 */
	GATEWAY_CHAT,
	/** 聊天到网关 */
	CHAT_GATEWAY,
	;
	
}
