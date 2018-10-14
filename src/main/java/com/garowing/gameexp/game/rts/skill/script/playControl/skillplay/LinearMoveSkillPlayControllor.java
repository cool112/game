package com.garowing.gameexp.game.rts.skill.script.playControl.skillplay;

import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.scene.objects.WarSceneObject;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.SkillPlayControllor;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

import commons.configuration.Property;

/**
 * 线性移动播放控制器
 * @author seg
 * 2017年3月28日
 */
@ScriptName(name = "linearMoveSp")
public class LinearMoveSkillPlayControllor extends SkillPlayControllor
{
	/**
	 * 移动速度
	 */
	@Property(key="moveSpeed", defaultValue="20")
	private float moveSpeed;
	
	/**
	 * 初始化延迟
	 */
	@Property(key="initDelay", defaultValue="0")
	private int initDelay;
	
	/**
	 * 预测时间
	 */
	private long predicted;
	
	@Override
	public long getWaitTime(SkillEntity skill, long currTime)
	{
		long remainTime = predicted - currTime;
		if(remainTime < 0)
			remainTime = 0;
		
		return remainTime;
	}

	@Override
	public long getInitDelay(SkillEntity skill, long currTime)
	{
		int type = skill.getCaster().getObjectType();
		if(type != GameObjectType.TROOP)
		{
			predicted = currTime + initDelay;
			return initDelay;
		}
			
		
		Troop troop = (Troop) skill.getCaster();
		float x = skill.getProjectileAttr().getX();
		if(x == 0)
			x = troop.getX();
		
		float y = skill.getProjectileAttr().getY();
		if(y == 0)
			x = troop.getY();
		
		float targetX = skill.getTargetX();
		if(targetX == 0)
			targetX = ((WarSceneObject)skill.getTargets().get(0)).getX();
		
		float targetY = skill.getTargetY();
		if(targetY == 0)
			targetY = ((WarSceneObject)skill.getTargets().get(0)).getY();
		
		float distance = MathUtil.getDistance(x, y, targetX, targetY);
		long flyTime = (long)( distance / moveSpeed * 2) * 1000;
//		System.err.println("flyTime[" + flyTime +"] distance[" + distance +"]");
		
		predicted = currTime + initDelay + flyTime;
		
		return initDelay;
	}

}
