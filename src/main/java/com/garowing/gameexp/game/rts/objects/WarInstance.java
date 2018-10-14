package com.garowing.gameexp.game.rts.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.grave.GraveManager;
import com.yeto.war.fightcore.scene.objects.WarScene;
import com.yeto.war.fightcore.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.constants.BattleErrorCode;
import com.garowing.gameexp.game.rts.handler.WarHandler;
import com.garowing.gameexp.game.rts.listener.objects.WarListenerList;
import com.garowing.gameexp.game.rts.model.WarModel;
import com.garowing.gameexp.game.rts.model.WarState;
import com.garowing.gameexp.game.rts.model.WinType;
import com.garowing.gameexp.game.rts.run.WarFrameTask;
import com.yeto.war.module.player.PlayerEntity;

import commons.thread.task.GameTask;

/**
 * 战争实例
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月16日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class WarInstance extends GameObject
{
	/**
	 * 强制摧毁等待时间
	 */
	public static final long FORCE_DESTROY_WAIT = 10000L;
	
	/**
	 * 战场ID生成器
	 */
	private final AtomicInteger idCreate = new AtomicInteger();; 
	
	/**
	 * 战争model
	 */
	private final WarModel model;
	
	/**
	 * 战争状态
	 */
	private WarState state;
	
	/**
	 * 是否暂停
	 */
	private boolean suspend;
	
	/**
	 * 战场内对象集合缓存
	 */
	private final Map<Integer, GameObject> mGOCache = new ConcurrentHashMap<Integer, GameObject>();
	
	/**
	 * 阵营集合
	 */
	private final Map<Integer, WarCampInstance> campCollection = new HashMap<>();
	
	/**
	 * 战场场景
	 */
	private final WarScene warScene;
	
	/**
	 * 战场事件集合
	 */
	private final WarListenerList warListenerList;
	
	/**
	 * 附带玩家集合
	 */
	private final List<Long> initPlayers;
	
	/**
	 * 已连接过的玩家
	 */
	private final List<Long> linkPlayers;

	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 开始时间
	 */
	private long startTime;
	
	// 是否强制摧毁, 强制摧毁如果是异常带上错误号, 默认最后开始时间,使用时间
	private boolean forceDestroy;
	private int forceDestroyError = BattleErrorCode.BATTLE_CREATE_SUCCESS.getCode();
	private long lastStartTime;
	private long useTime;
	
	// 计算战争结果, 胜利阵营
	private boolean winRusult;
	private int winCampID;
	// 胜利类型(0:正常胜利 1:对方掉线 2:对方投降)
	private WinType winType;
	
	// 其他中转数据
	private Object arg;
	
	/**
	 * 引导的id记录
	 */
	private Set<Integer> guideIds = new HashSet<Integer>();
	
	/**
	 * 同步锁
	 */
	private final Lock lock = new ReentrantLock();
	
	/**
	 * 技能管理器
	 */
	private SkillManager skillManager;
	
	/**
	 * 墓地管理器
	 */
	private GraveManager graveMgr = new GraveManager();
	
	/**
	 * 观战者map
	 */
	private Map<Integer, PlayerEntity> audienceMap = new HashMap<Integer, PlayerEntity>();
	
	/**
	 * 帧状态 0-空闲 1-执行
	 */
	private AtomicInteger frameState = new AtomicInteger(WarFrameTask.FRAME_STATE_IDLE);
	
	private DamageCounter damageCounter = new DamageCounter(this);
	
	public WarInstance (int objectId, WarModel model, List<Long> initPlayers)
	{
		super(objectId);
		this.model = model;
		this.initPlayers = initPlayers;
		Iterator<Long> iter = this.initPlayers.iterator();
		while (iter.hasNext())
		{
			long playerID = iter.next();
			if (playerID<= 0)
				iter.remove();
		}
		
		this.linkPlayers = new ArrayList<>();
		
		this.warListenerList = new WarListenerList();
		this.warScene = new WarScene(this, model.getMapID());
		this.skillManager = new SkillManager(this);
	}
	

	/**
	 * 获得下一个实例ID
	 * @return
	 */
	public int nextInstanceId()
	{
		return idCreate.incrementAndGet();
	}
	
	/**
	 * 获得战争处理类
	 * @return
	 */
	public WarHandler handler ()
	{
		return model.getWarType().handler;
	}
	
	/**
	 * 获取战斗中会出现的部队ID集合
	 * @return
	 */
	public Set<Integer> getTroopIds()
	{
		if(model == null)
			return null;
		
		return model.getTroopIds();
	}
	
	/**
	 * 获得战争原形ID
	 * @return
	 */
	public int gainModelID ()
	{
		return model.getId();
	}
	
	public int getWarType ()
	{
		return model.getWarType().code;
	}
	
	public int gainWarClientType ()
	{
		return model.getWarType().clientCode;
	}
	
	
	@Override
	public String getName() {
		return "战争";
	}
	
	/**
	 * 是否可以战斗
	 * @return
	 * @create	2015年4月14日	darren.ouyang
	 */
	public boolean stateFighting ()
	{
		return this.state == WarState.IN_FIGHT && !this.suspend;
	}
	
	/**
	 * 判断战争是在战斗中状态
	 * @return
	 * @create	2015年4月17日	darren.ouyang
	 */
	public boolean stateInFight ()
	{
		return this.state == WarState.IN_FIGHT;
	}
	
	/**
	 * 增加战场对象
	 * @param obj
	 */
	public void addObject(GameObject obj)
	{
		mGOCache.putIfAbsent(obj.getObjectId(), obj);
	}
	
	/**
	 * 移除战场对象
	 * @param obj
	 */
	public void removeObject(GameObject obj)
	{
		mGOCache.remove(obj.getObjectId());
	}
	
	/**
	 * 移除战场对象
	 * @param objId
	 */
	public void removeObject(int objId)
	{
		mGOCache.remove(objId);
	}
	
	/**
	 * 获取游戏对象
	 * @param id
	 */
	public GameObject getObject(int id)
	{
		return mGOCache.get(id);
	}
	
	/**
	 * 判断战斗是否已结束
	 * @return
	 * @create	2015年4月15日	darren.ouyang
	 */
	public boolean stateFightExit ()
	{
		return state == WarState.END;
	}
	
	/******************** camp 方法 ********************/
	
	/**
	 * 获得阵营实例
	 * @param id
	 * @return
	 * @create	2015年4月10日	darren.ouyang
	 */
	public WarCampInstance getCamp(int id)
	{
		return campCollection.get(id);
	}
	
	/**
	 * 增加一个阵营
	 * @param camp
	 * @create	2015年4月10日	darren.ouyang
	 */
	public void addCamp(WarCampInstance camp)
	{
		if(campCollection.putIfAbsent(camp.getObjectId(), camp) != null)
		{
			throw new DuplicateFormatFlagsException("战争[" + model.getId() + 
					"]实例ID[" + getObjectId() + "]已经存在阵营[" + camp.toString() + "]");
		}
	}
	
	/**
	 * 战争的所有阵营执行指定的函数
	 * @param action
	 */
	public void doOnAllCamps(BiConsumer<Integer, WarCampInstance> action)
	{
		campCollection.forEach(action);
	}
	
	/**
	 * 获得阵营列表(不可变)
	 * @return
	 */
	public Collection<WarCampInstance> unmodifiableCamps()
	{
		return Collections.unmodifiableCollection(campCollection.values());
	}
	
	/**
	 * 获得阵营列表(拷贝列表)
	 * @return
	 */
	public Collection<WarCampInstance> copyCamps()
	{
		return new ArrayList<>(campCollection.values());
	}
	
	/******************** control 方法 ********************/
	
	/**
	 * 战争的所有控制器执行指定的函数
	 * @param action
	 */
	public void doOnAllControls(Consumer<WarControlInstance> action)
	{
		List<WarControlInstance> controllerSet = getAllControllers();
		controllerSet.forEach(action);
	}
	
	/**
	 * 获取所有控制器集合
	 * @return
	 */
	public List<WarControlInstance> getAllControllers()
	{
		List<WarControlInstance> controllers = new ArrayList<WarControlInstance>();
		for(WarCampInstance camp : campCollection.values())
		{
			Collection<WarControlInstance> campControllers = camp.getAllControllers();
			controllers.addAll(campControllers);
		}
		return controllers;
	}


	/**
	 * 获得控制器列表(不可变)
	 * @return
	 */
	public Collection<WarControlInstance> unmodifiableControls()
	{
		return Collections.unmodifiableCollection(getAllControllers());
	}
	
	@Override
	public String toString() 
	{
		return getName() + "," + model.getWarType().info + "[" + model.getId() + "," + getObjectId() + "]";
	}
	
	public void lock() 
	{
		lock.lock();
	}
	
	public void unlock() 
	{
		lock.unlock();
	}
	
	public WarState getState()
	{
		return state;
	}

	public void setState(WarState state)
	{
		this.state = state;
	}

	public WarModel getModel()
	{
		return model;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public WarListenerList getWarListenerList()
	{
		return warListenerList;
	}

	public WarScene getWarScene()
	{
		return warScene;
	}

	public boolean isSuspend()
	{
		return this.suspend;
	}

	public void setSuspend(boolean suspend)
	{
		this.suspend = suspend;
	}

	public List<Long> getInitPlayers()
	{
		return initPlayers;
	}

	public List<Long> getLinkPlayers()
	{
		return linkPlayers;
	}

	public boolean isForceDestroy()
	{
		return forceDestroy;
	}

	public void setForceDestroy(boolean forceDestroy)
	{
		this.forceDestroy = forceDestroy;
	}

	public int getWinCampID()
	{
		return winCampID;
	}

	public void setWinCampID(int winCampID)
	{
		this.winCampID = winCampID;
	}

	public boolean isWinRusult()
	{
		return winRusult;
	}

	public void setWinRusult(boolean winRusult)
	{
		this.winRusult = winRusult;
	}

	public Object getArg()
	{
		return arg;
	}

	public void setArg(Object arg)
	{
		this.arg = arg;
	}

	public long getLastStartTime()
	{
		return lastStartTime;
	}

	public void setLastStartTime(long lastStartTime)
	{
		this.lastStartTime = lastStartTime;
	}

	public long getUseTime()
	{
		return useTime;
	}

	public void setUseTime(long useTime)
	{
		this.useTime = useTime;
	}

	public int getForceDestroyError()
	{
		return forceDestroyError;
	}

	public void setForceDestroyError(int forceDestroyError)
	{
		this.forceDestroyError = forceDestroyError;
	}

	public WinType getWinType()
	{
		return winType;
	}

	public void setWinType(WinType winType)
	{
		this.winType = winType;
	}


	public SkillManager getSkillManager()
	{
		return skillManager;
	}


	public void setSkillManager(SkillManager skillCache)
	{
		this.skillManager = skillCache;
	}

	public Set<Integer> getGuideIds()
	{
		return guideIds;
	}

	public void setGuideIds(Set<Integer> guideIds)
	{
		this.guideIds = guideIds;
	}
	
	/**
	 * 强制摧毁任务
	 * @author seg
	 * 2017年2月8日
	 */
	public abstract class ForceDestroyTask extends GameTask
	{
		private Object[] params;
		
		public ForceDestroyTask(Object... params)
		{
			super();
			this.params = params;
		}

		public Object[] getParams()
		{
			return params;
		}
		
		/**
		 * 返回战场实例
		 * @return
		 */
		public WarInstance getWar()
		{
			return WarInstance.this;
		}
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.WAR;
	}
	
	/**
	 * 设置帧状态
	 * @param expect
	 * @param value
	 * @return
	 */
	public boolean compareAndSetFrameState(int expect, int value)
	{
		return this.frameState.compareAndSet(expect, value);
	}


	public GraveManager getGraveMgr()
	{
		return graveMgr;
	}

	public AtomicInteger getFrameState()
	{
		return frameState;
	}


	public void setFrameState(AtomicInteger frameState)
	{
		this.frameState = frameState;
	}

	public DamageCounter getDamageCounter()
	{
		return damageCounter;
	}


	public void setDamageCounter(DamageCounter damageCounter)
	{
		this.damageCounter = damageCounter;
	}
	
	
}
