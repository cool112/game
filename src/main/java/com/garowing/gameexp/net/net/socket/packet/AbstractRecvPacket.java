/**
 * 
 */
package com.garowing.gameexp.net.net.socket.packet;

import java.io.Serializable;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 抽象接收包
 * @author gjs
 * 2018年7月25日
 */
public abstract class AbstractRecvPacket extends JmTypeReader implements Runnable, Serializable, Cloneable {
	private static final Logger LOG = Logger.getLogger(AbstractRecvPacket.class);
	private static final long serialVersionUID = -175432892186698907L;
	private ChannelHandlerContext ctx;
	/**
	 * 读取参数
	 * @param byteBuf
	 */
	public void readParams(ByteBuf byteBuf){
		setTmpByteBuf(byteBuf);
		try {
			readParamsImpl();
		} catch (Throwable e) {
			LOG.error("read packet fail", e);
		}
		finally{
			cleanBuf();
		}
	}
	
	/**
	 * 读取实现
	 */
	protected abstract void readParamsImpl();

	/**
	 * 获取包id,可以注解化
	 * @return
	 */
	public abstract int getPacketId();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	

	/**
	 * @return the ctx
	 */
	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	/**
	 * @param ctx the ctx to set
	 */
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
}
