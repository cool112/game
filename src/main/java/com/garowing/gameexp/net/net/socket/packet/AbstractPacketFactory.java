package com.garowing.gameexp.net.net.socket.packet;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.garowing.gameexp.net.net.socket.annotation.BzPacket;
import com.garowing.gameexp.net.net.socket.constants.PacketType;
import com.garowing.gameexp.net.utils.ClassUtils;
import com.garowing.gameexp.net.utils.ClassUtils.ClassFilter;

/**
 * 抽象包工厂,需要子类自己实现getInstance的静态方法
 * @author gjs
 *
 * @param <R>
 */
public abstract class AbstractPacketFactory <T extends AbstractRecvPacket> {
	private static final Logger LOG = Logger.getLogger(AbstractPacketFactory.class);
	/** 类搜索根路径 */
	private String packageRoot = "com.garowing.gameexp.net";
	/** 接收包集合 */
	private Map<Integer, T> recvMap = new HashMap<Integer, T>(1000);
	/**
	 * 扫描包类的根目录
	 * @param packagePath
	 */
	protected AbstractPacketFactory(String packagePath){
		init(packagePath);
	}
	private void init(String packagePath){
		packageRoot = packagePath;
		init();
	}
	
	private void init(){
		List<Class<T>> clazzList = ClassUtils.scanPackage(packageRoot, new ClassFilter() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public boolean accept(Class clazz) {
				if(ClassUtils.isAbstract(clazz))
					return false;
				if(clazz.getSuperclass() != getPacketSupperClass())
					return false;
				Annotation annotation = clazz.getAnnotation(BzPacket.class);
				if(annotation == null)
				{
					LOG.error("warnning! recv packet may forgot setting id! packet:" + clazz.getSimpleName());
					return false;
				}
				if(((BzPacket)annotation).type() != getRcvPacketType())
					return false;
				
				return true;
			}
			
		});
		for(Class<T> clazz : clazzList){
			BzPacket annotation = clazz.getAnnotation(BzPacket.class);
			try {
				addRecvPacket(annotation.id(), clazz.newInstance());
			} catch (Exception e) {
				LOG.error("init recv packet fail! packet:" + clazz.getSimpleName(), e);
			}
		}
	}

	/**
	 * @param id
	 * @param newInstance
	 */
	private void addRecvPacket(int id, T packet) {
		recvMap.put(id, packet);
	}

	/**
	 * 获取包实例
	 * @param packetId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T newRecvPacket(int packetId){
		T packet = recvMap.get(packetId);
		if(packet == null)
			return null;
		
		try {
			return (T) packet.clone();
		} catch (CloneNotSupportedException e) {
			LOG.error("recv packet instance fail!", e);
			return null;
		}
	}
	
	/**
	 * 包类型
	 * @return
	 */
	protected abstract PacketType getRcvPacketType();
	
	/**
	 * 获取包限定的超类
	 * @return
	 */
	protected abstract Class<T> getPacketSupperClass();
}
