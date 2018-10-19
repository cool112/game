package com.garowing.gameexp.zookeeper.client.watcher;

import java.util.List;

import org.apache.zookeeper.Watcher;

import com.garowing.gameexp.zookeeper.client.handler.WatchHandler;
/**
 * 默认客户端监听
 * @author gjs
 *
 */
public class DefaultClientWatcher extends AbstractClientWatcher {

	public DefaultClientWatcher(List<String> watchNodes, String parent,
			String selfNode, byte[] selfData, WatchHandler handler) {
		super(watchNodes, parent, selfNode, selfData, handler);
	}

	/**
	 * @param watchNodes
	 * @param parent
	 * @param selfNode
	 * @param selfData
	 * @param handler
	 * @param chainWatcher
	 */
	private DefaultClientWatcher(List<String> watchNodes, String parent,
			String selfNode, byte[] selfData, WatchHandler handler,
			Watcher chainWatcher) {
		super(watchNodes, parent, selfNode, selfData, handler, chainWatcher);
	}

}
