package com.garowing.gameexp.game.rts.ai.behaviac.node;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.NodeType;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.OperatorType;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.PropertyModelKey;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.AbstractTag;
import com.garowing.gameexp.game.rts.ai.behaviac.model.NodeModel;
import com.garowing.gameexp.game.rts.ai.behaviac.task.BehaviorTask;

/**
 * 条件节点
 * @author seg
 * 2017年4月7日
 */
@NodeName(name = NodeType.CONDITION)
public class ConditionNode extends BehaviorNode
{
	private static Logger mLogger = LogManager.getLogger(ConditionNode.class);
	
	/**
	 * 左操作数
	 */
	private AbstractTag operatedLeft;
	
	/**
	 * 右操作数
	 */
	private AbstractTag operatedRight;
	
	/**
	 * 运算符
	 */
	private String operator;
	
	/**
	 * 结果
	 */
	private boolean result = false;;
	
	@Override
	public void load(NodeModel model)
	{
		super.load(model);
		Object opl = model.getPropValue(PropertyModelKey.OPL.getName());
		if(opl instanceof AbstractTag)
			this.operatedLeft = (AbstractTag) opl;
		else
			mLogger.warn("condition node opl is null! nodeId:["+model.getId()+"]");
		
		Object opr = model.getPropValue(PropertyModelKey.OPR.getName());
		if(opr instanceof AbstractTag)
			this.operatedRight = (AbstractTag) opr;
		else
			mLogger.warn("condition node opr is null! nodeId:["+model.getId()+"]");
		
		this.operator = (String) model.getPropValue(PropertyModelKey.OPERATOR.getName());
	}
	
	@Override
	public EBTStatus update(Agent obj)
	{
//		System.out.println("oplt:"+operatedLeft +" oplr:"+operatedRight);
		if(!isSameType(operatedLeft.getRtType(), operatedRight.getRtType()))
			return EBTStatus.BT_FAILURE;
		
		Object oplValue = operatedLeft.rt(obj);
		if(oplValue == null)
			return EBTStatus.BT_FAILURE;
		
		Object oprValue = operatedRight.rt(obj);
		if(oprValue == null)
			return EBTStatus.BT_FAILURE;
		
		switch (operator)
		{
		case OperatorType.EQUAL:
			if(!oplValue.equals(oprValue))
			{
//				mLogger.warn("Maybe type check bug. opl[" + operatedLeft.getRtType() +" val:"+ oplValue+ "] opr[" + operatedRight.getRtType()+" val:" + oprValue+ "]");
				this.result = false;
				break;
			}
			
			this.result = true;	
			break;

		default:
			mLogger.warn("invalid operator:[" + operator + "]");
			return EBTStatus.BT_FAILURE;
		}
		
		return EBTStatus.BT_SUCCESS;
	}

	@Override
	public boolean isValid(Agent agent, BehaviorTask task)
	{
		if(operatedLeft == null || operatedRight == null)
			return false;
		
		return true;
	}
	
	/**
	 * 判断是否相同类型
	 * @param type1
	 * @param type2
	 * @return
	 */
	private boolean isSameType(String type1, String type2)
	{
		String unifyType1 = unifyType(type1);
		String unifyType2 = unifyType(type2);
		return unifyType1.equals(unifyType2);
		
	}

	/**
	 * 统一类型名
	 * @param type
	 * @return
	 */
	private String unifyType(String type)
	{
		if(type == null)
			return "";
		
		switch (type)
		{
		case "integer":
			return "int";
		case "bool":
			return "boolean";
		default:
			break;
		}
		return type;
	}

	public boolean getResult()
	{
		return result;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ConditionNode conditionNode = new ConditionNode();
		conditionNode.id = this.getId();
		conditionNode.operatedLeft = this.operatedLeft;
		conditionNode.operatedRight = this.operatedRight;
		conditionNode.operator = this.operator;
		return conditionNode;
	}
}
