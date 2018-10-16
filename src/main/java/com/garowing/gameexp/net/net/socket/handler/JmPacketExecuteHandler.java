/**
 * 
 */
package com.garowing.gameexp.net.net.socket.handler;

import com.garowing.gameexp.net.net.socket.packet.AbstractRecvPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 执行包业务
 * @author gjs
 * 2018年7月26日
 */
@Sharable
public class JmPacketExecuteHandler extends
		SimpleChannelInboundHandler<AbstractRecvPacket> {

	/* (non-Javadoc)
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			AbstractRecvPacket msg) throws Exception {
		if(msg == null)
			return;
		
		msg.run();
	}

}
