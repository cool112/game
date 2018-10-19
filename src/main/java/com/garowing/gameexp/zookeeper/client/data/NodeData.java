package com.garowing.gameexp.zookeeper.client.data;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 节点数据bean
 * @author gjs
 *
 */
@JSONType
public class NodeData {
	/** id */
	@JSONField
	private int id;
	/** 作为客户端的数据 clientMap.remoteAddr = localPort */
	@JSONField
	private Map<String, Integer> clientMap = new HashMap<String, Integer>();
	/** 作为服务器的数据 serverMap.listenPort = config */
	@JSONField
	private Map<Integer, String> serverMap = new HashMap<Integer, String>();;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the clientMap
	 */
	public Map<String, Integer> getClientMap() {
		return clientMap;
	}
	/**
	 * @param clientMap the clientMap to set
	 */
	public void setClientMap(Map<String, Integer> clientMap) {
		this.clientMap = clientMap;
	}
	/**
	 * @return the serverMap
	 */
	public Map<Integer, String> getServerMap() {
		return serverMap;
	}
	/**
	 * @param serverMap the serverMap to set
	 */
	public void setServerMap(Map<Integer, String> serverMap) {
		this.serverMap = serverMap;
	}
	/**
	 * 增加客户端信息
	 * @param string
	 * @param string2
	 */
	public void putClient(String remoteAddr, Integer localPort) {
		this.clientMap.put(remoteAddr, localPort);
	}
	
}
