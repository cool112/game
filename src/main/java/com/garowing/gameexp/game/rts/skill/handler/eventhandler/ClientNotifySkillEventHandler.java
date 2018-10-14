package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.ClientNotifySkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 客户端通知事件处理
 * @author seg
 *
 */
public class ClientNotifySkillEventHandler implements SkillEventHandler
{
	private static final Logger mLogger = Logger.getLogger(ClientNotifySkillEventHandler.class);

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.CLIENT_NOTIFY)
			return;
		
		ClientNotifySkillEvent clientNotifyEvent = (ClientNotifySkillEvent) event;
		WarInstance war = event.getWar();
		EffectEntity effect = (EffectEntity) war.getObject(clientNotifyEvent.getParams().getEffectInstanceId());
//		System.err.println("instanceId " + clientNotifyEvent.getParams().getEffectInstanceId() + " modelId " + clientNotifyEvent.getParams().getCurrentEffectModelId());
		if(effect == null)
			return;
		
		switch (clientNotifyEvent.getNoticeType())
		{
		case SkillManager.START:
			if (effect.getState() != SkillManager.START)
				return;
			
			SkillError errorCode = effect.getEffectPrototype().onStart(effect, currTime);
			if(errorCode != SkillError.SUCCESS)
			{
				effect.setState(SkillManager.END);
				effect.setEndTime(currTime);
				mLogger.warn("effect execute fail! ec:[" + errorCode.info + "] effectId:["+effect.getEffectPrototype().getModelId() +"]");
			}
			else
			{
				effect.setState(SkillManager.ACTIVATE);
				SkillEntity skill = effect.getSkill();
				if(skill != null && skill.getCurrentEffectIndex() == 0)
				{
					skill.setLastCastTime(currTime);
					skill.setNextCastTime(currTime + skill.getModel().getRealCd()); 
				}
			}
			break;

		default:
			break;
		}
	}

}
