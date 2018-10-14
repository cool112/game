package com.garowing.gameexp.game.rts.skill.handler.casthandler;

import com.yeto.war.fightcore.fight.FightEngine;
import com.garowing.gameexp.game.rts.skill.Skill2TriggerFactory;
import com.garowing.gameexp.game.rts.skill.entity.GeneralSkillEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.handler.SkillCastHandler;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.listener.WarListenerFactory;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.yeto.war.module.ErrorCode;
import com.yeto.war.module.fight.objects.PlayerControl;
import com.yeto.war.module.player.PlayerEntity;
import com.yeto.war.module.strategos.StrategosSkillModel;
import com.yeto.war.network.gs.sendable.fight.GC_CAST_GENERAL;

/**
 * 将军技能施法回调
 * @author seg
 * 2017年3月24日
 */
public class StrategosSkillCastHandler implements SkillCastHandler
{

	@Override
	public void onSuc(SkillEntity skill, long currTime)
	{
		AbsFightControl control = (AbsFightControl) skill.getOwnerControl();
		StrategosSkillModel strategosSkill = skill.getSourceStrategosSkill();
		Skill2TriggerFactory.onCastStrategosSkill(control, strategosSkill);
		if(control instanceof PlayerControl)
		{
			PlayerEntity player = ((PlayerControl) control).getPlayer();
			player.sendPacket(new GC_CAST_GENERAL(player.getId(), 0, skill.getModelId(), currTime + skill.getCd()));
		}
	}

	@Override
	public void onFail(SkillError error, SkillEntity skill)
	{
		ErrorCode result = ErrorCode.SUCCESS;
		if(error == SkillError.ERROR_NO_TARGET)
			result =  ErrorCode.BATTLE_SKILL_NO_TARGET;
		
		if (error != SkillError.SUCCESS)
			result =  ErrorCode.BATTLE_SKILL_FAILD;
		
		WarControlInstance control = skill.getOwnerControl();
		StrategosSkillModel strategosSkill = skill.getSourceStrategosSkill();
		if(control instanceof PlayerControl)
		{
			PlayerControl playerControl = (PlayerControl) control;
			playerControl.getPlayer().sendPacket(new GC_CAST_GENERAL(playerControl.gainPlayerID(), result.code(), strategosSkill.getId(), 0L));
		}
	}

	@Override
	public SkillError beforeCast(SkillEntity skill, long currTime)
	{
		AbsFightControl control = (AbsFightControl) skill.getOwnerControl();
		
		if(!(skill instanceof GeneralSkillEntity))
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		GeneralSkillEntity generalSkill = (GeneralSkillEntity) skill;
		if(!FightEngine.isReduceFood(control, generalSkill.getEnergy()))
			return SkillError.ERROR_EFF_NO_FOOD;
		
		FightEngine.reduceFood(control, generalSkill.getEnergy());
		WarListenerFactory.onUseGeneralSkill(control, generalSkill);
		
		return SkillError.SUCCESS;
	}

}
