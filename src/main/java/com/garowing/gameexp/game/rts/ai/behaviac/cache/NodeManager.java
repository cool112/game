package com.garowing.gameexp.game.rts.ai.behaviac.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
import com.garowing.gameexp.game.rts.ai.behaviac.node.BehaviorNode;
import com.garowing.gameexp.utils.ClassUtils;
import com.garowing.gameexp.utils.ClassUtils.ClassFilter;

/**
 * 节点管理器
 * @author seg
 * 2017年4月7日
 */
public class NodeManager
{
	private static Logger mLogger = LogManager.getLogger(NodeManager.class);
	/**
	 * 节点映射
	 * nodeMap.nodeNmae = nodeClass
	 */
	private static Map<String, Class<? extends BehaviorNode>> nodeMap = new HashMap<String, Class<? extends BehaviorNode>>();
	
	/**
	 * 扫描包名
	 */
	private static String SCAN_PACKAGE = "com.garowing.gameexp.game.rts.ai.behaviac.node";
	
	static
	{
		autoRegister(SCAN_PACKAGE, new ClassFilter(){

			@SuppressWarnings("rawtypes")
			@Override
			public boolean accept(Class clazz)
			{
				if(!ClassUtils.isSubclass(clazz, BehaviorNode.class))
					return false;
				
				if(ClassUtils.isAbstract(clazz))
					return false;
				
				return true;
			}
			
		});
	}

	/**
	 * 自动注册
	 * @param packageName
	 * @param classFilter
	 */
	private static void autoRegister(String packageName, ClassFilter classFilter)
	{
		Collection<Class<BehaviorNode>> classes = ClassUtils.scanPackage(packageName, classFilter);
		
		for(Class<BehaviorNode> clazz : classes)
		{
			NodeName nodeClass = clazz.getAnnotation(NodeName.class);
			if(nodeClass == null)
				continue;
			
			Class<? extends BehaviorNode> old = nodeMap.putIfAbsent(nodeClass.name(), clazz);
			if(old != null)
				mLogger.warn("duplicate key in BT nodes,node class:[" + clazz.getSimpleName() + "]");
		}
	}

	/**
	 * 创建一个新节点
	 * @param className
	 * @return
	 */
	public static BehaviorNode create(String className)
	{
		Class<? extends BehaviorNode> clazz = nodeMap.get(className);
		if(clazz == null)
			return null;
		BehaviorNode node = null;
		try
		{
			node = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			mLogger.error("BT node instance fail! node:[" + className + "]", e);
		}
		return node;
	}
	
	
}
