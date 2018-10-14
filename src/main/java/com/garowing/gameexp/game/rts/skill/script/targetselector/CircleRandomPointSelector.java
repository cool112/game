package com.garowing.gameexp.game.rts.skill.script.targetselector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.yeto.war.fightcore.scene.objects.WarSceneObject;
import com.yeto.war.fightcore.scene.objects.WarScenePointObj;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.filter.unit.UnitPropsFilter;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.utils.RandomUtil;

/**
 * 圆形区域随机点选择器
 * @author seg
 *
 */
public class CircleRandomPointSelector extends CircleTargetSelector
{
	private static final Logger mLogger = Logger.getLogger(CircleRandomPointSelector.class);
	
	@Override
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
		
		//TODO 是否可以是伤害过载目标，随机，可重复
		if(maxTarget > 0 )
		{
			if(maxTarget < rawData.size())
			{
				int[] selectedIndex = RandomUtil.newInstance().noRepeatXNum(0, rawData.size(), maxTarget);
				Iterator<WarObjectInstance> iterator = rawData.iterator();
				int index = 0;
				while(iterator.hasNext())
				{
					iterator.next();
					if(!contains(index, selectedIndex))
						iterator.remove();
					
					index ++;
				}
			}
			else
			{
				if(isRepeat && !rawData.isEmpty())
				{
					int originSize = rawData.size();
					for(int i = 0 ; i < maxTarget - originSize ; i++)
					{
						int randIndex = RandomUtil.newInstance().nextInt(originSize);
						rawData.add(((ArrayList<WarObjectInstance>)rawData).get(randIndex));
					}
				}
				else if(!isRepeat)
				{
					WarInstance war = ((GameObject)data).getWar();
					List<WarSceneObject> points = new ArrayList<WarSceneObject>(rawData);
					int originSize = rawData.size();
					for(int i = 0 ; i < maxTarget - originSize; i++)
					{
						WarScenePointObj point = getNewPoint(war, points, insideRadius);
						if(point != null)
							points.add(point);
					}
					
					return new ArrayList<GameObject>(points);
				}
			}
			
		}
		
		return new ArrayList<GameObject>(rawData);
	}
	
	/**
	 * 获取一个新的点
	 * @param war 
	 * @param points
	 * @param radius
	 * @return
	 */
	private WarScenePointObj getNewPoint(WarInstance war, List<WarSceneObject> points, float radius)
	{
		float[] point = WarSceneService.getInstance().findPointNearPoints(war, points, radius);
		if(point != null)
			return new WarScenePointObj(war);
		
		return null;
	}

	/**
	 * 是否包含
	 * @param index
	 * @param selecteds
	 * @return
	 */
	private boolean contains(int index, int[] selecteds)
	{
		for(Integer selected : selecteds)
		{
			if(index == selected.intValue())
				return true;
		}
		
		return false;
	}

}
