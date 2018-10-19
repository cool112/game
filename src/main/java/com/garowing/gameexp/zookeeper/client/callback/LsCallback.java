package com.garowing.gameexp.zookeeper.client.callback;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.KeeperException.Code;
/**
 * ls命令回调
 * @author gjs
 *
 */
public class LsCallback implements ChildrenCallback {
	private static final Logger LOG = Logger.getLogger(LsCallback.class);
	@Override
	public void processResult(int rc, String path, Object ctx,
			List<String> children) {
		switch (rc) {
		case Code.Ok:
			LOG.info("parent:" + path + " ls:" + children);
			break;
		case Code.NoNode:
			LOG.warn("parent not existed, parent:" + path);
			break;
		default:
			LOG.warn("ls rc:" + rc + " parent:" + path);
			break;
		}
	}

}
