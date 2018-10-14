package com.garowing.gameexp.game.rts.listener.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 战斗处理脚本ID
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月14日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HandlerID 
{

	int value();
}

