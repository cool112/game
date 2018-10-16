package com.garowing.gameexp.net.net.socket.config;

import io.netty.channel.ChannelHandler;

import java.net.SocketAddress;

/**
 * 客户端配置
 * @author gjs
 *
 */
public class ClientConfig {
	/**
	 * 远程端口
	 */
	private SocketAddress remoteAddr; 
	/**
	 * 本地端口 可选
	 */
	private SocketAddress localPort;
	/** 译码本 */
	private ChannelHandler codecHandler;
	/** 业务处理 */
	private ChannelHandler bzHandler;
	/**
	 * @return the remoteAddr
	 */
	public SocketAddress getRemoteAddr() {
		return remoteAddr;
	}
	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(SocketAddress remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	/**
	 * @return the localPort
	 */
	public SocketAddress getLocalPort() {
		return localPort;
	}
	/**
	 * @param localPort the localPort to set
	 */
	public void setLocalPort(SocketAddress localPort) {
		this.localPort = localPort;
	}
	/**
	 * @return the codecHandler
	 */
	public ChannelHandler getCodecHandler() {
		return codecHandler;
	}
	/**
	 * @param codecHandler the codecHandler to set
	 */
	public void setCodecHandler(ChannelHandler codecHandler) {
		this.codecHandler = codecHandler;
	}
	/**
	 * @return the bzHandler
	 */
	public ChannelHandler getBzHandler() {
		return bzHandler;
	}
	/**
	 * @param bzHandler the bzHandler to set
	 */
	public void setBzHandler(ChannelHandler bzHandler) {
		this.bzHandler = bzHandler;
	}
	
}
