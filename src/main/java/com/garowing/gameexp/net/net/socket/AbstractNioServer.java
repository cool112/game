package com.garowing.gameexp.net.net.socket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.garowing.gameexp.net.net.socket.config.ServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 抽象nio服务器
 * @author gjs
 * 2018年7月23日
 */
public abstract class AbstractNioServer implements NioServer {
	private static final Logger LOG = Logger.getLogger(AbstractNioServer.class);
	/**
	 * acceptor pool
	 */
	private EventLoopGroup bossGroup;
	/**
	 * i/o task schedule pool
	 */
	private EventLoopGroup workersGroup;
	
	/**
	 * listen channel promise
	 */
	private Set<ChannelFuture> channelFutureSet = new HashSet<ChannelFuture>();
	private EventExecutorGroup bzExecutorGroup;
	/* (non-Javadoc)
	 * @see com.garowing.gameexp.net.net.socket.NioServer#closeNioServer()
	 */
	@Override
	public void closeNioServer() throws IOException {
		try {
			for(ChannelFuture channelFuture : channelFutureSet){
				channelFuture.channel().close().sync();
			}
		} catch (InterruptedException e) {
			LOG.error("server close fail!", e);
		}finally{
			bzExecutorGroup.shutdownGracefully();
			workersGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	/**
	 * 
	 */
	public AbstractNioServer() {
		super();
		init();
	}

	/**
	 * 初始化
	 */
	protected void init(){
		int cores = Runtime.getRuntime().availableProcessors();
		bossGroup = new NioEventLoopGroup(Math.max(1, cores / 2));
		workersGroup = new NioEventLoopGroup(Math.min(16, cores * 2));
		bzExecutorGroup = new DefaultEventExecutorGroup(Math.min(32, cores * 4));
	}

	
	@Override
	public void bind(final ServerConfig config) throws IOException {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workersGroup).channel(NioServerSocketChannel.class)
			.handler(config.getServerHandler() == null ? new LoggingHandler("serverLogger", LogLevel.INFO) : config.getServerHandler())
			.childHandler(new ChannelInitializer<Channel>() {
	
				@Override
				protected void initChannel(Channel ch) throws Exception {
					if(config.getCodecHandler() != null)
						ch.pipeline().addLast("codec", config.getCodecHandler());
					if(config.getParsePacketHandler() != null)
						ch.pipeline().addLast("packetParse", config.getParsePacketHandler());
					if(config.getBzHandler() != null)
						ch.pipeline().addLast(bzExecutorGroup, "bzHandler", config.getBzHandler());
				}
			})
			.option(ChannelOption.SO_BACKLOG, config.getBacklog()).option(ChannelOption.SO_REUSEADDR, config.getReuseAddress())
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP, true);
		
			ChannelFuture channelFuture = bootstrap.bind(config.getListenAddr()).sync();
			channelFutureSet.add(channelFuture);
		} catch (InterruptedException e) {
			LOG.error("server start fail!", e);
		}
	
	}
	
}
