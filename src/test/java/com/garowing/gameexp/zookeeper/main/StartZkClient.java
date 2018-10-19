package com.garowing.gameexp.zookeeper.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import com.garowing.gameexp.zookeeper.client.ZooKeeperClient;
import com.garowing.gameexp.zookeeper.client.handler.PrintWatchHandler;
import com.garowing.gameexp.zookeeper.client.util.IpUtils;
import com.garowing.gameexp.zookeeper.client.watcher.DefaultClientWatcher;

public class StartZkClient {
	public static void main(String[] args) {
		String userDir = System.getProperty("user.dir");
		PropertyConfigurator.configure(userDir + "/conf/log4j.properties");
		String host = "127.0.0.1:2181";
		ArrayList<String> watchNodes = new ArrayList<String>();
		watchNodes.add("/hall");
		watchNodes.add("/activity");
		watchNodes.add("/playback");
		String parent = "/majong";
		String selfNode = "";
		List<String> ips = IpUtils.getLocalHost("192.169");
		if (ips.isEmpty())
			selfNode += "unknonw";
		else
			selfNode += ips.get(0);
		selfNode += ":port1:port2";
		byte[] selfData = "{port1:{}, port2:{}}".getBytes();
		DefaultClientWatcher watcher = new DefaultClientWatcher(watchNodes,
				parent, selfNode, selfData, new PrintWatchHandler());
		ZooKeeperClient zkClient = new ZooKeeperClient();
		zkClient.connect(host, 30000, watcher);
		byte[] bytes = new byte[1024];
		int len;
		loop: try {
			while ((len = System.in.read(bytes)) > 0) {
				byte[] tmp = new byte[len];
				System.arraycopy(bytes, 0, tmp, 0, len);
				switch (new String(tmp)) {
				case "quit\r\n":

					break loop;
				case "query\r\n":
					// create -e /majong/test 0 world:anyone:rwcda
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
