package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.state.FightMetaState;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillParams;
import com.garowing.gameexp.game.rts.skill.event.AttackSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.RatioUitl;

/**
 * 带攻击判定的伤害效果
 * @author seg
 *
 */
public class NormalAttackPrototype extends EffectPrototype
{

	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		if(effect.getCaster().getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		List<GameObject> targets = effect.getTargets();
		if(needTarget && (targets == null || targets.isEmpty()))
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
			{
				effect.removeTarget(target.getObjectId());
				continue;
			}
			
			Troop troop = (Troop) target;
			if(!troop.isLive())
				effect.removeTarget(target.getObjectId());
		}
		if(effect.getTargetIds().isEmpty()&& effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		SkillError result = onActivate(effect, currTime);
		if(result == SkillError.SUCCESS)
		{
			effect.getEffectHandler().onActivateSuc(effect, currTime);
			effect.addActivateCount();
			effect.setLastActivateTime(currTime);
			effect.setNextActivateTime(currTime + effect.getInterval());
		}
		else
			effect.getEffectHandler().onActivateFail(effect, currTime);
		
		return result;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		WarInstance war = effect.getWar();
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		Troop caster = effect.getCaster();
		if(caster == null)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		if(!effect.isTemporary())
		{
			if(!caster.canAttack())
			{
				war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, caster, effect, true));
				return SkillError.ERROR_CASTER_CAN_NOT_ACT;
			}
		}
		
		calculateNextAttackState(caster, (Troop) targets.get(0), effect);
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			if((troopTarget.getFightStates().getMetaStates() & FightMetaState.IMMUNE_DAMAGE) > 0)
			{
				HpAction action = new HpAction(troopTarget, caster, 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.IMMUSE);
				troopTarget.addAction(action);
				continue;
			}
			
			war.getSkillManager().addSkillEvent(new AttackSkillEvent(war, effect, (Troop)target));
		}
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

	/**
	 * 计算下次攻击者状态
	 * @param attack
	 * @param target
	 * @create	2015年8月13日	darren.ouyang
	 */
	private void calculateNextAttackState (Troop attack, Troop target, EffectEntity effect)
	{
		// 清理之前状态
		attack.getFightStates().setAttackCritical(false);
		
		// 计算下次攻击者是否暴击
		float critRT = attack.getCrit();
		float skillAddCcRT = attack.getAttrModList().getUnitModifiedTotal(ModifiedType.CRITICAL_ADD, target, effect);
		critRT += skillAddCcRT;
		if (critRT >= 1f || RatioUitl.isSuccess(critRT))
		{
			attack.getFightStates().setAttackCritical(true);
		}

//		// 通知客户端对象之后的命令
//		SendPacketUtil.sendObjectWarPacket(attack, (sender)->
//		{	
//			if (attack.getFightStates().isAttackCritical())
//				sender.sendPacket(new GC_NEXT_FIGHT_STATE(sender.getId(), 0, attack.getObjectId(), 0x00000001<<1));
//		});
	}
	
	@Override
	public SkillError checkClientParams(EffectEntity effect, SkillParams packect)
	{
		if(packect.getTargetIds() == null)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		if(packect.getTargetIds().size() != effect.getTargetIds().size())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return super.checkClientParams(effect, packect);
	}
}
