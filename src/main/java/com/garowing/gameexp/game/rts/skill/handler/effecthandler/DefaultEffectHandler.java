package com.garowing.gameexp.game.rts.skill.handler.effecthandler;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.SendPacketUtil;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.handler.SkillEffectHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.yeto.war.network.gs.sendable.fight.GC_END_SKILL;

/**
 * 默认效果回调处理
 * @author seg
 *
 */
public class DefaultEffectHandler implements SkillEffectHandler
{

	@Override
	public void onStartSuc(EffectEntity effect, long currTime)
	{
		effect.setState(SkillManager.ACTIVATE);
	}

	@Override
	public void onStartFail(EffectEntity effect, long currTime)
	{
		effect.setState(SkillManager.END);
		effect.setEndTime(currTime);
	}

	@Override
	public void onActivateSuc(EffectEntity effect, long currTime)
	{
	}

	@Override
	public void onActivateFail(EffectEntity effect, long currTime)
	{
		SkillEntity skill = effect.getSkill();
		if(skill != null && !skill.getModel().isPassive())
		{
			GameObject caster = skill.getCaster();
			skill.interrupt(caster, false);
			SendPacketUtil.sendWarPacket(skill.getWar(), (sender)->{
				sender.sendPacket(new GC_END_SKILL(sender.getId(), 0, skill.getObjectId(), skill.getModelId(), caster.getObjectId()));
			});
		}

	}

}
