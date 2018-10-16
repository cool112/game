package com.garowing.gameexp.net.net.socket.packet;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.FastThreadLocal;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import main.java.SocketServer.NetData;

/**
 * 类型变量读取器
 * @author gjs
 *
 */
public abstract class JmTypeReader {
	private static final Charset CHARSET = Charset.forName("utf-8");
	/**
	 * 临时的字节缓存,读取结束后会置null
	 */
	private FastThreadLocal<ByteBuf> tmpByteBuf = new FastThreadLocal<ByteBuf>();
	
	/**
	 * 对象读取器
	 * @author gjs
	 * 2018年7月25日
	 * @param <T>
	 */
	public static abstract class ObjectReader<T> {
		public abstract T readObj();
	}

	/**
	 * @return the tmpByteBuf
	 */
	public ByteBuf getTmpByteBuf() {
		return tmpByteBuf.get();
	}

	/**
	 * @param tmpByteBuf the tmpByteBuf to set
	 */
	public void setTmpByteBuf(ByteBuf tmpByteBuf) {
		this.tmpByteBuf.set(tmpByteBuf);
	}
	
	public void cleanBuf(){
		this.tmpByteBuf.set(null);
	}
	
	/**
	 * 读取字符串
	 * @param byteBuf
	 * @return
	 */
	protected String readString(){
//		byte[] stringBytes = new byte[byteBuf.bytesBefore((byte) 0)];
//		byteBuf.readBytes(stringBytes);
//		byteBuf.readByte();
//		return new String(stringBytes, CHARSET);
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_STRING)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:string");
		int stringLen = byteBuf.readShort();
		byte[] stringBytes = new byte[stringLen];
		byteBuf.readBytes(stringBytes);
		return new String(stringBytes, CHARSET);
	}
	
	/**
	 * 读取短整形
	 * @param byteBuf
	 * @return
	 */
	protected short readShort() {
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_SHORT)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:short");
		return byteBuf.readShort();
	}
	
	/**
	 * 读取整形
	 * @param byteBuf
	 * @return
	 */
	protected int readInt() {
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_INT)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:int");
		return byteBuf.readInt();
	}
	/**
	 * 读取长整形
	 * @param byteBuf
	 * @return
	 */
	protected long readLong() {
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_LONG)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:long");
		return byteBuf.readLong();
	}
	/**
	 * 读取浮点数
	 * @param byteBuf
	 * @return
	 */
	protected double readDouble() {
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_DOUBLE)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:double");
		return byteBuf.readDouble();
	}
	/**
	 * 读取布尔
	 * @param byteBuf
	 * @return
	 */
	protected boolean readBoolean() {
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_BOOL)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:bool");
		return byteBuf.readByte() == 1;
	}
	/**
	 * 读取字节
	 * @param byteBuf
	 * @return
	 */
	protected byte readByte(){
		ByteBuf byteBuf = tmpByteBuf.get();
		int type = byteBuf.readShort();
		if(type != NetData.VALUE_BYTE)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:byte");
		return byteBuf.readByte();
	}
	
	/**
	 * 集合读取,但是判断集合结束的条件是对象长度不同,虽然也有可能相同,但很多时候是通过不连续使用集合避免该问题..
	 * @param byteBuf
	 * @param reader
	 * @param out
	 */
	protected <T> List<T> readBytes(ObjectReader<T> reader){
		ArrayList<T> out;
		if(reader == null)
			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:bytes");
		ByteBuf byteBuf = tmpByteBuf.get();
		out = new ArrayList<T>();
		int ObjLen = 0;
		while(byteBuf.isReadable()){
			int type = byteBuf.getShort(byteBuf.readerIndex());
			if(type != NetData.VALUE_BYTES){
//			throw new IllegalArgumentException("packet parse fail! packetId:" + getPacketId() + " paramType:bytes");
				return out;
			}
			int len = byteBuf.getShort(byteBuf.readerIndex() + 2);;
			if(ObjLen > 0 && len != ObjLen)
				return out;
			byteBuf.skipBytes(4);
			int oldRidx = byteBuf.readerIndex();
			out.add(reader.readObj());
			if(ObjLen == 0)
				ObjLen = byteBuf.readerIndex() - oldRidx;
			
		}
		return out;
	}
	
	/**
	 * 读取剩余所有字节
	 * @return
	 */
	protected byte[] readRemain() {
		ByteBuf byteBuf = tmpByteBuf.get();
		byte[] data = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(data);
		return data;
	}
	
	/**
	 * 获取包id,可以注解化
	 * @return
	 */
	public abstract int getPacketId();
}
