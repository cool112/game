package com.garowing.gameexp.zookeeper.client.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * 抽象子节点查询处理
 * @author gjs
 *
 */
public abstract class AbstractLsWatchHandler implements WatchHandler {
	private static final Logger LOG = Logger.getLogger(AbstractLsWatchHandler.class);
	/**
	 * 兴趣节点
	 */
	private Set<String> watches = new HashSet<String>();
	/**
	 * 服务器列表查询回调
	 */
	private ChildrenCallback callback;
	
	/**
	 * @param callback
	 */
	public AbstractLsWatchHandler(ChildrenCallback callback) {
		super();
		this.callback = callback;
	}

	@Override
	public void onConnect(ZooKeeper zk, List<String> watches) {
		if( watches == null || watches.isEmpty()){
			LOG.warn("watches none node!");
			return;
		}
		if(this.watches.isEmpty())
			this.watches.addAll(watches);
		for(String watchNode : watches){
			Stat stat = null;
			try {
				stat = zk.exists(watchNode, true);
			} catch (Exception e) {
				LOG.error("check watch node exists fail!", e);
			} 
			if(stat == null)
				continue;
			zk.getChildren(watchNode, true, callback, null);
		}
	}

	@Override
	public void onCreate(ZooKeeper zk, String path) {
		LOG.info("new node:" + path);
		if(watches.contains(path))
			zk.getChildren(path, true, callback, null);
	}

	@Override
	public void onDelete(ZooKeeper zk, String path) {
		LOG.info("delete node:" + path);
	}

	@Override
	public void onDataChange(ZooKeeper zk, String path) {
		LOG.info("change node:" + path);
	}

	@Override
	public void onChildrenChange(ZooKeeper zk, String path) {
		zk.getChildren(path, true, callback, null);
	}

}
