package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

/**
 * 扇形目标选择器
 * @author seg
 *
 */
public class FanTargetSelector extends TargetSelector
{

	@Override
	public boolean check(Projectile data)
	{
		List<GameObject> targets = data.getTargets();
		if(targets == null || targets.isEmpty() || targets.get(0).getObjectType() != GameObjectType.TROOP)
		{
			float x = data.getTargetX();
			float y = data.getTargetY();
			if(x == 0f && y == 0f)
				return false;
		}
		
		if(this.radius <= 0)
			return false;
		
		if(this.radian <= 0)
			return false;
		
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
		
		float vx = targetX - cx;
		float vy = targetY - cy;
		float cos = (float) Math.cos(radian / 2);
		float sin = (float) Math.sin(radian / 2);
		float x3 = cx + (vx * cos - vy * sin);
		float y3 = cy + (vx * sin + vy * cos);
		float x4 = cx + (vx * cos + vy * sin);
		float y4 = cy + (-vx * sin + vy * cos);
		
		WarInstance war = ((GameObject)data).getWar();
		
		Collection<WarObjectInstance> visibles = null;
		if(radius > 0)
		{
			float modifiedRadius = radius;
			if(caster.getObjectType() == GameObjectType.TROOP && ((GameObject)data).getObjectType() == GameObjectType.SKILL_EFFECT)
			{
				modifiedRadius += ((Troop)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.EFFECT_RADIUS, (EffectEntity) data);
			}
			visibles = WarUtils.getLiveObjectsByRadius(war, cx, cy, modifiedRadius);
		}else{
			visibles = WarUtils.getObjects(war);
		}
		
		if(visibles == null)
			return null;
		
		List<WarObjectInstance> inFanObjs = new ArrayList<WarObjectInstance>();
		for(WarObjectInstance target : visibles)
		{
			float d1 = MathUtil.pointPositionToLine(target.getX(), target.getY(), cx, cy, x3, y3);
			float d2 = MathUtil.pointPositionToLine(target.getX(), target.getY(), x4, y4, cx, cy);
//			System.err.println(target.getX() + "," + target.getY() + "," + cx + "," + cy + "," + x3 + "," + y3 + "," + x4 + "," + y4 +" d1d2:" + d1+"," +d2);
			if (d1 >= 0 && d2 >= 0)
			{
				inFanObjs.add(target);
				continue;
			}
			
			if(d1 < 0 && d2 >= 0 && MathUtil.isCircleIntersectLine(target.getX(), target.getY(), target.getCollisionRadius(), cx, cy, x3, y3))
			{
				inFanObjs.add(target);
				continue;
			}
			
			if(d1 >= 0 && d2 < 0 && MathUtil.isCircleIntersectLine(target.getX(), target.getY(), target.getCollisionRadius(), cx, cy, x4, y4))
			{
				inFanObjs.add(target);
				continue;
			}
		}
		
		return inFanObjs;
	}
	
	
}
