package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;

/**
 * 重启效果事件处理
 * @author seg
 *
 */
public class RestartEffectSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.RESTART_EFFECT)
			return;
		
		EffectEntity effect = (EffectEntity) event.getTarget();	
		if(effect.getState() == SkillManager.IDLE)
			return;
		
		effect.getEffectPrototype().onExit(effect);
		effect.setActivateCount(0);
		effect.setState(SkillManager.ACTIVATE);
		effect.getEffectPrototype().onStart(effect, currTime);
	}

}
