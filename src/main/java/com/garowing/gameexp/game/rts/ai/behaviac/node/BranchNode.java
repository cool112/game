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
 * 分支节点
 * @author seg
 * 2017年4月7日
 */
@NodeName(name = "IfElse")
public class BranchNode extends BehaviorNode implements Cloneable
{
	private static Logger mLogger = LogManager.getLogger(BranchNode.class);
	
	@Override
	public void load(NodeModel model)
	{
		super.load(model);
		if(model.getChildren() == null || model.getChildren().size() < 3)
			return;
		
		NodeModel conditionModel = model.getChildren().get(0);
		if(!NodeType.CONDITION.equals(conditionModel.getClassName()))
		{
			mLogger.warn("Branch node init fail! id[" + model.getId() + "]");
			return;
		}
		
		BehaviorNode conditionNode = NodeManager.create(conditionModel.getClassName());
		if(conditionNode == null)
		{
			mLogger.warn("lost condition node! type[" + conditionModel.getClassName() + "]");
			return;
		}
		
		conditionNode.load(conditionModel);
		addChild(conditionNode);
		
		NodeModel trueModel = model.getChildren().get(1);
		BehaviorNode trueNode = NodeManager.create(trueModel.getClassName());
		if(trueNode == null)
		{
			mLogger.warn("lost true branch node! type[" + trueModel.getClassName() + "]");
			return;
		}
		
		trueNode.load(trueModel);
		addChild(trueNode);
		
		NodeModel falseModel = model.getChildren().get(2);
		BehaviorNode falseNode = NodeManager.create(falseModel.getClassName());
		if(falseNode == null)
		{
			mLogger.warn("lost false branch node! type[" + falseModel.getClassName() + "]");
			return;
		}
		
		falseNode.load(falseModel);
		addChild(falseNode);

	}

	@Override
	public EBTStatus update(Agent obj)
	{
		if(getChildren().size() < 3)
			return EBTStatus.BT_FAILURE;
		
		ConditionNode conditionNode = (ConditionNode) getChildren().get(0);
		if(conditionNode.update(obj) != EBTStatus.BT_SUCCESS)
			return EBTStatus.BT_FAILURE;
		
		if(conditionNode.getResult())
			return getChildren().get(1).update(obj);
		else
			return getChildren().get(2).update(obj);
	}

	@Override
	public boolean isValid(Agent agent, BehaviorTask task)
	{
		if(getChildren().size() < 3)
			return false;
		
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		BranchNode branchNode = new BranchNode();
		branchNode.id = this.getId();
		if(getChildren() != null)
		{
			for(BehaviorNode child : getChildren())
			{
				branchNode.addChild((BehaviorNode) child.clone());
			}
		}
		return branchNode;
	}
}
