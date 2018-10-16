/**
 * 
 */
package com.garowing.gameexp.net.net.socket.constants;

import io.netty.util.AttributeKey;

import com.garowing.gameexp.net.net.socket.conn.AbstractHeartbeatCheck;
import com.garowing.gameexp.net.net.socket.conn.IoSession;
import com.garowing.gameexp.net.net.socket.packet.HalfPacketCache;

/**
 * @author gjs
 * 2018年7月24日
 */
public interface ContextKeys {
	/** session */
	AttributeKey<IoSession> KEY_SESSION = AttributeKey.newInstance("ioSession");
	/** auth */
	AttributeKey<Boolean> KEY_AUTH = AttributeKey.newInstance("auth");
	/** 心跳检测器 */
	AttributeKey<AbstractHeartbeatCheck> KEY_HEARTBEAT = AttributeKey.newInstance("heartbeat");
	/** 半包缓存 */
	AttributeKey<HalfPacketCache> KEY_HALF_CACHE = AttributeKey.newInstance("halfCache");
}
