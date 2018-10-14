package com.garowing.gameexp.game.rts.ai.behaviac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代理类
 * @author seg
 * 2017年1月7日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgentClass
{
	/**
	 * 名称如果是默认值则取javabean的属性名
	 * @return
	 */
	String name() default "##default";
}
