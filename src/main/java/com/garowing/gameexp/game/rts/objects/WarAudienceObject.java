package com.garowing.gameexp.game.rts.objects;

/**
 * 场景外对象
 * @author seg
 * 2017年1月18日
 */
public abstract class WarAudienceObject extends GameObject implements HavingCamp
{
	/**
	 * 控制器
	 */
	private WarControlInstance control;

	public WarAudienceObject(WarInstance war, WarControlInstance control)
	{
		super(war);
		this.control = control;
	}

	public WarControlInstance getControl()
	{
		return control;
	}

	public void setControl(WarControlInstance control)
	{
		this.control = control;
	}
	
	@Override
	public int getCampId()
	{
		return control.gainCampID();
	}
}
