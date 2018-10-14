package com.garowing.gameexp.game.rts.listener.script;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import commons.script.java.classlistener.DefaultClassListener;
import commons.utils.ClassUtils;
import com.garowing.gameexp.game.rts.listener.objects.AbsWarListener;
import com.garowing.gameexp.game.rts.listener.objects.WarAction;
import com.garowing.gameexp.game.rts.listener.objects.WarCondition;

/**
 * 战争脚本加载器
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月29日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class WarListenerHandlerLoad extends DefaultClassListener
{
	private final static Logger log = Logger.getLogger(WarListenerHandlerLoad.class);
	
	private Map<Integer, Class<? extends AbsWarListener>> listenerMap;
	private Map<Integer, Class<? extends WarAction>> actionMap;
	private Map<Integer, Class<? extends WarCondition>> conditionMap;
	
	public WarListenerHandlerLoad()
	{
		listenerMap 	= new HashMap<Integer, Class<? extends AbsWarListener>>();
		actionMap 		= new HashMap<Integer, Class<? extends WarAction>>();
		conditionMap	= new HashMap<Integer, Class<? extends WarCondition>>();
	}
	

	public Map<Integer, Class<? extends AbsWarListener>> getListenerClassMap()
	{
		return new HashMap<>(listenerMap);
	}
	
	public Map<Integer, Class<? extends WarAction>> getActionClassMap()
	{
		return new HashMap<>(actionMap);
	}
	
	public Map<Integer, Class<? extends WarCondition>> getConditionClassMap()
	{
		return new HashMap<>(conditionMap);
	}
	
	public void clear()
	{
		listenerMap 	= null;
		actionMap 		= null;
		conditionMap	= null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes) 
	{
		for(Class<?> clazz : classes)
		{
			if(log.isDebugEnabled())
			{
				if(clazz.getSimpleName() != null && clazz.getSimpleName().equals("") && !clazz.getSimpleName().isEmpty() )
					log.debug("加载类: " + clazz.getSimpleName());
			}
			
			if(!isValidClass(clazz))
				continue;
			
			boolean isSuccess = false;
			HandlerID id = clazz.getAnnotation(HandlerID.class);
			
			if (id == null)
				isSuccess = false;
			else if (ClassUtils.isSubclass(clazz, AbsWarListener.class))
				isSuccess = (listenerMap.putIfAbsent(id.value(), (Class<? extends AbsWarListener>)clazz) == null);
			else if (ClassUtils.isSubclass(clazz, WarAction.class))
				isSuccess = (actionMap.putIfAbsent(id.value(), (Class<? extends WarAction>)clazz) == null);
			else if (ClassUtils.isSubclass(clazz, WarCondition.class))
				isSuccess = (conditionMap.putIfAbsent(id.value(), (Class<? extends WarCondition>)clazz) == null);
			
			if (!isSuccess)
				log.warn("脚本ID:" + id + " class:" + clazz.getSimpleName() + "加载失败！！！");
		}
		super.postLoad(classes);
	}

	@Override
	public void preUnload(Class<?>[] classes) 
	{
		if (log.isDebugEnabled())
			for (Class<?> c : classes)
				log.debug("卸载： " + c.getSimpleName());
		
		super.preUnload(classes);
	}
	
	
	public boolean isValidClass(Class<?> clazz)
	{
		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

		if (!Modifier.isPublic(modifiers))
			return false;

		return true;
	}
}
