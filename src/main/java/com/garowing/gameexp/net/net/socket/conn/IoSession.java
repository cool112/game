package com.garowing.gameexp.net.net.socket.conn;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.SocketAddress;


public interface IoSession
{
    public abstract int getId();

    public abstract String getRemoteIP();

    public abstract int getRemotePort();

    public abstract SocketAddress getRemoteAddress();

    public abstract Object getAttribute(Object obj);

    public abstract Object setAttribute(Object obj, Object obj1);

    public abstract Object removeAttribute(Object obj);

    public abstract void write(Object obj);
    /**
     * 当发送成功回调
     * @param obj
     * @param listener
     */
    public abstract void write(Object obj, GenericFutureListener<ChannelFuture> listener);

    public abstract boolean isConnected();

    public abstract void close();
    
    public abstract EventExecutor executor();
    
    public abstract void setExecutor(EventExecutor eventExecutor);
    
    public Channel getChannel();
}
