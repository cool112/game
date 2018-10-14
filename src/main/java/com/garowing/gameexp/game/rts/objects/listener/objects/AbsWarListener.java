package com.garowing.gameexp.game.rts.listener.objects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.listener.WarListenerService;
import com.garowing.gameexp.game.rts.listener.model.WarActionModel;
import com.garowing.gameexp.game.rts.listener.model.WarConditionModel;
import com.garowing.gameexp.game.rts.listener.model.WarListenerModel;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 抽象的战争监听器
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月30日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class AbsWarListener implements WarListener
{
	private final static Logger log = Logger.getLogger(AbsWarListener.class);
	
	/* model */
	protected WarListenerModel listenerModel;
	
	/* 行为对象 */
	private List<WarAction> actions = new ArrayList<WarAction>();
	
	/**
	 * 初始化数据
	 * @param listenerModel
	 * @return
	 */
	public final boolean init (WarInstance war, WarListenerModel listenerModel)
	{
		this.listenerModel = listenerModel;
		
		List<WarActionModel> actionModels = listenerModel.getActions();
		
		if(actionModels != null && !actionModels.isEmpty())
		{
			for (WarActionModel actionModel : actionModels)
			{
				boolean initAction = WarListenerService.getInstance().initAction(this, actionModel);
				if (!initAction)
				{
					log.warn("战场监听添加action失败--" + war + "listener[" + listenerModel.getScriptId() + "-" + 
							listenerModel.getInfo() + "]行为[" + actionModel.getScriptId() + "-" + actionModel.getInfo() + "]");
					return false;
				}
			}
		}
		else
		{
			log.warn("战场监听添加Action失败--[战场:" + war + "][Listener:" + listenerModel.getScriptId() + "-" + listenerModel.getInfo() + "]没有配置任何行为.");
		}
		
		return initListenerActionData(listenerModel);
	}
	
	/**
	 * 行为触发
	 * @param event	监听事件
	 */
	public final void action (GameObject event)
	{
		if (listenerModel.getType() == WarListenerType.CLEAR)
		{
			clearData();
		}
		else
		{
			event.getWar().getWarListenerList().removeListener(this);
		}
		
		for (WarAction action : actions)
			action.execute(event);
	}
	
	protected void clearData(){}
	
	/**
	 * 初始化监听行为的数据
	 * @param listenerModel
	 * @return
	 */
	protected boolean initListenerActionData (WarListenerModel listenerModel){return true;}
	
	public final void addAction(WarAction action) 
	{
		this.actions.add(action);
	}
	
	/**
	 * 战争条件
	 * @param event
	 * @return
	 */
	protected boolean checkCondition (GameObject event)
	{
		WarListenerConditionType conditionType = WarListenerConditionType.AND;
		if(listenerModel.getConditionType() != null)
			conditionType = listenerModel.getConditionType();
		
		List<WarConditionModel> conditionModels = listenerModel.getConditionModels();
		if(conditionModels != null && !conditionModels.isEmpty())
		{
			WarInstance war = event.getWar();
			for(WarConditionModel model : conditionModels)
			{
				WarCondition condition = WarListenerService.getInstance().gainCondition(model);
				if(condition == null)
					return false;
				
				if(conditionType == WarListenerConditionType.OR)
				{
					if(condition.checkCondition(war))
						return true;
				}
				else
				{
					if(!condition.checkCondition(war))
						return false;
				}
			}
			
			if(conditionType == WarListenerConditionType.OR)
				return false;
			else
				return true;
		}
		
		return true;
	}
	
}
