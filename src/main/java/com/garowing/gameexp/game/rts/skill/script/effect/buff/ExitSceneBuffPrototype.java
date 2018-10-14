package com.garowing.gameexp.game.rts.skill.script.effect.buff;

import java.util.ArrayList;
import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.SendPacketUtil;
import com.yeto.war.fightcore.scene.WarSceneService;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.network.gs.sendable.fight.GC_TROOP_EXIT;

/**
 * 退出场景buff,buff结束时推送离场动作
 * @author seg
 *
 */
public class ExitSceneBuffPrototype extends AbstractBuffPrototype
{

	@Override
	public SkillError onStart(EffectEntity effect, long currTime)
	{
		List<GameObject> targets = getTargets(effect);
		if(targets == null || targets.isEmpty())
		{
			if(needTarget)
				return SkillError.ERROR_BUFF_NO_TARGET;
			else
				return SkillError.SUCCESS;
		}
		
		List<Integer> newTargetIds = new ArrayList<Integer>();
		for(GameObject target : targets)
		{
			if(target.getObjectType() != GameObjectType.TROOP)
				continue;
			
			Troop troopTarget = (Troop) target;
			if(!troopTarget.isValidTarget(effect))
				continue;
			
			newTargetIds.add(troopTarget.getObjectId());
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
			
			Troop troopTarget = (Troop) target;
			sendDelBuffMsg(troopTarget, effect);
			troopTarget.setLive(false);
			SendPacketUtil.sendObjectWarPacket(troopTarget, (sender)->{
				sender.sendPacket(new GC_TROOP_EXIT(sender.getId(), 0, troopTarget.getObjectId()));
			});
			
			WarSceneService.getInstance().exitScene((WarObjectInstance) target);
		}
		return SkillError.SUCCESS;
	}

}
