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
 * 序列节点
 * @author seg
 * 2017年4月7日
 */
@NodeName(name = NodeType.SEQUENCE)
public class SequenceNode extends BehaviorNode implements Cloneable
{
	private static Logger mLogger = LogManager.getLogger(SequenceNode.class);
	
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
	public boolean isValid(Agent agent, BehaviorTask task)
	{
		if(children == null || children.isEmpty())
			return false;
		
		return true;
	}

	@Override
	public EBTStatus update(Agent obj)
	{
		if(children == null)
			return EBTStatus.BT_FAILURE;
		
		for(BehaviorNode node : children)
		{
			EBTStatus result = node.update(obj);
			if(result == null || result == EBTStatus.BT_FAILURE)
				return EBTStatus.BT_FAILURE;
		}
		return EBTStatus.BT_SUCCESS;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		SequenceNode sequenceNode = new SequenceNode();
		sequenceNode.id = this.id;
		if(getChildren() != null)
		{
			for(BehaviorNode child : getChildren())
			{
				sequenceNode.addChild((BehaviorNode) child.clone());
			}
		}
		
		return sequenceNode;
	}
}
