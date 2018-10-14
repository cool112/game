package com.garowing.gameexp.game.rts.skill;

import java.util.List;

import org.apache.log4j.Logger;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.scene.objects.WarSceneObject;
import com.garowing.gameexp.game.rts.skill.constants.EffectType;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.GeneralSkillEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillParams;
import com.garowing.gameexp.game.rts.skill.event.ClientNotifySkillEvent;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.RefreshEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.event.RestartEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillCastHandler;
import com.garowing.gameexp.game.rts.skill.handler.SkillEffectHandler;
import com.garowing.gameexp.game.rts.skill.handler.casthandler.StrategosSkillCastHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.EffectModel;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.model.SkillModel;
import com.garowing.gameexp.game.rts.skill.script.effect.active.SummonUnitPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.ErrorCode;
import com.yeto.war.module.card.CardObject;
import com.yeto.war.module.player.PlayerEntity;
import com.yeto.war.module.strategos.StrategosSkillModel;
import com.yeto.war.module.troop.model.TroopAttrN2Model;
import com.yeto.war.module.troop.model.TroopModel;
import com.yeto.war.network.gs.sendable.fight.GC_EXECUTE_SKILL;
import com.yeto.war.network.gs.sendable.fight.GC_START_SKILL;

/**
 * 技能引擎
 * 
 * @author darren.ouyang <ouyang.darren@gmail.com>
 * @date 2015年3月23日
 * @version 1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class SkillEngine
{
	final static Logger log = Logger.getLogger(SkillEngine.class);

	/**
	 * 绑定技能,复活的单位重新激活被动技能
	 * 
	 * @param object
	 * @param skillModelId
	 * @return
	 */
	public static SkillError bindSkill(HavingSkill object, int skillModelId, SkillCastHandler castHandler)
	{
		long currTime = System.currentTimeMillis();
		SkillModel skillModel = StaticDataManager.SKILL_DATA.getModel(skillModelId);
		if (skillModel == null)
			return SkillError.ERROR_NO_SKILL_MODEL;

		GameObject caster = (GameObject) object;
		WarInstance war = caster.getWar();
		List<SkillEntity> skillList = object.getBoundSkills();
		if (skillList != null && !skillList.isEmpty())
		{
			for (SkillEntity ownedSkill : skillList)
			{
				if (ownedSkill.getModelId() == skillModelId)
				{
					if (skillModel.isPassive())
					{
						ownedSkill.getTargetIds().clear();
						ownedSkill.getTargetIds().add(caster.getObjectId());
						ownedSkill.setState(SkillManager.START);
						ownedSkill.setStartTime(currTime);
						war.getSkillManager().addSkill(ownedSkill);
					}
					return SkillError.SUCCESS;
				}
			}
		}

		SkillEntity skillEntity = new SkillEntity(war, skillModel, caster, castHandler);
		object.bindNewSkill(skillEntity);
		if (skillModel.isPassive())
		{
			skillEntity.getTargetIds().clear();
			skillEntity.getTargetIds().add(caster.getObjectId());
			skillEntity.setState(SkillManager.START);
			skillEntity.setStartTime(currTime);
			war.getSkillManager().addSkill(skillEntity);
		}

		return SkillError.SUCCESS;
	}

	/**
	 * 绑定将军技能
	 * 
	 * @param object
	 * @param model
	 * @param castHandler
	 * @return
	 */
	public static SkillError bindStrategosSkill(HavingSkill object, StrategosSkillModel model,
			SkillCastHandler castHandler)
	{
		long currTime = System.currentTimeMillis();
		SkillModel skillModel = StaticDataManager.SKILL_DATA.getModel(model.getBaseSkillId());
		if (skillModel == null)
			return SkillError.ERROR_NO_SKILL_MODEL;

		GameObject caster = (GameObject) object;
		WarInstance war = caster.getWar();

		GeneralSkillEntity skillEntity = new GeneralSkillEntity(war, model, skillModel, caster, castHandler);
		object.bindNewSkill(skillEntity);
		if (skillModel.isPassive())
		{
			if (skillEntity.getCastHandler() == null)
				SkillEngine.sendSkillMsg(skillEntity);

			skillEntity.getTargetIds().clear();
			skillEntity.getTargetIds().add(caster.getObjectId());
			skillEntity.setState(SkillManager.START);
			skillEntity.setStartTime(currTime);
			war.getSkillManager().addSkill(skillEntity);
		}

		return SkillError.SUCCESS;
	}

	/**
	 * 立即停止所有技能效果
	 * 
	 * @param object
	 */
	public static void exitAllSkills(WarObjectInstance object)
	{
		WarInstance war = object.getWar();
		List<Integer> allTmpEffects = object.getAttachEffects().getTempEffects();
		for (Integer tmpEffectId : allTmpEffects)
		{
			GameObject effect = war.getObject(tmpEffectId);
			if (effect == null || effect.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;

			war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, null, (EffectEntity) effect, false));
		}

		allTmpEffects.clear();

		List<SkillEntity> skillList = object.getBoundSkills();
		for (SkillEntity skill : skillList)
		{
			for (Integer effectId : skill.getEffectEntityList())
			{
				GameObject effect = war.getObject(effectId);
				if (effect == null || effect.getObjectType() != GameObjectType.SKILL_EFFECT)
					continue;

				war.getSkillManager()
						.addSkillEvent(new InterruptEffectSkillEvent(war, null, (EffectEntity) effect, true));
			}
		}
	}

	/**
	 * 客户端通知执行技能效果
	 * 
	 * @param player
	 * @param params
	 * @return
	 */
	public static ErrorCode executeSkill(PlayerEntity player, SkillParams params)
	{
		EffectEntity effect = (EffectEntity) player.getWar().getObject(params.getEffectInstanceId());
		if (effect == null)
			return ErrorCode.BATTLE_NO_SKILL;

		EffectPrototype effectPrototype = effect.getEffectPrototype();
		if (effectPrototype == null || !effectPrototype.getPlayControl().equals("waitClientEp"))
			return ErrorCode.BATTLE_NO_SKILL;

		if (effect.getClientNotificationMap().containFlowId(params.getFlowId()))
			return ErrorCode.SUCCESS;

		SkillError result = effect.getEffectPrototype().checkClientParams(effect, params);
		if (result != SkillError.SUCCESS)
			return ErrorCode.BATTLE_SKILL_FAILD;

		effect.getClientNotificationMap().addNotification(player, params);

		WarInstance war = player.getWar();
		if (war == null)
			return ErrorCode.BATTLE_NO_WAR;

		int notifyType = SkillManager.START;
		if (effect.getEffectPrototype().getNotifyType() == 1)
			notifyType = SkillManager.ACTIVATE;

		war.getSkillManager().addSkillEvent(new ClientNotifySkillEvent(war, notifyType, params));
		// System.err.println("client notify exec id:" + effect.getPrototypeId()
		// + " instanceId:" + effect.getObjectId());
		player.sendPacket(new GC_EXECUTE_SKILL(player.getId(), 0, effect.getObjectId(), effect.getPrototypeId(),
				params.getCasterId()));
		return ErrorCode.SUCCESS;
	}

	/**
	 * 获取技能和效果的来源对象
	 * 
	 * @param data
	 * @return
	 */
	public static GameObject getCaster(GameObject data)
	{
		if (data.getObjectType() == GameObjectType.SKILL)
			return ((SkillEntity) data).getCaster();
		else if (data.getObjectType() == GameObjectType.SKILL_EFFECT)
			return ((EffectEntity) data).getCaster();

		return null;
	}

	/**
	 * 发送技能开始信息
	 * 
	 * @param skill
	 */
	public static void sendSkillMsg(SkillEntity skill)
	{
		EffectEntity effect = skill.getCurrentEffect();
		SendPacketUtil.sendWarPacket(skill.getWar(), (sender) ->
		{
			sender.sendPacket(
					new GC_START_SKILL(sender.getId(), 0, skill.getModelId(), effect.getObjectId(), skill.getCasterId(),
							effect.getPrototypeId(), skill.getTargetX(), skill.getTargetY(), skill.getTargetIds()));
		});
	}

	/**
	 * 发送效果开始信息
	 * 
	 * @param effect
	 */
	public static void sendSkillMsg(EffectEntity effect)
	{
		SendPacketUtil.sendWarPacket(effect.getWar(), (sender) ->
		{
			sender.sendPacket(new GC_START_SKILL(sender.getId(), 0, effect.getSkill().getModelId(),
					effect.getObjectId(), effect.getCaster().getObjectId(), effect.getPrototypeId(),
					effect.getTargetX(), effect.getTargetY(), effect.getTargetIds()));
		});

	}

	/**
	 * 控制器释放技能，卡牌、将军技能
	 * 
	 * @param object
	 * @param target
	 * @param x
	 * @param y
	 */
	public static void castSkillByControllor(HavingSkill object, int targetId, float targetX, float targetY)
	{
		long currTime = System.currentTimeMillis();
		GameObject caster = (GameObject) object;
		WarInstance war = caster.getWar();
		SkillEntity skill = object.chooseSkill(currTime, targetId, targetX, targetY);
		if(skill == null)
			return;
		
		skill.setState(SkillManager.CREATE);
		skill.setCreateTime(currTime);
		if (targetId > 0)
		{
			skill.getTargetIds().clear();
			skill.getTargetIds().add(targetId);
		}

		skill.setTargetX(targetX);
		skill.setTargetY(targetY);
		war.getSkillManager().addSkill(skill);
	}

	/**
	 * 施加或刷新临时效果
	 * 
	 * @param orginEffect
	 * @param target
	 * @param effectModelId
	 * @param currTime
	 */
	public static void castOrRefreshTempEffect(EffectEntity orginEffect, HavingSkill target, int effectModelId,
			long currTime)
	{
		WarInstance war = orginEffect.getWar();
		if (war == null)
			return;

		EffectEntity oldEffect = target.getAttachEffects().getTempEffect(orginEffect.getObjectId(), effectModelId);
		if (oldEffect != null)
		{
			war.getSkillManager().addSkillEvent(new RefreshEffectSkillEvent(war, orginEffect, oldEffect));
		} else
		{
			castTmpEffect(orginEffect, (GameObject) target, effectModelId, currTime, null, false);
		}
	}

	/**
	 * 施加或重启临时效果
	 * 
	 * @param orginEffect
	 * @param target
	 * @param effectModelId
	 * @param currTime
	 */
	public static void castOrRestartTempEffect(EffectEntity orginEffect, HavingSkill target, int effectModelId,
			long currTime)
	{
		WarInstance war = orginEffect.getWar();
		if (war == null)
			return;

		EffectEntity oldEffect = target.getAttachEffects().getTempEffect(orginEffect.getObjectId(), effectModelId);
		if (oldEffect != null)
		{
			war.getSkillManager().addSkillEvent(new RestartEffectSkillEvent(war, orginEffect, oldEffect));
		} else
		{
			castTmpEffect(orginEffect, (GameObject) target, effectModelId, currTime, null, false);
		}
	}

	/**
	 * 释放卡牌技能
	 * 
	 * @param card
	 * @param targetObject
	 * @param targetX
	 * @param targetY
	 * @param cardCastHandler
	 */
	public static ErrorCode castCardSkill(CardObject card, WarObjectInstance targetObject, float targetX, float targetY)
	{
		List<SkillEntity> skillList = card.getBoundSkills();
		if (skillList == null || skillList.isEmpty())
			return ErrorCode.BATTLE_NO_SKILL;

		SkillEntity skill = skillList.get(0);
		skill.setCasterId(card.getObjectId());
		if (targetObject != null)
		{
			skill.getTargetIds().clear();
			skill.getTargetIds().add(targetObject.getObjectId());
		}
		skill.setTargetX(targetX);
		skill.setTargetY(targetY);
		skill.setState(SkillManager.CREATE);
		skill.setCreateTime(System.currentTimeMillis());
		card.getWar().getSkillManager().addSkill(skill);

		return ErrorCode.SUCCESS;
	}

	/**
	 * 释放技能
	 * 
	 * @param caster
	 * @param target
	 * @param targetX
	 * @param targetY
	 * @return
	 */
	public static ErrorCode castSkill(SkillEntity skill, WarObjectInstance target, float targetX, float targetY)
	{
		if (target != null)
		{
			skill.getTargetIds().clear();
			skill.getTargetIds().add(target.getObjectId());
		}
		skill.setTargetX(targetX);
		skill.setTargetY(targetY);
		skill.setState(SkillManager.CREATE);
		skill.setCreateTime(System.currentTimeMillis());
		skill.getWar().getSkillManager().addSkill(skill);
		return ErrorCode.SUCCESS;
	}

	/**
	 * 创建临时效果
	 * 
	 * @param effect
	 * @param currTime
	 * @param target
	 * @param war
	 * @param effectModelId
	 * @param effectHandler
	 *            效果执行后的回调
	 * @param isolated
	 *            是否独立的效果
	 * @return
	 */
	public static void castTmpEffect(EffectEntity effect, GameObject target, Integer effectModelId, long currTime,
			SkillEffectHandler effectHandler, boolean isolated)
	{
		EffectModel model = StaticDataManager.EFFECT_DATA.getEffectModel(effectModelId);
		if (model == null)
			return;

		if (target == null)
			return;

		WarInstance war = effect.getWar();

		EffectEntity effectEntity = new EffectEntity(war, model);
		effectEntity.setState(SkillManager.CREATE);
		effectEntity.setCreateTime(currTime);
		effectEntity.setParentId(effect.getObjectId());
		effectEntity.addTarget(target.getObjectId());
		effectEntity.setTemporary(true);
		if (!isolated)
			((HavingSkill) target).getAttachEffects().addTempEffect(effectEntity.getObjectId());

		if (effectHandler != null)
			effectEntity.setEffectHandler(effectHandler);

		war.getSkillManager().addEffect(effectEntity);
	}
	
	/**
	 * 创建临时效果立即执行
	 * 
	 * @param effect
	 * @param currTime
	 * @param target
	 * @param war
	 * @param effectModelId
	 * @param effectHandler
	 *            效果执行后的回调
	 * @param isolated
	 *            是否独立的效果
	 * @return
	 */
	public static void castTmpEffectImmediately(EffectEntity effect, GameObject target, Integer effectModelId, long currTime,
			SkillEffectHandler effectHandler, boolean isolated)
	{
		EffectModel model = StaticDataManager.EFFECT_DATA.getEffectModel(effectModelId);
		if (model == null)
			return;

		if (target == null)
			return;

		WarInstance war = effect.getWar();

		EffectEntity effectEntity = new EffectEntity(war, model);
		effectEntity.setState(SkillManager.START);
		effectEntity.setCreateTime(currTime);
		effectEntity.setStartTime(currTime);
		effectEntity.setParentId(effect.getObjectId());
		effectEntity.addTarget(target.getObjectId());
		effectEntity.setTemporary(true);
		if (!isolated)
			((HavingSkill) target).getAttachEffects().addTempEffect(effectEntity.getObjectId());

		if (effectHandler != null)
			effectEntity.setEffectHandler(effectHandler);

		war.getSkillManager().addEffect(effectEntity);
	}

	/**
	 * 创建包含坐标的临时效果
	 * 
	 * @param effect
	 * @param currTime
	 * @param target
	 * @param war
	 * @param effectModelId
	 * @return
	 */
	public static void castTmpEffectWithPoint(EffectEntity effect, GameObject target, Integer effectModelId,
			long currTime)
	{
		EffectModel model = StaticDataManager.EFFECT_DATA.getEffectModel(effectModelId);
		if (model == null)
			return;

		if (target.getObjectType() != GameObjectType.TROOP && target.getObjectType() != GameObjectType.POINT)
			return;

		if (target.getObjectType() == GameObjectType.POINT)
			target.destroy();

		WarInstance war = effect.getWar();

		EffectEntity effectEntity = new EffectEntity(war, model);
		effectEntity.setState(SkillManager.CREATE);
		effectEntity.setCreateTime(currTime);
		effectEntity.setParentId(effect.getObjectId());
		effectEntity.addTarget(target.getObjectId());
		effectEntity.setTargetX(((WarSceneObject) target).getX());
		effectEntity.setTargetY(((WarSceneObject) target).getY());
		effectEntity.setTemporary(true);
		if (target instanceof HavingSkill)
			((HavingSkill) target).getAttachEffects().addTempEffect(effectEntity.getObjectId());
		war.getSkillManager().addEffect(effectEntity);
	}

	/**
	 * 地图炮,可以为非绑定技能
	 * 
	 * @param control
	 * @param skillModelId
	 * @param x
	 * @param y
	 */
	public static void castControllerSkill(WarControlInstance control, int skillModelId, int x, int y,
			SkillCastHandler castHandler)
	{
		long currTime = System.currentTimeMillis();
		SkillModel skillModel = StaticDataManager.SKILL_DATA.getModel(skillModelId);
		if (skillModel == null)
			return;

		WarInstance war = control.getWar();
		List<SkillEntity> skillList = control.getBoundSkills();
		SkillEntity skillEntity = null;
		if (skillList != null && !skillList.isEmpty())
		{
			for (SkillEntity ownedSkill : skillList)
			{
				if (ownedSkill.getModelId() == skillModelId)
				{
					if (skillModel.isPassive())
						break;

					skillEntity = ownedSkill;
				}
			}
		}

		if (skillEntity == null)
		{
			skillEntity = new SkillEntity(war, skillModel, control, castHandler);
			control.bindNewSkill(skillEntity);
		}

		skillEntity.setState(SkillManager.CREATE);
		skillEntity.setCreateTime(currTime);
		skillEntity.setTargetX(x);
		skillEntity.setTargetY(y);
		war.getSkillManager().addSkill(skillEntity);
	}

	/**
	 * 主动释放单位技能
	 * @param attack
	 * @param baseSkillId
	 * @param object
	 * @param targetX
	 * @param targetY
	 */
	public static void caseVisibleSkill(WarObjectInstance attack, int baseSkillId, GameObject object, float targetX,
			float targetY)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 客户端主动释放技能
	 * @param player
	 * @param skillParams
	 * @return
	 */
	@Deprecated
	public static ErrorCode castSkillByObj(PlayerEntity player, SkillParams skillParams)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取召唤技能的召唤troopModel,只针对简单召唤技能
	 * @param skill
	 * @return
	 */
	public static TroopModel getSummonSkillTroopModel(SkillEntity skill)
	{
		if (skill == null)
			return null;

		EffectEntity effect = skill.getCurrentEffect();
		if (effect == null)
			return null;

		EffectType type = EffectType.getTypeById(effect.getPrototypeId());
		if (type == null || type != EffectType.SUMMON)
			return null;

		SummonUnitPrototype prototype = (SummonUnitPrototype) effect.getEffectPrototype();
		int attrId = prototype.getAttrId();
		TroopAttrN2Model attrModel = StaticDataManager.ATTR_DATA.getAttrModel(attrId);
		if (attrModel == null)
			return null;
		
		TroopModel troopModel = StaticDataManager.TROOP_DATA.getById(attrModel.getTroopModelId());
		return troopModel;
	}
}
