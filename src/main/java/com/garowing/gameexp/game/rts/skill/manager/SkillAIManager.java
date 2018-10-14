package com.garowing.gameexp.game.rts.skill.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.script.skillai.AlliedSkillAIObj;
import com.garowing.gameexp.game.rts.skill.script.skillai.AoeSkillAIObj;
import com.garowing.gameexp.game.rts.skill.script.skillai.ChargeSkillAIObj;
import com.garowing.gameexp.game.rts.skill.script.skillai.CureSkillAIObj;
import com.garowing.gameexp.game.rts.skill.script.skillai.LowHp20SkillAIObj;
import com.garowing.gameexp.game.rts.skill.script.skillai.SummonSkillAIObj;
import com.garowing.gameexp.game.rts.skill.script.skillai.TargetLowHp20SkillAIObj;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;

/**
 * 技能ai管理器
 * @author seg
 *
 */
public class SkillAIManager
{

	private final static Logger log = Logger.getLogger(SkillAIManager.class);
	
	private Map<String, SkillAIObj> maps = new HashMap<String, SkillAIObj>();

	private SkillAIManager()
	{
		// 注册目标模板	
		addTemplate(SummonSkillAIObj.class);
		addTemplate(AoeSkillAIObj.class);
		addTemplate(LowHp20SkillAIObj.class);
		addTemplate(AlliedSkillAIObj.class);
		addTemplate(ChargeSkillAIObj.class);
		addTemplate(CureSkillAIObj.class);
		addTemplate(TargetLowHp20SkillAIObj.class);
	}
	
	/**
	 * 增加一个技能目标脚本
	 * @param clazz
	 * @create	2015年3月25日	darren.ouyang
	 */
	private void addTemplate(Class<? extends SkillAIObj> clazz)
	{
		ScriptName name = clazz.getAnnotation(ScriptName.class);
		if(name == null)
		{
			log.warn("技能目标模板:" + clazz.getName() + "没有注解ID。");
			return;
		}
		
		SkillAIObj action = null;
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
	 */
	public SkillAIObj createTemplate(String name)
	{
		SkillAIObj action = maps.get(name);
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
	
	public SkillAIObj getSkillAI(String name)
	{
		return maps.get(name);
	}
	
	public static SkillAIManager getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected final static SkillAIManager instance = new SkillAIManager();
	}


}
