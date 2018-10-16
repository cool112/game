/**
 * 
 */
package com.garowing.gameexp.net.net.socket.conn;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gjs
 * 2018年7月24日
 */
public class DefaultIoSession implements IoSession {
	private Channel channel;
	private int id = hashCode();
	private EventExecutor executor;
	private Map<Object, Object> attributeMap = new ConcurrentHashMap<Object, Object>();
	private String remoteIp;
	/**
	 * @param channel
	 */
	public DefaultIoSession(Channel channel) {
		super();
		this.channel = channel;
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#getRemoteIP()
	 */
	@Override
	public String getRemoteIP() {
		if(remoteIp == null){
			InetSocketAddress address = (InetSocketAddress)getRemoteAddress();
			remoteIp = address.getAddress().getHostAddress();
		}
	     return remoteIp;
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#getRemotePort()
	 */
	@Override
	public int getRemotePort() {
		InetSocketAddress address = (InetSocketAddress)getRemoteAddress();
        return address.getPort();
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#getRemoteAddress()
	 */
	@Override
	public SocketAddress getRemoteAddress() {
		return channel.localAddress();
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#getAttribute(java.lang.Object)
	 */
	@Override
	public Object getAttribute(Object obj) {
		return attributeMap.get(obj);
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#setAttribute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object setAttribute(Object obj, Object obj1) {
		return attributeMap.put(obj, obj1);
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#removeAttribute(java.lang.Object)
	 */
	@Override
	public Object removeAttribute(Object obj) {
		return attributeMap.remove(obj);
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#write(java.lang.Object)
	 */
	@Override
	public void write(final Object obj) {
		if(channel.eventLoop().inEventLoop()){
			channel.writeAndFlush(obj);
		}
		else{
			channel.eventLoop().execute(new Runnable() {
				
				@Override
				public void run() {
					channel.writeAndFlush(obj);
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return channel.isActive();
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#close()
	 */
	@Override
	public void close() {
		channel.close();
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#executor()
	 */
	@Override
	public EventExecutor executor() {
		return executor;
	}

	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.conn.IoSession#setExecutor()
	 */
	@Override
	public void setExecutor(EventExecutor eventLoop) {
		executor = eventLoop;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public void write(final Object obj, final GenericFutureListener<ChannelFuture> listener) {
		if(channel.eventLoop().inEventLoop()){
			channel.writeAndFlush(obj).addListener(listener);
		}
		else{
			channel.eventLoop().execute(new Runnable() {
				
				@Override
				public void run() {
					channel.writeAndFlush(obj).addListener(listener);
				}
			});
		}
	
		
	}
}
