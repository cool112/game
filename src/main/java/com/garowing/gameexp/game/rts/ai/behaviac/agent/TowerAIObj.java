//package com.garowing.gameexp.game.rts.ai.behaviac.agent;
//
//import com.garowing.gameexp.game.rts.ai.GameObject;
//import com.garowing.gameexp.game.rts.ai.GameObjectType;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentClass;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentMethod;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.PlayerCmd;
//import com.garowing.gameexp.game.rts.ai.skill.entity.SkillEntity;
//import com.garowing.gameexp.game.rts.ai.skill.manager.SkillManager;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarInstance;
//import com.yeto.war.module.troop.Troop;
//import com.yeto.war.utils.MathUtil;
//
///**
// * 箭塔ai
// * @author seg
// *
// */
//@AgentClass
//public class TowerAIObj extends BaseAIObj
//{
//
//	public TowerAIObj(WarInstance war)
//	{
//		super(war);
//	}
//	
//	public static TowerAIObj valueOf(GameObject owner)
//	{
//		TowerAIObj agent = new TowerAIObj(owner.getWar());
//		agent.ownerId = owner.getObjectId();
//		if(owner.getObjectType() == GameObjectType.TROOP)
//			agent.attackRange = ((Troop)owner).getAtkDis();
//		
//		return agent;
//	}
//	
//	/**
//	 * 强制改变目标,不在攻击范围内失败
//	 * @param targetId2
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus forceTarget(int targetId)
//	{
//		WarInstance war = getWar();
//		GameObject owner = war.getObject(ownerId);
//		if(owner.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject target = getWar().getObject(targetId);
//		if(target.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		if(targetId == this.targetId)
//			return EBTStatus.BT_FAILURE;
//		
//		if(!MathUtil.isInRange((Troop)owner, (Troop)target, getAttackRange()))
//			return EBTStatus.BT_FAILURE;
//		
//		if(actSkillId > 0)
//		{
//			GameObject skill = getWar().getObject(actSkillId);
//			if(skill != null && skill.getObjectType() == GameObjectType.SKILL && ((SkillEntity)skill).getState() < SkillManager.IDLE)
//			{
//				((SkillEntity)skill).interrupt(owner, true);
//			}
//			else
//				actSkillId = 0;
//		}
//		
//		this.playerCmd.set(true);
//		this.lastCmd = new PlayerCmd(0, 0, targetId);
////		System.err.println("force target:" + targetId);
//		return EBTStatus.BT_SUCCESS;
//	}
//
//}
