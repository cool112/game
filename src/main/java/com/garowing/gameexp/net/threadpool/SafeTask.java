package com.garowing.gameexp.net.threadpool;

import org.apache.log4j.Logger;

/**
 * 安全任务
 * @author gjs
 *
 */
public abstract class SafeTask implements Runnable {
	private static final Logger LOG = Logger.getLogger(SafeTask.class);

	@Override
	public void run() {
		try {
			runImpl();
		} catch (Throwable e) {
			LOG.error("task run fail! class:" + this.getClass().getSimpleName(), e);
		}
	}
	
	protected abstract void runImpl();

}
