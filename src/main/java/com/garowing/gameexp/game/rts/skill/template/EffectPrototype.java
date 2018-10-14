package com.garowing.gameexp.game.rts.skill.template;

import java.util.Collections;
import java.util.List;

import com.yeto.war.datastatic.StaticDataManager;
import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.entity.EffectEntity;
import com.garowing.gameexp.game.rts.skill.entity.SkillParams;
import com.garowing.gameexp.game.rts.skill.manager.EffectPlayControllorManager;
import com.garowing.gameexp.game.rts.skill.manager.ListIntPropertyTransformer;
import com.garowing.gameexp.game.rts.skill.model.SkillError;

import commons.configuration.Property;

/**
 * 效果原型
 * @author seg
 *
 */
public abstract class EffectPrototype
{
	/**
	 * modelId
	 */
	@Property(key = EffectKey.ID, defaultValue = "0")
	protected int modelId;
	
	/**
	 * 初始延迟
	 */
	@Property(key = EffectKey.INIT_DELAY, defaultValue = "0")
	protected long initDelay;
	
	/**
	 * 执行延迟
	 */
	@Property(key = EffectKey.EXEC_DELAY, defaultValue = "0")
	protected long execDelay;
	
	/**
	 * 飞行速度
	 */
	@Property(key = EffectKey.FLY_SPEED, defaultValue = "0")
	protected int flySpeed;
	
	/**
	 * 目标选择器
	 */
	@Property(key = EffectKey.TARGET_SELECTOR_ID, defaultValue = "0")
	protected int targetSelectorId;
	
	/**
	 * 是否需要目标
	 */
	@Property(key = EffectKey.NEED_TARGET, defaultValue = "true")
	protected boolean needTarget;
	
	/**
	 * 播放控制类型
	 */
	@Property(key = EffectKey.PLAY_CONTROL, defaultValue = "fixTimeEp")
	protected String playControl;
	
	/**
	 * 激活次数
	 */
	@Property(key = EffectKey.ACTIVATE_COUNT, defaultValue = "1")
	protected int activateCount;
	
	/**
	 * 模板id
	 */
	@Property(key = EffectKey.TEMPLATE, defaultValue = "0")
	protected int templateId;
	
	/**
	 * 间隔时间
	 */
	@Property(key = EffectKey.INTERVAL, defaultValue = "0")
	protected int interval;
	
	/**
	 * 后摇时间
	 */
	@Property(key = EffectKey.END_DELAY, defaultValue = "0")
	protected long endDelay;
	
	/**
	 * 通知类型
	 */
	@Property(key = EffectKey.NOTIFY_TYPE, defaultValue = "0")
	protected int notifyType;
	
	/**
	 * 效果id集合
	 */
	@Property(key = EffectKey.SUB_EFFECTS, defaultValue = "", propertyTransformer = ListIntPropertyTransformer.class)
	protected List<Integer> subEffectIds;
	
	/**
	 * 检查参数
	 * @param effect
	 * @return
	 */
	abstract public SkillError onCheck(EffectEntity effect);
	
	/**
	 * 开始效果
	 * @param effect
	 * @param currTime 
	 * @return
	 */
	abstract public SkillError onStart(EffectEntity effect, long currTime);
	
	/**
	 * 重复激活
	 * @param effect
	 * @return
	 */
	abstract public SkillError onActivate(EffectEntity effect, long currTime);
	
	/**
	 * 结束效果
	 * @param effect
	 * @return
	 */
	abstract public SkillError onExit(EffectEntity effect);
	
	/**
	 * 获取目标选择器
	 * @return
	 */
	public TargetSelector getTargetSelector()
	{
		return StaticDataManager.TARGET_SELECTOR_DATA.getTargetSelector(targetSelectorId);
	}
	
	/**
	 * 获取效果播放器
	 * @return
	 */
	public EffectPlayControllor getPlayControllor()
	{
		return EffectPlayControllorManager.getInstance().getEffectPlayControllor(playControl);
	}
	
	/**
	 * 检查客户端参数，需要客户端通知时校验
	 * @param effect
	 * @param packect
	 * @return
	 */
	public SkillError checkClientParams(EffectEntity effect, SkillParams packect){return SkillError.SUCCESS;}
	
	/**
	 * 获取目标
	 * @param effect
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GameObject> getTargets(EffectEntity effect)
	{
		TargetSelector targetSelector = effect.getEffectPrototype().getTargetSelector();
		if(targetSelector == null)
			return Collections.EMPTY_LIST;
		
		if(!targetSelector.check(effect))
			return Collections.EMPTY_LIST;
		
		return targetSelector.findTarget(effect);
	}

	public int getModelId()
	{
		return modelId;
	}

	public void setModelId(int modelId)
	{
		this.modelId = modelId;
	}

	public long getInitDelay()
	{
		return initDelay;
	}

	public void setInitDelay(long initDelay)
	{
		this.initDelay = initDelay;
	}

	public long getExecDelay()
	{
		return execDelay;
	}

	public void setExecDelay(long execDelay)
	{
		this.execDelay = execDelay;
	}

	public int getFlySpeed()
	{
		return flySpeed;
	}

	public void setFlySpeed(int flySpeed)
	{
		this.flySpeed = flySpeed;
	}

	public int getTargetSelectorId()
	{
		return targetSelectorId;
	}

	public void setTargetSelectorId(int targetSelectorId)
	{
		this.targetSelectorId = targetSelectorId;
	}

	public boolean isNeedTarget()
	{
		return needTarget;
	}

	public void setNeedTarget(boolean needTarget)
	{
		this.needTarget = needTarget;
	}

	public String getPlayControl()
	{
		return playControl;
	}

	public void setPlayControl(String playControl)
	{
		this.playControl = playControl;
	}

	public int getActivateCount()
	{
		return activateCount;
	}

	public void setActivateCount(int activateCount)
	{
		this.activateCount = activateCount;
	}

	public int getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(int templateId)
	{
		this.templateId = templateId;
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	public long getEndDelay()
	{
		return endDelay;
	}

	public void setEndDelay(long endDelay)
	{
		this.endDelay = endDelay;
	}

	public int getNotifyType()
	{
		return notifyType;
	}

	public void setNotifyType(int notifyType)
	{
		this.notifyType = notifyType;
	}

	public List<Integer> getSubEffectIds()
	{
		return subEffectIds;
	}

	public void setSubEffectIds(List<Integer> subEffectIds)
	{
		this.subEffectIds = subEffectIds;
	}
	
	
}
