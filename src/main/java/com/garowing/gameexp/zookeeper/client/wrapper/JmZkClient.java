package com.garowing.gameexp.zookeeper.client.wrapper;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import main.java.GameUtil.FileUtil;

import com.alibaba.fastjson.JSON;
import com.garowing.gameexp.zookeeper.client.ZooKeeperClient;
import com.garowing.gameexp.zookeeper.client.config.ZkCliConfig;
import com.garowing.gameexp.zookeeper.client.data.NodeData;
import com.garowing.gameexp.zookeeper.client.handler.PrintWatchHandler;
import com.garowing.gameexp.zookeeper.client.handler.WatchHandler;
import com.garowing.gameexp.zookeeper.client.util.IpUtils;
import com.garowing.gameexp.zookeeper.client.watcher.DefaultClientWatcher;

/**
 * 封装zookeeper客户端
 * @author gjs
 *
 */
public class JmZkClient {
	private static final Logger LOG = Logger.getLogger(JmZkClient.class);
	private String	hosts;
	private ZooKeeperClient zkClient;
	/**
	 * 
	 * @param confPath
	 * @param exposePortMap 端口:描述
	 * @param handler
	 */
	public void start(String confPath, Map<Integer, String> exposePortMap, WatchHandler handler){
		try {
			Map<String, String> conf = FileUtil.loadConfigFile(confPath);
			hosts = conf.get("zookeeperServer");
			String parent = "/" + conf.get("parentNode");
			String localAddrPrefix = conf.get("localAddrPrefix");
			String watchNodesStr = conf.get("watchNodes");
			List<String> watchNodes = getRootNodeList(watchNodesStr);
			String localIp;
			List<String> localIps = IpUtils.getLocalHost(localAddrPrefix);
			if(localIps == null || localIps.isEmpty())
				localIp = "unknwon";
			else
				localIp = localIps.get(0);
			StringBuilder sb = new StringBuilder(localIp);
			Iterator<Entry<Integer, String>> itr = exposePortMap.entrySet().iterator();
			while(itr.hasNext()){
				Entry<Integer, String> entry = itr.next();
				sb.append(":").append(entry.getKey());
				if(!itr.hasNext())
					break;
			}
			String node = sb.toString();
			ZkCliConfig.INSTANCE.setNode(node);
			ZkCliConfig.INSTANCE.setParent(parent);
			ZkCliConfig.INSTANCE.setWatchNodes(watchNodes);
			NodeData nodeData = new NodeData();
			nodeData.setServerMap(exposePortMap);
			byte[] data = JSON.toJSONBytes(nodeData);
			DefaultClientWatcher watcher = new DefaultClientWatcher(watchNodes, parent, node, data, handler);
			zkClient = new ZooKeeperClient();
			zkClient.connect(hosts, 3000, watcher);
		} catch (Exception e) {
			LOG.error("config not existed", e);
		}
	}
	/**
	 * 默认认为是根节点下的目录
	 * @param watchNodesStr
	 * @return
	 */
	private List<String> getRootNodeList(String watchNodesStr) {
		ArrayList<String> list = new ArrayList<String>();
		String[] nodeStrs = watchNodesStr.split("\\,");
		for(String nodeStr : nodeStrs){
			list.add("/" + nodeStr.trim());
		}
		return list;
	}
	/**
	 * 关闭客户端
	 */
	public void close(){
		if(zkClient != null)
			zkClient.shutdown();
	}
}
