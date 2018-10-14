package com.garowing.gameexp.game.rts.ai.behaviac.node.attachment;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.NodeType;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.OperatorType;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.PhaseType;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.PropertyModelKey;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.AbstractTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.PropertyTag;
import com.garowing.gameexp.game.rts.ai.behaviac.model.AttachmentModel;

/**
 * 效果器，后置效果
 * @author seg
 *
 */
@NodeName(name = NodeType.EFFECTOR)
public class EffectorAttachment extends AbstractAttachment
{
	private static Logger mLogger = LogManager.getLogger(EffectorAttachment.class);
	
	/**
	 * 左操作数
	 */
	private AbstractTag operatedLeft;
	
	/**
	 * 右操作数1
	 */
	private AbstractTag operatedRight;
	
	/**
	 * 右操作数2
	 */
	private AbstractTag operatedRight2;
	
	/**
	 * 运算符
	 */
	private String operator;
	
	/**
	 * 阶段条件
	 */
	private String phase; 

	/**
	 *目标状态 
	 */
	private EBTStatus targetStatus;
	
	@Override
	public void load(AttachmentModel model)
	{
		super.load(model);
		Object opl = model.getPropValue(PropertyModelKey.OPL.getName());
		if(opl instanceof AbstractTag)
			this.operatedLeft = (AbstractTag) opl;
		else
			mLogger.warn("effector node opl is null! nodeId:["+model.getId()+"]");
		
		Object opr = model.getPropValue(PropertyModelKey.OPR.getName());
		if(opr != null && opr instanceof AbstractTag)
			this.operatedRight= (AbstractTag) opr;
		else
			mLogger.warn("effector node opr is null! nodeId:["+model.getId()+"]");
		
		Object opr2 = model.getPropValue(PropertyModelKey.OPR2.getName());
		if(opr2 != null && opr2 instanceof AbstractTag)
			this.operatedRight2 = (AbstractTag) opr2;
		else
			mLogger.warn("effector node opr2 is null! nodeId:["+model.getId()+"]");
		
		this.operator = (String) model.getPropValue(PropertyModelKey.OPERATOR.getName());
		this.phase = (String) model.getPropValue(PropertyModelKey.PHASE.getName());
		switch (phase)
		{
		case PhaseType.FAILURE:
			this.targetStatus = EBTStatus.BT_FAILURE;
			break;
		case PhaseType.SUCCESS:
			this.targetStatus = EBTStatus.BT_SUCCESS;
			break;

		default:
			this.targetStatus = null;
			break;
		}
	}

	@Override
	public EBTStatus update(Agent obj)
	{
		Object oplValue;
		Object oprValue;
		Object opr2Value;
		switch (operator)
		{
		case OperatorType.ASSIGN:
			if(!(operatedLeft instanceof PropertyTag))
				return EBTStatus.BT_FAILURE;
			
			if(!isSameType(operatedLeft.getRtType(), operatedRight2.getRtType()))
				return EBTStatus.BT_FAILURE;
			
			opr2Value = operatedRight2.rt(obj);
			((PropertyTag)operatedLeft).setValue(obj, opr2Value);
			break;

		default:
			break;
		}
		return EBTStatus.BT_SUCCESS;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		EffectorAttachment effector = new EffectorAttachment();
		effector.id = this.id;
		effector.operatedLeft = this.operatedLeft;
		effector.operatedRight = this.operatedRight;
		effector.operatedRight2 = this.operatedRight2;
		effector.operator = this.operator;
		effector.phase = this.phase;
		return effector;
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

	public String getPhase()
	{
		return phase;
	}

	public EBTStatus getTargetStatus()
	{
		return targetStatus;
	}

	
}
