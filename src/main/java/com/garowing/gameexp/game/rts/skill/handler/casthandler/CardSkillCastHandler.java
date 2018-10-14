package com.garowing.gameexp.game.rts.skill.handler.casthandler;

import com.yeto.war.fightcore.fight.FightEngine;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.handler.SkillCastHandler;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.listener.WarListenerFactory;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.yeto.war.module.ErrorCode;
import com.yeto.war.module.card.CardObject;
import com.yeto.war.module.card.FightCardService;
import com.yeto.war.module.fight.objects.PlayerControl;
import com.yeto.war.module.player.PlayerEntity;
import com.yeto.war.network.gs.sendable.fight.GC_CASE_CARD;

/**
 * 卡牌技能施法回调
 * @author seg
 * 2017年3月24日
 */
public class CardSkillCastHandler implements SkillCastHandler
{

	@Override
	public void onSuc(SkillEntity skill, long currTime)
	{
		if(skill.getOwnerControl() instanceof AbsFightControl)
		{
			AbsFightControl control = (AbsFightControl) skill.getOwnerControl();
			if (FightCardService.canDeal(control))
				FightCardService.deal(control);
			
			if(control instanceof PlayerControl)
			{
				CardObject card = skill.getCaster();
				PlayerEntity player = ((PlayerControl) control).getPlayer();
				player.sendPacket(new GC_CASE_CARD(player.getId(), 0, card.getCardId()));
			}
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
		
		CardObject card = skill.getCaster();
		WarControlInstance control = skill.getOwnerControl();
		if(control instanceof PlayerControl)
		{
			PlayerControl playerControl = (PlayerControl) control;
			playerControl.getPlayer().sendPacket(new GC_CASE_CARD(playerControl.gainPlayerID(), result.code(), card.getCardId()));
		}

	}

	@Override
	public SkillError beforeCast(SkillEntity skill, long currTime)
	{
		if(skill.getOwnerControl() instanceof AbsFightControl)
		{
			AbsFightControl control = (AbsFightControl) skill.getOwnerControl();
			CardObject card = skill.getCaster();
			
			if(!FightEngine.isReduceFood(control, card.getProvision()))
				return SkillError.ERROR_EFF_NO_FOOD;
			
			/* 发命令扣除粮草与卡牌 */
			FightEngine.reduceFood(control, card.getProvision());
			
			// 通知释放卡牌
			WarListenerFactory.onUseCard(control, card);
		}
		
		return SkillError.SUCCESS;
	}

}
