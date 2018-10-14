package com.garowing.gameexp.game.rts.skill.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.TargetSelectorKey;
import com.garowing.gameexp.game.rts.skill.filter.unit.UnitPropsFilter;
import com.garowing.gameexp.game.rts.skill.manager.ListPointPropertyTransformer;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

import commons.configuration.Property;

/**
 * 目标选择器
 * @author seg
 *
 */
public abstract class TargetSelector
{
	private static final Logger mLogger = Logger.getLogger(TargetSelector.class);

	/**
	 * 选择器modelId
	 */
	@Property(key = TargetSelectorKey.ID, defaultValue = "0")
	protected int modelId;
	
	/**
	 * 最大目标数
	 */
	@Property(key = TargetSelectorKey.MAX_TARGET, defaultValue = "0")
	protected int maxTarget;
	
	/**
	 * 是否锁定目标
	 */
	@Property(key = TargetSelectorKey.IS_LOCKED, defaultValue = "false")
	protected boolean isLocked;
	
	/**
	 * 半径
	 */
	@Property(key = TargetSelectorKey.RADIUS, defaultValue = "0")
	protected float radius;
	
	/**
	 * 弧度
	 */
	@Property(key = TargetSelectorKey.RADIAN, defaultValue = "0")
	protected float radian;
	
	/**
	 * 过滤器id
	 */
	@Property(key = TargetSelectorKey.FILTERID, defaultValue = "0")
	protected int filterId;
	
	/**
	 * 多边形点集
	 */
	@Property(key = TargetSelectorKey.POLYGON_POINTS, propertyTransformer = ListPointPropertyTransformer.class)
	protected List<float[]> polygonPoints;
	
	/**
	 * 是否重复目标
	 */
	@Property(key = TargetSelectorKey.IS_REPEAT, defaultValue = "false")
	protected boolean isRepeat;
	
	/**
	 * 内半径
	 */
	@Property(key = TargetSelectorKey.INSIDE_RADIUS, defaultValue = "0")
	protected float insideRadius;
	
	/**
	 * 检测参数是否合法
	 * @return
	 */
	public abstract boolean check (Projectile data);
	
	/**
	 * 查找目标集合
	 * @return
	 */
	protected abstract Collection<WarObjectInstance> doFindTarget (Projectile data);

	/**
	 * 查找目标集合
	 * @return
	 */
	public List<GameObject> findTarget (Projectile data)
	{
		Collection<WarObjectInstance> rawData = doFindTarget(data);
		if (rawData == null || rawData.isEmpty())
			return Collections.emptyList();
		
		return universallyProcess(data, rawData);
	}
	
	/**
	 * 通用处理
	 * @param data
	 * @param rawData
	 * @return
	 */
	protected List<GameObject> universallyProcess(Projectile data, Collection<WarObjectInstance> rawData)
	{
		if(filterId > 0)
		{
			UnitPropsFilter filter = StaticDataManager.PROPS_FILTER_DATA.getFilter(filterId);
			if(filter == null)
				mLogger.warn("can't find filter ! selectorId:[" + modelId + "] filterId[" + filterId + "]");
			else
			{
				GameObject caster = data.getCaster();
				if(caster != null)
					rawData = filter.filter(data, caster, rawData);
			}
				
		}
		
		if(maxTarget > 0 )
		{
			if(maxTarget < rawData.size())
			{
				Iterator<WarObjectInstance> iterator = rawData.iterator();
				int index = 0;
				while(iterator.hasNext())
				{
					iterator.next();
					index ++;
					if(index > maxTarget)
						iterator.remove();
				}
			}
			else
			{
				if(isRepeat && !rawData.isEmpty())
				{
					WarObjectInstance firstEl = rawData.iterator().next();
					int originSize = rawData.size();
					for(int i = 0 ; i < maxTarget - originSize ; i++)
						rawData.add(firstEl);
				}
			}
			
		}
		
		return new ArrayList<GameObject>(rawData);
	}

	public float getRadius()
	{
		return radius;
	}
	
}
