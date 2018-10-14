package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.garowing.gameexp.game.rts.skill.constants.SkillFuncType;

/**
 * 脚本名称
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月24日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScriptName 
{
	String name();
	
	/**
	 * 技能类型
	 * @return
	 */
	SkillFuncType type() default SkillFuncType.NONE;
}
