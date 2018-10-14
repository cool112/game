package com.garowing.gameexp.game.rts.skill;

import com.garowing.gameexp.game.rts.module.strategos.StrategosSkillModel;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;

/**
 * 技能触发工厂
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月27日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public final class Skill2TriggerFactory 
{
	
	public static void onCastStrategosSkill(AbsFightControl control, StrategosSkillModel strategosSkill)
	{
		
	}

	public static boolean isDieRecoverHp(WarObjectInstance ownerObject)
	{
		return false;
	}

	public static int triggerInitAddFood(AbsFightControl control)
	{
		return 0;
	}
	
}

