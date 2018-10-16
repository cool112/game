package com.garowing.gameexp.net.net.socket.conn;

import org.apache.log4j.Logger;

/**
 * 抽象心跳检查
 * @author gjs
 *
 */
public abstract class AbstractHeartbeatCheck {
	private static final Logger LOG = Logger.getLogger(AbstractHeartbeatCheck.class);
	/** 上次接收时间 */
	protected long lastRecvTime;
	/** 上次发送时间 */
	protected long lastSendTime;
	/** 重发次数 */
	protected int sendCount;
	/**
	 * 获取超时时间
	 * @return
	 */
	public abstract long getTimeout();
	/**
	 * 获取最大重发数
	 * @return
	 */
	public abstract long getMaxResend();
	/**
	 * @param lastRecvTime the lastRecvTime to set
	 */
	public void setLastRecvTime() {
		this.lastRecvTime = System.currentTimeMillis();
	}
	/**
	 * 主动请求心跳
	 */
	protected abstract void sendHeartbeat();
	/**
	 * 处理超时
	 */
	protected abstract void dealTimeout();
	/**
	 * 断开连接
	 */
	protected abstract void disconnect();
	/**
	 * 检查心跳
	 */
	public void check(long now){
		if(lastRecvTime == 0)
			return;
		long inteval =  getTimeout();
		if(now - lastRecvTime < inteval * 1.5)
		{
			sendCount = 0;
			return ;
		}
		if(lastSendTime == 0 || now - lastSendTime < inteval)
		{
			return ;
		}
		sendHeartbeat();
		lastSendTime = now;
		sendCount ++;
		LOG.info("网关到大厅心跳:超时次数="+sendCount);
		if(sendCount <= getMaxResend())
		{
			return;
		}
		try {
			dealTimeout();
		} catch (Exception e) {
			disconnect();
			LOG.error("心跳重新连接失败", e);
		}
		sendCount = 0;
	}
}
