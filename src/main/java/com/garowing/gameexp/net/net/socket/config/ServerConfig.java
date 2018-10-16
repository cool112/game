package com.garowing.gameexp.net.net.socket.config;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandler;

/**
 * 服务器配置
 * @author gjs
 *
 */
public class ServerConfig
{
    private ChannelHandler serverHandler;
    private ChannelHandler codecHandler;
    private ChannelHandler parsePacketHandler;
    private ChannelHandler bzHandler;
    private int backlog;
    private boolean reuseAddress;
    private SocketAddress listenAddr;
    public ServerConfig()
    {
        backlog = 128;
        reuseAddress = false;
    }

    public void setBacklog(int backlog)
    {
        this.backlog = backlog;
    }

    public int getBacklog()
    {
        return backlog;
    }

    public void setReuseAddress(boolean on)
    {
        reuseAddress = on;
    }

    public boolean getReuseAddress()
    {
        return reuseAddress;
    }

	/**
	 * @return the serverHandler
	 */
	public ChannelHandler getServerHandler() {
		return serverHandler;
	}

	/**
	 * @param serverHandler the serverHandler to set
	 */
	public void setServerHandler(ChannelHandler serverHandler) {
		this.serverHandler = serverHandler;
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
	 * @return the parsePacketHandler
	 */
	public ChannelHandler getParsePacketHandler() {
		return parsePacketHandler;
	}

	/**
	 * @param parsePacketHandler the parsePacketHandler to set
	 */
	public void setParsePacketHandler(ChannelHandler parsePacketHandler) {
		this.parsePacketHandler = parsePacketHandler;
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

	/**
	 * @return the listenAddr
	 */
	public SocketAddress getListenAddr() {
		return listenAddr;
	}

	/**
	 * @param listenAddr the listenAddr to set
	 */
	public void setListenAddr(SocketAddress listenAddr) {
		this.listenAddr = listenAddr;
	}
    
}
