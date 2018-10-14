package com.garowing.gameexp.game.rts.skill.script.effect.active;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.fight.action.HpAction;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.yeto.war.fightcore.fight.state.FightStateService;
import com.yeto.war.fightcore.scene.WarMoveService;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.fight.model.HpUpdateType;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_KNOCKBACK;
import com.yeto.war.utils.MathUtil;

import commons.configuration.Property;

/**
 * 击退效果
 * @author seg
 *
 */
public class KnockbackPrototype extends EffectPrototype
{
	/**
	 * 击退效果类型, 0-敌方，1-敌退我进
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float type;
	
	/**
	 * 最大击退距离
	 */
	@Property(key = EffectKey.FACTOR_B, defaultValue = "1")
	private float maxDistance;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		List<GameObject> targets = effect.getTargets();
		if(targets == null || targets.isEmpty())
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
		if(effect.getTargetIds().isEmpty())
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
		
		Troop troopCaster = (Troop) caster;
		int effectId = effect.getObjectId();
		int effectModelId = effect.getPrototypeId();
		List<Integer> newTarget = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			if((troopTarget.getFightStates().getMetaStates() & FightStateEnum.IMMUNE_KNOCKBACK.getStates()) > 0)
			{
				HpAction action = new HpAction(troopTarget, caster, 0, effect.getObjectId(), effect.getPrototypeId(), HpUpdateType.IMMUSE);
				troopTarget.addAction(action);
				continue;
			}
			
			float dx = troopTarget.getX() - troopCaster.getX();
			float dy = troopTarget.getY() - troopCaster.getY();
			
			float[] unitVector = MathUtil.getUnitVector(new float[]{dx, dy});
			
			newTarget.add(troopTarget.getObjectId());
			if(type == 0)
			{
				float[] targetData = knockback(troopTarget, troopCaster, unitVector, effect);
				
				SendPacketUtil.sendObjectWarPacket(caster, (sender)->
				{
					sender.sendPacket(new GC_KNOCKBACK(sender.getId(), 0, effectId, effectModelId,
							troopCaster.getObjectId(), troopCaster.getX(), troopCaster.getY(), troopCaster.getX(), troopCaster.getY(), 
							target.getObjectId(), targetData[0], targetData[1], targetData[2], targetData[3], new ArrayList<>()));
				}
				);
			}
			else if(type == 1)
			{
				float[] targetData = knockback(troopTarget, troopCaster, unitVector, effect);
				
				// 对于冲锋只是位移到相对位置
				float ownerAfterX = troopCaster.getX() + (targetData[2]-targetData[0]);
				float ownerAfterY = troopCaster.getY() + (targetData[3]-targetData[1]);
				float[] ownerData =  new float []{troopCaster.getX(), troopCaster.getY(), ownerAfterX, ownerAfterY};
				if ((targetData[2]-targetData[0]) != 0f || (targetData[3]-targetData[1]) != 0f)
					WarMoveService.getInstance().changePoint(troopCaster, ownerAfterX, ownerAfterY);
				
				SendPacketUtil.sendObjectWarPacket(troopCaster, (sender)->{
					sender.sendPacket(new GC_KNOCKBACK(sender.getId(), 0, effectId, effectModelId,
							troopCaster.getObjectId(), ownerData[0], ownerData[1], ownerData[2], ownerData[3], 
							target.getObjectId(), targetData[0], targetData[1], targetData[2], targetData[3], new ArrayList<>()));
				});
			}
		}
		
		effect.setTargetIds(newTarget);
		return SkillError.SUCCESS;
	}

	/**
	 * 击退计算
	 * @param troopTarget
	 * @param troopCaster
	 * @param unitVector
	 * @return
	 */
	private float[] knockback(Troop troopTarget, Troop troopCaster, float[] unitVector, EffectEntity effect)
	{
		float x = troopTarget.getX();
		float y = troopTarget.getY();
		float lastX = x;
		float lastY = y;
		
		for (float dis = 1; dis<=this.maxDistance; ++dis)
		{
			float currentX = lastX + unitVector[0];
			float currentY = lastY + unitVector[1];
			
			// 不可以移动到达的点
			if((int)currentX != (int)x || (int)currentY != (int)y)
			{
				if (!WarSceneService.getInstance().isMovePostionIgnoreUnit(troopTarget.getWar(), currentX, currentY))
				{
					break;
				}
			}
				
			
			if (WarMoveService.getInstance().isCollisionOthers(troopTarget, currentX, currentY, troopTarget.getCollisionRadius() + 1, troopCaster.getObjectId()))
			{
				break;
			}
			
			lastX = currentX;
			lastY = currentY; 
		}
		
		if ((lastX-x) != 0f || (lastY-y) != 0f)
		{
			WarMoveService.getInstance().changePoint(troopTarget, lastX, lastY);
			FightStateService.addStateEffect(troopTarget, FightStateEnum.KNOCKBACK, effect);
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
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target == null || target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			FightStateService.removeStateEffect((WarObjectInstance) target, FightStateEnum.KNOCKBACK, effect);
		}
		
		return SkillError.SUCCESS;
	}

}
