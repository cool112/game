package com.garowing.gameexp.game.rts.skill.manager;

/**
 * 没有找到技能效果模板异常
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月24日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class NotFoundEffectTemplateException extends RuntimeException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5552352411993476951L;

	public NotFoundEffectTemplateException() {
		super();
	}

	public NotFoundEffectTemplateException(String s) 
	{
		super(s);
	}

	public NotFoundEffectTemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotFoundEffectTemplateException(Throwable cause) 
	{
		super(cause);
	}
}
