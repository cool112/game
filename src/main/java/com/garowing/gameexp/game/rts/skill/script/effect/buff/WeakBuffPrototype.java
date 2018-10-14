package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.yeto.war.fightcore.fight.attr.FixValue;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.yeto.war.fightcore.fight.attr.ModifiedValueKey;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.yeto.war.fightcore.fight.state.FightStateService;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.manager.AttributesPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_TROOP_ATTR;

import commons.configuration.Property;

/**
 * 麻痹buff
 * @author seg
 *
 */
public class WeakBuffPrototype extends AbstractBuffPrototype
{

	/**
	 * cd增加百分比
	 */
	@Property(key = EffectKey.FACTOR_A, defaultValue = "0")
	private float cdAddPercent;
	
	/**
	 * 移速修正
	 */
	@Property(key = EffectKey.ATTRIBUTES, defaultValue = "", propertyTransformer = AttributesPropertyTransformer.class)
	private AttrEntity moveSpeedAttr;
	
	/**
	 * 计算类型
	 */
	@Property(key = EffectKey.CALCULATE_TYPE, defaultValue = "0")
	private int calType;
	
	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			FightStateService.addStateEffect(troopTarget, FightStateEnum.WEAK, effect);
			AttrEntity attrEntity = troopTarget.getAttrEntity();
			attrEntity.add(moveSpeedAttr);
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), attrEntity.getAttrMap()));
			});
			
			for(Integer normalAtkId : StaticDataManager.SKILL_DATA.getNormalAttackIds())
			{
				troopTarget.getAttrModList().addModifiedValue(ModifiedType.CD_ADD, effect, new ModifiedValueKey(normalAtkId, 0, 0), FixValue.valueOf(calType, cdAddPercent));
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
		List<GameObject> targets = effect.getTargets();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troop = (Troop) target;
			AttrEntity attrEntity = troop.getAttrEntity();
			attrEntity.reduce(moveSpeedAttr);
			SendPacketUtil.sendObjectWarPacket(target, (sender)->{
				sender.sendPacket(new GC_TROOP_ATTR(sender.getId(), 0, target.getObjectId(), attrEntity.getAttrMap()));
			});
			FightStateService.removeStateEffect(troop, FightStateEnum.WEAK, effect);
			troop.getAttrModList().removeModifiedValue(ModifiedType.CD_ADD, effect);
			sendDelBuffMsg(target, effect);
		}
		return SkillError.SUCCESS;
	}

}
