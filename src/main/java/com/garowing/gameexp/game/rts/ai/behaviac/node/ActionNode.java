package com.garowing.gameexp.game.rts.ai.behaviac.node;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.NodeName;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.PropertyModelKey;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.EnumTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.MethodTag;
import com.garowing.gameexp.game.rts.ai.behaviac.model.NodeModel;
import com.garowing.gameexp.game.rts.ai.behaviac.model.PropertyModel;
import com.garowing.gameexp.game.rts.ai.behaviac.node.attachment.AbstractAttachment;
import com.garowing.gameexp.game.rts.ai.behaviac.node.attachment.EffectorAttachment;
import com.garowing.gameexp.game.rts.ai.behaviac.task.BehaviorTask;

/**
 * 动作节点
 * @author seg
 * 2017年1月9日
 */
@NodeName(name = "Action")
public class ActionNode extends BehaviorNode
{
	private static final Logger mLogger = LogManager.getLogger(ActionNode.class);
	/**
	 * 调用的方法
	 */
	private MethodTag method;
	
	/**
	 * 结果选项：<br/>
	 * invalid - 使用method的返回值,返回类型必须是EBTStatus,否则可以定义多一个<b>状态返回函数</b>进行调用<br/>
	 * success - 永远返回成功<br/>
	 * failure - 永远返回失败<br/>
	 * running - 还在运行
	 */
	private EBTStatus resultOption;
	
	/**
	 * 状态返回函数
	 */
	private MethodTag resultFunc;
	
	@Override
	public void load(NodeModel model)
	{
		super.load(model);
		PropertyModel resultOpt = model.getProp(PropertyModelKey.RESULT_OPTION.getName());
		if(resultOpt == null)
			return;
		
		try
		{
			EnumTag resultTag = (EnumTag) resultOpt.getValue();
			resultOption = resultTag.rt(EBTStatus.class);
			PropertyModel methodProp = model.getProp(PropertyModelKey.METHOD.getName());
			if(methodProp == null)
				return;
			
			this.method = (MethodTag) methodProp.getValue();
			if(method.getReturnType() != EBTStatus.class && resultOption == EBTStatus.BT_INVALID)
			{
				PropertyModel resultFuncProp = model.getProp(PropertyModelKey.RESULT_FUNCTOR.getName());
				if(resultFuncProp == null)
					return;
				
				resultFunc = (MethodTag) resultFuncProp.getValue();
			}
		} catch (Exception e)
		{
			mLogger.warn("action node load fail!", e);
		}
	}

	@Override
	public boolean isValid(Agent agent, BehaviorTask task)
	{
		return false;
	}

	@Override
	public EBTStatus update(Agent obj)
	{
		Object result = method.invoke(obj);
		if(effectors != null)
		{
			for(EffectorAttachment effector : effectors)
			{
				if(effector.getTargetStatus() != null && effector.getTargetStatus() != (EBTStatus)result)
					continue;
				
				effector.update(obj);
			}
		}
		if(resultOption == EBTStatus.BT_INVALID)
			return (EBTStatus) result;
			
		return resultOption;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		ActionNode actionNode = new ActionNode();
		actionNode.id = this.getId();
		actionNode.method = this.method;
		actionNode.resultOption = this.resultOption;
		actionNode.resultFunc = this.resultFunc;
		if(getAttachments() != null)
		{
			for(AbstractAttachment attachment : attachments)
				actionNode.addAttachment((AbstractAttachment) attachment.clone());
		}
		return actionNode;
	}
	
}
