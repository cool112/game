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

import com.garowing.gameexp.game.rts.skill.constants.TargetSelectorKey;
import com.garowing.gameexp.game.rts.skill.constants.TargetSelectorType;
import com.garowing.gameexp.game.rts.skill.template.TargetSelector;

import commons.configuration.ConfigurableProcessor;

/**
 * 目标选择器model
 * @author seg
 *
 */
@XmlRootElement(name = "target_selector")
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetSelectorModel
{
	private static final Logger mLogger = Logger.getLogger(TargetSelectorModel.class);
	
	/**
	 * id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 模板id
	 */
	@XmlAttribute
	private int templateId;
	
	/**
	 * 最大目标数
	 */
	@XmlAttribute
	private int maxTargetCount;
	
	/**
	 * 是否锁定目标
	 */
	@XmlAttribute
	private boolean lockTarget;
	
	/**
	 * 过滤器id
	 */
	@XmlAttribute
	private int filterId;
	
	/**
	 * 半径
	 */
	@XmlAttribute
	private float radius;
	
	/**
	 * 弧度
	 */
	@XmlAttribute
	private float radian;
	
	/**
	 * 多边形端点(0,0)为施法者
	 */
	@XmlAttribute
	private String polygon;
	
	/**
	 * 是否重复
	 */
	@XmlAttribute
	private boolean isRepeat;

	/**
	 * 内半径
	 */
	@XmlAttribute
	private float insideRadius;
	
	/**
	 * 目标选择器实体
	 */
	@XmlTransient
	private TargetSelector targetSelector;
	
	@SuppressWarnings("unchecked")
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put(TargetSelectorKey.ID, id + "");
		params.put(TargetSelectorKey.MAX_TARGET, maxTargetCount + "");
		params.put(TargetSelectorKey.IS_LOCKED, lockTarget + "");
		params.put(TargetSelectorKey.RADIUS, radius + "");
		params.put(TargetSelectorKey.RADIAN, radian + "");
		params.put(TargetSelectorKey.FILTERID, filterId + "");
		params.put(TargetSelectorKey.POLYGON_POINTS, polygon);
		params.put(TargetSelectorKey.IS_REPEAT, isRepeat + "");
		params.put(TargetSelectorKey.INSIDE_RADIUS, insideRadius + "");
		
		TargetSelectorType selectorType = TargetSelectorType.getTypeById(templateId);
		if(selectorType == null)
		{
			mLogger.error("init targetSelector fail! modelId{" + id + "] templateId[" + templateId + "]");
			return;
		}
		
		Class<? extends TargetSelector> clazz = selectorType.getImplClass();
		if(clazz == null)
		{
			mLogger.error("init targetSelector fail! no implClass found! modelId{" + id + "] templateId[" + templateId + "]");
			return;
		}
		
		try
		{
			targetSelector = clazz.newInstance();
			ConfigurableProcessor.process(targetSelector, params);
		} catch (InstantiationException | IllegalAccessException e)
		{
			mLogger.error("init targetSelector fail! modelId{" + id + "]", e);
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

	public int getMaxTargetCount()
	{
		return maxTargetCount;
	}

	public boolean isLockTarget()
	{
		return lockTarget;
	}

	public int getFilterId()
	{
		return filterId;
	}

	public float getRadius()
	{
		return radius;
	}

	public float getRadian()
	{
		return radian;
	}

	public String getPolygon()
	{
		return polygon;
	}

	public TargetSelector getTargetSelector()
	{
		return targetSelector;
	}

	public float getInsideRadius()
	{
		return insideRadius;
	}
	
	
	
	
}
