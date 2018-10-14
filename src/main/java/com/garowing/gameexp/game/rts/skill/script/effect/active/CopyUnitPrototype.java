package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarCampInstance;
import com.yeto.war.module.card.CardObject;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.module.troop.model.TroopAttrN2Model;
import com.yeto.war.module.troop.model.TroopModel;

import commons.configuration.Property;

/**
 * 复制单位
 * 
 * @author seg
 *
 */
public class CopyUnitPrototype extends AbstractSummonPrototype
{
	/**
	 * 位置类型,0-己方基地 1-目标附近
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float positionType;
	
	/**
	 * 优先召唤方向， 0-随机，1-向前，2-向后
	 */
	@Property(key = EffectKey.FACTOR_F, defaultValue = "0")
	private float direction;

	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		if (effect.getTargetIds().isEmpty() && effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;

		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if (targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;

		// TODO 获取一个单位多次复制
		effect.setTargets(targets);
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = effect.getTargets();
		if (targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;

		GameObject target = targets.get(0);
		if (target.getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_EFFECT_NO_TARGET;

		Troop troopTarget = (Troop) target;
		if (!troopTarget.isLive())
			return SkillError.ERROR_EFFECT_NO_TARGET;

		HavingCamp caster = effect.getCaster();
		if (caster == null)
			return SkillError.ERROR_EFF_CALL_TROOP_NO_CONTROL;
		
		if(caster instanceof HavingSkill)
		{
			int exists = ((HavingSkill)caster).getAttachEffects().getChildrenCount(effect.getPrototypeId());
			int modifiedMaxSummon = 0;
			if(((GameObject)caster).getObjectType() == GameObjectType.TROOP)
				modifiedMaxSummon = (int) ((Troop)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.SUMMON_NUM_ADD, effect);
			
			int maxSummonNum = maxNum + modifiedMaxSummon;
			if(exists >= maxSummonNum)
				return SkillError.ERROR_CHILD_SUM_NO_CHILD;
		}

		TroopAttrN2Model attrModel = troopTarget.getAttrModel();
		TroopModel troopModel = troopTarget.getModel();
		float bornX = troopTarget.getX();
		float bornY = troopTarget.getY();
		if (positionType == 0)
		{
			Troop general = WarUtils.getGeneralByControl(caster.getControl());
			if (general != null)
			{
				bornX = general.getX();
				bornY = general.getY();
			}
		}

		Troop troop = new Troop(caster.getControl(), troopModel, attrModel);
		float[] xy = WarSceneService.getInstance().findBornXYOptimized(troop, troopModel.getCollisionRadius(), bornX, bornY, (int) direction);
		if (xy == null || xy.length <= 1 || !WarSceneService.getInstance().isMovePostionIgnoreUnit(troop.getWar(), xy[0], xy[1]))
		{
			xy = WarSceneService.getInstance().findBornXY(effect.getWar(), troopModel.getCollisionRadius(), bornX, bornY,
					troop.getModelType(), troopModel.getCollisionRadius());
			if (xy == null || xy.length <= 1)
				return SkillError.ERROR_EFFECT_NO_TARGET;
		}
		
		troop.appendHP(troopTarget.getHp());
		troop.appendStar(troopTarget.getStar());
		troop.setCallerId(((GameObject) caster).getObjectId());
		switch (((GameObject)caster).getObjectType())
		{
		case GameObjectType.TROOP:
			troop.setCallerType(1);
			break;
		case GameObjectType.STRATEGOS:
			troop.setCallerType(2);
			break;
		default:
			break;
		}
		CardObject sourceCard = troopTarget.getSourceCard();
		if(sourceCard == null)
		{
			troop.appendMaxHP(troopTarget.getMaxHp()).appendEffects(troopTarget.getInitEffects());
		}else
		{
			troop.appendCard(sourceCard);
		}
		
		long endTime = currTime + liveTime;
		if(liveTime > 0)
			troop.setEndTime(endTime);
		
		if(aiDelay > 0)
			troop.setInitAIDelay((int) aiDelay);
		
		WarCampInstance camp = effect.getWar().getCamp(troop.getCampId());
		
		FightEngine.callTroop(troop, xy[0], xy[1], camp.getModel().getDirection(), effect.getPrototypeId());
		
		if(caster instanceof HavingSkill)
			((HavingSkill)caster).getAttachEffects().addChild(effect, troop);
		
		return SkillError.SUCCESS;
	}

}
