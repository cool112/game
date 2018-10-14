package com.garowing.gameexp.game.rts.skill.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.script.playControl.effectplay.FixTimeEffectPlayControllor;
import com.garowing.gameexp.game.rts.skill.script.playControl.effectplay.LinearMoveEffectPlayControllor;
import com.garowing.gameexp.game.rts.skill.script.playControl.effectplay.WaitClientEffectPlayControllor;
import com.garowing.gameexp.game.rts.skill.template.EffectPlayControllor;

/**
 * 效果播放控制管理器
 * @author seg
 * 2017年4月2日
 */
public class EffectPlayControllorManager
{
	
	private final static Logger log = Logger.getLogger(EffectPlayControllorManager.class);
	
	private Map<String, EffectPlayControllor> maps = new HashMap<String, EffectPlayControllor>();

	private EffectPlayControllorManager()
	{
		// 注册目标模板	
		addTemplate(LinearMoveEffectPlayControllor.class);
		addTemplate(FixTimeEffectPlayControllor.class);
		addTemplate(WaitClientEffectPlayControllor.class);
	}
	
	/**
	 * 增加一个技能目标脚本
	 * @param clazz
	 * @create	2015年3月25日	darren.ouyang
	 */
	private void addTemplate(Class<? extends EffectPlayControllor> clazz)
	{
		ScriptName name = clazz.getAnnotation(ScriptName.class);
		if(name == null)
		{
			log.warn("技能目标模板:" + clazz.getName() + "没有注解ID。");
			return;
		}
		
		EffectPlayControllor action = null;
		try 
		{
			action = clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new Error();
		}
		maps.put(name.name(), action);
	}
	
	/**
	 * 创建一个目标模板实例
	 * @param name
	 * @return
	 * @create	2015年3月25日	darren.ouyang
	 */
	public EffectPlayControllor createTemplate(String name)
	{
		EffectPlayControllor action = maps.get(name);
		if(action == null)
			throw new NotFoundEffectTemplateException();
		
		try
		{
			return action.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		throw new NotFoundEffectTemplateException();
	}
	
	public EffectPlayControllor getEffectPlayControllor(String name)
	{
		return maps.get(name);
	}
	
	public static EffectPlayControllorManager getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected final static EffectPlayControllorManager instance = new EffectPlayControllorManager();
	}

}
