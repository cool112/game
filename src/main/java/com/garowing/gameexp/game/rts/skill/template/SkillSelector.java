package com.garowing.gameexp.game.rts.skill.template;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.SendPacketUtil;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.network.gs.sendable.fight.GC_END_SKILL;
import com.yeto.war.utils.RatioUitl;

/**
 * 技能选择器
 * @author seg
 *
 */
abstract public class SkillSelector
{
	/**
	 * 选择技能
	 * @param skills
	 * @param currTime
	 * @return
	 */
	public SkillEntity chooseSkill(List<SkillEntity> skills, long currTime, int targetId, float targetX, float targetY)
	{
		if(skills == null || skills.isEmpty())
			return null;
		
		List<SkillEntity> firstFiltered = new ArrayList<SkillEntity>();
		int currPriority = Integer.MAX_VALUE;
		WarInstance war = skills.get(0).getWar();
		GameObject target = war.getObject(targetId);
		for(SkillEntity skill : skills)
		{
			if(skill.getModel().isPassive())
				continue;
			
			int originPriority = skill.getModel().getPriority();
			if(skill.getState() != SkillManager.IDLE)
			{
				if(skill.isParallel())
				{
					if(skill.getCurrentEffect().getState() < SkillManager.ACTIVATE)
					{
						return null;
					}
					else
						continue;
				}
				else
				{
					if(skill.canInterrupt() && currPriority < originPriority)
					{
						GameObject caster = skill.getCaster();
						if(skill.interrupt(caster, false))
						{
							System.err.println(currTime+": send interrupt skill state " + skill.getState());
							SendPacketUtil.sendWarPacket(skill.getWar(), (sender)->{
								sender.sendPacket(new GC_END_SKILL(sender.getId(), 0, skill.getObjectId(), skill.getModelId(), caster.getObjectId()));
							});
						}
					}
					return null;
				}
					
			}
			
			if(skill.isInCD(currTime))
			{
				continue;
			}
			
			if(target != null)
			{
				if(!((HavingSkill)target).getAttachEffects().isValidSkill(skill))
					continue;
			}
			
			SkillAIObj skillAi = skill.getModel().getSkillAI();
			if(skillAi != null)
				originPriority += skillAi.getModifiedPriority(skill);
			
			if(currPriority < originPriority)
				break;
			
			if(skillAi != null && !skillAi.isValid(skill, targetId, targetX, targetY))
				continue;
			
			firstFiltered.add(skill);
			currPriority = originPriority;
		}
		
		if(firstFiltered.isEmpty())
			return null;
		
		if(firstFiltered.size() == 1)
			return firstFiltered.get(0);
		
		return RatioUitl.randomWeight(firstFiltered);
	}
	
	
}
