package com.garowing.gameexp.game.rts.ai.behaviac.entity;

/**
 * 抽象标签
 * @author seg
 * 2017年1月6日
 */
public abstract class AbstractTag
{
	/**
	 * 实例类型 Self 或类名（静态）
	 */
	protected String instance;
	
	/**
	 * 类名
	 */
	protected String className;
	
	/**
	 * 名称
	 */
	protected String name;
	
	/**
	 * 是否静态
	 */
	protected boolean isStatic;
	
	/**
	 * 是否常量
	 */
	protected boolean isConst;
	
	/**
	 * 返回值，如果有的话
	 * @return
	 */
	public abstract <T> T rt(Object obj);
	
	/**
	 * 获取返回值类型
	 * @return
	 */
	public abstract String getRtType();
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(instance).append('.').append(className).append('.').append(name);
		return sb.toString();
	}

	public String getInstance()
	{
		return instance;
	}

	public void setInstance(String instance)
	{
		this.instance = instance;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isStatic()
	{
		return isStatic;
	}

	public void setStatic(boolean isStatic)
	{
		this.isStatic = isStatic;
	}

	public boolean isConst()
	{
		return isConst;
	}

	public void setConst(boolean isConst)
	{
		this.isConst = isConst;
	}
	
	
}
