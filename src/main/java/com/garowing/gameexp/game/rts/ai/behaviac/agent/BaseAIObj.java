//package com.garowing.gameexp.game.rts.ai.behaviac.agent;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import com.garowing.gameexp.game.rts.ai.GameObject;
//import com.garowing.gameexp.game.rts.ai.GameObjectType;
//import com.garowing.gameexp.game.rts.ai.attr.constants.UnitType;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentClass;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentMethod;
//import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentProperty;
//import com.garowing.gameexp.game.rts.ai.behaviac.constants.EBTStatus;
//import com.garowing.gameexp.game.rts.ai.behaviac.entity.PlayerCmd;
//import com.garowing.gameexp.game.rts.ai.fight.FightEngine;
//import com.garowing.gameexp.game.rts.ai.scene.WarMoveService;
//import com.garowing.gameexp.game.rts.ai.scene.WarSceneService;
//import com.garowing.gameexp.game.rts.ai.scene.objects.WarSceneObject;
//import com.garowing.gameexp.game.rts.ai.skill.entity.EffectEntity;
//import com.garowing.gameexp.game.rts.ai.skill.entity.SkillEntity;
//import com.garowing.gameexp.game.rts.ai.skill.event.TargetChangeSkillEvent;
//import com.garowing.gameexp.game.rts.ai.skill.manager.SkillManager;
//import com.garowing.gameexp.game.rts.ai.war.WarUtils;
//import com.garowing.gameexp.game.rts.ai.war.objects.HavingCamp;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarInstance;
//import com.garowing.gameexp.game.rts.ai.war.objects.WarObjectInstance;
//import com.yeto.war.module.troop.Troop;
//import com.yeto.war.utils.MathUtil;
//
///**
// * 基础AI对象
// * @author seg
// * 2017年4月7日
// */
//@AgentClass
//public class BaseAIObj extends Agent
//{
//	/* 阵营类型 */
//	@AgentProperty
//	public final static int CAMP_ALL = 0;
//
//	@AgentProperty
//	public final static int CAMP_ENEMY = 2;
//
//	@AgentProperty
//	public final static int CAMP_FRIEND = 1;
//
//	/* 筛选类型 
//	 * 1-最小生命,但为了跟随可能会选择满血目标，治疗ai用
//	 * 2-攻击范围内距离最远，攻击范围外最近
//	 * 3-类型优先，其次距离最近
//	 * 4-随机
//	 * 5-距离最近*/
//	@AgentProperty
//	public final static int FILTER_MIN_HP = 1;
//	
//	@AgentProperty
//	public final static int FILTER_FARTHEST = 2;
//
//	@AgentProperty
//	public final static int FILTER_TYPE_AND_NEAREST = 3;
//
//	@AgentProperty
//	public final static int FILTER_RANDOM = 4;
//
//	@AgentProperty
//	public final static int FILTER_NEAREST = 5;
//
//	/* 兵种类型 */
//	public final static int TROOP_ALL = 0xffffffff;
//	
//	public final static int TROOP_BUILDING = UnitType.BUILDING.getMask();
//
//	public final static int TROOP_GENERAL = UnitType.GENERAL.getMask();
//
//	public final static int TROOP_MACHINE = UnitType.MACHINE.getMask();
//
//	public final static int TROOP_HUMAN_LIVING = UnitType.HUNMAN.getMask() | UnitType.LIVING_THING.getMask();
//	
//	public final static int TROOP_MONKEY_LIVING = UnitType.MONKEY.getMask() | UnitType.LIVING_THING.getMask();
//	
//	public final static int TROOP_LIVING_THING = UnitType.LIVING_THING.getMask();
//	
//	public final static int TROOP_SAINT_LIVING = UnitType.SAINT.getMask() | UnitType.LIVING_THING.getMask();
//	
//	@AgentProperty
//	public final static List<Integer> TROOP_B_G = Arrays.asList(TROOP_BUILDING, TROOP_GENERAL);
//	
//	@AgentProperty
//	public final static List<Integer> TROOP_B = Arrays.asList(TROOP_BUILDING);
//	
//	@AgentProperty
//	public final static List<Integer> TROOP_G = Arrays.asList(TROOP_GENERAL);
//	
//	@AgentProperty
//	public final static List<Integer> TROOP_M = Arrays.asList(TROOP_MACHINE);
//	
//	@AgentProperty
//	public final static List<Integer> TROOP_M_B = Arrays.asList(TROOP_MACHINE, TROOP_BUILDING);
//	
//	/**
//	 * 全部生物
//	 */
//	@AgentProperty
//	public final static List<Integer> TROOP_L = Arrays.asList(TROOP_LIVING_THING);
//	
//	/**
//	 * 人类和猿类生物
//	 */
//	@AgentProperty
//	public final static List<Integer> TROOP_HL_ML = Arrays.asList(TROOP_HUMAN_LIVING, TROOP_MONKEY_LIVING);
//	
//	/**
//	 * 人类猿类圣徒生物
//	 */
//	@AgentProperty
//	public final static List<Integer> TROOP_HL_ML_SL = Arrays.asList(TROOP_HUMAN_LIVING, TROOP_MONKEY_LIVING, TROOP_SAINT_LIVING);
//	
//	/**
//	 * 寻怪间隔
//	 */
//	private static final long TARGET_INTERVAL = 500L;
//	
//	/**
//	 * 寻路间隔，需要移动的格子数
//	 */
//	private static final int FIND_PATH_INTERVAL = 2;
//	
//	/**
//	 * 宿主id
//	 */
//	protected int ownerId;
//	
//	/**
//	 * 目标id
//	 */
//	protected int targetId;
//	
//	/**
//	 * 攻击距离
//	 */
//	@AgentProperty
//	protected float attackRange = 0;
//	
//	/**
//	 * 上次寻路时间戳
//	 */
//	private long lastPathTime;
//	
//	/**
//	 * 上次寻找目标时间戳
//	 */
//	@AgentProperty
//	protected long lastTargetTime;
//	
//	/**
//	 * 移动坐标x
//	 */
//	@AgentProperty
//	protected float targetX;
//	
//	/**
//	 * 移动坐标y
//	 */
//	@AgentProperty
//	protected float targetY;
//	
//	/**
//	 * 选择的技能id
//	 */
//	protected int chosenSkillId;
//	
//	/**
//	 * 是否有玩家操作
//	 */
//	protected AtomicBoolean playerCmd = new AtomicBoolean(false);
//	
//	/**
//	 * 玩家最后的指令
//	 */
//	protected PlayerCmd lastCmd;
//	
//	/**
//	 * 在动作中的技能
//	 */
//	protected int actSkillId;
//	
//	public BaseAIObj(WarInstance war)
//	{
//		super(war);
//	}
//	
//	public static BaseAIObj valueOf(GameObject obj)
//	{
//		BaseAIObj aiObj = new BaseAIObj(obj.getWar());
//		aiObj.ownerId = obj.getObjectId();
//		if(obj.getObjectType() == GameObjectType.TROOP)
//			aiObj.attackRange = ((Troop)obj).getAtkDis();
//		
//		return aiObj;
//	}
//	
//	/**
//	 * 选择技能
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus selectSkill()
//	{
////		long startTime = System.nanoTime();
//		if(actSkillId > 0)
//		{
//			SkillEntity actSkill = (SkillEntity) getWar().getObject(actSkillId);
//			if(actSkill != null && isActing(actSkill))
//				return EBTStatus.BT_FAILURE;
//		}
//		
//		long currentTime = System.currentTimeMillis();
//		WarObjectInstance owner = (WarObjectInstance) getWar().getObject(ownerId);
//		SkillEntity skill = owner.chooseSkill(currentTime, targetId, targetX, targetY);
//		if(skill != null)
//		{
//			this.chosenSkillId = skill.getObjectId();
//			updateAttackRange();
//		}
//		else
//			this.chosenSkillId = 0;
//		
////		if(owner.getModelID() == 10101)
////			System.err.println("select skill[" + skill + "]");
//		
////		System.err.println("selectSkill() :" + (System.nanoTime() - startTime));
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 是否正在动作
//	 * @param skill
//	 * @return
//	 */
//	private boolean isActing(SkillEntity skill)
//	{
//		if(!skill.canInterrupt() && skill.getState() != SkillManager.IDLE)
//			return true;
//		
//		if(skill.getModel().isParallel())
//		{
//			EffectEntity currEffect = skill.getCurrentEffect();
//			if(currEffect.getState() < SkillManager.ACTIVATE)
//				return true;
//		}
//		return false;
//	}
//
//	/**
//	 * 更新攻击距离
//	 */
//	private void updateAttackRange()
//	{
//		SkillEntity skill = (SkillEntity) getWar().getObject(chosenSkillId);
//		this.attackRange = skill.getCastRange();
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
//		if(target == null)
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
//		skill.getTargetIds().clear();
//		skill.getTargetIds().add(targetId);
//		skill.setState(SkillManager.CREATE);
//		skill.setCreateTime(System.currentTimeMillis());
//		getWar().getSkillManager().addSkill(skill);
//		
//		this.actSkillId = chosenSkillId;
//		
//		if(skill.getModelId() == 103 )
//			System.err.println("ai attack[" + skill + "]");
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 	寻找最近的敌人
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus findNearestEnemy()
//	{
//		return findTargetNoType(CAMP_ENEMY, FILTER_NEAREST, 0f);
//	}
//
//	/**
//	 * 通用找寻目标
//	 * @param campType 0全部 1敌方 2我方
//	 * @param troopTypes null-不限
//	 * @param filterType 
//	 * @param range 0-不限
//	 * @param restrictType 严格类型，非严格则只要包含即可
//	 * @param doubleFind 治疗类ai的特殊规则，视野内寻找非满血目标，全局寻找含满血目标
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus findTarget(int campType, List<Integer> troopTypes, int filterType, float range, boolean restrictType, boolean doubleFind)
//	{
////		long startTime = System.nanoTime();
//		long currTime = System.currentTimeMillis();
//		long lastTargetTime = getLastTargetTime();
//		GameObject oldTarget = getWar().getObject(targetId);
//		if(lastTargetTime > 0 && currTime - lastTargetTime < TARGET_INTERVAL)
//		{
//			if(oldTarget == null)
//				return EBTStatus.BT_FAILURE;
//			else
//				return EBTStatus.BT_SUCCESS;
//		}
//			
//		
//		Troop owner = (Troop) getWar().getObject(ownerId);
//		Troop target = WarUtils.findOneTroop(owner, campType, troopTypes, range, filterType, restrictType);
//		if(doubleFind && range > 0 && (target == null || target.isHpFull()))
//			target = WarUtils.findOneTroop(owner, campType, troopTypes, 0, filterType, restrictType);
////		System.err.println("WarUtils.findOneTroop :" + (System.nanoTime() - startTime));
//		setLastTargetTime(currTime);
//		if(target == null)
//		{
////			if(owner.getModelID() == 10015)
////				System.err.println("find target fail! owner:["+owner+"] campType:["+campType+"] types:["+troopTypes+"] range:["+range+"] filter:["+filterType+"]");
//			
//			stopMove();
//			this.targetId = 0;
//			return EBTStatus.BT_FAILURE;
//		}
//		
////		if(owner.getModelID() == 10015)
////			System.err.println("find target suc! owner:["+owner+"] campType:["+campType+"] types:["+troopTypes+"] range:["+range+"] filter:["+filterType+"]");
//		
//		if(targetId != target.getObjectId())
//		{
////			System.err.println("change target event");
//			owner.getWar().getSkillManager().addSkillEvent(new TargetChangeSkillEvent(getWar(), owner, target));
//			this.targetId = target.getObjectId();
//			this.targetX = 0;
//			this.targetY = 0;
//		}
//	
////		if(owner.getModelID() == 10101)
////			System.err.println("find target[" + targetId + "] x[" + targetX + "] y[" + targetY + "]");
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 以自己为目标
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus targetSelf()
//	{
//		this.targetId = ownerId;
//		this.targetX = 0;
//		this.targetY = 0;
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 寻找目标，不限制部队类型
//	 * @param campType
//	 * @param filterType
//	 * @param range
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus findTargetNoType(int campType, int filterType, float range)
//	{
//		return findTarget(campType, null, filterType, range, false, false);
//	}
//
//	/**
//	 * 当前目标存活，并且在攻击范围
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus isTargetAliveAndInRange()
//	{
//		EBTStatus result = isCurrTargetAlive();
//		if(result != EBTStatus.BT_SUCCESS)
//			return result;
//		
//		return isInRange();
//	}
//	
//	/**
//	 * 当前目标是否有效
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus isCurrTargetAlive()
//	{
//		if(targetId == 0)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject target = getWar().getObject(targetId);
//		if(target == null)
//			return EBTStatus.BT_FAILURE;
//		
//		if(target.getObjectType() == GameObjectType.TROOP && !((Troop)target).isLive())
//		{
//			playerCmd.set(false);
//			return EBTStatus.BT_FAILURE;
//		}
//		
//		WarObjectInstance owner = (WarObjectInstance) getWar().getObject(ownerId);
//		if(!((Troop)owner).canAttack((Troop) target))
//			return EBTStatus.BT_FAILURE;
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 是否在攻击范围内
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus isInRange()
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
////		if(((WarObjectInstance) owner).getModelID() == 10101)
////			System.err.println("in range[" + getAttackRange() + "]");
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 是否有敌人（忽略对方基地、将军）
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus hasEnemiesIgnoreGeneral()
//	{
//		WarInstance war = getWar();
//		GameObject owner = war.getObject(ownerId);
//		int campId = ((HavingCamp)owner).getCampId();
//		Collection<WarSceneObject> objects = WarUtils.getObjectsByEnemyNoJiDi(war, campId);
//		if(objects == null || objects.isEmpty())
//			return EBTStatus.BT_FAILURE;
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 宿主是否存活
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus isOwnerAlive()
//	{
//		WarInstance war = getWar();
//		if(war == null)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject owner = getWar().getObject(ownerId);
//		if(owner == null)
//			return EBTStatus.BT_FAILURE;
//		
//		if(owner.getObjectType() == GameObjectType.TROOP && !((Troop)owner).isLive())
//			return EBTStatus.BT_FAILURE;
//		
//		long currTime = getUpdateTime();
//		long endTime = ((Troop)owner).getEndTime();
//		if(endTime > 0 && endTime <= currTime)
//		{
//			FightEngine.objectDie((WarObjectInstance) owner, currTime);
//			return EBTStatus.BT_FAILURE;
//		}
//			
//		if(lastCmd != null)
//		{
//			System.err.println("player cmd[" + lastCmd.getTargetId() + ", " + lastCmd.getX() + ", " + lastCmd.getY() + "]");
//			int newTargetId = lastCmd.getTargetId();
//			if(this.targetId != newTargetId)
//			{
//				GameObject target = war.getObject(newTargetId);
//				war.getSkillManager().addSkillEvent(new TargetChangeSkillEvent(war, (Troop)owner, (Troop)target));
//				this.targetId = newTargetId;
//			}
//			
//			this.targetX = lastCmd.getX();
//			this.targetY = lastCmd.getY();
//			this.lastCmd = null;
//		}
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 向目標移動
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus move()
//	{
//		GameObject owner = getWar().getObject(ownerId);
//		if(owner.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject target = getWar().getObject(targetId);
//		if(target.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		if (!((Troop) owner).canMove())
//			return EBTStatus.BT_FAILURE;
//		
//		if(getWar().isSuspend())
//			return EBTStatus.BT_FAILURE;
//		
//		if(actSkillId > 0)
//		{
//			GameObject skill = getWar().getObject(actSkillId);
//			if(skill != null && skill.getObjectType() == GameObjectType.SKILL && ((SkillEntity)skill).getState() < SkillManager.IDLE)
//			{
////				WarMoveService.getInstance().stopMove((WarObjectInstance) owner, ((WarObjectInstance)owner).getX(), ((WarObjectInstance)owner).getY(), ((WarObjectInstance)owner).getD(), true);
////				System.err.println("stop when skill act");
//				return EBTStatus.BT_FAILURE;
//			}
//			else
//				actSkillId = 0;
//		}
//		
//		int speed = (int) ((WarSceneObject) owner).getSpeed();
//		long findGapTime = (long) (FIND_PATH_INTERVAL * 50000f / speed) ;
//		// 检测是否超过寻路时间
//		long currentTime = System.currentTimeMillis();
//		Long lastPathTime = getLastPathTime();
//		if (lastPathTime != null && (currentTime - lastPathTime) < findGapTime)
//			return EBTStatus.BT_FAILURE;
//		
//		setLastPathTime(currentTime);
//		
////		if(((WarObjectInstance) owner).getModelID() == 10101)
////			System.err.println("find path targetid[" + targetId + "]");
////		long startTime = System.nanoTime();
//		WarMoveService.getInstance().thinkMove((Troop)owner, (Troop)target, currentTime, getAttackRange());
////		System.err.println("thinkMove :" + (System.nanoTime() - startTime));
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 定点移动
//	 * @param x
//	 * @param y
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus moveTo(float x, float y)
//	{
//		WarInstance war = getWar();
//		
//		if(x ==0 && y == 0) 
//			return EBTStatus.BT_FAILURE;
//
//		if(war == null)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject owner = war.getObject(ownerId);
//		if(owner.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		if (!((WarSceneObject) owner).canMove())
//			return EBTStatus.BT_FAILURE;
//		
//		if(getWar().isSuspend())
//			return EBTStatus.BT_FAILURE;
//		
//		if(Math.abs(((WarSceneObject) owner).getX() - x) < 1 && Math.abs(((WarSceneObject) owner).getY() - y) < 1)
//		{
//			WarMoveService.getInstance().stopMove((WarObjectInstance) owner, x, y, 0, true);
//			this.targetX = 0;
//			this.targetY = 0;
//			playerCmd.set(false);
//			return EBTStatus.BT_SUCCESS;
//		}
//		
//		int speed = (int) ((WarSceneObject) owner).getSpeed();
//		long findGapTime = (long) (FIND_PATH_INTERVAL * 50000f / speed) ;
//		// 检测是否超过寻路时间
//		long currentTime = System.currentTimeMillis();
//		Long lastPathTime = getLastPathTime();
//		if (lastPathTime != null && (currentTime - lastPathTime) < findGapTime)
//			return EBTStatus.BT_FAILURE;
//		
//		setLastPathTime(currentTime);
//		
////		System.err.println("find path target[" + x + "," + y + "] current[" +((Troop)owner).getX() + "," + ((Troop)owner).getY() + "]");
//		
//		WarMoveService.getInstance().thinkMove((Troop)owner, x, y, currentTime, 0f);
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 自定义脚本移动
//	 * @param x
//	 * @param y
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus moveToNextPoint()
//	{
//		
//		WarInstance war = getWar();
//		if(war == null)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject owner = war.getObject(ownerId);
//		if(owner.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_FAILURE;
//		
//		if (!((WarSceneObject) owner).canMove())
//			return EBTStatus.BT_FAILURE;
//		
//		if(getWar().isSuspend())
//			return EBTStatus.BT_FAILURE;
//		
//		float[] point = ((Troop)owner).getRoleData().getCurrentMovePoint();
//		if(point == null || point.length < 2)
//			return EBTStatus.BT_FAILURE;
//		
//		float x = point[0];
//		float y = point[1];
//		
//		if(Math.abs(((WarSceneObject) owner).getX() - x) < 1 && Math.abs(((WarSceneObject) owner).getY() - y) < 1)
//		{
//			WarMoveService.getInstance().stopMove((WarObjectInstance) owner, x, y, 0, true);
//			((Troop)owner).getRoleData().nextMove();
//			return EBTStatus.BT_SUCCESS;
//		}
//		
//		int speed = (int) ((WarSceneObject) owner).getSpeed();
//		long findGapTime = (long) (FIND_PATH_INTERVAL * 50000f / speed) ;
//		long currentTime = System.currentTimeMillis();
//		Long lastPathTime = getLastPathTime();
//		if (lastPathTime != null && (currentTime - lastPathTime) < findGapTime)
//			return EBTStatus.BT_FAILURE;
//		
//		setLastPathTime(currentTime);
//		
//		WarMoveService.getInstance().thinkMove((Troop)owner, x, y, currentTime, 0f);
//		return EBTStatus.BT_SUCCESS;
//	}
//
//	/**
//	 * 停止移动
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus stopMove()
//	{
//		GameObject owner = getWar().getObject(ownerId);
//		if(owner.getObjectType() != GameObjectType.TROOP)
//			return EBTStatus.BT_SUCCESS;
//		
//		if (((WarSceneObject) owner).isMove())
//			WarMoveService.getInstance().stopMove((WarObjectInstance) owner);
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 强制改变目标
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
//		if(actSkillId > 0)
//		{
//			GameObject skill = getWar().getObject(actSkillId);
//			if(skill != null && skill.getObjectType() == GameObjectType.SKILL && ((SkillEntity)skill).getState() < SkillManager.IDLE)
//			{
//				((SkillEntity)skill).interrupt(owner, true);
//				
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
//	/**
//	 * 强制改变目标
//	 * @param x
//	 * @param y
//	 * @return
//	 */
//	@AgentMethod
//	synchronized public EBTStatus forceTarget(float x, float y)
//	{
//		WarInstance war = getWar();
//		if(x == 0 && y == 0)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject owner = war.getObject(ownerId);
//		if (!((Troop) owner).canMove())
//			return EBTStatus.BT_FAILURE;
//		
//		if(!WarSceneService.getInstance().isMovePostion(war, x, y, ((WarSceneObject) owner).getModelType()))
//			return EBTStatus.BT_FAILURE;
//		
//		this.playerCmd.set(true);
//		this.lastCmd = new PlayerCmd(x, y, 0);
////		System.err.println("force x,y:" + x + "," + y);
//		
//		if(actSkillId > 0)
//		{
//			GameObject skill = getWar().getObject(actSkillId);
//			if(skill != null && skill.getObjectType() == GameObjectType.SKILL && ((SkillEntity)skill).getState() < SkillManager.IDLE)
//			{
//				((SkillEntity)skill).interrupt(owner, true);
//				
//			}
//			else
//				actSkillId = 0;
//		}
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 是否目标点合法
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus isTargetPointValid()
//	{
//		if(this.targetX == 0 && this.targetY == 0)
//			return EBTStatus.BT_FAILURE;
//		
//		WarInstance war = getWar();
//		if(war == null)
//			return EBTStatus.BT_FAILURE;
//		
//		GameObject owner = war.getObject(ownerId);
//		if(!WarSceneService.getInstance().isMovePostion(war, targetX, targetY, ((WarSceneObject) owner).getModelType()))
//			return EBTStatus.BT_FAILURE;
//		
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	/**
//	 * 是否手动操作
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus isManual()
//	{
//		if(playerCmd.get())
//			return EBTStatus.BT_SUCCESS;
//		
//		return EBTStatus.BT_FAILURE;
//	}
//	
//	/**
//	 * 重置寻找目标cd
//	 * @return
//	 */
//	@AgentMethod
//	public EBTStatus resetFindTargetCd()
//	{
//		this.lastTargetTime = 0L;
//		return EBTStatus.BT_SUCCESS;
//	}
//	
//	@Override
//	public String getName()
//	{
//		return "BaseAIObj";
//	}
//
//	@Override
//	public int getObjectType()
//	{
//		return GameObjectType.AI;
//	}
//	
//	public void setAttackRange(float value)
//	{
//		attackRange = value;
//	}
//	
//	@AgentMethod
//	public float getAttackRange()
//	{
//		return attackRange;
//	}
//
//	public int getOwnerId()
//	{
//		return ownerId;
//	}
//
//	public int getTargetId()
//	{
//		return targetId;
//	}
//
//	public void setLastPathTime(long currentTime)
//	{
//		this.lastPathTime = currentTime;
//	}
//
//	public long getLastPathTime()
//	{
//		return this.lastPathTime ;
//	}
//
//	public long getLastTargetTime()
//	{
//		return lastTargetTime;
//	}
//
//	public void setLastTargetTime(long lastTargetTime)
//	{
//		this.lastTargetTime = lastTargetTime;
//	}
//
//	public float getTargetX() {
//		return targetX;
//	}
//
//	public void setTargetX(float targetX) {
//		this.targetX = targetX;
//	}
//
//	public float getTargetY() {
//		return targetY;
//	}
//
//	public void setTargetY(float targetY) {
//		this.targetY = targetY;
//	}
//
//	public int getActSkillId()
//	{
//		return actSkillId;
//	}
//
//	public void setActSkillId(int actSkillId)
//	{
//		this.actSkillId = actSkillId;
//	}
//
//	
//	
//}
