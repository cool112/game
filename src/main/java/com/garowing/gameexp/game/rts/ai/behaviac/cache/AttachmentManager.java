package com.garowing.gameexp.game.rts.ai.behaviac.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
import com.garowing.gameexp.game.rts.ai.behaviac.node.attachment.AbstractAttachment;
import com.garowing.gameexp.utils.ClassUtils;
import com.garowing.gameexp.utils.ClassUtils.ClassFilter;

public class AttachmentManager
{
	private static Logger mLogger = LogManager.getLogger(AttachmentManager.class);
	/**
	 * 节点映射
	 * nodeMap.nodeNmae = nodeClass
	 */
	private static Map<String, Class<? extends AbstractAttachment>> nodeMap = new HashMap<String, Class<? extends AbstractAttachment>>();
	
	/**
	 * 扫描包名
	 */
	private static String SCAN_PACKAGE = "com.garowing.gameexp.game.rts.ai.behaviac.node.attachment";
	
	static
	{
		autoRegister(SCAN_PACKAGE, new ClassFilter(){

			@SuppressWarnings("rawtypes")
			@Override
			public boolean accept(Class clazz)
			{
				if(!ClassUtils.isSubclass(clazz, AbstractAttachment.class))
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
		Collection<Class<AbstractAttachment>> classes = ClassUtils.scanPackage(packageName, classFilter);
		
		for(Class<AbstractAttachment> clazz : classes)
		{
			NodeName nodeClass = clazz.getAnnotation(NodeName.class);
			if(nodeClass == null)
				continue;
			
			Class<? extends AbstractAttachment> old = nodeMap.putIfAbsent(nodeClass.name(), clazz);
			if(old != null)
				mLogger.warn("duplicate key in BT attachment nodes,node class:[" + clazz.getSimpleName() + "]");
		}
	}

	/**
	 * 创建一个新节点
	 * @param className
	 * @return
	 */
	public static AbstractAttachment create(String className)
	{
		Class<? extends AbstractAttachment> clazz = nodeMap.get(className);
		if(clazz == null)
			return null;
		AbstractAttachment node = null;
		try
		{
			node = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			mLogger.error("BT attachment node instance fail! node:[" + className + "]", e);
		}
		return node;
	}
	
	


}
