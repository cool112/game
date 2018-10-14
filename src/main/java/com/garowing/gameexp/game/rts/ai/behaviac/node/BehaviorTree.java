package com.garowing.gameexp.game.rts.ai.behaviac.node;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
import com.garowing.gameexp.game.rts.ai.behaviac.cache.NodeManager;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.NodeType;
import com.garowing.gameexp.game.rts.ai.behaviac.model.NodeModel;
import com.garowing.gameexp.game.rts.ai.behaviac.task.BehaviorTask;

/**
 * 行为树根节点
 * @author seg
 * 2017年1月7日
 */
@NodeName(name = NodeType.TREE_ROOT)
public class BehaviorTree extends BehaviorNode implements Cloneable
{
	private static Logger mLogger = LogManager.getLogger(BehaviorTree.class);
	/**
	 * 名称，唯一
	 */
	private String name;

	@Override
	public void load(NodeModel model)
	{
		BehaviorNode node = NodeManager.create(model.getClassName());
		if(node == null)
		{
			mLogger.warn("BT root init fail! model:[" + model.getClassName() + "]");
			return;
		}
		
		node.load(model);
		addChild(node);
	}

	@Override
	public boolean isValid(Agent agent, BehaviorTask task)
	{
		if(children == null || children.isEmpty())
			return false;
		
		return true;
	}

	@Override
	public EBTStatus update(Agent obj)
	{
		if(children == null || children.isEmpty())
			return EBTStatus.BT_FAILURE;
		
		BehaviorNode node = children.get(0);
		return node.update(obj);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BehaviorTree treeNode = new BehaviorTree();
		treeNode.id = this.id;
		treeNode.setName(name);
		treeNode.setAgentType(this.getAgentType());
		if(getChildren() != null)
		{
			for(BehaviorNode child : getChildren())
			{
				treeNode.addChild((BehaviorNode) child.clone());
			}
		}
		
		return treeNode;
	}
	
}
