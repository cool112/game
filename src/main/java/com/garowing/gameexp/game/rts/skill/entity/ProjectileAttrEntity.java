package com.garowing.gameexp.game.rts.skill.entity;

import java.util.List;

import com.garowing.gameexp.game.rts.skill.script.targetselector.PolygonTargetSelector.LineSeg;

/**
 * 投射物属性实体
 * @author seg
 *
 */
public class ProjectileAttrEntity
{
	/**
	 * x坐标
	 */
	private float x;
	
	/**
	 * y坐标
	 */
	private float y;
	
	/**
	 * 半径
	 */
	private float radius;
	
	/**
	 * 弧度
	 */
	private float radian;
	
	/**
	 * 多边形
	 */
	private List<LineSeg> polygon;
	
	/**
	 * 速度向量
	 */
	private float[] velocity;
	
	/**
	 * 清除数据
	 */
	public void clean()
	{
		if(polygon != null)
			polygon.clear();
		
		radius = 0f;
		radian = 0f;
		x = 0f;
		y = 0f;
		velocity = null;
	}
	
	public ProjectileAttrEntity()
	{
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

	public float getRadius()
	{
		return radius;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public float getRadian()
	{
		return radian;
	}

	public void setRadian(float radian)
	{
		this.radian = radian;
	}

	public List<LineSeg> getPolygon()
	{
		return polygon;
	}

	public void setPolygon(List<LineSeg> polygon)
	{
		this.polygon = polygon;
	}

	public float[] getVelocity()
	{
		return velocity;
	}

	public void setVelocity(float[] velocity)
	{
		this.velocity = velocity;
	}

}
