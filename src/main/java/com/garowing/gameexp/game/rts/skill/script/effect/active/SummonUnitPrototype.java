package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.List;

import org.apache.log4j.Logger;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.attr.AttrHelp;
import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.yeto.war.fightcore.attr.model.AttrKey;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.GeneralSkillEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarCampInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.card.CardObject;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.module.troop.TroopEngine;
import com.yeto.war.module.troop.model.TroopAttrN2Model;
import com.yeto.war.module.troop.model.TroopModel;

import commons.configuration.Property;

/**
 * 召唤单位效果原型
 * @author seg
 *
 */
public class SummonUnitPrototype extends AbstractSummonPrototype
{
	private static Logger mLogger = Logger.getLogger(SummonUnitPrototype.class);
	
	/**
	 * 单位模板id
	 */
	@Property(key = EffectKey.ATTR_ID, defaultValue = "0")
	private int attrId;
	
	/**
	 * 血量继承系数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "1.0")
	private float inheritHpCoe;
	
	/**
	 * 攻击继承系数
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "1.0")
	private float inheritAttackCoe;

	/**
	 * 优先召唤方向
	 */
	@Property(key = EffectKey.FACTOR_F, defaultValue = "0.0")
	private float direction;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		List<GameObject> targets = effect.getTargets();
		if(targets != null )
		{
			for(GameObject target : targets)
			{
				if(target.getObjectType() != GameObjectType.TROOP)
				{
					effect.removeTarget(target.getObjectId());
					continue;
				}
				
			}
		}
		
		if(effect.getTargetIds().isEmpty() && effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		GameObject caster = effect.getCaster();
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
		
		TroopAttrN2Model attrModel = TroopEngine.getAttrModel(attrId);
		if (attrModel == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		TroopModel troopModel = TroopEngine.getTroopModel(attrModel.getTroopModelId());
		if (troopModel == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		int initMaxHp = 0;
		int initAp = 0;
		int callerType = 0;
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
			Troop troop = (Troop) caster;
			initMaxHp = (int)(((float)troop.getMaxHp() * inheritHpCoe)); 
			initAp = (int)(((float)troop.getAttack() * inheritAttackCoe));
			callerType = 1;
		}
		else if(caster.getObjectType() == GameObjectType.STRATEGOS)
		{
			GeneralSkillEntity skill = (GeneralSkillEntity) effect.getSkill();
			AttrEntity attrEntity = attrModel.getAttrEntity();
			initMaxHp = (int) (attrEntity.getValue(AttrKey.HP) + (skill.getLevel() * inheritHpCoe));
			initAp = (int) (attrEntity.getValue(AttrKey.ATTACK) + (skill.getLevel() * inheritAttackCoe));
			callerType = 2;
		}
			

		float x = effect.getTargetX();
		float y = effect.getTargetY();
		
		List<GameObject> targets = getTargets(effect);
		if(targets != null && !targets.isEmpty())
		{
			x = ((WarObjectInstance)targets.get(0)).getX();
			y = ((WarObjectInstance)targets.get(0)).getY();
		}
		
		long endTime = currTime + liveTime;
		
		if(x == 0 && y == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		Troop troop = new Troop(((HavingCamp)caster).getControl(), troopModel, attrModel);
		float[] xy = WarSceneService.getInstance().findBornXYOptimized(troop, troopModel.getCollisionRadius(), x, y, (int) direction);
		if (xy == null || xy.length <= 1 || !WarSceneService.getInstance().isMovePostionIgnoreUnit(troop.getWar(), xy[0], xy[1]))
		{
			xy = WarSceneService.getInstance().findBornXY(effect.getWar(), troopModel.getCollisionRadius(), x, y, troop.getModelType(), troopModel.getCollisionRadius());
			if(xy == null || xy.length <= 1)
				return SkillError.ERROR_EFFECT_NO_TARGET;
		}
			
		troop.appendAttrID(attrModel.getID()).appendMaxHP(initMaxHp).appendAP(initAp);
		troop.setCallerId(caster.getObjectId());
		troop.setCallerType(callerType);
		if(caster.getObjectType() == GameObjectType.CARD)
			troop.appendCard((CardObject) caster).appendStar(((CardObject) caster).getStar());
		else if(caster.getObjectType() == GameObjectType.TROOP || caster.getObjectType() == GameObjectType.STRATEGOS)
			troop.appendEffects(attrModel.getAttachEffects()).appendStar(attrModel.getStar());
		
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

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

	public int getAttrId()
	{
		return attrId;
	}


	public float getInheritFactor()
	{
		return inheritHpCoe;
	}
	

}
