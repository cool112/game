package com.garowing.gameexp.game.rts.skill.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.yeto.war.fightcore.attr.constants.UnitType;

/**
 * 技能目标ai model
 * @author seg
 *
 */
@XmlRootElement(name = "skill_target_ai")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillTargetAIModle
{
	private static final Logger mLogger = Logger.getLogger(SkillTargetAIModle.class);
	
	/**
	 * id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 阵营类型
	 */
	@XmlAttribute
	private int campType;
	
	/**
	 * 部队类型集合，掩码列表<p>
	 * 格式：(1 2),(1 4)
	 */
	@XmlAttribute(name = "troopTypes")
	private String troopTypesStr;
	
	/**
	 * 过滤器类型
	 * 1-最少血量
	 * 2-最远
	 * 3-类型优先，其次最近
	 * 4-随机
	 * 5-最近
	 */
	@XmlAttribute
	private int filterType;
	
	/**
	 * 搜寻半径超出的忽略，0-无限
	 */
	@XmlAttribute
	private float range;
	
	/**
	 * 是否严格限制类型
	 */
	@XmlAttribute
	private boolean restrictType;
	
	/**
	 * 是否必须改变目标
	 */
	@XmlAttribute
	private boolean changeTarget;
	
	/**
	 * 部队类型掩码
	 */
	@XmlTransient
	private List<Integer> troopTypes = new ArrayList<Integer>();
	
	@SuppressWarnings("unchecked")
	void afterUnmarshal(Unmarshaller u, Object p)
	{
		try
		{
			String[] subStrs = troopTypesStr.split("\\,");
			if (subStrs == null)
			{
				troopTypes = Collections.EMPTY_LIST;
				return;
			}
			for (String subStr : subStrs)
			{
				subStr = subStr.replaceAll("\\(|\\)", "");
				String[] intStrs = subStr.split(" +");
				if (intStrs == null)
					continue;

				List<Integer> typeList = new ArrayList<Integer>();
				for (String intStr : intStrs)
				{
					Integer code = Integer.valueOf(intStr);
					if (code != null)
						typeList.add(code);
				}

				int mask = UnitType.mergeUnitType(typeList);
				troopTypes.add(mask);
			} 
		} catch (Exception e)
		{
			mLogger.error("skillTargetModel init fail! id[" + id +"]", e);
		}
	}
	
}
