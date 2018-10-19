package com.garowing.gameexp.zookeeper.client.callback;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooKeeper;

import com.garowing.gameexp.zookeeper.constants.ServerRootNode;
/**
 * 抽象获取服务器列表回调
 * @author gjs
 *
 */
public abstract class AbstractServerListCallback implements ChildrenCallback {
	private static final Logger LOG = Logger.getLogger(AbstractServerListCallback.class);
	@Override
	public void processResult(int rc, String path, Object ctx,
			List<String> children) {
		try {
			switch (rc) {
			case Code.Ok:
				handleList(path, children, ctx);
				break;
			case Code.NoNode:
				LOG.warn("parent not existed, parent:" + path);
				break;
			default:
				LOG.warn("ls rc:" + rc + " parent:" + path);
				break;
			}
		} catch (Exception e) {
			LOG.error("ls rc:" + rc + " parent:" + path, e);
		}
	}
	/**
	 * 处理服务器列表
	 * @param path
	 * @param children
	 * @param ctx 
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private void handleList(String path, List<String> children, Object ctx) throws Exception {
		LOG.info("parent:" + path + " ls:" + children);
		ZooKeeper zk = null;
		if(ctx != null && ctx instanceof ZooKeeper)
			zk = (ZooKeeper) ctx;
		switch (path) {
		case ServerRootNode.HALL:
			onHallList(zk, children);
			break;
		case ServerRootNode.ACTIVITY:
			onActivityList(children);
			break;
		case ServerRootNode.PLAYBACK:
			onPlaybackList(children);
			break;
		case ServerRootNode.MAJONG:
			onMajongList(children);
			break;
		default:
			break;
		}
	}
	/**
	 * 大厅服务器列表
	 * @param zk 
	 * @param list
	 * @param path 
	 * @throws Exception
	 */
	protected abstract void onHallList(ZooKeeper zk, List<String> list) throws Exception;
	/**
	 * 活动服服务器列表
	 * @param list
	 * @throws Exception
	 */
	protected abstract void onActivityList(List<String> list) throws Exception;
	/**
	 * 回放服服务器列表
	 * @param list
	 * @throws Exception
	 */
	protected abstract void onPlaybackList(List<String> list) throws Exception;
	/**
	 * 麻将服服务器列表
	 * @param list
	 * @throws Exception
	 */
	protected abstract void onMajongList(List<String> list) throws Exception;
}
