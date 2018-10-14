package com.garowing.gameexp.game.rts.skill.handler.effecthandler;

import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.yeto.war.module.troop.Troop;

/**
 * 入场技能效果处理
 * @author seg
 *
 */
public class EnterSceneEffectHandler extends DefaultEffectHandler
{
	@Override
	public void onStartFail(EffectEntity effect, long currTime)
	{
		super.onStartFail(effect, currTime);
		Troop caster = effect.getCaster();
		caster.getAgent().setEnabled(true);
	}
}
