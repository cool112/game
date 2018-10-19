package com.garowing.gameexp.zookeeper.main.watcher;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * ��ӡ��Ϣ
 * @author gjs
 *
 */
public class PrintWatcher implements Watcher {
ZooKeeper zk;

	/**
 * @param zk
 */
public PrintWatcher(ZooKeeper zk) {
	super();
	this.zk = zk;
}

	@Override
	public void process(WatchedEvent event) {
		switch (event.getState()) {
		case AuthFailed:
		case Expired:
		case Disconnected:
			System.err.println("client is invalid");
			return;

		default:
			break;
		}
		try {
			switch (event.getType()) {
			case None:
				System.out.println("none node find, path:" + event.getPath());
				break;
			case NodeCreated:
				System.out.println("code created success! path:" + event.getPath());
				byte[] data = zk.getData(event.getPath(), true, null);
				System.out.println("data:" + new String(data, Charset.forName("utf-8")));
				break;
			case NodeDeleted:
				
				break;
			case NodeDataChanged:
				System.out.println("node data change, path:" + event.getPath());
				zk.getData(event.getPath(), true, new DataCallback() {
					
					@Override
					public void processResult(int rc, String path, Object ctx, byte[] data,
							Stat stat) {
						System.out.println("update node data, path:" + path);
						System.out.println("data:" + new String(data, Charset.forName("utf-8")));
					}
				}, null);
				break;
			case NodeChildrenChanged:
				System.out.println("children change, path:" + event.getPath());
				zk.getChildren(event.getPath(), true, new ChildrenCallback() {

					@Override
					public void processResult(int rc, String path, Object ctx,
							List<String> children) {
						System.out.println("update new nodes, parent:" + path + " children:" + children);
						for(String childPath : children){
							zk.getData(path + "/" + childPath, true, new DataCallback() {
								
								@Override
								public void processResult(int rc, String path, Object ctx, byte[] data,
										Stat stat) {
									System.out.println("update node data, path:" + path);
									System.out.println("data:" + new String(data, Charset.forName("utf-8")));
								}
							}, null);
						}
					}
					
				}, null);
				break;
			default:
				break;
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
