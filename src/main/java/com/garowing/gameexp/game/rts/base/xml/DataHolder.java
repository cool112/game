package com.garowing.gameexp.game.rts.base.xml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 原型数据读取注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataHolder
{
	public static final String	DEFAULT_VALUE	= "DO_NOT_OVERWRITE_INITIALIAZION_VALUE";
	
	/**
	 * 读取的文件路径
	 * @return  返回文件的路径名
	 */
	public String name();

}
