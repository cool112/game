package com.garowing.gameexp.game.rts.ai.behaviac.filter;

import java.util.Collection;

import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 目标过滤器
 * @author seg
 * 2017年1月5日
 */
public abstract class AbstractTargetFilter
{
	/**
	 * 获取类型码
	 * @return
	 */
	public abstract int getType();
	
	/**
	 * 获取目标
	 * @param onwer
	 * @param controlId
	 * @return
	 */
	public WarObjectInstance getTarget(Collection<WarObjectInstance> warInstanceSet, WarObjectInstance onwer, int controlId)
	{
		if(warInstanceSet == null)
			return null;
					
		WarObjectInstance target = null;
		for(WarObjectInstance warInstance : warInstanceSet)
		{
			if(filerImpl(warInstance, onwer, controlId))
				target = compareImpl(warInstance, target, onwer, controlId);
		}
		
		return target;
	}

	/**
	 * 比较器实现
	 * @param currentTarget
	 * @param preTarget
	 * @return
	 */
	protected abstract WarObjectInstance compareImpl(WarObjectInstance currentTarget, WarObjectInstance preTarget, WarObjectInstance onwer, int controlId);

	/**
	 * 过滤器实现
	 * @param warInstance
	 * @param controlId
	 * @return
	 */
	protected abstract boolean filerImpl(WarObjectInstance warInstance, WarObjectInstance onwer, int controlId);
}
