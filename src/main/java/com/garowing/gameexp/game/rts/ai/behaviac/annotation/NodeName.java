package com.garowing.gameexp.game.rts.ai.behaviac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 节点名称
 * @author seg
 * 2017年4月7日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeName
{
	/**
	 * 名称
	 * @return
	 */
	String name();
}
