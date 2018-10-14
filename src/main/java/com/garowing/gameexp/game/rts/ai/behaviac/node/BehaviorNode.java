package com.garowing.gameexp.game.rts.ai.behaviac.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.cache.AttachmentManager;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.model.AttachmentModel;
import com.garowing.gameexp.game.rts.ai.behaviac.model.NodeModel;
import com.garowing.gameexp.game.rts.ai.behaviac.node.attachment.AbstractAttachment;
import com.garowing.gameexp.game.rts.ai.behaviac.node.attachment.EffectorAttachment;
import com.garowing.gameexp.game.rts.ai.behaviac.task.BehaviorTask;

/**
 * 抽象节点
 * @author seg
 * 2017年1月5日
 */
public abstract class BehaviorNode
{
	private static Logger mLogger = LogManager.getLogger(BehaviorNode.class);
	
	/**
	 *树中唯一id 
	 */
	protected int id;
	
	/**
	 * agent类型
	 */
	private String agentType;
	
	/**
	 * 附件
	 */
	protected List<AbstractAttachment> attachments;
	
	/**
	 * 后置效果
	 */
	protected List<EffectorAttachment> effectors;
	
	/**
	 * 父节点
	 */
	protected BehaviorNode parent;
	
	/**
	 * 子节点
	 */
	protected List<BehaviorNode> children;
	
	/**
	 * 从模型初始化
	 * @param model
	 */
	public void load(NodeModel model)
	{
		this.id = model.getId();
		Map<Integer, AttachmentModel> attachmentMap = model.getAttachMap();
		if(attachmentMap != null && !attachmentMap.isEmpty())
		{
			for(AttachmentModel attachmentModel : attachmentMap.values())
			{
				AbstractAttachment attachNode = AttachmentManager.create(attachmentModel.getClassName());
				if(attachNode == null)
				{
					mLogger.warn("attachment node init fail! modelId:" + attachmentModel.getId() + " class:" + attachmentModel.getClassName());
					continue;
				}
				attachNode.load(attachmentModel);
				addAttachment(attachNode);
			}
		}
	}
	
	/**
	 * 更新节点
	 * @return
	 */
	public abstract EBTStatus update(Agent obj);
	
	/**
	 * 任务有效性检查
	 * @param agent
	 * @param task
	 * @return
	 */
	public abstract boolean isValid(Agent agent, BehaviorTask task);
	
	/**
	 * 增加子节点
	 * @param node
	 */
	public void addChild(BehaviorNode node)
	{
		if(this.children == null)
			this.children = new ArrayList<BehaviorNode>();
		
		this.children.add(node);
		node.setParent(this);
	}
	
	/**
	 * 增加附件
	 * @param attachNode
	 */
	public void addAttachment(AbstractAttachment attachNode)
	{
		if(attachNode instanceof EffectorAttachment)
		{
			if(effectors == null)
				effectors = new ArrayList<EffectorAttachment>();
			
			effectors.add((EffectorAttachment) attachNode);
		}
		
		if(attachments == null)
			attachments = new ArrayList<AbstractAttachment>();
		
		attachments.add(attachNode);
		attachNode.setParent(this);
	}
	
	/**
	 * 获取树名
	 * @return
	 */
	public String getBtName()
	{
		BehaviorNode currNode = this;
		while(currNode.getParent() != null)
		{
			currNode = currNode.getParent();
		}
		
		return ((BehaviorTree)currNode).getName();
	}
	
	@Override
	public abstract Object clone() throws CloneNotSupportedException;
	
	public String getAgentType()
	{
		return agentType;
	}



	public void setAgentType(String agentType)
	{
		this.agentType = agentType;
	}



	public List<AbstractAttachment> getAttachments()
	{
		return attachments;
	}



	public void setAttachments(List<AbstractAttachment> attachments)
	{
		this.attachments = attachments;
	}

	public BehaviorNode getParent()
	{
		return parent;
	}

	public void setParent(BehaviorNode parent)
	{
		this.parent = parent;
	}



	public List<BehaviorNode> getChildren()
	{
		return children;
	}



	public void setChildren(List<BehaviorNode> children)
	{
		this.children = children;
	}

	public int getId()
	{
		return id;
	}

	public List<EffectorAttachment> getEffectors()
	{
		return effectors;
	}

}
