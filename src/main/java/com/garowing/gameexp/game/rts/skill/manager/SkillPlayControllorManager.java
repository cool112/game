package com.garowing.gameexp.game.rts.skill.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.script.playControl.skillplay.FixTimeSkillPlayControllor;
import com.garowing.gameexp.game.rts.skill.template.SkillPlayControllor;

/**
 * 技能播放控制管理
 * @author seg
 * 2017年3月15日
 */
public class SkillPlayControllorManager
{
	private final static Logger log = Logger.getLogger(SkillPlayControllorManager.class);
	
	private Map<String, Class<? extends SkillPlayControllor>> maps = new HashMap<String, Class<? extends SkillPlayControllor>>();

	private SkillPlayControllorManager()
	{
		// 注册目标模板	
		addTemplate(FixTimeSkillPlayControllor.class);
	}
	
	/**
	 * 增加一个技能目标脚本
	 * @param clazz
	 * @create	2015年3月25日	darren.ouyang
	 */
	private void addTemplate(Class<? extends SkillPlayControllor> clazz)
	{
		ScriptName name = clazz.getAnnotation(ScriptName.class);
		if(name == null)
		{
			log.warn("技能播放控制模板:" + clazz.getName() + "没有注解ID。");
			return;
		}
		
		@SuppressWarnings("unused")
		SkillPlayControllor action = null;
		try 
		{
			action = clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new Error();
		}
		maps.put(name.name(), clazz);
	}
	
	/**
	 * 创建一个目标模板实例
	 * @param name
	 * @return
	 */
	public SkillPlayControllor createTemplate(String name)
	{
		Class<? extends SkillPlayControllor> action = maps.get(name);
		if(action == null)
			throw new NotFoundEffectTemplateException();
		
		try
		{
			return action.newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		throw new NotFoundEffectTemplateException();
	}
	
	public static SkillPlayControllorManager getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected final static SkillPlayControllorManager instance = new SkillPlayControllorManager();
	}
}
