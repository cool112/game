package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;

/**
 * 中断效果事件处理
 * @author seg
 *
 */
public class InterrupEffectSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.INTERRUPT)
			return;
		
		InterruptEffectSkillEvent interruptEvent = (InterruptEffectSkillEvent) event;
		EffectEntity target = (EffectEntity) interruptEvent.getTarget();
		if(target == null)
			return;
		
		if(target.getState() == SkillManager.IDLE)
			return;
		
		target.setState(SkillManager.END);
		target.setEndTime(currTime);
		target.setForceEnd(interruptEvent.isForce());
	}

}
