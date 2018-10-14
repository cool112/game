//package com.garowing.gameexp.game.rts.ai.behaviac.node;
//
//import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
//import com.garowing.gameexp.game.rts.ai.behaviac.cache.BTCache;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.NodeType;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.PropertyModelKey;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.ConstantTag;
//import com.garowing.gameexp.game.rts.ai.behaviac.model.NodeModel;
//import com.garowing.gameexp.game.rts.ai.behaviac.task.BehaviorTask;
//
///**
// * 引用节点
// * @author seg
// * 2017年4月7日
// */
//@NodeName(name = NodeType.REFERENCED_BEHAVIOR)
//public class ReferencedNode extends BehaviorNode implements Cloneable
//{
//	/**
//	 * 引用树名
//	 */
//	private String behaviorTreeName;
//	
//	@Override
//	public void load(NodeModel model)
//	{
//		super.load(model);
//		Object valueEntity = model.getProp(PropertyModelKey.REFERENCE_BEHAVIOR.getName()).getValue();
//		if(valueEntity instanceof ConstantTag)
//			behaviorTreeName = (String) ((ConstantTag)valueEntity).getConstantValue();
//		else
//			behaviorTreeName = (String) valueEntity;
//		
//		
//	}
//
//	@Override
//	public EBTStatus update(Agent obj)
//	{
//		//TODO 第一次调用时，引用复制子树时可能存在并发风险
////		if(getBtName().equals("TypePriorityMachineAndBuildingBT"))
////			System.err.println("reference node id["+getId()+"] ref btName["+behaviorTreeName+"] rootName["+getBtName()+"]");
//		
//		if(getChildren() != null && !getChildren().isEmpty() && getChildren().get(0) != null)
//			return getChildren().get(0).update(obj);
//		
//		BehaviorTree behaviorTree = BTCache.copyBehaviorTree(behaviorTreeName);
//		if(behaviorTree == null)
//			return EBTStatus.BT_FAILURE;
//		
//		addChild(behaviorTree);
//		
//		return behaviorTree.update(obj);
//	}
//
//	@Override
//	public boolean isValid(Agent agent, BehaviorTask task)
//	{
//		if(!BTCache.contains(behaviorTreeName))
//			return false;
//		
//		return true;
//	}
//	
//	@Override
//	public Object clone() throws CloneNotSupportedException
//	{
//		ReferencedNode referencedNode = new ReferencedNode();
//		referencedNode.id = this.getId();
//		referencedNode.behaviorTreeName = this.behaviorTreeName;
//		if(getChildren() != null)
//		{
//			for(BehaviorNode child : getChildren())
//			{
//				referencedNode.addChild((BehaviorNode) child.clone());
//			}
//		}
//		return referencedNode;
//	}
//
//}
