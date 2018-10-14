package com.garowing.gameexp.game.rts.base.xml;

/**
 * 使用DataHolderFolder注解时，指定的路径为空时，抛出此异常
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月11日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class DataHolderFolderNullException extends Error 
{

	private static final long serialVersionUID = -5063068978058746170L;
	
	
	public DataHolderFolderNullException()
	{
		super();
	}
	
	public DataHolderFolderNullException(String msg)
	{
		super(msg);
	}
	
	
	public DataHolderFolderNullException(Throwable t)
	{
		super(t);
	}
	
	
	
	public DataHolderFolderNullException(String msg, Throwable t)
	{
		super(msg, t);
	}
	
}
