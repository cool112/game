package com.garowing.gameexp.game.rts.skill.constants;

import com.garowing.gameexp.game.rts.skill.script.effect.active.AttackBasedHealPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.ChargeIgnorePathPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.ChargePrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.CompositeEffectPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.CopyUnitPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.EnergyAddPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.InstantKillPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.KnockbackPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.ModInitEnergyPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.NormalAttackPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.NormalHarmPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.RaiseRandomDeadPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.RebirthPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.ReplaceSkillPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.SacredHarmPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.SuicidePrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.active.SummonUnitPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.aureole.DynamicEffectAureolePrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.aureole.FixEffectAureolePrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.aureole.GeneratorAureolePrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.aureole.SerialGeneratorAureolePrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.BaseAttrBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.BleedingBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.DistanceVarPtAttrBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.DizzyBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.EffectModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.EffectTargetEnergyModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.EffectTargetHpModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.EffectTargetStatusModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.EffectTargetTypeModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ExitSceneBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.FixModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.FrozenBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ImmuneEffectBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ImmuneGeneralSkillBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ImmuneSkillBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ImmuneStatusBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ImmuneStealthPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.InvincibleStatusBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.LaunchedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.PetrifactBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.PoisonBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.PretendDieBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ShieldBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.ShortCircuitBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.SkillModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.StealthBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.TimeVarPtAttrBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.UnitCountFixModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.UnitCountModifiedBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.WeakBuffPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.AttackEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.BeAttackedEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.BeSkillHarmEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.BlockEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.CampCompareEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.DieEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.EnterSceneEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.KillEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.LowHpEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.OtherDieEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.SkillHarmEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.TargetChangeForceEndTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.TroopNumEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.UseCardEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.script.effect.trigger.UseGeneralSkillEffectTriggerPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;

/**
 * 效果类型
 * @author seg
 *
 */
public enum EffectType
{
	/* =================ACTIVE(1)============== */
	/**
	 * 普通攻击效果，带攻击判定，可以格挡、闪避、暴击
	 */
	NORMAL_ATTACK			(EffectTopType.ACTIVE, 1, NormalAttackPrototype.class),
	
	/**
	 * 普通伤害
	 */
	NORMAL_HARM				(EffectTopType.ACTIVE, 2, NormalHarmPrototype.class),
	
	/**
	 * 基于攻击力治疗
	 */
	ATK_BASED_HEAL			(EffectTopType.ACTIVE, 3, AttackBasedHealPrototype.class),
	
	/**
	 * 击退
	 */
	KNOCKBACK				(EffectTopType.ACTIVE, 4, KnockbackPrototype.class),
	
	/**
	 * 组合
	 */
	COMPOSITE				(EffectTopType.ACTIVE, 5, CompositeEffectPrototype.class),
	
	/**
	 * 增加能量
	 */
	ENERGY_ADD				(EffectTopType.ACTIVE, 6, EnergyAddPrototype.class),
	
	/**
	 * 自杀效果
	 */
	SUICIDE					(EffectTopType.ACTIVE, 7, SuicidePrototype.class),
	
	/**
	 * 技能替换效果
	 */
	REPLACE_SKILL			(EffectTopType.ACTIVE, 8, ReplaceSkillPrototype.class),
	
	/**
	 * 冲锋
	 */
	CHARGE					(EffectTopType.ACTIVE, 9, ChargePrototype.class),
	
	/**
	 * 修改初始能量点
	 */
	MOD_INIT_ENERGY			(EffectTopType.ACTIVE, 10, ModInitEnergyPrototype.class),
	
	/**
	 * 即死
	 */
	INSTANT_KILL			(EffectTopType.ACTIVE, 11, InstantKillPrototype.class),
	
	/**
	 * 神圣伤害
	 */
	SACRED_HARM				(EffectTopType.ACTIVE, 12, SacredHarmPrototype.class),
	
	/**
	 * 忽视路径冲锋
	 */
	CHARGE_IGNORE_PATH		(EffectTopType.ACTIVE, 13, ChargeIgnorePathPrototype.class),
	/* =================BUFF(2)============== */
	/**
	 * 基础属性buff
	 */
	BASE_ATTR_BUFF			(EffectTopType.BUFF, 1, BaseAttrBuffPrototype.class),
	
	/**
	 * 时间变量buff
	 */
	TIME_VAR_BUFF			(EffectTopType.BUFF, 2, TimeVarPtAttrBuffPrototype.class),
	
	/**
	 * 距离变量buff
	 */
	DISTANCE_VAR_BUFF		(EffectTopType.BUFF, 3, DistanceVarPtAttrBuffPrototype.class),
	
	/**
	 * 眩晕buff
	 */
	DIZZY_BUFF				(EffectTopType.BUFF, 4, DizzyBuffPrototype.class),
	
	/**
	 * 中毒buff
	 */
	POISON_BUFF				(EffectTopType.BUFF, 5, PoisonBuffPrototype.class),
	
	/**
	 * 隐身buff
	 */
	STEALTH_BUFF			(EffectTopType.BUFF, 6, StealthBuffPrototype.class),
	
	/**
	 * 免疫隐身buff
	 */
	IMMUNE_STEALTH_BUFF		(EffectTopType.BUFF, 7, ImmuneStealthPrototype.class),
	
	/**
	 * 虚弱buff
	 */
	WEAK_BUFF				(EffectTopType.BUFF, 8, WeakBuffPrototype.class),
	
	/**
	 * 免疫效果buff
	 */
	IMMUSE_EFFECT_BUFF		(EffectTopType.BUFF, 9, ImmuneEffectBuffPrototype.class),
	
	/**
	 * 流血buff
	 */
	BLEEDING_BUFF			(EffectTopType.BUFF, 10, BleedingBuffPrototype.class),
	
	/**
	 * 效果修改buff
	 */
	EFFECT_MOD_BUFF			(EffectTopType.BUFF, 11, EffectModifiedBuffPrototype.class),
	
	/**
	 * 固定修正值buff
	 */
	FIX_MOD_BUFF			(EffectTopType.BUFF, 12, FixModifiedBuffPrototype.class),
	
	/**
	 * 技能修正值buff
	 */
	SKILL_MOD_BUFF			(EffectTopType.BUFF, 13, SkillModifiedBuffPrototype.class),
	
	/**
	 * 效果-目标血量修正值buff
	 */
	TARGET_HP_MOD_BUFF		(EffectTopType.BUFF, 14, EffectTargetHpModifiedBuffPrototype.class),
	
	/**
	 * 效果-目标状态修正值buff
	 */
	TARGET_STATUS_MOD_BUFF	(EffectTopType.BUFF, 15, EffectTargetStatusModifiedBuffPrototype.class),
	
	/**
	 * 效果-目标类型修正值buff
	 */
	TARGET_TYPE_MOD_BUFF	(EffectTopType.BUFF, 16, EffectTargetTypeModifiedBuffPrototype.class),
	
	/**
	 * 单位数量修正值buff
	 */
	UNIT_COUNT_MOD_BUFF		(EffectTopType.BUFF, 17, UnitCountModifiedBuffPrototype.class),
	
	/**
	 * 冰冻buff
	 */
	FROZEN_BUFF				(EffectTopType.BUFF, 18, FrozenBuffPrototype.class),
	
	/**
	 * 状态免疫buff
	 */
	IMMUSE_STATUS_BUFF		(EffectTopType.BUFF, 19, ImmuneStatusBuffPrototype.class),
	
	/**
	 * 击飞buff
	 */
	LAUNCHED_BUFF			(EffectTopType.BUFF, 20, LaunchedBuffPrototype.class),
	
	/**
	 * 石化buff
	 */
	PETRIFACT_BUFF			(EffectTopType.BUFF, 21, PetrifactBuffPrototype.class),
	
	/**
	 * 单位数量固定修正buff
	 */
	UNIT_COUNT_FIX_MOD_BUFF	(EffectTopType.BUFF, 22, UnitCountFixModifiedBuffPrototype.class),
	
	/**
	 * 短路buff
	 */
	SHORT_CIRCUIT_BUFF		(EffectTopType.BUFF, 23, ShortCircuitBuffPrototype.class),
	
	/**
	 * 护盾buff
	 */
	SHIELD_BUFF				(EffectTopType.BUFF, 24, ShieldBuffPrototype.class),
	
	/**
	 * 无敌buff
	 */
	INVINCIBLE_BUFF			(EffectTopType.BUFF, 25, InvincibleStatusBuffPrototype.class),
	
	/**
	 * 假死buff
	 */
	PRETEND_DIE_BUFF		(EffectTopType.BUFF, 26, PretendDieBuffPrototype.class),
	
	/**
	 * 离场buff
	 */
	EXIT_SCENE_BUFF			(EffectTopType.BUFF, 27, ExitSceneBuffPrototype.class),
	
	/**
	 * 目标能量点修正buff
	 */
	TARGET_ENERGY_BUFF		(EffectTopType.BUFF, 28, EffectTargetEnergyModifiedBuffPrototype.class),
	
	/**
	 * 免疫技能buff
	 */
	IMMUSE_SKILL_BUFF		(EffectTopType.BUFF, 29, ImmuneSkillBuffPrototype.class),
	
	/**
	 * 免疫将军技能buff
	 */
	IMMUSE_GENERAL_SKILL_BUFF(EffectTopType.BUFF, 30, ImmuneGeneralSkillBuffPrototype.class),
	
	/* =================TRIGGER(3)============== */
	/**
	 * 攻击触发
	 */
	ATTACK_TRIGGER			(EffectTopType.TRIGGER, 1, AttackEffectTriggerPrototype.class),
	
	/**
	 * 被攻击攻击触发
	 */
	BE_ATTACKED_TRIGGER		(EffectTopType.TRIGGER, 2, BeAttackedEffectTriggerPrototype.class),
	
	/**
	 * 死亡效果触发器
	 */
	DIE_TRIGGER				(EffectTopType.TRIGGER, 3, DieEffectTriggerPrototype.class),
	
	/**
	 * 杀死效果触发器
	 */
	KILL_TRIGGER			(EffectTopType.TRIGGER, 4, KillEffectTriggerPrototype.class),
	
	/**
	 * 进入战场效果触发器
	 */
	ENTER_SCENE_TRIGGER		(EffectTopType.TRIGGER, 5, EnterSceneEffectTriggerPrototype.class),
	
	/**
	 * 其他单位死亡
	 */
	OTHER_DIE_TRIGGER		(EffectTopType.TRIGGER, 6, OtherDieEffectTriggerPrototype.class),
	
	/**
	 * 部队数量门限
	 */
	TROOP_NUM_TRIGGER		(EffectTopType.TRIGGER, 7, TroopNumEffectTriggerPrototype.class),
	
	/**
	 * 使用卡牌
	 */
	USE_CARD_TRIGGER		(EffectTopType.TRIGGER, 8, UseCardEffectTriggerPrototype.class),
	
	/**
	 * 阵营部队数量比较
	 */
	CAMP_COMP_TRIGGER		(EffectTopType.TRIGGER, 9, CampCompareEffectTriggerPrototype.class),
	
	/**
	 * 攻击触发格挡
	 */
	BLOCK_TRIGGER			(EffectTopType.TRIGGER, 10, BlockEffectTriggerPrototype.class),
	
	/**
	 * 目标改变触发结束效果
	 */
	TARGET_CHANGE_END_TRIGGER(EffectTopType.TRIGGER, 11, TargetChangeForceEndTriggerPrototype.class),
	
	/**
	 * 低血量触发
	 */
	LOW_HP_TRIGGER			(EffectTopType.TRIGGER, 12, LowHpEffectTriggerPrototype.class),
	
	/**
	 * 技能伤害触发
	 */
	SKILL_HARM_TRIGGER		(EffectTopType.TRIGGER, 13, SkillHarmEffectTriggerPrototype.class),
	
	/**
	 * 被技能伤害触发
	 */
	BE_SKILL_HARMED_TRIGGER	(EffectTopType.TRIGGER, 14, BeSkillHarmEffectTriggerPrototype.class),
	
	/**
	 * 使用将军技能触发
	 */
	GENERAL_SKILL_TRIGGER	(EffectTopType.TRIGGER, 15, UseGeneralSkillEffectTriggerPrototype.class),
	
	
	/* =================SUMMON(4)============== */
	/**
	 * 基础召唤效果
	 */
	SUMMON					(EffectTopType.SUMMON, 1, SummonUnitPrototype.class),
	
	/**
	 * 随机亡灵复生
	 */
	RAISE_RANDOM_DEAD		(EffectTopType.SUMMON, 2, RaiseRandomDeadPrototype.class),
	
	/**
	 * 复制单位
	 */
	COPY_UNIT				(EffectTopType.SUMMON, 3, CopyUnitPrototype.class),
	
	/**
	 * 重生
	 */
	REBIRTH					(EffectTopType.SUMMON, 4, RebirthPrototype.class),
	
	/* =================AUREOLE(5)============== */
	/**
	 * 固定效果光环
	 */
	FIX_EFFECT_AURE			(EffectTopType.AUREOLE, 1, FixEffectAureolePrototype.class),
	
	/**
	 * 动态效果光环
	 */
	DYNAMIC_EFFECT_AURE		(EffectTopType.AUREOLE, 2, DynamicEffectAureolePrototype.class),
	
	/**
	 * 效果生成器光环
	 */
	GENERATOR_AURE			(EffectTopType.AUREOLE, 3, GeneratorAureolePrototype.class),
	
	/**
	 * 序列效果生成器光环
	 */
	SERIAL_GENERATOR_AURE	(EffectTopType.AUREOLE, 4, SerialGeneratorAureolePrototype.class);
	
	/**
	 * 大类，主动-1、buff-2、触发-3
	 */
	private int topType;
	
	/**
	 * 子id
	 */
	private int subId;
	
	/**
	 * 编号
	 */
	private int code;
	
	/**
	 * 实现类
	 */
	private Class<? extends EffectPrototype> implClass;
	
	private EffectType(int topType, int subId, Class<? extends EffectPrototype> implClass)
	{
		this.topType = topType;
		this.subId = subId;
		this.code = topType * 1000 + subId;
		this.implClass = implClass;
	}

	/**
	 * 根据id获取类型
	 * @param templateId
	 * @return
	 */
	public static EffectType getTypeById(int templateId)
	{
		EffectType[] types = EffectType.values();
		for(EffectType type : types)
		{
			if(type.getCode() == templateId)
				return type;
		}
		
		return null;
	}
	
	public int getCode()
	{
		return code;
	}

	public Class<? extends EffectPrototype> getImplClass()
	{
		return implClass;
	}

	public int getTopType()
	{
		return topType;
	}

	public int getSubId()
	{
		return subId;
	}


	
	
}
