package com.garowing.gameexp.game.rts.skill.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.manager.SkillAIManager;
import com.garowing.gameexp.game.rts.skill.manager.SkillPlayControllorManager;
import com.garowing.gameexp.game.rts.skill.template.SkillAIObj;
import com.garowing.gameexp.game.rts.skill.template.SkillPlayControllor;

import commons.configuration.ConfigurableProcessor;

/**
 * 技能model
 * @author seg
 *
 */
@XmlRootElement( name = "skill")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillModel
{
	private static final Logger mLogger = Logger.getLogger(SkillModel.class);
	
	/**
	 * id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 目标选择器id
	 */
	@XmlAttribute
	private int targetSelectorId;
	
	/**
	 * 施法距离
	 */
	@XmlAttribute
	private int range;
	
	/**
	 * 效果集合
	 */
	@XmlAttribute
	private List<Integer> effects;
	
	/**
	 * 需要目标类型 0-不需要 1-单位或坐标 2-单位
	 */
	@XmlAttribute
	private int needTarget;
	
	/**
	 * cd,毫秒
	 */
	@XmlAttribute
	private long cd;
	
	/**
	 * 优先级,升序
	 */
	@XmlAttribute
	private int priority;
	
	/**
	 * 权重,相同优先级之间才使用
	 */
	@XmlAttribute
	private int weight;
	
	/**
	 * 播放控制器
	 */
	@XmlAttribute
	private String playControl;
	
	
	/**
	 * 是否是被动技能
	 */
	@XmlAttribute
	private boolean passive;
	
	/**
	 * 技能ai
	 */
	@XmlAttribute
	private String ai;
	
	/**
	 * 时间因子，做施法加速用，前端需求
	 */
	@XmlAttribute
	private float timeFactor;
	
	/**
	 * 是否可以打断
	 */
	@XmlAttribute
	private boolean canInterrupt;
	
	/**
	 * 是否普通攻击
	 */
	@XmlAttribute
	private boolean isNormal;
	
	/**
	 * 并行的
	 */
	@XmlAttribute
	private boolean parallel;
	
	/**
	 * 播放控制器
	 */
	@XmlTransient
	private SkillPlayControllor playControllor;

	/**
	 * 真实距离
	 */
	@XmlTransient
	private float realRange;
	
	/**
	 * 技能ai
	 */
	@XmlTransient
	private SkillAIObj skillAI;
	
	/**
	 * 真实cd
	 */
	@XmlTransient
	private long realCd;
	
	
	@SuppressWarnings("unchecked")
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		// 播放控制脚本
		Map<String, String> playControlMap = new HashMap<String, String>();
		String[] playControlStrs = playControl.split(",");
		for(String playControlStr : playControlStrs)
		{
			String[] args = playControlStr.split("=");
			if (args.length != 2)
			{
				mLogger.error("技能表解析数据错误,播放控制模板字符串格式出错--技能ID["+ id +"] playControl["+ playControlStr +"]");
			}
			playControlMap.put(args[0], args[1]);
		}
		
		SkillPlayControllor playControllor = SkillPlayControllorManager.getInstance().createTemplate(playControlMap.get("sc"));
		
		try
		{
			ConfigurableProcessor.process(playControllor, playControlMap);
			this.playControllor = playControllor;
		}
		catch (Exception e)
		{
			mLogger.error("技能表解析数据错误,播放控制模板失败--技能ID["+ id +"] playControl["+ playControl +"]", e);
		}
		
		this.realRange = range / 50f;
		
		this.skillAI = SkillAIManager.getInstance().getSkillAI(ai);
		
		this.timeFactor = timeFactor == 0 ? 1 : timeFactor;
		this.realCd = (long) (cd * timeFactor);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getTargetSelectorId()
	{
		return targetSelectorId;
	}

	public void setTargetSelectorId(int targetSelectorId)
	{
		this.targetSelectorId = targetSelectorId;
	}

	public int getRange()
	{
		return range;
	}

	public void setRange(int range)
	{
		this.range = range;
	}

	public List<Integer> getEffects()
	{
		return effects;
	}

	public void setEffects(List<Integer> effects)
	{
		this.effects = effects;
	}

	public int getNeedTarget()
	{
		return needTarget;
	}

	public void setNeedTarget(int needTarget)
	{
		this.needTarget = needTarget;
	}

	public long getCd()
	{
		return cd;
	}

	public void setCd(long cd)
	{
		this.cd = cd;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
	}

	public SkillPlayControllor getPlayControllor()
	{
		return playControllor;
	}

	public String getPlayControl()
	{
		return playControl;
	}

	public boolean isPassive()
	{
		return passive;
	}

	public float getRealRange()
	{
		return realRange;
	}

	public String getAi()
	{
		return ai;
	}

	public SkillAIObj getSkillAI()
	{
		return skillAI;
	}

	public long getRealCd()
	{
		return realCd;
	}

	public float getTimeFactor()
	{
		return timeFactor <= 0 ? 1 : timeFactor;
	}

	public boolean isCanInterrupt()
	{
		return canInterrupt;
	}

	public boolean isNormal()
	{
		return isNormal;
	}

	public boolean isParallel()
	{
		return parallel;
	}
	
}
