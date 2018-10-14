package com.garowing.gameexp.game.rts.skill.handler;

import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;

/**
 * 技能效果处理
 * @author seg
 *
 */
public interface SkillEffectHandler
{
	/**
	 * 开始成功
	 * @param effect
	 */
	public void onStartSuc(EffectEntity effect, long currTime);
	
	/**
	 * 开始失败
	 * @param effect
	 */
	public void onStartFail(EffectEntity effect, long currTime);
	
	/**
	 * 激活成功
	 * @param effect
	 */
	public void onActivateSuc(EffectEntity effect, long currTime);
	
	/**
	 * 激活失败
	 * @param effect
	 */
	public void onActivateFail(EffectEntity effect, long currTime);
}
