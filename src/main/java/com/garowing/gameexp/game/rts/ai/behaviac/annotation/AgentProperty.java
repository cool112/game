package com.garowing.gameexp.game.rts.ai.behaviac.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代理属性
 * @author seg
 * 2017年1月5日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgentProperty
{
	String name() default "##default";
}
