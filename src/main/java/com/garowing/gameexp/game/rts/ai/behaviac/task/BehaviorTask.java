package com.garowing.gameexp.game.rts.ai.behaviac.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.node.BehaviorNode;

/**
 * 节点任务
 * @author seg
 * 2017年1月10日
 */
public abstract class BehaviorTask
{
	protected Logger logger = LogManager.getLogger(this.getClass());
	/**
	 * 任务状态
	 */
	protected EBTStatus status;
	
	/**
	 * 取消任务
	 * @param agent
	 */
	public void abort(Agent agent)
	{
		
	}
	
	/**
	 * 执行任务
	 * @param agent
	 * @return
	 */
	public EBTStatus exec(Agent agent)
	{
		EBTStatus childrenStatus = EBTStatus.BT_RUNNING;
		return exec(agent, childrenStatus);
	}

	/**
	 * 执行任务
	 * @param agent
	 * @param childrenStatus
	 * @return
	 */
	public EBTStatus exec(Agent agent, EBTStatus childrenStatus)
	{
		if(!assertTask(agent.getNode(), agent))
			logger.warn("agent: ["+agent.getClass()+"] node:["+agent.getNode()+"]");
		
		boolean enterResult = false;
		if(status == EBTStatus.BT_RUNNING)
		{
			enterResult = true;
		}
		else
		{
			status = EBTStatus.BT_INVALID;
			
			enterResult = onEnter(agent);
		}
		
		if(enterResult)
		{
			boolean valid = checkParentUpdatePrecondition(agent);
			
			if(valid)
			{
				status = updateCurrent(agent, childrenStatus);
			}
			else
			{
				status = EBTStatus.BT_FAILURE;
				
				if(getCurrentTask() != null)
				{
					updateCurrent(agent, EBTStatus.BT_FAILURE);
				}
			}
			
			if(status != EBTStatus.BT_RUNNING)
			{
				onExit(agent, status);
			}
			else
			{
				BranchTask tree = getTopManageBranchTask();
				
				if(tree != null)
				{
					tree.setCurrentTask(this);
				}
			}
		}
		else
		{
			status = EBTStatus.BT_FAILURE;
		}
		
		return status;
	}
	
	/**
	 * 批量任务的根节点
	 * @return
	 */
	public BranchTask getTopManageBranchTask()
	{
		return null;
	}

	/**
	 * 退出任务
	 * @param agent
	 * @param status2
	 */
	protected void onExit(Agent agent, EBTStatus status2)
	{
		
	}

	/**
	 * 获取当前任务
	 * @return
	 */
	public BehaviorTask getCurrentTask()
	{
		return null;
	}

	/**
	 * 更新当前状态
	 * @param agent
	 * @param childrenStatus
	 * @return
	 */
	public EBTStatus updateCurrent(Agent agent, EBTStatus childrenStatus)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 检查父节点更新的先决条件
	 * @param agent
	 * @return
	 */
	protected boolean checkParentUpdatePrecondition(Agent agent)
	{
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean onEnter(Agent agent)
	{
		return false;
	}

	protected boolean assertTask(BehaviorNode node, Agent agent)
	{
		if(node == null)
			return false;
		
		if(!node.isValid(agent, this))
			return false;
		
		return true;
	}
}
