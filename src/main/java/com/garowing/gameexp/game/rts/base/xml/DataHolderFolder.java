package com.garowing.gameexp.game.rts.base.xml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 将一个文件夹下的XML读取到一个XML中，并进行原型数据的映射
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月11日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataHolderFolder
{
	public static final String	DEFAULT_VALUE	= "";
	
	/**
	 * 合并那个目录的文件
	 * @return 返回目录名
	 */
	public String folderName();
	
	
	/**
	 * 存储的文件，并读取
	 * @return
	 */
	public String fileName() default DEFAULT_VALUE;
	
	/**
	 * 是否递归
	 * @return
	 */
	public boolean recursive() default false;
	
}
