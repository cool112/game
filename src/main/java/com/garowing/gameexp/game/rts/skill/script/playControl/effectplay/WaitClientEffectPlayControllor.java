package com.garowing.gameexp.game.rts.skill.script.playControl.effectplay;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.template.EffectPlayControllor;

/**
 * 等待客户端效果播放控制
 * @author seg
 *
 */
@ScriptName(name = "waitClientEp")
public class WaitClientEffectPlayControllor extends EffectPlayControllor
{
	private static final Logger mLogger = Logger.getLogger(WaitClientEffectPlayControllor.class);
	
	@Override
	public long getWaitTime(EffectEntity effect, long currTime)
	{
//		long waitTimeout = effect.getEffectPrototype().getExecDelay();
		long waitTimeout = effect.getRealExecDelay();
		if(waitTimeout <= 0)
		{
			mLogger.warn("waitClient timeout is missing! effectId[" + effect.getPrototypeId() + "]");
			effect.setState(SkillManager.END);
			effect.setEndTime(currTime);
		}
		
		long remainTime = effect.getStartTime() + waitTimeout - currTime;
		if(remainTime <= 0)
		{
			mLogger.warn("waitClient timeout! effectId[" + effect.getPrototypeId() + "]");
			effect.setState(SkillManager.END);
			effect.setEndTime(currTime);
		}
		
		return Long.MAX_VALUE;
	}

	@Override
	public long getInitDelay(EffectEntity effect, long currTime)
	{
		long remainTime = effect.getCreateTime() + effect.getEffectPrototype().getInitDelay() - currTime;
		if(remainTime < 0)
			remainTime = 0;
		
		return remainTime;
	}
	
}
