package com.garowing.gameexp.game.rts.ai.behaviac.entity;

/**
 * 玩家指令
 * @author seg
 *
 */
public class PlayerCmd
{
	private float x;
	
	private float y;
	
	private int targetId;
	
	public PlayerCmd(float x, float y, int targetId)
	{
		super();
		this.x = x;
		this.y = y;
		this.targetId = targetId;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public int getTargetId()
	{
		return targetId;
	}

	public void setTargetId(int targetId)
	{
		this.targetId = targetId;
	}
	
	
}
