package com.garowing.gameexp.game.rts.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.yeto.war.fightcore.attr.model.AttrKey;
import com.yeto.war.fightcore.behaviac.agent.Agent;
import com.yeto.war.fightcore.fight.action.Action;
import com.yeto.war.fightcore.fight.attr.FightAddList;
import com.yeto.war.fightcore.fight.state.FightMetaState;
import com.yeto.war.fightcore.fight.state.FightStateList;
import com.yeto.war.fightcore.scene.objects.WarSceneObject;
import com.yeto.war.fightcore.skill.entity.AttachEffectsList;

/**
 * 战争中的对象
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @version 	1.0
 */
public abstract class WarObjectInstance extends WarSceneObject implements HavingCamp, HavingStats, HavingSkill
{
	/**
	 * 控制器
	 */
	private WarControlInstance control;

	/**
	 * 是否存活
	 */
	private boolean isLive;
	
	/**
	 * 血量
	 */
	private int hp;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 结束时间
	 */
	private long endTime;
	
	/**
	 * 乐观锁
	 */
	private AtomicBoolean lock = new AtomicBoolean(false);
	/**
	 * 行为队列
	 */
	private List<Action> actionList = new ArrayList<Action>();
	
	/**
	 * 战斗状态集合
	 */
	private final FightStateList fightStates = new FightStateList();
	
	/**
	 * 属性
	 */
	private final AttrEntity attrEntity ;
	
	/**
	 * 战斗加成属性
	 */
	private final FightAddList fightAttributeMap = new FightAddList();
	
	
	/**
	 * 行为代理
	 */
	private Agent agent;
	
	/**
	 * 附加效果集合
	 */
	private AttachEffectsList attachEffects;
	
	
	public WarObjectInstance (WarInstance war, WarControlInstance owner, AttrEntity attrEntity)
	{
		super(war);
		this.control = owner;
		this.isLive = true;
		this.createTime = System.currentTimeMillis();
		this.attrEntity = attrEntity;
		this.attachEffects = new AttachEffectsList(war);
	}
	
	/**
	 * 判断是否存活
	 * @return
	 * @create	2015年4月13日	darren.ouyang
	 */
	public boolean isLive ()
	{
		return isLive && (this.hp > 0);
	}
	
	/**
	 * 获得ModelID
	 * @return
	 * @create	2015年4月15日	darren.ouyang
	 */
	public abstract int getModelID();
	
	/**
	 * 获得对象类型
	 * @return
	 */
	public abstract int getModelType();
	
	/******************** 地图相关 ********************/
	
	
	/**
	 * 是否可以移动
	 * @return
	 */
	public boolean canMove()
	{
		float sp = getSpeed();
		if (sp <= 0)
			return false;
		
		if (isStop())
			return false;
		
		int state = this.fightStates.getMetaStates();
		return (state & FightMetaState.CAN_NOT_MOVE) == 0;
	}
	
	/******************** 攻击相关 ********************/
	
	/**
	 * 获得攻击的ID
	 * @return
	 */
	public abstract int getAttackID ();
	
	
	/**
	 * 是否可以攻击
	 * @return
	 */
	public boolean canAttack()
	{
		int states = fightStates.getMetaStates();
		return (states & FightMetaState.CAN_NOT_ATK) == 0;
	}
	
	/**
	 * 是否可以释放技能
	 * @return
	 */
	public boolean canCaseSkill()
	{
		int states = fightStates.getMetaStates();
		
		return (states & FightMetaState.SILENCE) == 0;
	}
	
	/**
	 * 是否可以被攻击
	 * @return
	 */
	public boolean canAttacked ()
	{
		int states = fightStates.getMetaStates();
		
		return (states & FightMetaState.CAN_NOT_BE_TARGETED) == 0;
	}
	
	/******************** 战争,阵营,控制者相关 ********************/
	
	/**
	 * 获得所属阵营ID
	 * @return
	 */
	public final int getCampId ()
	{
		return control.gainCampID();
	}
	
	/**
	 * 获得所属控制器ID
	 * @return
	 */
	public final int gainControlID ()
	{
		return control.getObjectId();
	}
	
	/**
	 * 获得战争实例
	 * @return
	 */
	public WarInstance getWar ()
	{
		return control.getWar();
	}
	
	/**
	 * 获取攻击力
	 * @return
	 */
	public int getAttack()
	{
		return (int) attrEntity.getValue(AttrKey.ATTACK);
	}
	
	/**
	 * 获取速度
	 * @return
	 */
	public float getSpeed()
	{
		return attrEntity.getValue(AttrKey.MOVE_SPEED);
	}
	
	/**
	 * 获取攻击距离
	 * @return
	 */
	public float getAtkDis()
	{
		return attrEntity.getValue(AttrKey.ATTACK_DIS) / 50;
	}
	
	/**
	 * 获取最大血量
	 * @return
	 */
	public int getMaxHp()
	{
		return (int) Math.floor(attrEntity.getValue(AttrKey.HP));
	}
	
	/**
	 * 是否满血
	 * @return
	 */
	public boolean isHpFull()
	{
		return getHp() >= getMaxHp();
	}
	
	/**
	 * 获取攻击速度
	 * @author zhouxiaofeng
	 * 2016年2月24日
	 * @return
	 */
	public float getAs()
	{
		return attrEntity.getValue(AttrKey.ATTACK_SPEED);
	}
	
	/**
	 * 免伤百分比
	 * @return
	 */
	public final float getDRPT ()
	{
		return attrEntity.getValue(AttrKey.DRPT);
	}
	
	/**
	 * 暴击率
	 * @return
	 */
	public final float getCrit ()
	{
		return attrEntity.getValue(AttrKey.CRIT);
	}
	
	/**
	 * 暴击伤害加成百分比
	 * @return
	 */
	public final float getCritHarm ()
	{
		return attrEntity.getValue(AttrKey.CRIT_HARM);
	}
	
	/**
	 * 格挡率
	 * @return
	 */
	public final float getBlock ()
	{
		return attrEntity.getValue(AttrKey.BLOCK);
	}
	
	/**
	 * 格挡伤害减免百分比
	 * @return
	 */
	public final float getBlockHarm ()
	{
		return attrEntity.getValue(AttrKey.BLOCK_HARM);
	}
	
	/**
	 * 闪避率
	 * @return
	 * @create	2014年12月3日	darren.ouyang
	 */
	public final float getDodge ()
	{
		return attrEntity.getValue(AttrKey.DODGE);
	}
	
	/**
	 * 获取下个action
	 * @return
	 */
	public Action nextAction(long currTime)
	{
		for(;;)
		{
			if(lock.compareAndSet(false, true))
			{
				try
				{
					return removeAction(currTime);
				}finally
				{
					lock.set(false);
				}
			}
		}
	}
	
	/**
	 * 移除一个可执行的action,并返回
	 * @return
	 */
	private Action removeAction(long currTime)
	{
		if(actionList == null || actionList.isEmpty())
			return null;
		
		Iterator<Action> it = actionList.iterator();
		while(it.hasNext())
		{
			Action action = it.next();
			if(action.isCanAction(currTime))
			{
				it.remove();
				return action;
			}
		}
		return null;
	}
	
	/**
	 * 增加可执行的action
	 * @param action
	 */
	public void addAction(Action action)
	{
		for(;;)
		{
			if(lock.compareAndSet(false, true))
			{
				try
				{
					actionList.add(action);
					break;
				}finally
				{
					lock.set(false);
				}
			}
		}
	}
	
	@Override
	public AttachEffectsList getAttachEffects()
	{
		return attachEffects;
	}
	
	/**
	 * 获取护盾值
	 * @return
	 */
	public int getShield()
	{
		int shield = 0;
		for(Integer shieldVal : getAttachEffects().getEffectShieldMap().values())
		{
			shield += shieldVal;
		}
		return shield;
	}
	
	/**
	 * 减少护盾值
	 * @param damage
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> reduceShield(int damage)
	{
		Map<Integer, Integer> shieldMap = getAttachEffects().getEffectShieldMap();
		if(shieldMap == null || shieldMap.isEmpty())
			return Collections.EMPTY_MAP;
		
//		System.err.println("shield map before:" + shieldMap + " damage:" + damage);
		Map<Integer, Integer> shieldReduceMap = new HashMap<Integer, Integer>();
		Iterator<Entry<Integer, Integer>> shieldItr = shieldMap.entrySet().iterator();
		while(damage > 0 && shieldItr.hasNext())
		{
			Entry<Integer, Integer> shieldEnrty = shieldItr.next();
			Integer shieldVal = shieldEnrty.getValue();
			if(shieldVal > damage)
			{
				shieldEnrty.setValue(shieldVal - damage);
				shieldReduceMap.put(shieldEnrty.getKey(), damage);
				break;
			}
			else
			{
				damage -= shieldVal;
				shieldReduceMap.put(shieldEnrty.getKey(), shieldVal);
				shieldItr.remove();
			}
		}
//		System.err.println("shield map after:" + shieldMap);
		return shieldReduceMap;
	}
	
	/**
	 * 重生标志位
	 * @return
	 */
	public boolean getLiveFlag()
	{
		return this.isLive;
	}
	
	public WarControlInstance getControl() 
	{
		return control;
	}
	
	public void setControl(WarControlInstance control) 
	{
		this.control = control;
	}

	public FightStateList getFightStates() 
	{
		return fightStates;
	}

	public FightAddList getAttrModList()
	{
		return fightAttributeMap;
	}
	
	public int getHp() 
	{
		return hp;
	}

	public void setHp(int hp) 
	{
		this.hp = hp;
	}

	public void setLive(boolean isLive) 
	{
		this.isLive = isLive;
	}

	public AttrEntity getAttrEntity() 
	{
		return attrEntity;
	}


	public long getCreateTime() 
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public Agent getAgent()
	{
		return agent;
	}

	public void setAgent(Agent agent)
	{
		this.agent = agent;
	}

	
	
}
