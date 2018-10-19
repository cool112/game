package com.garowing.gameexp.zookeeper.client.handler;

import java.util.List;

import org.apache.zookeeper.ZooKeeper;

/**
 * 处理事件
 * @author gjs
 *
 */
public interface WatchHandler {
	/**
	 * 连接成功后
	 * @param zk
	 * @param watches
	 */
	public void onConnect(ZooKeeper zk, List<String> watches);
	/**
	 * 创建节点时
	 * @param zk
	 * @param path
	 */
	public void onCreate(ZooKeeper zk, String path);
	/**
	 * 删除节点时
	 * @param zk
	 * @param path
	 */
	public void onDelete(ZooKeeper zk, String path);
	/**
	 * 节点附件数据改变
	 * @param zk
	 * @param path
	 */
	public void onDataChange(ZooKeeper zk, String path);
	/**
	 * 子节点结构改变
	 * @param zk
	 * @param path
	 */
	public void onChildrenChange(ZooKeeper zk, String path);
}
