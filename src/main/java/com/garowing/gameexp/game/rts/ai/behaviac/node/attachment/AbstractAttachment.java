package com.garowing.gameexp.game.rts.ai.behaviac.node.attachment;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.model.AttachmentModel;
import com.garowing.gameexp.game.rts.ai.behaviac.node.BehaviorNode;

/**
 * 抽象附件
 * @author seg
 *
 */
public abstract class AbstractAttachment
{
	/**
	 *树中唯一id 
	 */
	protected int id;
	
	/**
	 * agent类型
	 */
	private String agentType;
	
	/**
	 * 父节点
	 */
	protected BehaviorNode parent;
	
	/**
	 * 从模型初始化
	 * @param model
	 */
	public void load(AttachmentModel model)
	{
		this.id = model.getId();
	}
	
	/**
	 * 更新节点
	 * @return
	 */
	public abstract EBTStatus update(Agent obj);
	
	@Override
	public abstract Object clone() throws CloneNotSupportedException;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getAgentType()
	{
		return agentType;
	}

	public void setAgentType(String agentType)
	{
		this.agentType = agentType;
	}

	public BehaviorNode getParent()
	{
		return parent;
	}

	public void setParent(BehaviorNode parent)
	{
		this.parent = parent;
	}
	
}
