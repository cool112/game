package com.garowing.gameexp.game.rts.skill.template;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 技能播放控制器
 * @author seg
 * 2017年3月15日
 */
public abstract class SkillPlayControllor
{
	/**
	 * 获取播放时间
	 * @param skill
	 * @param currTime 
	 * @return
	 */
	public abstract long getWaitTime(SkillEntity skill, long currTime);
	
	/**
	 * 技能是否结束
	 * @param skill
	 * @param currTime
	 * @return
	 */
	public boolean isSkillEnd(SkillEntity skill, long currTime)
	{
		WarInstance war = skill.getWar();
		if(war == null)
			return true;
		
		GameObject caster = skill.getCaster();
		if(caster != null && caster.getObjectType() == GameObjectType.TROOP)
		{
			Troop troopCaster = (Troop) caster;
			if(!skill.getModel().isPassive() && (!troopCaster.isLive() || !troopCaster.canAttack() || !troopCaster.canCaseSkill()))
			{
				for(Integer effectId : skill.getEffectEntityList())
				{
					GameObject obj = war.getObject(effectId);
					if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
						continue;
					
					EffectEntity effect = (EffectEntity) obj;
					war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, caster, effect, true));
				}
			}
		}
		
		for(Integer effectId : skill.getEffectEntityList())
		{
			GameObject obj = war.getObject(effectId);
			if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			EffectEntity effect = (EffectEntity) obj;
			if(effect.getState() < SkillManager.IDLE)
				return false;
		}
		
		return true;
	}
	
	/**
	 * 获取初始延迟
	 * @return
	 */
	public abstract long getInitDelay(SkillEntity skill, long currTime);
}
