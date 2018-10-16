package com.garowing.gameexp.net.net.socket;

import io.netty.channel.ChannelFuture;

import java.io.IOException;

import com.garowing.gameexp.net.net.socket.config.ClientConfig;
/**
 * nio客户端池,每个连接共用eventloop和eventExecutor,但有不同的上下文
 * @author gjs
 *
 */
public interface NioClientPool {
	/**
	 * 创建一个链接,同步操作,返回channelFuture
	 * @param config
	 * @return
	 * @throws IOException
	 */
	public ChannelFuture connect(ClientConfig config)
	        throws IOException;
	
	/**
	 * 创建连接
	 * @param config
	 * @param isSync
	 * @return
	 */
	public ChannelFuture connect(ClientConfig config, boolean isSync) throws IOException;
	
	/**
	 * 关闭所有连接
	 */
	public void close();
}
