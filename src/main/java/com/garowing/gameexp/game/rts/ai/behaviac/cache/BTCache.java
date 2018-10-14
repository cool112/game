package com.garowing.gameexp.game.rts.ai.behaviac.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.node.BehaviorTree;

/**
 * 行为树缓存
 * @author seg
 * 2017年1月7日
 */
public class BTCache
{
	private static Logger mLogger = LogManager.getLogger(BTCache.class);
	/**
	 * 行为树集合
	 */
	private static Map<String, BehaviorTree> treeMap = new HashMap<String, BehaviorTree>();

	/**
	 * 获取树模板
	 * @param behaviorTreeName
	 * @return
	 */
	public static BehaviorTree getBehaviorTree(String behaviorTreeName)
	{
		return treeMap.get(behaviorTreeName);
	}
	
	/**
	 * 引用树时需要一个新实例确定上下节点
	 * @param btNmae
	 * @return
	 */
	public static BehaviorTree copyBehaviorTree(String btNmae)
	{
		BehaviorTree treeNode = treeMap.get(btNmae);
		if(treeNode == null)
			return null;
		
		try
		{
			return (BehaviorTree) treeNode.clone();
		} catch (CloneNotSupportedException e)
		{
			mLogger.error("bt clone fail! bt[" + btNmae + "]", e);
			return null;
		}
	}

	/**
	 * 是否包含
	 * @param behaviorTreeName
	 * @return
	 */
	public static boolean contains(String behaviorTreeName)
	{
		return treeMap.containsKey(behaviorTreeName);
	}
	
	/**
	 * 增加行为树实例
	 * @param bt
	 */
	public static void add(BehaviorTree bt)
	{
		BehaviorTree oldBt = treeMap.putIfAbsent(bt.getName(), bt);
		if(oldBt != null)
			mLogger.warn("duplicated key:[" + bt.getName() + "]");
	}
}
