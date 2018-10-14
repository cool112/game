package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

/**
 * 技能格挡触发器
 * @author seg
 *
 */
public class BlockEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.ATTACK)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		Troop target = (Troop)event.getTarget();
		target.getFightStates().setEffectBlock(true);
		
		return SkillError.SUCCESS;
	}
	
	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.BE_ATTACK_EVENT;
	}
}
