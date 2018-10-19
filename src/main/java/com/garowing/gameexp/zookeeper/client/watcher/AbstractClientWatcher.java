package com.garowing.gameexp.zookeeper.client.watcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.garowing.gameexp.zookeeper.client.callback.CreateNodeCallback;
import com.garowing.gameexp.zookeeper.client.handler.WatchHandler;
/**
 * 抽象客户端watcher
 * @author gjs
 *
 */
public abstract class AbstractClientWatcher implements Watcher {
	private static final Logger LOG = Logger.getLogger(AbstractClientWatcher.class);
	private List<String> watchNodes;
	private ZooKeeper zk;
	private String parent;
	private String selfNode;
	/** 客户端节点的绝对路径 */
	private String absSelfNode;
	private byte[] selfData;
	private WatchHandler handler;
	private Watcher chainWatcher;
	
	public AbstractClientWatcher(List<String> watchNodes, String parent,
			String selfNode, byte[] selfData, WatchHandler handler){
		this(watchNodes, parent, selfNode, selfData, handler, null);
	}
	/**
	 * @param watchNodes
	 * @param parent
	 * @param selfNode
	 * @param selfData
	 * @param handler
	 * @param chainWatcher
	 */
	public AbstractClientWatcher(List<String> watchNodes, String parent,
			String selfNode, byte[] selfData, WatchHandler handler,
			Watcher chainWatcher) {
		super();
		this.watchNodes = watchNodes;
		this.parent = parent;
		this.selfNode = selfNode;
		this.selfData = selfData;
		this.absSelfNode = parent + "/" + selfNode;
		this.handler = handler;
		this.chainWatcher = chainWatcher;
	}
	@Override
	public void process(WatchedEvent event) {
		try {
			String path = event.getPath();
			switch (event.getType()) {
			case None:
				checkState(event);
				break;
			case NodeCreated:
				if(handler != null)
					handler.onCreate(zk, path);
				break;
			case NodeDeleted:
				if(path.equals(parent)){
					LOG.warn("parent node being deleted!");
				}
				else if(path.equals(absSelfNode)){//通常非正常关闭server时,临时节点会在重启后失效
					LOG.warn("self node being deleted!");
					zk.create(absSelfNode, selfData, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new CreateNodeCallback(), zk);
				}
				else{
					if(handler != null)
						handler.onDelete(zk, path);
				}
				break;
			case NodeDataChanged:
				if(handler != null)
					handler.onDataChange(zk, path);
				break;
			case NodeChildrenChanged:
				if(handler != null)
					handler.onChildrenChange(zk, path);
				break;
			default:
				break;
			}
		} catch (KeeperException e) {
			LOG.error("client watch logic exception!", e);
		} catch (InterruptedException e) {
			LOG.error("client watch interrupted!", e);
		}
		if(chainWatcher != null)
			chainWatcher.process(event);
	}
	/**
	 * 检查状态改变
	 * @param event
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private void checkState(WatchedEvent event) throws KeeperException, InterruptedException {
		switch (event.getState()) {
		case AuthFailed:
		case Expired:
		case Disconnected:
			LOG.warn("client is invalid, state:" + event.getState());
			return;
		case SaslAuthenticated:
			break;
		case SyncConnected:
			onConnect();
		case ConnectedReadOnly:
			if(zk != null && handler != null)
				handler.onConnect(zk, watchNodes);
			break;
		default:
			break;
		}
	}
	/**
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	protected void onConnect() throws KeeperException, InterruptedException {
		if(zk == null){
			return;
		}
		Stat parentStat = zk.exists(parent, false);
		if(parentStat == null){
			try {
				zk.create(parent, new byte[]{}, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (KeeperException e) {
				if(e.code() == KeeperException.Code.NODEEXISTS)
					LOG.warn("parent node already exists! " + parent);
				else
					throw e;
			}
		}
		Stat selfNodeStat = zk.exists(absSelfNode, true);
		if(selfNodeStat != null)
			return;
		zk.create(absSelfNode, selfData, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new CreateNodeCallback(), zk);
	}
	/**
	 * @return the zk
	 */
	public ZooKeeper getZk() {
		return zk;
	}
	/**
	 * 由于第一个事件通常比设置慢,可以在连接建立后才设置zk
	 * @param zk the zk to set
	 */
	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}
	/**
	 * @return the absSelfNode
	 */
	public String getAbsSelfNode() {
		return absSelfNode;
	}

}
