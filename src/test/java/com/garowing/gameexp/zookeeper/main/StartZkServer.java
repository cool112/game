package com.garowing.gameexp.zookeeper.main;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;

import com.garowing.gameexp.zookeeper.server.ZkServer;

/**
 * 启动服务器
 * @author gjs
 *
 */
public class StartZkServer {
	public static void main(String[] args) {
		String userDir = System.getProperty("user.dir");
		PropertyConfigurator.configure(userDir+ "/conf/log4j.properties");
		String cfgPath = userDir + File.separator + "conf" + File.separator + "zoo.cfg";
		ZkServer zkServer = new ZkServer();
		zkServer.start(cfgPath);
	}
}
