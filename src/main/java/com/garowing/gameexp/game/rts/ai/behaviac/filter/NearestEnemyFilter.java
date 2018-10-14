package com.garowing.gameexp.game.rts.ai.behaviac.filter;

import com.garowing.gameexp.game.rts.ai.behaviac.constants.TargetFilterType;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.utils.MathUtil;

/**
 * 最近敌方目标过滤器
 * @author seg
 * 2017年1月5日
 */
public class NearestEnemyFilter extends AbstractTargetFilter
{

	@Override
	public int getType()
	{
		return TargetFilterType.NEAREST_ENEMY.getCode();
	}

	@Override
	protected WarObjectInstance compareImpl(WarObjectInstance currentTarget, WarObjectInstance preTarget,
			WarObjectInstance onwer, int controlId)
	{
		float curTargetDistance = MathUtil.getDistance(currentTarget, onwer);
		float preTargetDistance = MathUtil.getDistance(preTarget, onwer);
		if(curTargetDistance < preTargetDistance)
			return currentTarget;
		
		return preTarget;
	}

	@Override
	protected boolean filerImpl(WarObjectInstance warInstance, WarObjectInstance onwer, int controlId)
	{
		if(warInstance.getCampId() != onwer.getCampId())
			return true;
		
		return false;
	}

}
