package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.network.gs.sendable.fight.GC_ADD_BUFF;
import com.yeto.war.network.gs.sendable.fight.GC_REMOVE_BUFF;

/**
 * 抽象buff原型
 * @author seg
 *
 */
abstract public class AbstractBuffPrototype extends EffectPrototype
{
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
		}
		
		if(effect.getTargetIds().isEmpty() && effect.getTargetX() == 0 && effect.getTargetY() == 0)
			return SkillError.ERROR_EFFECT_NO_TARGET;
		
		return SkillError.SUCCESS;
	}

	/**
	 * 发送增加buff消息
	 * @param target
	 * @param effect
	 */
	protected void sendAddBuffMsg(GameObject target, EffectEntity effect)
	{
		WarInstance war = effect.getWar();
		int effectId = effect.getObjectId();
		int skillModelId = effect.getSkillModelId();
		int effectModelId = effect.getPrototypeId();
		
		SendPacketUtil.sendWarPacket(war, (sender)->{
			sender.sendPacket(new GC_ADD_BUFF(sender.getId(), 0, effectId, skillModelId, 
					effectModelId, target.getObjectId(), effect.getStartTime(), 0));
		});
	}
	
	/**
	 * 发送删除buff消息
	 * @param target
	 * @param effect
	 */
	protected void sendDelBuffMsg(GameObject target, EffectEntity effect)
	{
		WarInstance war = effect.getWar();
		int effectId = effect.getObjectId();
		int skillModelId = effect.getSkillModelId();
		int effectModelId = effect.getPrototypeId();
		
		SendPacketUtil.sendWarPacket(war, (sender)->{
			sender.sendPacket(new GC_REMOVE_BUFF(sender.getId(), 0, effectId, 
					skillModelId, effectModelId, target.getObjectId()));
		});
	}

}
