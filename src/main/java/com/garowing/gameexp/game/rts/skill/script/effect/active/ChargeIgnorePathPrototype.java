package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.yeto.war.fightcore.fight.state.FightStateService;
import com.yeto.war.fightcore.scene.WarMoveService;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.SkillEngine;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.WarUtils;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_KNOCKBACK;
import com.yeto.war.utils.MathUtil;

import commons.configuration.Property;

/**
 * 冲锋忽视路径
 * @author seg
 *
 */
public class ChargeIgnorePathPrototype extends EffectPrototype
{
	/**
	 * 最小冲锋距离
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float minDistance;
	
	/**
	 * 阵营类型，0-全部，1-敌方，2-友方
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "0")
	private float campType;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		if(effect.getTargetIds().isEmpty() && effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		GameObject caster = effect.getCaster();
		if(caster.getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		SkillEntity skill = effect.getSkill();
		if(skill == null)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		float maxDistance = skill.getCastRange();
		Troop troopCaster = (Troop) caster;
		int effectId = effect.getObjectId();
		int effectModelId = effect.getPrototypeId();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			float dx = troopTarget.getX() - troopCaster.getX();
			float dy = troopTarget.getY() - troopCaster.getY();
			
			float[] unitVector = MathUtil.getUnitVector(new float[]{dx, dy});
			float[] casterData = chargeTo(troopTarget, troopCaster, unitVector, effect, currTime, maxDistance);
			
			SendPacketUtil.sendObjectWarPacket(troopCaster, (sender)->{
				sender.sendPacket(new GC_KNOCKBACK(sender.getId(), 0, effectId, effectModelId,
						troopCaster.getObjectId(), casterData[0], casterData[1], casterData[2], casterData[3], 
						target.getObjectId(), troopTarget.getX(), troopTarget.getY(), troopTarget.getX(), troopTarget.getY(), new ArrayList<>()));
			});
		}
		
		return SkillError.SUCCESS;
	}

	/**
	 * 向目标冲锋
	 * @param troopTarget
	 * @param troopCaster
	 * @param unitVector
	 * @param effect
	 * @param currTime 
	 * @return
	 */
	private float[] chargeTo(Troop troopTarget, Troop troopCaster, float[] unitVector, EffectEntity effect, long currTime, float maxDistance)
	{
		float x = troopCaster.getX();
		float y = troopCaster.getY();
		float targetX = troopTarget.getX();
		float targetY = troopTarget.getY();
		float lastX = x;
		float lastY = y;
		
		for (float dis = 1; dis <= maxDistance; ++dis)
		{
			float currentX = lastX + unitVector[0];
			float currentY = lastY + unitVector[1];
			
			// 不可以移动到达的点
			if((int)currentX != (int)targetX || (int)currentY != (int)targetY)
			{
				if (!WarSceneService.getInstance().isMovePostionIgnoreUnit(troopCaster.getWar(), currentX, currentY))
				{
					System.err.println("block by line point:" + currentX + "," + currentY);
					break;
				}
			}
				
			List<WarObjectInstance> blockObjs = WarMoveService.getInstance().getCollisonUnits(troopCaster, currentX, currentY, troopCaster.getCollisionRadius(), troopTarget.getObjectId());
			if (blockObjs != null && !blockObjs.isEmpty())
			{
				WarObjectInstance blockObj = blockObjs.get(0);
				System.err.println("block by obj:" + blockObj.getObjectId());
				int campId = 0;
				if(campType == 1)
					campId = WarUtils.getEnemyCamp(troopCaster.getControl()).getObjectId();
				else if(campType == 2)
					campId = troopCaster.getCampId();
				
				if(campId > 0 && campId != blockObj.getCampId())
					break;
				
				if(subEffectIds != null && !subEffectIds.isEmpty())
				{
					for(Integer effectModelId : subEffectIds)
						SkillEngine.castTmpEffect(effect, blockObj, effectModelId, currTime, null, true);
				}
				
				break;
			}
			
			lastX = currentX;
			lastY = currentY; 
		}
		
		if ((lastX-x) != 0f || (lastY-y) != 0f)
		{
			WarMoveService.getInstance().changePoint(troopCaster, lastX, lastY);
			FightStateService.addStateEffect(troopCaster, FightStateEnum.CHARGE, effect);
		}
			
		
		return new float []{x, y, lastX, lastY};
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		FightStateService.removeStateEffect((WarObjectInstance) effect.getCaster(), FightStateEnum.CHARGE, effect);
		return SkillError.SUCCESS;
	}

	public float getMinDistance()
	{
		return minDistance;
	}
}
