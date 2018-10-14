package com.garowing.gameexp.game.rts.skill.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.constants.EffectKey;
import com.garowing.gameexp.game.rts.skill.constants.EffectType;
import com.garowing.gameexp.game.rts.skill.template.EffectPrototype;

import commons.configuration.ConfigurableProcessor;

/**
 * 效果model
 * @author seg
 *
 */
@XmlRootElement(name = "effect")
@XmlAccessorType(XmlAccessType.NONE)
public class EffectModel
{
	private static final Logger mLogger = Logger.getLogger(EffectModel.class);
	
	/**
	 * 唯一id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 模板id
	 */
	@XmlAttribute
	private int templateId;
	
	/**
	 * 部队类型
	 */
	@XmlAttribute
	private String troopTypes;
	
	/**
	 * 效果cd, 如被动触发之类
	 */
	@XmlAttribute
	private long cd;
	
	/**
	 * 间隔时间, 如buff
	 */
	@XmlAttribute
	private long interval;
	
	/**
	 * 计算类型，属性修改后的叠加方式
	 */
	@XmlAttribute
	private int calculateType;
	
	/**
	 * 子效果列表，在诸如组合效果、触发效果时使用
	 */
	@XmlAttribute
	private String subEffects;
	
	/**
	 * 播放控制，定时、线性、等待
	 */
	@XmlAttribute
	private String playControl;
	
	/**
	 * 因数A，公式使用
	 */
	@XmlAttribute
	private float factorA;
	
	/**
	 * 因数B，公式使用
	 */
	@XmlAttribute
	private float factorB;
	
	/**
	 * 因数C，公式使用
	 */
	@XmlAttribute
	private float factorC;
	
	/**
	 * 因数D，公式使用
	 */
	@XmlAttribute
	private float factorD;
	
	/**
	 * 因数E，公式使用
	 */
	@XmlAttribute
	private float factorE;
	
	/**
	 * 因数F，公式使用
	 */
	@XmlAttribute
	private float factorF;
	
	/**
	 * 属性串
	 */
	@XmlAttribute
	private String attributes;
	
	/**
	 * 概率
	 */
	@XmlAttribute
	private float probability;
	
	/**
	 * 目标选择器id
	 */
	@XmlAttribute
	private int targetSelectorId;
	
	/**
	 * 激活次数，持续性效果使用
	 */
	@XmlAttribute
	private int activateCount;
	
	/**
	 * 是否需要目标，除了少量地面技能不需要
	 */
	@XmlAttribute
	private boolean needTarget;
	
	/**
	 * 状态列表, buff或debuff的状态，多作为条件判断使用
	 */
	@XmlAttribute
	private String status;
	
	/**
	 * 初始化延迟,毫秒
	 */
	@XmlAttribute
	private long initDelay;
	
	/**
	 * 执行延迟，毫秒
	 */
	@XmlAttribute
	private long execDelay;
	
	/**
	 * 飞行速度
	 */
	@XmlAttribute
	private int flySpeed;
	
	/**
	 * 单位属性id
	 */
	@XmlAttribute
	private int attrId;
	
	/**
	 * 数量，如召唤物最大数量
	 */
	@XmlAttribute
	private int number;
	
	/**
	 * 存活时间，单位秒
	 */
	@XmlAttribute
	private int liveTime;
	
	/**
	 * 结束延迟
	 */
	@XmlAttribute
	private long endDelay;
	
	/**
	 * 通知类型
	 */
	@XmlAttribute
	private int notifyType;
	
	/**
	 * 效果原型
	 */
	@XmlTransient
	private EffectPrototype prototype;
	
	/**
	 * 时间因子，做施法加速用，前端需求
	 */
	@XmlAttribute
	private float timeFactor;
	
	/**
	 * 能量点序列
	 */
	@XmlAttribute
	private String energy;
	
	@SuppressWarnings("unchecked")
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put(EffectKey.ID, id + "");
		params.put(EffectKey.TROOP_TYPES, troopTypes);
		params.put(EffectKey.CD, cd + "");
		params.put(EffectKey.INTERVAL, (long)(interval * getTimeFactor()) + "");
		params.put(EffectKey.CALCULATE_TYPE, calculateType + "");
		params.put(EffectKey.SUB_EFFECTS, subEffects);
		params.put(EffectKey.PLAY_CONTROL, playControl);
		params.put(EffectKey.FACTOR_A, factorA + "");
		params.put(EffectKey.FACTOR_B, factorB + "");
		params.put(EffectKey.FACTOR_C, factorC + "");
		params.put(EffectKey.FACTOR_D, factorD + "");
		params.put(EffectKey.FACTOR_E, factorE + "");
		params.put(EffectKey.FACTOR_F, factorF + "");
		params.put(EffectKey.ATTRIBUTES, attributes);
		params.put(EffectKey.PROBABILITY, probability + "");
		params.put(EffectKey.TARGET_SELECTOR_ID, targetSelectorId + "");
		params.put(EffectKey.ACTIVATE_COUNT, activateCount + "");
		params.put(EffectKey.NEED_TARGET, needTarget + "");
		params.put(EffectKey.STATUS, status);
		params.put(EffectKey.INIT_DELAY, (long)(initDelay * getTimeFactor()) + "");
		params.put(EffectKey.EXEC_DELAY, (long)(execDelay * getTimeFactor()) + "");
		params.put(EffectKey.FLY_SPEED, flySpeed + "");
		params.put(EffectKey.TEMPLATE, templateId + "");
		params.put(EffectKey.ATTR_ID, attrId + "");
		params.put(EffectKey.NUMBER, number + "");
		params.put(EffectKey.LIVE_TIME, liveTime + "");
		params.put(EffectKey.END_DELAY, endDelay + "");
		params.put(EffectKey.NOTIFY_TYPE, notifyType + "");
		params.put(EffectKey.ENERGY, energy + "");
		
		EffectType effectType = EffectType.getTypeById(templateId);
		if(effectType == null)
		{
			mLogger.warn("effect init fail! no template! id[" + id + "] templateId[" + templateId + "]");
			return;
		}
		
		Class<? extends EffectPrototype> clazz = effectType.getImplClass();
		if(clazz == null)
		{
			mLogger.warn("effect init fail! no implClass! id[" + id + "] templateId[" + templateId + "]");
			return;
		}
		
		try
		{
			prototype = clazz.newInstance();
			ConfigurableProcessor.process(prototype, params);
		} catch (InstantiationException | IllegalAccessException e)
		{
			mLogger.error("effect init fail! id[" + id + "] templateId[" + templateId + "]", e);
		}
	}

	public int getId()
	{
		return id;
	}

	public int getTemplateId()
	{
		return templateId;
	}

	public long getCd()
	{
		return cd;
	}

	public long getInterval()
	{
		return interval;
	}

	public int getCalculateType()
	{
		return calculateType;
	}

	public String getPlayControl()
	{
		return playControl;
	}

	public float getFactorA()
	{
		return factorA;
	}

	public float getFactorB()
	{
		return factorB;
	}

	public float getFactorC()
	{
		return factorC;
	}

	public float getFactorD()
	{
		return factorD;
	}

	public float getFactorE()
	{
		return factorE;
	}

	public float getFactorF()
	{
		return factorF;
	}

	public String getAttributes()
	{
		return attributes;
	}

	public float getProbability()
	{
		return probability;
	}

	public int getTargetSelectorId()
	{
		return targetSelectorId;
	}

	public int getActivateCount()
	{
		return activateCount;
	}

	public boolean isNeedTarget()
	{
		return needTarget;
	}

	public long getInitDelay()
	{
		return initDelay;
	}

	public long getExecDelay()
	{
		return execDelay;
	}

	public int getFlySpeed()
	{
		return flySpeed;
	}

	public EffectPrototype getPrototype()
	{
		return prototype;
	}

	public long getEndDelay()
	{
		return endDelay;
	}

	public int getNotifyType()
	{
		return notifyType;
	}

	public String getTroopTypes()
	{
		return troopTypes;
	}

	public String getSubEffects()
	{
		return subEffects;
	}

	public String getStatus()
	{
		return status;
	}

	public float getTimeFactor()
	{
		return timeFactor <= 0 ? 1 : timeFactor;
	}
	
	
}
