package com.garowing.gameexp.game.rts.skill.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.behaviac.agent.Agent;
import com.yeto.war.fightcore.behaviac.agent.BaseAIObj;
import com.yeto.war.fightcore.fight.attr.ModifiedType;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.event.InterruptEffectSkillEvent;
import com.garowing.gameexp.game.rts.skill.filter.skill.SkillFilter;
import com.garowing.gameexp.game.rts.skill.handler.SkillCastHandler;
import com.garowing.gameexp.game.rts.skill.manager.SkillManager;
import com.garowing.gameexp.game.rts.skill.model.EffectModel;
import com.garowing.gameexp.game.rts.skill.model.SkillError;
import com.garowing.gameexp.game.rts.skill.model.SkillModel;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;
import com.garowing.gameexp.game.rts.objects.HavingCamp;
import com.garowing.gameexp.game.rts.objects.HavingSkill;
import com.garowing.gameexp.game.rts.objects.WarControlInstance;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.yeto.war.module.strategos.StrategosObject;
import com.yeto.war.module.strategos.StrategosSkillModel;
import com.yeto.war.module.troop.Troop;
import com.yeto.war.utils.RatioUitl.IWeight;

/**
 * 技能实体
 * @author seg
 *
 */
public class SkillEntity extends GameObject implements Projectile, Comparable<SkillEntity>, IWeight
{
	private static final Logger mLogger = Logger.getLogger(SkillEntity.class);

	/**
	 * modelId
	 */
	private int modelId;
	
	/**
	 * 效果实体id集合
	 */
	private List<Integer> effectEntityList = new Vector<Integer>();
	
	/**
	 * 目标集合
	 */
	private List<Integer> targetIds = new ArrayList<Integer>();
	
	/**
	 * 非单位目标坐标x
	 */
	private float targetX;
	
	/**
	 * 非单位坐标y
	 */
	private float targetY;
	
	/**
	 * 上次释放时间
	 */
	private long lastCastTime;
	
	/**
	 * 下次释放时间
	 */
	private long nextCastTime;
	
	/**
	 * 技能状态
	 */
	private int state = SkillManager.IDLE;
	
	/**
	 * 施法者
	 */
	private int casterId;
	
	/**
	 * 施法处理器
	 */
	private SkillCastHandler castHandler;
	
	/**
	 * 创建时间
	 */
	private long createTime;
	
	/**
	 * 开始时间
	 */
	private long startTime;
	
	/**
	 * 当前效果序号
	 */
	private int currentEffectIndex;

	public SkillEntity(WarInstance war, SkillModel model, GameObject caster, SkillCastHandler castHandler)
	{
		super(war);
		this.modelId = model.getId();
		this.casterId = caster.getObjectId();
		this.castHandler = castHandler;
		for(Integer effectId : model.getEffects())
		{
			EffectModel effectModel = StaticDataManager.EFFECT_DATA.getEffectModel(effectId);
			if(effectModel == null)
			{
				mLogger.error("effect model property error! can not find effect! id[" + model.getId() + "] subId[" + effectId + "]");
				continue;
			}
			
			EffectEntity effectEntity  = new EffectEntity(war, effectModel);
			effectEntity.setParentId(this.getObjectId());
			this.effectEntityList.add(effectEntity.getObjectId());
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
		StringBuilder sb = new StringBuilder("skill id[");
		sb.append(modelId).append("] targets[").append(targetIds.isEmpty()?0 : targetIds.get(0));
//		for(Integer targetId : targetIds)
//		{
//			sb.append(targetId).append(",");
//		}
		sb.append("] targetx[").append(targetX).append("] targety[").append(targetY)
		.append("] casterId[").append(casterId).append("] state[").append(state).append("]");
		return sb.toString();
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.SKILL;
	}
	
	/**
	 * 获取技能model
	 * @return
	 */
	public SkillModel getModel()
	{
		return StaticDataManager.SKILL_DATA.getModel(modelId);
	}
	
	/**
	 * 获取目标选择器
	 * @return
	 */
	public TargetSelector getTargetSelector()
	{
		SkillModel model = getModel();
		if(model == null)
			return null;
		
		return StaticDataManager.TARGET_SELECTOR_DATA.getTargetSelector(model.getTargetSelectorId());
	}
	
	/**
	 * 增加当前效果索引
	 */
	public void addEffectIndex()
	{
		if(currentEffectIndex >= (effectEntityList.size() - 1))
			currentEffectIndex = 0;
		else
			currentEffectIndex += 1;
	}

	/**
	 * 获取当前执行的效果
	 * @return
	 */
	public EffectEntity getCurrentEffect()
	{
		int effecInstanceId = effectEntityList.get(currentEffectIndex);
		GameObject effect = getWar().getObject(effecInstanceId);
		if(effect.getObjectType() != GameObjectType.SKILL_EFFECT)
			return null;
		
		return (EffectEntity) effect;
	}
	
	/**
	 * 获取施法者
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends GameObject> T getCaster()
	{
		return (T) getWar().getObject(casterId);
	}
	
	/**
	 * 执行当前效果
	 * @param currTime
	 * @return
	 */
	public SkillError executeCurrentEffect(long currTime)
	{
		if(currentEffectIndex >= effectEntityList.size())
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		EffectEntity effect = getCurrentEffect();
		if(effect == null)
			return SkillError.ERROR_BASE_DATA_ERROR;
		
		if(effect.getState() != SkillManager.IDLE)
			return SkillError.SUCCESS;
		
		effect.setState(SkillManager.CREATE);
		effect.setCreateTime(currTime);
		effect.setTargetIds(targetIds);
		effect.setTargetX(targetX);
		effect.setTargetY(targetY);
		getWar().getSkillManager().addEffect(effect);
		return SkillError.SUCCESS;
	}
	
	@Override
	public int compareTo(SkillEntity o)
	{
		if(o == null)
			return -1;
		
		if(this.getModel().getPriority() < o.getModel().getPriority())
			return -1;
		
		if(this.getModel().getPriority() > o.getModel().getPriority())
			return 1;
		
		if(this.getModel().getWeight() > o.getModel().getWeight())
			return -1;
		
		if(this.getModel().getWeight() < o.getModel().getWeight())
			return 1;
		
		return 0;
	}
	
	/**
	 * 是否cd中
	 * @param currTime
	 * @return
	 */
	public boolean isInCD(long currTime)
	{
		SkillModel model = getModel();
		if(model == null)
			return true;
		
		long reduceCd = 0L;
		GameObject caster = getCaster();
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
//			if(this.getModelId() == 101)
//				System.err.println("skill cd change:" + reduceCd);
			
			reduceCd = (long) (model.getRealCd() * ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.CD_ADD, modelId));
			
		}
		
		return nextCastTime + reduceCd > currTime;
	}
	
	/**
	 * 获取剩余cd时间
	 * @param currTime
	 * @return
	 */
	public long getRemainCd(long currTime)
	{
		SkillModel model = getModel();
		if(model == null)
			return 0;
		
		long reduceCd = 0L;
		GameObject caster = getCaster();
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
			reduceCd = (long) (model.getRealCd() * ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.CD_ADD, modelId));
			
		}
		
		return nextCastTime + reduceCd - currTime;
	}
	
	/**
	 * 获取cd时间
	 * @return
	 */
	public long getCd()
	{
		SkillModel model = getModel();
		long cd = model.getRealCd();
		long reduceCd = 0L;
		GameObject caster = getCaster();
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
			reduceCd = (long) (cd * ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.CD_ADD, modelId));
		}
		return cd - reduceCd;
	}
	
	/**
	 * 能否打断
	 * @return
	 */
	public boolean canInterrupt()
	{
		SkillModel model = getModel();
		return !model.isPassive() && model.isCanInterrupt();
	}

	@Override
	public float getWeight()
	{
		return getModel().getWeight();
	}
	
	/**
	 * 获取技能施法者的控制器
	 * @return
	 */
	public WarControlInstance getOwnerControl() 
	{
		GameObject caster = getCaster();
		if(caster instanceof HavingCamp)
			return ((HavingCamp)caster).getControl();
		
		return null;
	}
	
	/**
	 * 获取将军技能model
	 * @return
	 */
	public StrategosSkillModel getSourceStrategosSkill()
	{
		GameObject caster = this.getCaster();
		if(this.getCaster().getObjectType() != GameObjectType.STRATEGOS)
			return null;
		
		StrategosObject strategos = (StrategosObject) caster;
		return strategos.getSkillById(modelId);
	}
	
	/**
	 * 设置目标
	 * @param targets
	 */
	public void setTargets(List<GameObject> targets)
	{
		this.targetIds.clear();
		for(GameObject target : targets)
		{
			if(isSkillValid(target))
				targetIds.add(target.getObjectId());
		}
	}
	
	/**
	 * 技能是否有效
	 * @param target
	 * @return
	 */
	private boolean isSkillValid(GameObject target)
	{
		List<SkillFilter> filters = ((HavingSkill)target).getAttachEffects().getSkillFilters();
		for(SkillFilter filter : filters)
		{
			if(!filter.isValid(this))
				return false;
		}
		
		return true;
	}
	
	/**
	 * 获取施法距离
	 * @return
	 */
	public float getCastRange()
	{
		float originRange = getModel().getRealRange();
		GameObject caster = getCaster();
		if(caster.getObjectType() == GameObjectType.TROOP)
		{
			originRange += ((Troop)caster).getAttrModList().getSkillModifiedTotal(ModifiedType.RANGE, modelId);
		}
		
		return originRange;
	}
	
	/**
	 * 是否并行的
	 * @return
	 */
	public boolean isParallel()
	{
		SkillModel model = getModel();
		return model.isParallel();
	}
	
	@Override
	public ProjectileAttrEntity getProjectileAttr()
	{
		return null;
	}
	
	/**
	 * 结束处理
	 */
	public void onExit()
	{
		this.setState(SkillManager.IDLE);
		if(!this.getModel().isPassive() && !this.isParallel())
		{
			GameObject caster = this.getCaster();
			if(caster.getObjectType() == GameObjectType.TROOP)
			{
				Agent agent = ((Troop)caster).getAgent();
				if(agent instanceof BaseAIObj)
					((BaseAIObj)agent).setActSkillId(0);
			}
				
		}
	}
	
	/**
	 * 打断技能
	 * @param source
	 */
	public boolean interrupt(GameObject source, boolean isForce)
	{
		WarInstance war = getWar();
		boolean interruptSuccess = true;
		for(Integer effectId : getEffectEntityList())
		{
			EffectEntity effect = (EffectEntity) war.getObject(effectId);
			if(effect.getState() >= SkillManager.END && effect.isForceEnd() == isForce)
				interruptSuccess = false;
			else
				war.getSkillManager().addSkillEvent(new InterruptEffectSkillEvent(war, source, effect, isForce));
		}
		return interruptSuccess;
	}
	
	public int getModelId()
	{
		return modelId;
	}

	public void setModelId(int modelId)
	{
		this.modelId = modelId;
	}

	public List<Integer> getEffectEntityList()
	{
		return effectEntityList;
	}

	public void setEffectEntityList(List<Integer> effectEntityList)
	{
		this.effectEntityList = effectEntityList;
	}

	public List<Integer> getTargetIds()
	{
		return targetIds;
	}

	public void setTargetIds(List<Integer> targetIds)
	{
		this.targetIds = targetIds;
	}

	public long getLastCastTime()
	{
		return lastCastTime;
	}

	public void setLastCastTime(long lastCastTime)
	{
		this.lastCastTime = lastCastTime;
	}

	public long getNextCastTime()
	{
		return nextCastTime;
	}

	public void setNextCastTime(long nextCastTime)
	{
		this.nextCastTime = nextCastTime;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getCasterId()
	{
		return casterId;
	}

	public void setCasterId(int casterId)
	{
		this.casterId = casterId;
	}

	public SkillCastHandler getCastHandler()
	{
		return castHandler;
	}

	public void setCastHandler(SkillCastHandler castHandler)
	{
		this.castHandler = castHandler;
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

	public void setTargetX(float targetX)
	{
		this.targetX = targetX;
	}

	public void setTargetY(float targetY)
	{
		this.targetY = targetY;
	}

	public int getCurrentEffectIndex()
	{
		return currentEffectIndex;
	}

	public void setCurrentEffectIndex(int currentEffectIndex)
	{
		this.currentEffectIndex = currentEffectIndex;
	}

	

	

}
