package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;

/**
 * 刷新效果持续时间事件处理
 * @author seg
 *
 */
public class RefreshEffectSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.REFRESH_EFFECT)
			return;
		
		EffectEntity effect = (EffectEntity) event.getTarget();	
		if(effect.getState() != SkillManager.ACTIVATE)
			return;
		
		effect.setActivateCount(0);
	}

}
