package com.garowing.gameexp.game.rts.skill.script.effect.trigger;

import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.EnterSceneSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEffectHandler;
import com.garowing.gameexp.game.rts.skill.handler.effecthandler.EnterSceneEffectHandler;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

/**
 * 进入战场效果触发器
 * @author seg
 *
 */
public class EnterSceneEffectTriggerPrototype extends AbstractEffectTriggerPrototype
{
	/**
	 * 入场效果处理，释放失败ai优化
	 */
	public static final SkillEffectHandler ENTER_SCENE_EFFECT_HANDLER = new EnterSceneEffectHandler();
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		SkillError result = super.onStart(effect, currTime);
		if(result == SkillError.SUCCESS)
		{
			/* 保证出场触发技能在嵌套、后加的情况释放 */
			effect.getWar().getSkillManager().addSkillEvent(new EnterSceneSkillEvent(effect.getWar(), effect.getCaster()));
		}
			
		return result;
	}
	@Override
	public SkillError handleEventImpl(AbstractSkillEvent event, EffectEntity effect, long currTime)
	{
		if(event.getEventType() != SkillEventType.ENTER_SCENE)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		Troop target = (Troop)event.getSource();
		for(Integer effectModelId : subEffectIds)
		{
			SkillEngine.castTmpEffect(effect, target, effectModelId, currTime, ENTER_SCENE_EFFECT_HANDLER, false);
		}
		return SkillError.SUCCESS;
	}

	@Override
	public TriggerEventType getEventType()
	{
		return TriggerEventType.START_FIGTH_EVENT;
	}
	
}
