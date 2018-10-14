package com.garowing.gameexp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.fightcore.scene.objects.WarSceneObject;
import com.yeto.war.fightcore.skill.script.targetselector.PolygonTargetSelector.LineSeg;

/**
 * 计算工具类
 * 
 * @author darren.ouyang <ouyang.darren@gmail.com>
 * @date 2015年4月13日
 * @version 1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class MathUtil
{
	/**
	 * 初始非零质数
	 */
	private static final int INITIAL_ODD = 305668771;

	/**
	 * 乘数非零质数
	 */
	private static final int MULTIPLIER_ODD = 1793910479;

	/**
	 * 零向量
	 */
	private static final float[] ZERO_VECTOR = new float[] { 0, 0 };

	/**
	 * 计算2点之间的距离
	 * 
	 * @param x
	 *            点1的X坐标
	 * @param y
	 *            点1的Y坐标
	 * @param targetX
	 *            点2的X坐标
	 * @param targetY
	 *            点2的Y坐标
	 * @return 返回2点之间的距离
	 */
	public static float getDistance(float x1, float y1, float x2, float y2)
	{
		float x = x1 - x2;
		float y = y1 - y2;
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * 计算2个对象的距离
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	public static float getDistance(WarObjectInstance object1, WarObjectInstance object2)
	{
		float offset = object1.getCollisionRadius() + object2.getCollisionRadius();
		return getDistance(object1.getX(), object1.getY(), object2.getX(), object2.getY()) - offset;
	}

	/**
	 * 判断对象与圆是否相交
	 * 
	 * @param object1
	 * @param x
	 * @param y
	 * @param range
	 * @return
	 */
	public static boolean isInRange(WarSceneObject object, float x, float y, float range)
	{
		return isInRange(object.getX(), object.getY(), object.getCollisionRadius(), x, y, range);
	}

	/**
	 * 判断对象与圆是否相交
	 * 
	 * @param object1
	 * @param x
	 * @param y
	 * @param range
	 * @return
	 */
	public static boolean isInRange(float x1, float y1, float r1, float x2, float y2, float r2)
	{
		float diffX = x1 - x2;
		float diffY = y1 - y2;
		float sumR = r1 + r2;
		return diffX * diffX + diffY * diffY < sumR * sumR;
	}

	/**
	 * 计算对象是否在距离内
	 * 
	 * @param object1
	 * @param object2
	 * @param range
	 * @return
	 */
	public static boolean isInRange(WarSceneObject object1, WarSceneObject object2, float distance)
	{
		float sumR = object1.getCollisionRadius() + object2.getCollisionRadius() + distance;
		float diffX = object1.getX() - object2.getX();
		float diffY = object1.getY() - object2.getY();
		return diffX * diffX + diffY * diffY <= sumR * sumR;
	}

	/**
	 * 忽略碰撞体积计算是否在范围内
	 * 
	 * @param object1
	 * @param object2
	 * @param distance
	 * @return
	 */
	public static boolean isInRangeIgnoreCollision(WarSceneObject object1, WarSceneObject object2, float distance)
	{
		float diffX = object1.getX() - object2.getX();
		float diffY = object1.getY() - object2.getY();
		return diffX * diffX + diffY * diffY <= distance * distance;
	}

	/**
	 * 计算对象是否在距离内, 带上冗余百分比
	 * 
	 * @param object1
	 * @param object2
	 * @param range
	 * @return
	 */
	public static boolean isInRangeRedundancy(WarObjectInstance object1, WarObjectInstance object2, float distance,
			float reduRatio)
	{
		float sumR = (object1.getCollisionRadius() + object2.getCollisionRadius() + distance) * reduRatio;
		float diffX = object1.getX() - object2.getX();
		float diffY = object1.getY() - object2.getY();
		return diffX * diffX + diffY * diffY <= sumR * sumR;
	}

	/**
	 * 两个圆，一个可移动，一个不可移送，获得可移动圆外切时圆心位置，(x1, y1)表示移动点，(x2, y2)表示不可移动点
	 * 
	 * @return
	 */
	public static float[] getCircumscribedPoint(float x1, float y1, float r1, float x2, float y2, float r2)
	{
		float[] moveVector = getCircumscribedVector(x1, y1, r1, x2, y2, r2);
		return new float[] { x1 + moveVector[0], y1 + moveVector[1] };
	}

	/**
	 * 获取第一个圆向第二个圆移动到外切位置最短的向量
	 * 
	 * @param x1
	 * @param y1
	 * @param r1
	 * @param x2
	 * @param y2
	 * @param r2
	 * @return
	 */
	public static float[] getCircumscribedVector(float x1, float y1, float r1, float x2, float y2, float r2)
	{
		float deltaDistance = getDistance(x1, y1, x2, y2) - (r1 + r2);
		float[] moveVector = ZERO_VECTOR;
		if (deltaDistance > 0)
			moveVector = getOffsetVector(x2, y2, r2, x1, y1, r1, deltaDistance);
		else if (deltaDistance < 0)
			moveVector = getOffsetVector(x1, y1, r1, x2, y2, r2, -deltaDistance);

		return moveVector;
	}

	/**
	 * 两个圆，一个可移动，一个不可移送，获得可移动圆内切时圆心位置，(x1, y1)表示移动点，(x2, y2)表示不可移动点
	 * 
	 * @return
	 */
	public static float[] getInscribedPoint(float x1, float y1, float r1, float x2, float y2, float r2)
	{
		float[] moveVector = getInscribedVector(x1, y1, r1, x2, y2, r2);
		return new float[] { x1 + moveVector[0], y1 + moveVector[1] };
	}

	/**
	 * 获取第一个圆向第二个圆移动到内切位置最短的向量
	 * 
	 * @param x1
	 * @param y1
	 * @param r1
	 * @param x2
	 * @param y2
	 * @param r2
	 * @return
	 */
	public static float[] getInscribedVector(float x1, float y1, float r1, float x2, float y2, float r2)
	{
		float deltaDistance = getDistance(x1, y1, x2, y2) - Math.abs(r1 - r2);
		float[] moveVector = ZERO_VECTOR;
		if (deltaDistance > 0)
			moveVector = getOffsetVector(x2, y2, r2, x1, y1, r1, deltaDistance);
		else if (deltaDistance < 0)
			moveVector = getOffsetVector(x1, y1, r1, x2, y2, r2, -deltaDistance);

		return moveVector;
	}

	/**
	 * 获取远离偏置向量
	 * 
	 * @param x1
	 * @param y1
	 * @param r1
	 * @param x2
	 * @param y2
	 * @param r2
	 * @param deltaRadius2
	 * @return
	 */
	public static float[] getOffsetVector(float x1, float y1, float r1, float x2, float y2, float r2, float deltaRadius)
	{
		float[] unitVector = getUnitVector(new float[] { x1 - x2, y1 - y2 });
		if (deltaRadius <= 0)
			return ZERO_VECTOR;

		int moveUnit = (int) (deltaRadius + 1);
		return new float[] { moveUnit * unitVector[0], moveUnit * unitVector[1] };
	}

	/**
	 * 获取单位向量
	 * 
	 * @param fs
	 * @return
	 */
	public static float[] getUnitVector(float[] fs)
	{
		float mod = (float) Math.sqrt(fs[0] * fs[0] + fs[1] * fs[1]);
		if (new Float(mod).equals(Float.NaN) || mod == 0)
			mod = 0.001f;

		return new float[] { fs[0] / mod, fs[1] / mod };
	}

	/**
	 * 获取hash值
	 * 
	 * @param params
	 * @return
	 */
	public static int getHashCode(int... params)
	{
		int total = INITIAL_ODD;
		for (int i : params)
		{
			total = total * MULTIPLIER_ODD + i;
		}

		return total;
	}

	/**
	 * 点和线的位置关系
	 * 
	 * @param targetX
	 * @param targetY
	 * @param cx
	 * @param cy
	 * @param x3
	 * @param x4
	 * @return result > 0,在线上方， result < 0在线下方，result == 0 在线上
	 */
	public static float pointPositionToLine(float targetX, float targetY, float x1, float y1, float x2, float y2)
	{
		float a = y2 - y1;
		float b = x1 - x2;
		float c = x2 * y1 - x1 * y2;

		return a * targetX + b * targetY + c;
	}

	/**
	 * 圆和直线相交
	 * 
	 * @param x
	 * @param y
	 * @param collisionRadius
	 * @param cx
	 * @param cy
	 * @param x3
	 * @param y3
	 * @return
	 */
	public static boolean isCircleIntersectLine(float cx, float cy, float radius, float x1, float y1, float x2,
			float y2)
	{
		float vx1 = cx - x1;
		float vy1 = cy - y1;
		float vx2 = x2 - x1;
		float vy2 = y2 - y1;
		float[] unitV2 = getUnitVector(new float[] { vx2, vy2 });

		float project = vx1 * unitV2[0] + vy1 * unitV2[1];
		float distanceD = (vx1 * vx1 + vy2 * vy2) - project * project;
		return distanceD * distanceD <= radius * radius;
	}

	/**
	 * 点是否在闭合多边形内
	 * 
	 * @param x
	 * @param y
	 * @param sides
	 * @return
	 */
	public static boolean isPointInFace(float x, float y, List<LineSeg> sides)
	{
		for (LineSeg side : sides)
		{
			float d = pointPositionToLine(x, y, side.getStartX(), side.getStartY(), side.getEndX(), side.getEndY());
			if (d > 0)
				return false;
		}

		return true;
	}

	/**
	 * 圆与线段相交
	 * 
	 * @param cx
	 * @param cy
	 * @param modifiedRadius
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @return
	 */
	public static boolean isCircleIntersectLineSeg(float cx, float cy, float radius, float x1, float y1, float x2,
			float y2)
	{
		float vx2 = x2 - x1;
		float vy2 = y2 - y1;
		float len = (float) Math.sqrt(vx2 * vx2 + vy2 * vy2);
		vx2 /= len;
		vy2 /= len;

		float vx1 = cx - x1;
		float vy1 = cy - y1;
		float u = vx1 * vx2 + vy1 * vy2;

		float x0 = 0.f;
		float y0 = 0.f;
		if (u <= 0)
		{
			x0 = x1;
			y0 = y1;
		} else if (u >= len)
		{
			x0 = x2;
			y0 = y2;
		} else
		{
			x0 = x1 + vx2 * u;
			y0 = y1 + vy2 * u;
		}

		return (cx - x0) * (cx - x0) + (cy - y0) * (cy - y0) <= radius * radius;
	}
	
	/**
	 * 获取指定半径，覆盖目标数量最多的圆的圆心
	 * @param r 圆的半径
	 * @param points 目标点
	 * @return [x,y,count]
	 */
	public static int[] getCircleCoverMost(float radius, int[]... points)
	{
		return getCircleCoverMost(radius, null, points);
	
	}
	
	/**
	 * 获取指定半径，覆盖目标数量最多的圆的圆心
	 * @param r 圆的半径
	 * @param vectors 向量集
	 * @param points 目标点
	 * @return [x,y,count]
	 */
	public static int[] getCircleCoverMost(float radius, List<int[]> vectors, int[]...points)
	{
		int minWidth = Integer.MAX_VALUE;
		int minHeight = Integer.MAX_VALUE;
		int width = 0;
		int height = 0;
		for (int[] point : points)
		{
			minWidth = Math.min(minWidth, point[0]);
			minHeight = Math.min(minHeight, point[1]);
			width = Math.max(width, point[0]);
			height = Math.max(height, point[1]);
		}
		int r = (int) Math.ceil(radius);
		if(vectors == null)
		{
			vectors = new ArrayList<>(r * r * 4);
			for (int i = -r; i <= r; i++)
				for (int j = -r; j <= r; j++)
					if (i * i + j * j <= r * r)
						vectors.add(new int[]{i, j});
		}
		
		int maxValue = -1;
		int candidateX = 0;
		int candidateY = 0;
		Map<Integer, Map<Integer, Integer>> map = new HashMap<>(r * 2);
		for (int[] point : points)
		{
			for (int[] vector : vectors)
			{
				int x = point[0] + vector[0];
				int y = point[1] + vector[1];
				if (x < minWidth || x > width || y < minHeight || y > height)
					continue;
				Map<Integer, Integer> xMap = map.get(x);
				if (xMap == null)
				{
					xMap = new HashMap<>(r * 2);
					map.put(x, xMap);
				}
				Integer oldValue = xMap.get(y);
				if (oldValue == null)
					oldValue = 1;
				else
					oldValue += 1;
				
				xMap.put(y, oldValue);
				if(oldValue > maxValue)
				{
					maxValue = oldValue;
					candidateX = x;
					candidateY = y;
				}
			}
		}
		
		return new int[]{candidateX, candidateY, maxValue};
	}
}
