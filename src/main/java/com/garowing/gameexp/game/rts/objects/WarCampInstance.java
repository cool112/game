package com.garowing.gameexp.game.rts.objects;

import java.util.Collection;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.Map;

import com.garowing.gameexp.game.rts.objects.model.WarCampModel;

/**
 * 战争阵营对象
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月10日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public final class WarCampInstance extends GameObject
{

	// 战争阵营Model
	private final WarCampModel model;
	
	/**
	 * 阵营下控制器集合
	 */
	private Map<Integer, WarControlInstance> controlMap = new HashMap<Integer, WarControlInstance>();
	
	public WarCampInstance (WarInstance war, WarCampModel model)
	{
		super(war);
		this.model = model;
	}
	
	/**
	 * 增加控制器
	 * @param control
	 */
	public void addControl(WarControlInstance control)
	{	int controlId = control.getObjectId();
		if(controlMap.containsKey(controlId))
		{
			throw new DuplicateFormatFlagsException("重复的控制器添加，Controller:["+ control.getName() +"]");
		}
		
		control.setCamp(this);
		
		controlMap.put(controlId, control);
	}
	
	/**
	 * 控制器集合
	 * @return
	 */
	public Collection<WarControlInstance> getAllControllers()
	{
		return controlMap.values();
	}
	
	@Override
	public String getName() {
		return "camp: [type:" + model.getCampType() + ", num:" + model.getCampNumb() + ", id:" + getObjectId() + "]";
	}
	
	/******************** get/set方法 ********************/
	
	public WarCampModel getModel() {
		return model;
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.CAMP;
	}

}
