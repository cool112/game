// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NioServer.java

package com.garowing.gameexp.net.net.socket;

import java.io.IOException;

import com.garowing.gameexp.net.net.socket.config.ServerConfig;

/**
 * 可监听多个端口,共用boss\worker\bzExecutor
 * @author gjs
 *
 */
public interface NioServer
{

    public abstract void closeNioServer()
        throws IOException;

    public void bind(ServerConfig config) throws IOException;
}
