package com.garowing.gameexp.zookeeper.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import com.garowing.gameexp.zookeeper.client.watcher.AbstractClientWatcher;
/**
 * zookeeper客户端
 * @author gjs
 *
 */
public class ZooKeeperClient {
	private static final Logger LOG = Logger.getLogger(ZooKeeperClient.class);
	private ZooKeeper zk;
	public void connect(String hosts, int sessionTimeout, AbstractClientWatcher watcher){
		try {
			zk = new ZooKeeper(hosts, sessionTimeout, watcher);
			watcher.setZk(zk);
		} catch (IOException e) {
			LOG.error("zk client connect fail!", e);
		}
	}
	
	public void shutdown(){
		if(zk != null)
			try {
				zk.close();
			} catch (InterruptedException e) {
				LOG.error("zk client close fail!", e);
			}
	}
}
