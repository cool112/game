package com.garowing.gameexp.game.rts.skill.script.playControl.effectplay;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.scene.objects.WarSceneObject;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.EffectPlayControllor;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.MathUtil;

/**
 * 线性移动效果播放器
 * @author seg
 * 2017年4月1日
 */
@ScriptName(name = "linearMoveEp")
public class LinearMoveEffectPlayControllor extends EffectPlayControllor
{

	@Override
	public long getWaitTime(EffectEntity effect, long currTime)
	{
//		long execDealy = effect.getEffectPrototype().getExecDelay();
		long execDealy = effect.getRealExecDelay();
		execDealy = execDealy < 0 ? 0 : execDealy;
		long waitTime = effect.getStartTime() + execDealy - currTime;
//		GameObject caster = effect.getCaster();
//		if(caster != null && caster.getObjectType() == GameObjectType.TROOP)
//		{
//			float reduceCoe = 0f;
//			reduceCoe = ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.CD_ADD, effect.getSkillModelId());
//			waitTime = effect.getStartTime() + (long)(execDealy * (1 + reduceCoe)) -currTime;
//		}
		
		float flySpeed = effect.getEffectPrototype().getFlySpeed();
		if(flySpeed <= 0)
			return waitTime;
		
		Troop troop = (Troop) effect.getCaster();
		float x = troop.getX();
		if(x == 0)
			return waitTime;
		
		float y = troop.getY();
		if(y == 0)
			return waitTime;
		
		List<GameObject> targets = effect.getTargets();
		float targetX =  effect.getTargetX();
		if(targetX <= 0)
			if(!targets.isEmpty())
				targetX = ((WarSceneObject)targets.get(0)).getX();
		
		float targetY =  effect.getTargetY();
		if(targetY <= 0)
			if(!targets.isEmpty())
				targetY = ((WarSceneObject)targets.get(0)).getY();
		
		float distance = MathUtil.getDistance(x, y, targetX, targetY);
		long flyTime = (long)((distance/(flySpeed * 2)) * 1000);
//		System.err.println("effect flyTime[" + flyTime +"] distance[" + distance +"]");
		
		return waitTime + flyTime;
	}

	@Override
	public long getInitDelay(EffectEntity effect, long currTime)
	{
		long initTime = effect.getCreateTime() + effect.getEffectPrototype().getInitDelay() - currTime;
		if(initTime <= 0)
			return 0;
		
		return initTime;
	}

}
