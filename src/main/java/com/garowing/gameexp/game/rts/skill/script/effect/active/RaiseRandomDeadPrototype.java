package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.ArrayList;
import java.util.Collection;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.model.WarCampModel;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarCampInstance;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.RatioUitl;
import com.yeto.war.utils.TimeUtil;

import commons.configuration.Property;

/**
 * 复活随机尸体
 * @author seg
 *
 */
public class RaiseRandomDeadPrototype extends AbstractSummonPrototype
{
	/**
	 * 阵营类型，0-全部，1-己方 ，2-敌方，3-友方
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float campType;
	
	/**
	 * 最大能量点
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float maxEnergy;
	
	/**
	 * 是否包含自己， 0-包含，1-不包含
	 */
	@Property(key = EffectKey.FACTOR_C, defaultValue = "0")
	private float excludeSelf;
	
	/**
	 * 优先召唤方向， 0-随机，1-向前，2-向后
	 */
	@Property(key = EffectKey.FACTOR_F, defaultValue = "0")
	private float direction;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		WarInstance war = effect.getWar();
		if(war.getGraveMgr().isEmpty())
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		SkillError result = onActivate(effect, currTime);
		if(result == SkillError.SUCCESS)
		{
			effect.addActivateCount();
			effect.setLastActivateTime(currTime);
			effect.setNextActivateTime(currTime + effect.getInterval());
		}
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		WarInstance war = effect.getWar();
		
		HavingCamp caster = effect.getCaster();
		if(caster == null)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
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
		
		WarControlInstance controllor = caster.getControl();
		
		int campId = 0;
		campId = getCampId(caster, controllor);
		
		int objId = getTombObjId(war, campId, caster);
		GameObject dead = war.getObject(objId);
		if(dead == null || dead.getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		Troop troopDead = (Troop) dead;
		int oldCampId = troopDead.getCampId();
		int energy = troopDead.getModel().getFood();
		
		troopDead.setControl(controllor);
		
		if(liveTime > 0)
			troopDead.setEndTime(currTime + liveTime * TimeUtil.SECOND); 
		
		if(aiDelay > 0)
			troopDead.setInitAIDelay((int) aiDelay);
		
		float[] xy = null;
		if(((GameObject)caster).getObjectType() == GameObjectType.TROOP)
		{
			xy = WarSceneService.getInstance().findBornXYNearTarget(troopDead, (WarObjectInstance) caster, troopDead.getCollisionRadius(), (int) direction);
			troopDead.setCallerType(1);
		}
		else if(((GameObject)caster).getObjectType() == GameObjectType.STRATEGOS)
		{
			Troop general = WarUtils.getGeneralByControl(controllor);
			if(general == null)
				return SkillError.ERROR_EFFECT_NO_ATTACK;
			
			xy = WarSceneService.getInstance().findBornXYNearTarget(troopDead, general, troopDead.getCollisionRadius(), (int) direction);
			troopDead.setCallerType(2);
		} else
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		WarCampInstance newCamp = war.getCamp(troopDead.getCampId());
		WarCampModel campModel = newCamp.getModel();
		troopDead.setCallerId(((GameObject)caster).getObjectId());
		FightEngine.callTroop(troopDead, xy[0], xy[1],  campModel == null ? 0 :campModel.getDirection(), effect.getPrototypeId());
		
		if(caster instanceof HavingSkill)
		{
			((HavingSkill)caster).getAttachEffects().addChild(effect, troopDead);
		}
		
		war.getGraveMgr().removeTomb(oldCampId, energy, objId);
		
		return SkillError.SUCCESS;
	}

	/**
	 * 获取阵营id
	 * @param caster
	 * @param controllor
	 * @param campId
	 * @return
	 */
	private int getCampId(HavingCamp caster, WarControlInstance controllor)
	{
		int campId = 0;
		switch ((int)campType)
		{
		case 1:
			campId = caster.getCampId();
			break;
		case 2:
			campId = WarUtils.getEnemyCampId(controllor);
			break;
		default:
			break;
		}
		return campId;
	}
	
	/**
	 * 获取墓碑id
	 * @param war
	 * @param campId
	 * @param caster
	 * @return
	 */
	private int getTombObjId(WarInstance war, int campId, HavingCamp caster)
	{
		Collection<Integer> objIds = war.getGraveMgr().getTombs(campId, (int) maxEnergy);
		if(objIds == null || objIds.isEmpty())
			return 0;
		
		if((int)excludeSelf == 1)
		{
			if(((GameObject)caster).getObjectType() == GameObjectType.TROOP && !((Troop)caster).isLive())
				objIds.remove(((GameObject)caster).getObjectId());
		}
		
		int objId = RatioUitl.random(new ArrayList<>(objIds));
		return objId;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		return SkillError.SUCCESS;
	}

}
