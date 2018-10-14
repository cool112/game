package com.garowing.gameexp.game.rts.ai.behaviac.agent;

import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
import com.garowing.gameexp.game.rts.ai.behaviac.node.BehaviorNode;
import com.garowing.gameexp.game.rts.objects.GameObject;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 代理基类
 * @author seg
 * 2017年1月4日
 */
public abstract class Agent extends GameObject
{
	/**
	 * 行为树根节点
	 */
	protected BehaviorNode node;
	
	/**
	 * 启用
	 */
	protected boolean enabled;
	
	/**
	 * 更新时间
	 */
	protected long updateTime;
	
	public Agent(WarInstance war)
	{
		super(war);
	}
	
	/**
	 * 调用方法
	 * @param methodName
	 * @param params
	 * @return
	 */
	public <T> T callMethod(String methodName, Object...params)
	{
		return null;
	}
	
	/**
	 * 获取成员
	 * @param memberName
	 * @return
	 */
	public Object getMember(String memberName)
	{
		return null;
	}
	
	/**
	 * 给成员赋值
	 * @param value
	 */
	public void setMember(Object value)
	{
		
	}
	
	/**
	 * 行为树执行
	 * @return
	 */
	public EBTStatus btExec(long currTime)
	{
		if(!enabled || node == null)
			return EBTStatus.BT_FAILURE;
		
		this.updateTime = currTime;
		return node.update(this);
	}

	public BehaviorNode getNode()
	{
		return node;
	}

	public void setNode(BehaviorNode node)
	{
		this.node = node;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public long getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(long updateTime)
	{
		this.updateTime = updateTime;
	}
	
}
