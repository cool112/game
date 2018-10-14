//package com.garowing.gameexp.game.rts.ai.behaviac.agent;
//
//import com.garowing.gameexp.game.rts.ai.GameObject;
//import com.garowing.gameexp.game.rts.ai.GameObjectType;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarInstance;
//import com.yeto.war.module.troop.Troop;
//
///**
// * 不受玩家控制ai
// * @author seg
// *
// */
//public class LostControlAIObj extends BaseAIObj
//{
//
//	public LostControlAIObj(WarInstance war)
//	{
//		super(war);
//	}
//	
//	public static LostControlAIObj valueOf(GameObject owner)
//	{
//		LostControlAIObj agent = new LostControlAIObj(owner.getWar());
//		agent.ownerId = owner.getObjectId();
//		if(owner.getObjectType() == GameObjectType.TROOP)
//			agent.attackRange = ((Troop)owner).getAtkDis();
//		
//		return agent;
//	}
//	
//	@Override
//	synchronized public EBTStatus forceTarget(float x, float y)
//	{
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	@Override
//	synchronized public EBTStatus forceTarget(int targetId)
//	{
//		return EBTStatus.BT_SUCCESS;
//	}
//
//}
