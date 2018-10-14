package com.garowing.gameexp.game.rts.ai.behaviac.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.garowing.gameexp.game.rts.ai.behaviac.cache.BTCache;
import com.garowing.gameexp.game.rts.ai.behaviac.cache.NodeManager;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.NodeType;
import com.garowing.gameexp.game.rts.ai.behaviac.node.BehaviorTree;
import com.garowing.gameexp.game.rts.base.xml.AbstractData;

/**
 * 行为树数据
 * @author seg
 * 2017年1月9日
 */
@XmlRootElement(name = "behaviac_root")
@XmlAccessorType(XmlAccessType.FIELD)
public class BTData extends AbstractData 
{
	/**
	 * 行为树集合
	 */
	@XmlElement(name = "behavior")
	private List<BehaviorModel> list;
	
	/**
	 * 行为树map
	 * btMap.name = behaviorTree
	 */
	@XmlTransient
	private Map<String, BehaviorModel> btMap;

	@Override
	protected void loadData()
	{
		
	}

	@Override
	protected void initData()
	{
		for(BehaviorModel model : list)
		{
			if(btMap == null)
				btMap = new HashMap<String, BehaviorModel>();
			
			btMap.put(model.getName(), model);
			
			BehaviorTree treeNode = (BehaviorTree) NodeManager.create(NodeType.TREE_ROOT);
			treeNode.setAgentType(model.getShortAgenTtype());
			treeNode.setName(model.getName());
			treeNode.load(model.getRoot());
			BTCache.add(treeNode);
		}
		
		list.clear();
		list = null;
	}
	
	
}
