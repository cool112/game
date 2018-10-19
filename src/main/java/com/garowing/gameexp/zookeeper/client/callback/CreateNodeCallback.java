package com.garowing.gameexp.zookeeper.client.callback;

import org.apache.log4j.Logger;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * 创建节点回调
 * @author gjs
 *
 */
public class CreateNodeCallback implements StringCallback {
	private static final Logger LOG = Logger.getLogger(CreateNodeCallback.class);
	@SuppressWarnings("deprecation")
	@Override
	public void processResult(int rc, String path, Object ctx, String name) {
		switch (rc) {
		case Code.Ok:
			LOG.info("create node suc, node:" + name);
			break;
		case Code.NodeExists:
			LOG.info("create node but exists, node:" + path);
			ZooKeeper zk = (ZooKeeper) ctx;
			try {
				Stat stat = zk.exists(path, true);
				if(stat != null){
					LOG.info("stored node sessionId:" + stat.getEphemeralOwner());
				}
			} catch (KeeperException e) {
				LOG.error("exist exception", e);
			} catch (InterruptedException e) {
				LOG.error("exist exception", e);
			}
			break;
		default:
			LOG.info("create node rc:" + rc + " expext node:" + path);
			break;
		}
	}

}
