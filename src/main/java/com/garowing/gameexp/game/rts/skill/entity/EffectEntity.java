package com.garowing.gameexp.game.rts.skill.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.EffectTopType;
import com.garowing.gameexp.game.rts.skill.constants.EffectType;
import com.garowing.gameexp.game.rts.skill.handler.SkillEffectHandler;
import com.garowing.gameexp.game.rts.skill.handler.effecthandler.DefaultEffectHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.EffectModel;
import com.garowing.gameexp.game.rts.skill.script.effect.buff.AbstractBuffPrototype;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;
import com.garowing.gameexp.game.rts.objects.HavingStats;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 技能效果实体
 * @author seg
 *
 */
public class EffectEntity extends GameObject implements Projectile
{
	private static final Logger mLogger = Logger.getLogger(EffectEntity.class);
	
	/**
	 * 默认效果处理
	 */
	public static final SkillEffectHandler DEFAULT_EFFECT_HANDLER = new DefaultEffectHandler();
	
	/**
	 * 父节点id(技能或效果)
	 */
	private int parentId;
	
	/**
	 * 效果原型id
	 */
	private int prototypeId;
	
	/**
	 *目标ids
	 */
	private List<Integer> targetIds = new ArrayList<Integer>();
	
	/**
	 * 上次执行时间
	 */
	private long lastActivateTime;
	
	/**
	 * 效果状态
	 */
	private int state = SkillManager.IDLE;
	
	/**
	 * 执行次数
	 */
	private int activateCount;
	
	/**
	 * 下次执行时间
	 */
	private long nextActivateTime;
	
	/**
	 * 非单位目标x
	 */
	private float targetX;
	
	/**
	 * 非单位目标Y
	 */
	private float targetY;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 开始时间
	 */
	private long startTime;
	
	/**
	 * 子效果实体ids
	 */
	private List<Integer> subEffectIds = new ArrayList<Integer>();
	
	/**
	 * 是否临时效果
	 */
	private boolean isTemporary;
	
	/**
	 * 是否是触发器
	 */
	private boolean isTrigger;
	
	/**
	 * 真实执行延迟（动作前摇）
	 */
	private long realExecDelay;
	
	/**
	 * 伪随机队列
	 */
	private Queue<Integer> pseudoRamdon;
	
	/**
	 * 客户端通知记录
	 */
	private ClientNotificationMap clientNotificationMap;
	
	/**
	 * 真实结束延迟（动作后摇）
	 */
	private long realEndDelay;
	
	/**
	 * 进入结束状态的时间
	 */
	private long endTime;
	
	/**
	 * 投射物属性
	 */
	private ProjectileAttrEntity projectileAttr;
	
	/**
	 * 是否强制结束
	 */
	private boolean isForceEnd;
	
	/**
	 * 效果执行回调
	 */
	private SkillEffectHandler effectHandler;
	
	public EffectEntity(WarInstance war, EffectModel model)
	{
		super(war);
		this.prototypeId = model.getId();
		this.realExecDelay = (long) (model.getExecDelay() * model.getTimeFactor());
		this.realEndDelay = (long) (model.getEndDelay() * model.getTimeFactor());
		this.clientNotificationMap = new ClientNotificationMap(war);
		this.projectileAttr = new ProjectileAttrEntity();
		this.effectHandler = DEFAULT_EFFECT_HANDLER;
		EffectType type = EffectType.getTypeById(model.getTemplateId());
		if(type == null)
			return;
		
		if(type.getTopType() == EffectTopType.TRIGGER)
		{
			this.isTrigger = true;
			return;
		}
		
		if(type.getTopType() != EffectTopType.ACTIVE)
			return;
		
		for(Integer effectId : model.getPrototype().getSubEffectIds())
		{
			if(effectId == 0)
				continue;
			
			EffectModel effectModel = StaticDataManager.EFFECT_DATA.getEffectModel(effectId);
			if(effectModel == null)
			{
				mLogger.error("effect model property error! can not find effect! id[" + model.getId() + "] subId[" + effectId + "]");
				continue;
			}
			
			EffectEntity effectEntity = new EffectEntity(war, effectModel);
			effectEntity.setParentId(this.getObjectId());
			this.subEffectIds.add(effectEntity.getObjectId());
		}
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameObject> getTargets()
	{
		if(this.targetIds == null || this.targetIds.isEmpty())
			return Collections.EMPTY_LIST;
		
		List<GameObject> targets = new ArrayList<GameObject>();
		WarInstance war = getWar();
		if(war == null)
			return Collections.EMPTY_LIST;
		
		for(Integer targetId : targetIds)
		{
			GameObject obj = war.getObject(targetId);
			if(obj != null)
				targets.add(obj);
		}
		return targets;
	}

	@Override
	public float getTargetX()
	{
		return this.targetX;
	}

	@Override
	public float getTargetY()
	{
		return this.targetY;
	}

	@Override
	public String getName()
	{
		return "effectEntity modelId[" + prototypeId + "] targets[" + targetIds + "] state[" + state + "] ";
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.SKILL_EFFECT;
	}
	
	/**
	 * 获取施法者
	 * @return
	 */
	public <T extends GameObject> T getCaster()
	{
		WarInstance war = getWar();
		if(war == null)
			return null;
		
		GameObject parent = war.getObject(parentId);
		if(parent == null)
			return null;
		
		while(parent.getObjectType() != GameObjectType.SKILL)
		{
			if(parent.getObjectType() != GameObjectType.SKILL_EFFECT)
				return null;
			
			parent = war.getObject(((EffectEntity)parent).getParentId());
			if(parent == null)
				return null;
		}
		
		return ((SkillEntity)parent).getCaster();
	}

	/**
	 * 移除目标
	 * @param objectId
	 */
	public void removeTarget(int objectId)
	{
		Iterator<Integer> iterator = targetIds.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().intValue() == objectId)
				iterator.remove();
		}
	}
	
	/**
	 * 增加激活次数
	 */
	public void addActivateCount()
	{
		this.activateCount += 1;
	}
	
	/**
	 * 获取效果原型
	 * @return
	 */
	public EffectPrototype getEffectPrototype()
	{
		return StaticDataManager.EFFECT_DATA.getEffectPrototype(prototypeId);
	}
	
	/**
	 * 获取技能id
	 * @return
	 */
	public int getSkillId()
	{
		WarInstance war = getWar();
		if(war == null)
			return 0;
		
		GameObject parent = war.getObject(parentId);
		if(parent == null)
			return 0;
		
		while(parent.getObjectType() != GameObjectType.SKILL)
		{
			if(parent.getObjectType() != GameObjectType.SKILL_EFFECT)
				return 0;
			
			parent = war.getObject(((EffectEntity)parent).getParentId());
			if(parent == null)
				return 0;
		}
		
		return ((SkillEntity)parent).getObjectId();
	}
	
	/**
	 * 获取封装效果id
	 * @return
	 */
	public int getWrapperEffectId()
	{
		WarInstance war = getWar();
		GameObject parent = war.getObject(parentId);
		if(parent == null)
			return 0;
		
		int warpperEffectId = prototypeId;
		while(parent.getObjectType() != GameObjectType.SKILL)
		{
			if(parent.getObjectType() != GameObjectType.SKILL_EFFECT)
				return 0;
			
			warpperEffectId = ((EffectEntity)parent).getPrototypeId();
			parent = war.getObject(((EffectEntity)parent).getParentId());
			if(parent == null)
				return 0;
		}
		
		return warpperEffectId;
	}
	
	/**
	 * 获取技能实体
	 * @return
	 */
	public SkillEntity getSkill()
	{
		int skillId = getSkillId();
		GameObject skill = getWar().getObject(skillId);
		if(skill == null)
			return null;
		
		return (SkillEntity) skill;
	}
	
	/**
	 * 获取技能的modelId
	 * @return
	 */
	public int getSkillModelId()
	{
		SkillEntity skill = getSkill();
		if(skill == null)
			return 0;
		
		return skill.getModelId();
	}
	
	/**
	 * 增加目标
	 * @param objectId
	 */
	public void addTarget(int objectId)
	{
		this.targetIds.add(objectId);
	}
	
	@Override
	public void destroy()
	{
		if(isTemporary)
		{
			WarInstance war = getWar();
			for(Integer targetId : targetIds)
			{
				GameObject target = war.getObject(targetId);
				if(target == null || target.getObjectType() != GameObjectType.TROOP)
					continue;
				
				Troop troop = (Troop) target;
				troop.getAttachEffects().removeTempEffect(getObjectId());
			}
			
//			for(Integer subEffectId : subEffectIds)
//			{
//				GameObject effect = war.getObject(subEffectId);
//				if(effect != null)
//					effect.destroy();
//			}
		}
		
		super.destroy();
	}
	
	/**
	 * 获取执行间隔
	 * @return
	 */
	public int getInterval()
	{
		int originValue = getEffectPrototype().getInterval();
		GameObject caster = getCaster();
		if(caster instanceof HavingStats)
		{
//			EffectType type = EffectType.getTypeById(getEffectPrototype().getTemplateId());
			if(getEffectPrototype() instanceof AbstractBuffPrototype)
				originValue *=(1 + ((HavingStats)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.EFFECT_INTERVAL, this));
			else
				originValue *=(1 + ((HavingStats)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.CD_ADD, this));
		}
			
		
		return originValue;
	}
	
	/**
	 * 获取最大执行次数
	 * @return
	 */
	public int getMaxActivateCount()
	{
		int originValue = getEffectPrototype().getActivateCount();
		GameObject caster = getCaster();
		if(caster instanceof HavingStats)
			originValue += ((HavingStats)caster).getAttrModList().getEffectModifiedTotal(ModifiedType.EFFECT_ACTIVATE_COUNT, this);
		
		return originValue;
	}
	
	/**
	 * 设置目标
	 * @param findTarget
	 */
	public void setTargets(List<GameObject> findTarget)
	{
		this.targetIds.clear();
		for(GameObject target : findTarget)
		{
			targetIds.add(target.getObjectId());
		}
	}
	
	/**
	 * 获取下一个伪随机数
	 * @return
	 */
	public Integer getNextPseudoNum()
	{
		if(pseudoRamdon == null || pseudoRamdon.isEmpty())
			return -1;
		
		return pseudoRamdon.poll();
	}
	
	@Override
	public ProjectileAttrEntity getProjectileAttr()
	{
		return this.projectileAttr;
	}
	
	/**
	 * 数据清除
	 */
	public void clean()
	{
		this.setState(SkillManager.IDLE);
		this.setActivateCount(0);
		this.getClientNotificationMap().clean();
		this.getProjectileAttr().clean();
		this.setForceEnd(false);
	}
	
	public Queue<Integer> getPseudoRamdon()
	{
		return pseudoRamdon;
	}

	public void setPseudoRamdon(Queue<Integer> pseudoRamdon)
	{
		this.pseudoRamdon = pseudoRamdon;
	}

	public int getParentId()
	{
		return parentId;
	}

	public void setParentId(int parentId)
	{
		this.parentId = parentId;
	}

	public int getPrototypeId()
	{
		return prototypeId;
	}

	public void setPrototypeId(int prototypeId)
	{
		this.prototypeId = prototypeId;
	}

	public List<Integer> getTargetIds()
	{
		return targetIds;
	}

	public void setTargetIds(List<Integer> targetIds)
	{
		this.targetIds = new ArrayList<Integer>(targetIds);
	}

	public long getLastActivateTime()
	{
		return lastActivateTime;
	}

	public void setLastActivateTime(long lastActivateTime)
	{
		this.lastActivateTime = lastActivateTime;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getActivateCount()
	{
		return activateCount;
	}

	public void setActivateCount(int activateCount)
	{
		this.activateCount = activateCount;
	}

	public long getNextActivateTime()
	{
		return nextActivateTime;
	}

	public void setNextActivateTime(long nextActivateTime)
	{
		this.nextActivateTime = nextActivateTime;
	}

	public void setTargetX(float targetX)
	{
		this.targetX = targetX;
	}

	public void setTargetY(float targetY)
	{
		this.targetY = targetY;
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

	public List<Integer> getSubEffectIds()
	{
		return subEffectIds;
	}

	public void setSubEffectIds(List<Integer> subEffectIds)
	{
		this.subEffectIds = subEffectIds;
	}

	public boolean isTemporary()
	{
		return isTemporary;
	}

	public void setTemporary(boolean isTemporary)
	{
		this.isTemporary = isTemporary;
	}

	public boolean isTrigger()
	{
		return isTrigger;
	}

	public void setTrigger(boolean isTrigger)
	{
		this.isTrigger = isTrigger;
	}

	public long getRealExecDelay()
	{
		return realExecDelay;
	}

	public void setRealExecDelay(long realExecDelay)
	{
		this.realExecDelay = realExecDelay;
	}

	public ClientNotificationMap getClientNotificationMap()
	{
		return clientNotificationMap;
	}

	public void setClientNotificationMap(ClientNotificationMap clientNotificationMap)
	{
		this.clientNotificationMap = clientNotificationMap;
	}

	public long getRealEndDelay()
	{
		return realEndDelay;
	}

	public void setRealEndDelay(long realEndDelay)
	{
		this.realEndDelay = realEndDelay;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long activateTime)
	{
		this.endTime = activateTime;
	}

	public boolean isForceEnd()
	{
		return isForceEnd;
	}

	public void setForceEnd(boolean isForceEnd)
	{
		this.isForceEnd = isForceEnd;
	}

	public SkillEffectHandler getEffectHandler()
	{
		return effectHandler;
	}

	public void setEffectHandler(SkillEffectHandler effectHandler)
	{
		this.effectHandler = effectHandler;
	}

}
