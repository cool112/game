package com.garowing.gameexp.game.rts.ai.behaviac.entity;

import java.util.Collections;
import java.util.List;

/**
 * 角色数据，单位自定义行为的参数
 * @author seg
 *
 */
public class RoleData
{
	/**
	 * 移动路径
	 */
	@SuppressWarnings("unchecked")
	private List<float[]> movePoints = Collections.EMPTY_LIST;
	
	/**
	 * 是否循环
	 */
	private boolean isResyclable = true;
	
	/**
	 * 当前移动点下标
	 */
	private int currMoveIndex;
	
	/**
	 * 获取当前移动点
	 * @return
	 */
	public float[] getCurrentMovePoint()
	{
		if(currMoveIndex >= movePoints.size())
			return null;
		
		return movePoints.get(currMoveIndex);
	}
	
	/**
	 * 下一个行动
	 */
	public void nextMove()
	{
		currMoveIndex++;
		if(isResyclable && currMoveIndex >= movePoints.size())
			currMoveIndex = 0;
	}

	public List<float[]> getMovePoints()
	{
		return movePoints;
	}

	public void setMovePoints(List<float[]> movePoints)
	{
		this.movePoints = movePoints;
	}

	public boolean isResyclable()
	{
		return isResyclable;
	}

	public void setResyclable(boolean isResyclable)
	{
		this.isResyclable = isResyclable;
	}
	
	
}
