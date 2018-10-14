package com.garowing.gameexp.game.rts.skill.script.effect.active;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.module.troop.TroopEngine;
import com.yeto.war.utils.TimeUtil;

import commons.configuration.Property;

/**
 * 重生 效果原地复活,死亡触发器需要相应延时，会释放出场技能
 * @author seg
 *
 */
public class RebirthPrototype extends AbstractSummonPrototype
{
	/**
	 * 血量继承系数
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "1.0")
	private float inheritHpCoe;
	
	/**
	 * 攻击继承系数
	 */
	@Deprecated
	@Property(key = EffectKey.FACTOR_B, defaultValue = "1.0")
	private float inheritAttackCoe;
	
	@Override
	public SkillError onCheck(EffectEntity effect)
	{
		GameObject caster = effect.getCaster();
		if(caster == null || caster.getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		WarInstance war = effect.getWar();
		
		GameObject caster = effect.getCaster();
		if(caster == null || caster.getObjectType() != GameObjectType.TROOP)
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		Troop troopCaster = (Troop) caster;
		if(troopCaster.isLive())
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		int campId = troopCaster.getCampId();
		int energy = troopCaster.getModel().getFood();
		if(!war.getGraveMgr().contains(campId, energy, troopCaster.getObjectId()))
			return SkillError.ERROR_EFFECT_NO_ATTACK;
		
		if(liveTime > 0)
			troopCaster.setEndTime(currTime + liveTime * TimeUtil.SECOND); 
		
		if(aiDelay > 0)
			troopCaster.setInitAIDelay((int) aiDelay);
		
		float originX = troopCaster.getX();
		float originY = troopCaster.getY();
		float[] xy = WarSceneService.getInstance().findBornXYOptimized(troopCaster, troopCaster.getCollisionRadius(), originX, originY);
		if (xy == null || xy.length <= 1 || !WarSceneService.getInstance().isMovePostionIgnoreUnit(war, xy[0], xy[1]))
		{
			xy = WarSceneService.getInstance().findBornXY(war, troopCaster.getCollisionRadius(), originX, originY, troopCaster.getModelType(), troopCaster.getCollisionRadius());
			if(xy == null || xy.length <= 1)
				return SkillError.ERROR_EFFECT_NO_TARGET;
		}
		
		//TODO 删除出场技能？
		troopCaster.appendHP((int) (inheritHpCoe * troopCaster.getMaxHp()));
		FightEngine.callTroop(troopCaster, xy[0], xy[1], (int) troopCaster.getD(), effect.getObjectId());
		
		war.getGraveMgr().removeTomb(campId, energy, troopCaster.getObjectId());
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

}
