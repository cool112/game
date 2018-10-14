package com.garowing.gameexp.game.rts.listener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import commons.script.java.scriptmanager.ScriptManager;
import com.garowing.gameexp.game.rts.listener.model.WarActionModel;
import com.garowing.gameexp.game.rts.listener.model.WarConditionModel;
import com.garowing.gameexp.game.rts.listener.model.WarListenerModel;
import com.garowing.gameexp.game.rts.listener.objects.AbsWarListener;
import com.garowing.gameexp.game.rts.listener.objects.WarAction;
import com.garowing.gameexp.game.rts.listener.objects.WarCondition;
import com.garowing.gameexp.game.rts.listener.script.WarListenerHandlerLoad;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 战争监听器服务类
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年7月29日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public final class WarListenerService 
{
	
	final static Logger log = Logger.getLogger(WarListenerService.class);

	/**
	 * 监听脚本相关配置
	 */
	private final File file = new File("./script/dungeon.xml");
	private ScriptManager scriptManager = new ScriptManager();
	private Map<Integer, Class<? extends AbsWarListener>> listenerMap 	= new HashMap<Integer, Class<? extends AbsWarListener>>();
	private Map<Integer, Class<? extends WarAction>> actionMap 			= new HashMap<Integer, Class<? extends WarAction>>();
	private Map<Integer, Class<? extends WarCondition>> conditionMap	= new HashMap<Integer, Class<? extends WarCondition>>();
	
	/**
	 * 加载脚本数据
	 */
	public void load()
	{
		
		WarListenerHandlerLoad load = new WarListenerHandlerLoad();
		scriptManager.setGlobalClassListener(load);
		
		try 
		{
			scriptManager.load(file);
		} 
		catch (Exception e)
		{
			throw new Error("加载战斗脚本失败。", e);
		}
		listenerMap 	= load.getListenerClassMap();
		actionMap 		= load.getActionClassMap();
		conditionMap	= load.getConditionClassMap();
		load.clear();
	}

	/**
	 * 
	 * @create	2014年7月29日	darren.ouyang
	 */
	public void shutdown()
	{
		scriptManager.shutdown();
		scriptManager = null;
		listenerMap.clear();
		actionMap.clear();
		conditionMap.clear();
	}
	
	/**
	 * 初始化监听器集合
	 * @param war
	 * @create	2014年7月29日	darren.ouyang
	 */
	public void initListeners (WarInstance war)
	{
		List<WarListenerModel> models = war.getModel().getListeners();
		for (WarListenerModel model : models)
		{
			boolean result = addListener(war, model);
			if (!result)
			{
				log.warn("添加战争监听器初始化失败--"+ war + "监听器[" + model.getScriptId() + "-" + model.getData() + "]");
			}
		}
	}
	
	/**
	 * 初始化监听器的行为
	 * @param listener
	 * @param model
	 * @return
	 * @create	2014年7月29日	darren.ouyang
	 */
	public boolean initAction (AbsWarListener listener, WarActionModel model)
	{
		Class<? extends WarAction> actionClz = actionMap.get(model.getScriptId());
		try 
		{
			WarAction action = actionClz.newInstance();
			boolean result = action.initData(model);	
			listener.addAction(action);
			if (!result)
				return false;
			
			return true;
		} 
		catch (Exception e) 
		{
			log.warn("战争初始化监听器的行为失败--行为[" + model.getScriptId() + "-" + model.getData() + "]");
		}
		
		return false;
	}
	
	/**
	 * 增加监听器
	 * @param war
	 * @param model
	 * @return
	 * @create	2014年8月21日	darren.ouyang
	 */
	public boolean addListener (WarInstance war, WarListenerModel model)
	{
		Class<? extends AbsWarListener> listenerClz = listenerMap.get(model.getScriptId());
		try 
		{
			AbsWarListener listener = listenerClz.newInstance();
			boolean result = listener.init(war, model);
			if (!result)
				return false;
			
			war.getWarListenerList().addListener(listener);
			return true;
		} catch (Exception e) 
		{
			log.warn("添加战争监听器失败--"+ war +"监听器[" + model.getScriptId() + "-" + model.getData() + "]", e);
			return false;
		}
	}
	
	/**
	 * 获取战争条件
	 * @param model
	 * @return
	 */
	public WarCondition gainCondition (WarConditionModel model)
	{
		Class<? extends WarCondition> conditionClz = conditionMap.get(model.getScriptId());
		try 
		{
			WarCondition condition = conditionClz.newInstance();
			
			boolean result = condition.initConditionData(model);
			if(!result)
				return null;
			
			return condition;
		} 
		catch (Exception e) 
		{
			log.warn("获取战争条件失败[ConditionScriptId:" + model.getScriptId() + "][data:" + model.getData() + "]", e);
			return null;
		}
	}
	
	public static WarListenerService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private WarListenerService(){}
	
	protected static class SingletonHolder
	{
		private final static WarListenerService instance = new WarListenerService();
	}
	
	/**
	 * 获取action类型
	 * @param actionId
	 * @return
	 */
	public Class<? extends WarAction> getActionClass(int actionId)
	{
		return actionMap.get(actionId);
	}
}
