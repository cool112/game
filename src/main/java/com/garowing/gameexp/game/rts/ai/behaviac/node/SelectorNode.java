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
 * 选择节点
 * @author seg
 *
 */
@NodeName(name = NodeType.SELECTOR)
public class SelectorNode extends BehaviorNode implements Cloneable
{
	private static Logger mLogger = LogManager.getLogger(SelectorNode.class);
	
	@Override
	public void load(NodeModel model)
	{
		super.load(model);
		if(model.getChildren() == null || model.getChildren().isEmpty())
			return;
		
		for(NodeModel childModel :model.getChildren())
		{
			BehaviorNode node = NodeManager.create(childModel.getClassName());
			if(node == null)
			{
				mLogger.warn("lost child node! class:[" +childModel.getClassName()+"] id:[" + childModel.getId() + "]");
				continue;
			}
			
			node.load(childModel);
			addChild(node);
		}
	}

	@Override
	public EBTStatus update(Agent obj)
	{
		if(children == null)
			return EBTStatus.BT_FAILURE;
		
		for(BehaviorNode node : children)
		{
			EBTStatus result = node.update(obj);
			if(result != null && result == EBTStatus.BT_SUCCESS)
				return EBTStatus.BT_SUCCESS;
		}
		return EBTStatus.BT_FAILURE;
	}

	@Override
	public boolean isValid(Agent agent, BehaviorTask task)
	{
		if(children == null || children.isEmpty())
			return false;
		
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		SelectorNode selectorNode = new SelectorNode();
		selectorNode.id = this.id;
		if(getChildren() != null)
		{
			for(BehaviorNode child : getChildren())
			{
				selectorNode.addChild((BehaviorNode) child.clone());
			}
		}
		
		return selectorNode;
	}

}
