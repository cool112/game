package com.garowing.gameexp.game.rts.skill.entity;

import java.util.List;

/**
 * 技能参数
 * @author seg
 *
 */
public class SkillParams
{
	/**
	 *效果实例id 
	 */
	private int effectInstanceId;
	
	/**
	 * 技能模型id
	 */
	private int skillModelId;
	
	/**
	 * 当前效果modelId
	 */
	private int currentEffectModelId;
	
	/**
	 * 施法者id
	 */
	private int casterId;
	
	/**
	 * 目标ids
	 */
	private List<Integer> targetIds;
	
	/**
	 * 目标中心x
	 */
	private int targetX;
	
	/**
	 * 目标中心y
	 */
	private int targetY;
	
	/**
	 * 半径
	 */
	private float radius;
	
	/**
	 * 开始弧度
	 */
	private float startRadian;
	
	/**
	 * 结束弧度
	 */
	private float endRadian;
	
	/**
	 * 多边形点集
	 */
	private List<int[]> polygonPoints;
	
	/**
	 * 事件时间
	 */
	private long eventTime;
	
	/**
	 * 流水id
	 */
	private int flowId;
	
	/**
	 * 构造实体
	 * @param skillInstanceId
	 * @param skillModelId
	 * @param currentEffectModelId
	 * @param casterId
	 * @param targetIds
	 * @param targetX
	 * @param targetY
	 * @param radius
	 * @param startRadian
	 * @param endRadian
	 * @param polygonPoints
	 * @return
	 */
	public static SkillParams valueOf(int skillInstanceId, int skillModelId, int currentEffectModelId, int casterId,
			List<Integer> targetIds, int targetX, int targetY, float radius, float startRadian, float endRadian,
			List<int[]> polygonPoints, long eventTime, int flowId)
	{
		SkillParams skillParams = new SkillParams();
		skillParams.effectInstanceId = skillInstanceId;
		skillParams.skillModelId = skillModelId;
		skillParams.currentEffectModelId = currentEffectModelId;
		skillParams.casterId = casterId;
		skillParams.targetIds = targetIds;
		skillParams.targetX = targetX;
		skillParams.targetY = targetY;
		skillParams.radius = radius;
		skillParams.startRadian = startRadian;
		skillParams.endRadian = endRadian;
		skillParams.polygonPoints = polygonPoints;
		skillParams.eventTime = eventTime;
		skillParams.flowId = flowId;
		return skillParams;
	}

	public int getEffectInstanceId()
	{
		return effectInstanceId;
	}

	public void setEffectInstanceId(int effectInstanceId)
	{
		this.effectInstanceId = effectInstanceId;
	}

	public int getSkillModelId()
	{
		return skillModelId;
	}

	public void setSkillModelId(int skillModelId)
	{
		this.skillModelId = skillModelId;
	}

	public int getCurrentEffectModelId()
	{
		return currentEffectModelId;
	}

	public void setCurrentEffectModelId(int currentEffectModelId)
	{
		this.currentEffectModelId = currentEffectModelId;
	}

	public int getCasterId()
	{
		return casterId;
	}

	public void setCasterId(int casterId)
	{
		this.casterId = casterId;
	}

	public List<Integer> getTargetIds()
	{
		return targetIds;
	}

	public void setTargetIds(List<Integer> targetIds)
	{
		this.targetIds = targetIds;
	}

	public int getTargetX()
	{
		return targetX;
	}

	public void setTargetX(int targetX)
	{
		this.targetX = targetX;
	}

	public int getTargetY()
	{
		return targetY;
	}

	public void setTargetY(int targetY)
	{
		this.targetY = targetY;
	}

	public float getRadius()
	{
		return radius;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public float getStartRadian()
	{
		return startRadian;
	}

	public void setStartRadian(float startRadian)
	{
		this.startRadian = startRadian;
	}

	public float getEndRadian()
	{
		return endRadian;
	}

	public void setEndRadian(float endRadian)
	{
		this.endRadian = endRadian;
	}

	public List<int[]> getPolygonPoints()
	{
		return polygonPoints;
	}

	public void setPolygonPoints(List<int[]> polygonPoints)
	{
		this.polygonPoints = polygonPoints;
	}

	public long getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(long eventTime)
	{
		this.eventTime = eventTime;
	}

	public int getFlowId()
	{
		return flowId;
	}

	public void setFlowId(int flowId)
	{
		this.flowId = flowId;
	}
	
}
