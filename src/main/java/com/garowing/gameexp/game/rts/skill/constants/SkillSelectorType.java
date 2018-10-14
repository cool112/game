package com.garowing.gameexp.game.rts.skill.constants;

import com.garowing.gameexp.game.rts.skill.script.skillselector.DefaultSkillSelector;
import com.garowing.gameexp.game.rts.skill.template.SkillSelector;

/**
 * 技能选择器类型
 * @author seg
 *
 */
public enum SkillSelectorType
{
	/**
	 * 默认
	 */
	DEFAULT(0, new DefaultSkillSelector());
	
	/**
	 * 编号
	 */
	private int code;
	
	/**
	 * 技能选择器
	 */
	private SkillSelector skillSelector;

	private SkillSelectorType(int code, SkillSelector skillSelector)
	{
		this.code = code;
		this.skillSelector = skillSelector;
	}
	
	/**
	 * 根据id获取类型
	 * @param id
	 * @return
	 */
	public static SkillSelectorType getTypeById(int id)
	{
		SkillSelectorType[] types = SkillSelectorType.values();
		for(SkillSelectorType type : types)
		{
			if(type.getCode() == id)
				return type;
		}
		
		return null;
	}
	
	/**
	 * 根据id获取选择器
	 * @param id
	 * @return
	 */
	public static SkillSelector getSelectorById(int id)
	{
		SkillSelectorType[] types = SkillSelectorType.values();
		for(SkillSelectorType type : types)
		{
			if(type.getCode() == id)
				return type.getSkillSelector();
		}
		
		return null;
	}

	public int getCode()
	{
		return code;
	}

	public SkillSelector getSkillSelector()
	{
		return skillSelector;
	}
	
	
}
