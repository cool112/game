package com.garowing.gameexp.net.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 * @author gjs
 *
 */
public class ThreadPoolManager {
	/**
	 * 长时低频\短时中频 任务处理
	 */
	private static final ExecutorService SINGLE_EXECUTOR = Executors.newSingleThreadExecutor();
	/**
	 * 延迟\周期任务执行
	 */
	private static final ScheduledExecutorService SCHEDULE_EXECUTOR = Executors.newScheduledThreadPool(2);
	
	/**
	 * 执行任务
	 * @param task
	 */
	public static void execute(Runnable task){
		SINGLE_EXECUTOR.execute(task);
	}
	
	/**
	 * 固定周期任务
	 * @param task
	 * @param delay
	 * @param period
	 * @param unit
	 */
	public static void scheduleAtFixedRate(Runnable task, long delay, long period, TimeUnit unit){
		SCHEDULE_EXECUTOR.scheduleAtFixedRate(task, delay, period, unit);
	}
	
	/**
	 * 固定间隔任务
	 * @param task
	 * @param delay
	 * @param period
	 * @param unit
	 */
	public static void scheduleWithFixedDelay(Runnable task, long delay, long period, TimeUnit unit){
		SCHEDULE_EXECUTOR.scheduleWithFixedDelay(task, delay, period, unit);
	}
	
	/**
	 * 延时任务
	 * @param task
	 * @param delay
	 * @param unit
	 */
	public static void schedule(Runnable task, long delay, TimeUnit unit){
		SCHEDULE_EXECUTOR.schedule(task, delay, unit);
	}
	
	/**
	 * 停止任务执行
	 */
	public static void shutdown(){
		SINGLE_EXECUTOR.shutdown();
		SCHEDULE_EXECUTOR.shutdown();
	}
}
