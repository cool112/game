package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

/**
 * 多边形目标选择器，(0,0)是施法者初始坐标，(1,0)是施法者初始方向
 * <br>多目标选择第一个目标作为方向
 * @author seg
 *
 */
public class PolygonTargetSelector extends TargetSelector
{
	/**
	 * 线段集
	 */
	private List<LineSeg> lineSegs;
	
	/**
	 * 最大半径
	 */
	private float maxRadius;
	
	@Override
	public boolean check(Projectile data)
	{
		if(polygonPoints == null || polygonPoints.isEmpty() || polygonPoints.size() < 2)
			return false;
		
		if(lineSegs == null)
		{
			lineSegs = new ArrayList<LineSeg>();
			maxRadius = 0f;
			for(int i = 0 ; i < polygonPoints.size() ; i++)
			{
				int j = i + 1;
				if(j == polygonPoints.size())
					j = 0;
				
				float distance = (float) Math.sqrt(polygonPoints.get(i)[0] * polygonPoints.get(i)[0] + polygonPoints.get(i)[1] * polygonPoints.get(i)[1]);
				if(distance > maxRadius)
					maxRadius = distance;
				
				LineSeg lineSeg = new LineSeg(polygonPoints.get(i), polygonPoints.get(j));
				lineSegs.add(lineSeg);
			}
		}
		
		return true;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		GameObject caster = data.getCaster();
		if(caster.getObjectType() != GameObjectType.TROOP)
			return null;
		
		float cx = ((Troop)caster).getX();
		float cy = ((Troop)caster).getY();
		float targetX = 0f;
		float targetY = 0f;
		List<GameObject> targets = data.getTargets();
		if(targets == null || targets.isEmpty())
		{
			targetX = data.getTargetX();
			targetY = data.getTargetY();
		}
		else
		{
			GameObject target = targets.get(0);
			if(target != null && target.getObjectType() == GameObjectType.TROOP)
			{
				Troop troop = (Troop) target; 
				targetX = troop.getX();
				targetY = troop.getY();
			}
		}
		
		if(targetX == 0f && targetY == 0f)
			return null;
		
		if(maxRadius <= 0)
			return null;
		
		float[] direct = MathUtil.getUnitVector(new float[]{targetX - cx, targetY - cy});
		float cos = direct[0];
		float sin = direct[1];
		
		List<LineSeg> modLineSegs = new ArrayList<LineSeg>();
		for(LineSeg lineSeg : lineSegs)
		{
			modLineSegs.add(lineSeg.copy().rotate(0, 0, cos, sin).transform(cx, cy));
		}
		
		WarInstance war = ((GameObject)data).getWar();
		
		Collection<WarObjectInstance> visibles = new ArrayList<WarObjectInstance>();;
		
		float modifiedRadius = maxRadius;
		
		for(WarObjectInstance obj : WarUtils.getObjects(war))
		{
			if(!MathUtil.isInRange(obj, cx, cy, modifiedRadius))
				continue;
			
			if(MathUtil.isPointInFace(obj.getX(), obj.getY(), modLineSegs))
			{
				visibles.add(obj);
				continue;
			}
			
			for(LineSeg lineSeg : modLineSegs)
			{
				if(MathUtil.isCircleIntersectLineSeg(obj.getX(), obj.getY(), obj.getCollisionRadius(), lineSeg.getStartX(), lineSeg.getStartY(), lineSeg.getEndX(), lineSeg.getEndY()))
				{
					visibles.add(obj);
					break;
				}
			}
		}
		
		return visibles;
	}
	
	/**
	 * 线段
	 * @author seg
	 *
	 */
	public static class LineSeg
	{
		/**
		 * 起点
		 */
		private float[] startPoint;
		
		/**
		 * 终点
		 */
		private float[] endPoint;
		
		public LineSeg(float[] startPoint, float[] endPoint)
		{
			super();
			this.startPoint = startPoint;
			this.endPoint = endPoint;
		}
		
		public LineSeg(float startX, float startY, float endX, float endY)
		{
			super();
			this.startPoint = new float[]{startX, startY};
			this.endPoint = new float[]{endX, endY};
		}

		public float[] getStartPoint()
		{
			return startPoint;
		}

		public void setStartPoint(float[] startPoint)
		{
			this.startPoint = startPoint;
		}

		public float[] getEndPoint()
		{
			return endPoint;
		}

		public void setEndPoint(float[] endPoint)
		{
			this.endPoint = endPoint;
		}
		
		public float getStartX()
		{
			return this.startPoint[0];
		}
		
		public float getStartY()
		{
			return this.startPoint[1];
		}
		
		public float getEndX()
		{
			return this.endPoint[0];
		}
		
		public float getEndY()
		{
			return this.endPoint[1];
		}
		
		/**
		 * 平移
		 * @param vx
		 * @param vy
		 */
		public LineSeg transform(float vx, float vy)
		{
			this.startPoint[0] = startPoint[0] + vx;
			this.startPoint[1] = startPoint[1] + vy;
			this.endPoint[0] = endPoint[0] + vx;
			this.endPoint[1] = endPoint[1] + vy;
			return this;
		}
		
		/**
		 * 旋转
		 * @param cx
		 * @param cy
		 * @param cos
		 * @param sin
		 * @return
		 */
		public LineSeg rotate(float cx, float cy, float cos, float sin)
		{
			float startVx = getStartX() - cx;
			float startVy = getStartY() - cy;
			this.startPoint[0] = cx + (startVx * cos - startVy * sin);
			this.startPoint[1] = cy + (startVx * sin + startVy * cos);
			float endVx = getEndX() - cx;
			float endVy = getEndY() - cy;
			this.endPoint[0] = cx + (endVx * cos - endVy * sin);
			this.endPoint[1] = cy + (endVx * sin + endVy * cos);
			return this;
		}
		
		/**
		 * 复制线段
		 * @return
		 */
		public LineSeg copy()
		{
			return new LineSeg(getStartX(), getStartY(), getEndX(), getEndY());
		}
		
		@Override
		public String toString()
		{
			return "[(" + startPoint[0] + "," + startPoint[1] + "), (" + endPoint[0] + "," + endPoint[1] + ")]";
		}
	}

}
