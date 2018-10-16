package com.garowing.gameexp.net.game.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * 游戏到大厅的编解码器
 * @author gjs
 *
 */
public class GHCodec extends MessageToMessageCodec<ByteBuf, byte[]> {

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg,
			List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 默认有一个为0的socketId,并且基本不会有消息头里的accId
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		
	}

}
