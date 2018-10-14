//package com.garowing.gameexp.game.rts.ai.behaviac.agent;
//
//import com.garowing.gameexp.game.rts.ai.GameObject;
//import com.garowing.gameexp.game.rts.ai.GameObjectType;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentClass;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentMethod;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
//import com.garowing.gameexp.game.rts.ai.skill.entity.SkillEntity;
//import com.garowing.gameexp.game.rts.ai.skill.manager.SkillManager;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarInstance;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarObjectInstance;
//import com.yeto.war.module.troop.Troop;
//import com.yeto.war.utils.MathUtil;
//
///**
// * 治疗ai
// * @author seg
// *
// */
//@AgentClass
//public class CureAIObj extends BaseAIObj
//{
//
//	public CureAIObj(WarInstance war)
//	{
//		super(war);
//	}
//	
//	public static CureAIObj valueOf(GameObject obj)
//	{
//		CureAIObj agent = new CureAIObj(obj.getWar());
//		agent.ownerId = obj.getObjectId();
//		if(obj.getObjectType() == GameObjectType.TROOP)
//			agent.attackRange = ((Troop)obj).getAtkDis();
//		
//		return agent;
//	}
//	
//	/**
//	 * 是否在攻击范围内
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus isInRange()
//	{
//		GameObject owner = getWar().getObject(ownerId);
//		if(owner == null || owner.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject target = getWar().getObject(targetId);
//		if(target == null || target.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		if(!MathUtil.isInRange((Troop)owner, (Troop)target, getAttackRange()))
//			return EBTStatus.BT_FAILURE;
//		
////		System.err.println("in range[" + getAttackRange() + "] target[" + targetId +"] skill[" + chosenSkillId +"]");
//		this.playerCmd.set(false);
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 当前目标是否存活
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus isCurrTargetAlive()
//	{
//		if(targetId == 0)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject target = getWar().getObject(targetId);
//		if(target == null)
//			return EBTStatus.BT_FAILURE;
//		
//		if(target.getObjectType() == GameObjectType.TROOP && !((Troop)target).isLive())
//			return EBTStatus.BT_FAILURE;
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 攻击
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus attack()
//	{
//		WarObjectInstance owner = (WarObjectInstance) getWar().getObject(ownerId);
//		GameObject target = getWar().getObject(targetId);
//		if(target == null )
//			return EBTStatus.BT_FAILURE;
//		
//		/* 不可攻击 */
//		if(!owner.canAttack())
//			return EBTStatus.BT_FAILURE;
//		
//		if(!((Troop)owner).canAttack((Troop) target))
//			return EBTStatus.BT_FAILURE;
//		
//		if(chosenSkillId == 0)
//			return EBTStatus.BT_FAILURE;
//		
//		SkillEntity skill = (SkillEntity) getWar().getObject(chosenSkillId);
////		if(skill.getModel().isNormal() && ((HavingCamp)target).getCampId() != owner.getCampId())
////			return EBTStatus.BT_FAILURE;
//		
//		skill.getTargetIds().clear();
//		skill.getTargetIds().add(targetId);
//		skill.setState(SkillManager.CREATE);
//		skill.setCreateTime(System.currentTimeMillis());
//		getWar().getSkillManager().addSkill(skill);
//		
//		if(skill.getModelId() == 1000604)
//			System.err.println("ai attack[" + skill + "]");
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//}
