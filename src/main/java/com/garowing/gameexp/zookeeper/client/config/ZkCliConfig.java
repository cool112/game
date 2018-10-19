package com.garowing.gameexp.zookeeper.client.config;

import java.util.List;

/**
 * 客户端配置缓存
 * @author gjs
 *
 */
public class ZkCliConfig {
	public static final ZkCliConfig INSTANCE = new ZkCliConfig();
	/**
	 * 
	 */
	private ZkCliConfig() {
		super();
	}
	/** 相对自身节点 */
	private String node;
	private String parent;
	private List<String> watchNodes;
	/** 绝对(完整)自身节点 */
	private String absNode;
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the watchNodes
	 */
	public List<String> getWatchNodes() {
		return watchNodes;
	}
	/**
	 * @param watchNodes the watchNodes to set
	 */
	public void setWatchNodes(List<String> watchNodes) {
		this.watchNodes = watchNodes;
	}
	
	public String getCliNode(){
		if(absNode == null)
			absNode = parent + "/" + node;
		return absNode;
	}
}
