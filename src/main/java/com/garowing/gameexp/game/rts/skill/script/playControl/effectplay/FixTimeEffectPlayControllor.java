package com.garowing.gameexp.game.rts.skill.script.playControl.effectplay;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.ScriptName;
import com.garowing.gameexp.game.rts.skill.template.EffectPlayControllor;
import com.yeto.war.module.troop.Troop;

/**
 * 固定时间效果播放控制
 * @author seg
 * 2017年4月5日
 */
@ScriptName(name = "fixTimeEp")
public class FixTimeEffectPlayControllor extends EffectPlayControllor
{

	@Override
	public long getWaitTime(EffectEntity effect, long currTime)
	{
//		long remainTime = effect.getStartTime() + effect.getEffectPrototype().getExecDelay() - currTime;
		float reduceCoe = 0f;
		GameObject caster = effect.getCaster();
		if(caster != null && caster.getObjectType() == GameObjectType.TROOP)
		{
			reduceCoe = ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.CD_ADD, effect.getSkillModelId());
		}
		long delay = (long) (effect.getRealExecDelay() * (1 + reduceCoe));
		long remainTime = effect.getStartTime() + delay - currTime;
		if(remainTime < 0)
			remainTime = 0;
		
		return remainTime;
	}

	@Override
	public long getInitDelay(EffectEntity effect, long currTime)
	{
		long remainTime = effect.getCreateTime() + effect.getEffectPrototype().getInitDelay() - currTime;
		if(remainTime < 0)
			remainTime = 0;
		
		return remainTime;
	}
	
	public static void main(String[] args)
	{
		long b = (long) (1500986937880L + 1037L * 1.0 - 1500986937880L);
		System.out.println(b);
	}

}
