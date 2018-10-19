package com.garowing.gameexp.zookeeper.client.handler;

import com.garowing.gameexp.zookeeper.client.callback.LsCallback;
/**
 * 信息打印监视处理
 * @author gjs
 *
 */
public class PrintWatchHandler extends AbstractLsWatchHandler {
	public PrintWatchHandler() {
		super(new LsCallback());
	}
}
