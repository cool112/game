package com.garowing.gameexp.game.rts.skill.constants;

import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.AttackSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.ClientNotifySkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.EnterSceneSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.InterrupEffectSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.InitControlSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.RefreshEffectSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.RestartEffectSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.SkillHarmSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.TargetChangeSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.TroopDieSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.TroopNumSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.UnitHpChangeSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.UseCardSkillEventHandler;
import com.garowing.gameexp.game.rts.skill.handler.eventhandler.UseStrategosSkillEventHandler;

/**
 * 技能事件类型
 * @author seg
 * 2017年3月24日
 */
public enum SkillEventType
{
	/* 技能触发控制 */
	/**
	 * 攻击事件，包含被攻击
	 */
	ATTACK(new AttackSkillEventHandler()),
	
	/**
	 * 进入战场，涉及开场技能
	 */
	ENTER_SCENE(new EnterSceneSkillEventHandler()),
	
	/**
	 * 初始化控制器，如增加初始粮草的技能
	 */
	INIT_CONTROL(new InitControlSkillEventHandler()),
	
	/**
	 * 使用将军技能
	 */
	USE_GENERAL_SKILL(new UseStrategosSkillEventHandler()),
	
	/**
	 * 部队数改变
	 */
	TROOP_COUNT_CHANGE(new TroopNumSkillEventHandler()),
	
	/**
	 * 部队死亡
	 */
	TROOP_DIE(new TroopDieSkillEventHandler()),
	
	/**
	 * 使用卡牌
	 */
	USE_CARD(new UseCardSkillEventHandler()),
	
	/**
	 * 目标改变事件
	 */
	TARGET_CHANGE(new TargetChangeSkillEventHandler()),
	
	/**
	 * 血量改变
	 */
	HP_CHANGE(new UnitHpChangeSkillEventHandler()),
	
	/**
	 * 技能伤害
	 */
	SKILL_HARM(new SkillHarmSkillEventHandler()),
	
	/* 技能生命周期控制 */
	/**
	 * 客户端通知
	 */
	CLIENT_NOTIFY(new ClientNotifySkillEventHandler()),
	
	/**
	 * 技能强制结束
	 */
	INTERRUPT(new InterrupEffectSkillEventHandler()),
	
	/**
	 * 重置效果
	 */
	RESTART_EFFECT(new RestartEffectSkillEventHandler()),
	
	/**
	 * 刷新效果持续事件
	 */
	REFRESH_EFFECT(new RefreshEffectSkillEventHandler()),
	
	;
	
	private final SkillEventHandler handler;
	
	SkillEventType(SkillEventHandler handler)
	{
		this.handler = handler;
	}

	public SkillEventHandler getHandler()
	{
		return handler;
	}
	
}
