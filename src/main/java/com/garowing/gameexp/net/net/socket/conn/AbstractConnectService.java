package com.garowing.gameexp.net.net.socket.conn;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.garowing.gameexp.net.net.socket.config.ClientConfig;
import com.garowing.gameexp.net.net.socket.constants.ContextKeys;

import io.netty.channel.ChannelFuture;

/**
 * 抽象连接服务,约定子类实现单例模式
 * @author gjs
 *
 */
public abstract class AbstractConnectService {
	private static final Logger LOG = Logger.getLogger(AbstractConnectService.class);
	/** 通道集合 */
	protected Map<ChannelFuture, ClientConfig> channelMap = new ConcurrentHashMap<ChannelFuture, ClientConfig>();
	/** 
	 * 连接到远程服务器
	 * @param name
	 * @param ip
	 * @param port
	 * @param localPort
	 * @param isSync
	 * @return
	 */
	public abstract ChannelFuture connect(final String name, String ip, int port, int localPort, boolean isSync);
	public ChannelFuture connect(String ip, int port, int localPort){
		return connect("", ip, port, localPort, true);
	}
	/**
	 * 通过配置连接
	 * @param name
	 * @param isSync
	 * @param config
	 * @return
	 */
	protected abstract ChannelFuture connect(final String name, boolean isSync, final ClientConfig config);
	protected ChannelFuture connect(final ClientConfig config){
		return connect("", true, config);
	}
	/**
	 * 发送消息
	 * @param msg
	 */
	public abstract void send(Object msg);
	/**
	 * 初始化连接
	 */
	public void init(){
		channelMap.clear();
		initImpl();
	}
	
	protected abstract void initImpl();
	
	/**
	 * 清除异常或废弃的连接
	 */
	public abstract void clean();
	
	/**
	 * 重新连接所有断开配置
	 */
	public void reconnect(){
		Map<ChannelFuture, ClientConfig> copyMap = new HashMap<ChannelFuture, ClientConfig>(channelMap);
		for(Entry<ChannelFuture, ClientConfig> entry : copyMap.entrySet()){
			if(entry.getKey().channel().isWritable())
				continue;
			
			channelMap.remove(entry.getKey());
			ChannelFuture ch = connect(entry.getValue());
			if(ch != null){
				//do something
			}
		}
	}
	/**
	 * 心跳检查,可选项
	 * @param now
	 */
	public void heartCheck(long now){
		for(ChannelFuture chf : channelMap.keySet()){
			if(chf == null)
				continue;
			AbstractHeartbeatCheck heartCheck = chf.channel().attr(ContextKeys.KEY_HEARTBEAT).get();
			if(heartCheck == null)
				continue;
			
			heartCheck.check(now);
		}
	}
	
	/**
	 * 同步安全关闭所有连接
	 */
	public void close(){
		for(ChannelFuture chf : channelMap.keySet()){
			try {
				chf.channel().close().sync();
			} catch (InterruptedException e) {
				LOG.error("close channel interrupted!", e);
			}
		}
	}
	/**
	 * @return the channelMap
	 */
	public Map<ChannelFuture, ClientConfig> getChannelMap() {
		return channelMap;
	}
	
}
