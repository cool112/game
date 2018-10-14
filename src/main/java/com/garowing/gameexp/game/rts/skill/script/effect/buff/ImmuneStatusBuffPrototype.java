package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.yeto.war.fightcore.fight.state.FightStateService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;

import commons.configuration.Property;

/**
 * 免疫状态buff
 * @author seg
 *
 */
public class ImmuneStatusBuffPrototype extends AbstractBuffPrototype
{
	/**
	 * 免疫状态列表
	 */
	@Property(key = EffectKey.STATUS, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	private List<Integer> statusIds;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		List<FightStateEnum> immuseStatus = new ArrayList<FightStateEnum>();
		for(Integer statusId : statusIds)
		{
			FightStateEnum status = FightStateEnum.getEnum(statusId);
			if(status != null && status.isImmune())
				immuseStatus.add(status);
		}
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			for(FightStateEnum status : immuseStatus)
			{
				FightStateService.addStateEffect(troopTarget, status, effect);
			}
			
			newTargetIds.add(target.getObjectId());
			sendAddBuffMsg(target, effect);
		}
		
		effect.setTargetIds(newTargetIds);
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onActivate(EffectEntity effect, long currTime)
	{
		return SkillError.SUCCESS;
	}

	@Override
	public SkillError onExit(EffectEntity effect)
	{
		List<FightStateEnum> immuseStatus = new ArrayList<FightStateEnum>();
		for(Integer statusId : statusIds)
		{
			FightStateEnum status = FightStateEnum.getEnum(statusId);
			if(status != null && status.isImmune())
				immuseStatus.add(status);
		}
		
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troop = (Troop) target;
			for(FightStateEnum status : immuseStatus)
			{
				FightStateService.removeStateEffect(troop, status, effect);
			}
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
