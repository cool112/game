package com.garowing.gameexp.game.rts.ai.behaviac.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.utils.ClassUtils;
import com.garowing.gameexp.utils.ClassUtils.ClassFilter;

/**
 * 目标过滤器管理类
 * @author seg
 * 2017年1月5日
 */
public class TargetFilterManager
{
	private static final Logger logger = LogManager.getLogger(TargetFilterManager.class);
	
	/**
	 * 包名
	 */
	private static final String PACKAGE_NAME = "com.garowing.gameexp.game.rts.ai.behaviac.filter";
	
	/**
	 * 过滤器缓存集合
	 */
	private static Map<Integer, AbstractTargetFilter> filters = new HashMap<Integer, AbstractTargetFilter>();
	
	static{
		autoRegister();
	}
	
	/**
	 * 自动注册
	 */
	private static void autoRegister()
	{
		List<Class<AbstractTargetFilter>> handlers = ClassUtils.scanPackage(PACKAGE_NAME, new ClassFilter()
		{
			
			@SuppressWarnings("rawtypes")
			@Override
			public boolean accept(Class clazz)
			{
				if(!ClassUtils.isSubclass(clazz, AbstractTargetFilter.class))
					return false;
				
				if(ClassUtils.isAbstract(clazz))
					return false;
				
				return true;
			}
		});
		
		for(Class<AbstractTargetFilter> clazz:handlers)
		{
			try
			{
				register(clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e)
			{
				logger.error("targetFilter注册失败", e);
			}
		}
		logger.info("targetFilter注册完成,filter size:"+TargetFilterManager.filters.size());
	}
	
	/**
	 * 注册过滤器
	 * @param handler
	 */
	public static void register(AbstractTargetFilter handler){
		filters.putIfAbsent(handler.getType(), handler);
	}
	
	/**
	 * 获取过滤器
	 * @param type
	 * @return
	 */
	public static AbstractTargetFilter getFilter(int type){
		return filters.get(type);
	}
	
	/**
	 * 获取所有活动处理类
	 * @return
	 */
	public static Map<Integer, AbstractTargetFilter> getFilters(){
		return filters;
	}
}
