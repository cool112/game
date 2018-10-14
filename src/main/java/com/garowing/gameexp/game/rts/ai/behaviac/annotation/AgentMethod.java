package com.garowing.gameexp.game.rts.ai.behaviac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代理方法
 * @author seg
 * 2017年1月5日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgentMethod
{
	String name() default "##default";
}
