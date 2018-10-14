package com.garowing.gameexp.game.rts.skill.constants;

import com.garowing.gameexp.game.rts.skill.script.targetselector.CastRangeTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.CircleRandomPointSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.CircleRandomTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.CircleTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.CurrentUnitTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.DefaultTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.FanTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.GlobalRandomTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.GlobalTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.LinearMovePolygonTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.PolygonTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.RingTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.SelfCircleRandomTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.SelfCircleTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.SelfTargetSelector;
import com.garowing.gameexp.game.rts.skill.script.targetselector.UnitParentTargetSelector;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;

/**
 * 目标选择器类型
 * @author seg
 *
 */
public enum TargetSelectorType
{
	/**
	 * 无，返回当前目标
	 */
	NONE				(0, DefaultTargetSelector.class),
	
	/**
	 * 圆形，参数优先级：
	 * <p>1、如果有目标单位，圆心是目标，多目标只取第一个
	 * <p>2、如果有坐标，圆心是坐标
	 */
	CIRCLE				(1, CircleTargetSelector.class),
	
	/**
	 * 施法者为目标
	 */
	SELF				(2, SelfTargetSelector.class),
	
	/**
	 * 全局选择
	 */
	GLOBAL				(3, GlobalTargetSelector.class),
	
	/**
	 * 当前目标只允许怪物选择器
	 */
	CURRENT_UNIT 		(4, CurrentUnitTargetSelector.class),
	
	/**
	 * 扇形选择器
	 */
	FAN 				(5, FanTargetSelector.class),
	
	/**
	 * 多边形选择器
	 */
	POLYGON 			(6, PolygonTargetSelector.class),
	
	/**
	 * 圆形随机选择器
	 */
	CIRCLE_RANDOM 		(7, CircleRandomTargetSelector.class),
	
	/**
	 * 环状目标选择器
	 */
	RING				(8, RingTargetSelector.class),
	
	/**
	 * 自己为圆心选择器
	 */
	SELF_CIRCLE			(9, SelfCircleTargetSelector.class),
	
	/**
	 * 线性移动多边形选择器
	 */
	LINEAR_MOVE_POLYGON	(10, LinearMovePolygonTargetSelector.class),
	
	/**
	 * 圆形范围随机点选择器（需要注意这些点对象的回收）
	 */
	CIRCLE_RANDOM_POINT	(11, CircleRandomPointSelector.class),
	
	/**
	 * 施法距离内怪物选择器
	 */
	CAST_RANGE_UNIT		(12, CastRangeTargetSelector.class),
	
	/**
	 * 全局随机目标选择器
	 */
	GLOBAL_RANDOM		(13, GlobalRandomTargetSelector.class),
	
	/**
	 * 自我圆心随机目标选择器
	 */
	SELF_CIRCLE_RANDOM	(14, SelfCircleRandomTargetSelector.class),
	
	/**
	 * 单位召唤者选择器
	 */
	UNIT_PARENT			(15, UnitParentTargetSelector.class);
	
	
	/**
	 * 编号
	 */
	private int code;
	
	/**
	 * 实现类
	 */
	private Class<? extends TargetSelector> implClass;

	private TargetSelectorType(int code, Class<? extends TargetSelector> implClass)
	{
		this.code = code;
		this.implClass = implClass;
	}
	
	/**
	 * 根据id获取枚举
	 * @param id
	 * @return
	 */
	public static TargetSelectorType getTypeById(int id)
	{
		TargetSelectorType[] types = TargetSelectorType.values();
		for(TargetSelectorType type : types)
		{
			if(type.getCode() == id)
				return type;
		}
		
		return null;
	}

	public int getCode()
	{
		return code;
	}

	public Class<? extends TargetSelector> getImplClass()
	{
		return implClass;
	}
	
	
}
