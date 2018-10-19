package com.garowing.gameexp.zookeeper.server;

import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZkQuorumMain;

/**
 * zookeeper服务器
 * @author gjs
 *
 */
public class ZkServer {
	public ZkQuorumMain start(String configPath){
		String userDir = System.getProperty("user.dir");
		System.setProperty("zookeeper.log.dir", userDir);
		System.setProperty("zookeeper.root.logger", "INFO,CONSOLE");
		System.setProperty(ServerCnxnFactory.ZOOKEEPER_SERVER_CNXN_FACTORY, "org.apache.zookeeper.server.NettyServerCnxnFactory");
		ZkQuorumMain zkServer = ZkQuorumMain.start(new String[]{configPath});
		return zkServer;
	}
}
