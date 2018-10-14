package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.script.targetselector.PolygonTargetSelector.LineSeg;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

/**
 * 线性移动多边形目标选择器
 * @author seg
 *
 */
public class LinearMovePolygonTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile data)
	{
		if(polygonPoints == null || polygonPoints.isEmpty() || polygonPoints.size() < 2)
			return false;
		
		if(((GameObject)data).getObjectType() != GameObjectType.SKILL_EFFECT)
			return false;
		
		List<LineSeg> lineSegs = data.getProjectileAttr().getPolygon();
		if(lineSegs == null || lineSegs.isEmpty())
		{
			if(lineSegs == null)
			{
				lineSegs = new ArrayList<LineSeg>();
				data.getProjectileAttr().setPolygon(lineSegs);
			}
			
			GameObject caster = data.getCaster();
			if(caster.getObjectType() != GameObjectType.TROOP)
				return false;
			
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
				return false;
			
			float[] direct = MathUtil.getUnitVector(new float[]{targetX - cx, targetY - cy});
			float cos = direct[0];
			float sin = direct[1];
			
			float maxRadius = 0f;
			for(int i = 0 ; i < polygonPoints.size() ; i++)
			{
				int j = i + 1;
				if(j == polygonPoints.size())
					j = 0;
				
				float distance = (float) Math.sqrt(polygonPoints.get(i)[0] * polygonPoints.get(i)[0] + polygonPoints.get(i)[1] * polygonPoints.get(i)[1]);
				if(distance > maxRadius)
					maxRadius = distance;
				
				LineSeg lineSeg = new LineSeg(polygonPoints.get(i)[0], polygonPoints.get(i)[1], polygonPoints.get(j)[0], polygonPoints.get(j)[1]).rotate(0, 0, cos, sin).transform(cx, cy);
				lineSegs.add(lineSeg);
			}
			
			data.getProjectileAttr().setRadius(maxRadius);
			
			EffectEntity effect = (EffectEntity) data;
			float velocityMod = (effect.getEffectPrototype().getInterval() * effect.getEffectPrototype().getFlySpeed() * 2) / 1000f;
			direct[0] = cos * velocityMod;
			direct[1] = sin * velocityMod;
			
			data.getProjectileAttr().setVelocity(direct);
			data.getProjectileAttr().setX(cx);
			data.getProjectileAttr().setY(cy);
		}
		
		return true;
	}

	@Override
	protected Collection<WarObjectInstance> doFindTarget(Projectile data)
	{
		
		float effectX = data.getProjectileAttr().getX();
		float effectY = data.getProjectileAttr().getY();
		
		WarInstance war = ((GameObject)data).getWar();
		
		Collection<WarObjectInstance> visibles = new ArrayList<WarObjectInstance>();;
		
		float modifiedRadius = data.getProjectileAttr().getRadius();
		
		for(WarObjectInstance obj : WarUtils.getObjects(war))
		{
			if(!MathUtil.isInRange(obj, effectX, effectY, modifiedRadius))
				continue;
			
			if(MathUtil.isPointInFace(obj.getX(), obj.getY(), data.getProjectileAttr().getPolygon()))
			{
				visibles.add(obj);
				continue;
			}
		}
		
//		for(LineSeg lineSeg : data.getProjectileAttr().getPolygon())
//		{
//			System.err.println(lineSeg);
//		}
//		System.err.println(war.getWarScene().print());
		
		float[] velocity = data.getProjectileAttr().getVelocity();
		
		for(LineSeg lineSeg : data.getProjectileAttr().getPolygon())
		{
			lineSeg.transform(velocity[0], velocity[1]);
		}
		
		data.getProjectileAttr().setX(effectX + velocity[0]);
		data.getProjectileAttr().setY(effectY + velocity[1]);
		return visibles;
	}

}
