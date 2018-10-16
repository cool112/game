package com.garowing.gameexp.net.net.socket.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

/**
 * 半包缓存
 * @author gjs
 *
 */
public class HalfPacketCache {
	/** 包长 */
	private short length;
	/** 缓存 */
	private ByteBuf cache;
	/** 当前长度 */
	private int curLen;
	/**
	 * 追加缓冲
	 * @param byteBuf
	 */
	public void append(ByteBuf byteBuf){
		int remain = length - curLen;
		remain = Math.min(remain, byteBuf.readableBytes());
		curLen += remain;
		cache.writeBytes(byteBuf, remain);
	}
	/**
	 * 初始化
	 * @param length
	 * @param byteBuf
	 */
	public void init(short length, ByteBuf byteBuf){
		this.length = length;
		cache = Unpooled.buffer(length);
		curLen = byteBuf.readableBytes();
		cache.writeBytes(byteBuf);
	}
	/**
	 * 是否完整
	 * @return
	 */
	public boolean isComplete(){
		return length > 0 && curLen == length;
	}
	
	/**
	 * 清除缓存
	 */
	public void clean(){
		length = 0;
		curLen = 0;
		ReferenceCountUtil.release(cache);
		cache = null;
	}
	/**
	 * 是否是半包
	 * @return
	 */
	public boolean isHalfPacket(){
		return length > 0 && curLen < length;
	}
	/**
	 * 获取缓存
	 * @return
	 */
	public ByteBuf getCache(){
		return cache.retain();
	}
	
	@Override
	public String toString() {
		return "length:" + length;
	}
	/**
	 * 获取长度
	 * @return
	 */
	public short getLength() {
		return length;
	}
}
