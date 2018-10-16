package com.garowing.gameexp.net.net.socket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.garowing.gameexp.net.net.socket.config.ClientConfig;
import com.garowing.gameexp.net.net.socket.conn.DefaultIoSession;
import com.garowing.gameexp.net.net.socket.conn.IoSession;
import com.garowing.gameexp.net.net.socket.conn.SessionManager;
import com.garowing.gameexp.net.net.socket.constants.ContextKeys;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 抽象nio客户端池
 * 
 * @author gjs
 *
 */
public abstract class AbstractNioClientPool implements NioClientPool {
	private static final Logger LOG = Logger.getLogger(AbstractNioClientPool.class);
	/**
	 * i/o task schedule pool
	 */
	protected EventLoopGroup			workersGroup;
	/** bz task pool */
	protected EventExecutorGroup		bzExecutorGroup;
	/**
	 * sessionMap
	 */
	protected Map<Integer, IoSession>	sessionMap	= new ConcurrentHashMap<Integer, IoSession>();

	/**
	 * 
	 */
	public AbstractNioClientPool() {
		init();
	}

	/**
	 * 初始化线程池
	 */
	protected void init() {
		int cores = Runtime.getRuntime().availableProcessors();
		workersGroup = new NioEventLoopGroup(Math.min(4, cores * 2));
		bzExecutorGroup = new DefaultEventExecutorGroup(Math.min(8, cores * 4));
	}

	@Override
	public ChannelFuture connect(final ClientConfig config) throws IOException {
		return connect(config, true);
	}

	@Override
	public ChannelFuture connect(final ClientConfig config, boolean isSync) throws IOException{
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workersGroup).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						if (config.getCodecHandler() != null)
							ch.pipeline().addLast("codec", config.getCodecHandler());
						if (config.getBzHandler() != null)
							ch.pipeline().addLast(bzExecutorGroup, "bzExecutor",
									config.getBzHandler());
					}
				});
		bootstrap.option(ChannelOption.TCP_NODELAY, true)
		// .option(ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP,
		// true)//没有同步要求的可以不用
				.option(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture channelFuture = null;
		try {
			if (config.getLocalPort() != null)
				channelFuture = bootstrap.connect(config.getRemoteAddr(),
						config.getLocalPort());
			else
				channelFuture = bootstrap.connect(config.getRemoteAddr());

			if (channelFuture != null) {
				channelFuture
						.addListener(new GenericFutureListener<ChannelFuture>() {

							@Override
							public void operationComplete(
									ChannelFuture future)
									throws Exception {
								if (future.isSuccess()) {
									IoSession session = new DefaultIoSession(future.channel());
									session = SessionManager.putIfAbsent(session);
									future.channel().attr(ContextKeys.KEY_SESSION).compareAndSet(null, session);
									LOG.info("server connect session:" + session.getId() + " remoteAddr:" + config.getRemoteAddr());
									sessionMap.put(session.getId(), session);
								}
								else
									LOG.error("server connect fail! remoteAddr:" + config.getRemoteAddr());
							}
						});
			}
			if(isSync)
				channelFuture.sync();

		} catch (InterruptedException e) {
			LOG.error("client connect fail!", e);
		}
		return channelFuture;
	}

}
