package com.garowing.gameexp.game.rts.skill.entity;

import java.util.HashMap;
import java.util.Map;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.player.PlayerEntity;

/**
 * 客户端通知集合
 * @author seg
 *
 */
public class ClientNotificationMap extends GameObject
{
	/**
	 * 客户端通知集合
	 * clientNotifyMap.flowId.controlId = notificationParams
	 */
	private Map<Integer, Map<Integer, SkillParams>> clientNotifyMap = new HashMap<Integer, Map<Integer, SkillParams>>();;
	
	/**
	 * 当前技能参数
	 */
	private SkillParams currParams;
	
	/**
	 * 当前流水id
	 */
	private int currFlowId;
	
	public ClientNotificationMap(WarInstance war)
	{
		super(war);
	}

	@Override
	public String getName()
	{
		return "client notification map";
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.CLIENT_NOTIFICATION_MAP;
	}


	/**
	 * 增加通知
	 * @param player
	 * @param params
	 */
	public void addNotification(PlayerEntity player, SkillParams params)
	{
		Map<Integer, SkillParams> flowMap = clientNotifyMap.get(params.getFlowId());
		if(flowMap == null)
		{
			flowMap = new HashMap<Integer, SkillParams>();
			clientNotifyMap.put(params.getFlowId(), flowMap);
		}
		int controlId = player.getControl().getObjectId();
		flowMap.putIfAbsent(controlId, params);
	}
	
	/**
	 * 是否包含该流水id
	 * @param flowId
	 * @return
	 */
	public boolean containFlowId(int flowId)
	{
		return clientNotifyMap.containsKey(flowId);
	}
	
	public SkillParams getCurrParams()
	{
		return currParams;
	}

	public void setCurrParams(SkillParams currParams)
	{
		this.currParams = currParams;
	}

	public int getCurrFlowId()
	{
		return currFlowId;
	}

	public void setCurrFlowId(int currFlowId)
	{
		this.currFlowId = currFlowId;
	}

	/**
	 * 清除数据
	 */
	public void clean()
	{
		clientNotifyMap.clear();
		currParams = null;
		currFlowId = 0;
	}

	

	
	
}
