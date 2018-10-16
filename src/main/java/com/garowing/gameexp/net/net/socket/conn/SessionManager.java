/**
 * 
 */
package com.garowing.gameexp.net.net.socket.conn;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import main.java.GameUtil.LoggerFile;

/**
 * @author gjs
 * 2018年7月24日
 */
public class SessionManager {
	private static final int CAPACITY = 1500;
	private static ConcurrentHashMap<Integer, IoSession> sessionMap = new ConcurrentHashMap<Integer, IoSession>(CAPACITY);
//	private static EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(4);
	public static IoSession putIfAbsent(IoSession session){
		if(session == null)
			return null;
		IoSession oldSession = sessionMap.putIfAbsent(session.getId(), session);
		if(oldSession != null)
			return oldSession;
		
//		session.setExecutor(eventExecutorGroup.next());
		return session;
	}
	
	public static IoSession get(int sessionId){
		return sessionMap.get(sessionId);
	}
	
	public static void remove(int sessionId){
		IoSession sesseion = get(sessionId);
		if(!sesseion.isConnected()){
			LoggerFile.info("remove session:" + sessionId);
			sessionMap.remove(sessionId);
		}
	}

	/**
	 * @return
	 */
	public static int sessionSize() {
		return sessionMap.size();
	}

	/**
	 * 获取session集合
	 * @return
	 */
	public static Collection<IoSession> getSessions() {
		return sessionMap.values();
	}
}
