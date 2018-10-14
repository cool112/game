package com.garowing.gameexp.game.rts.skill.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import com.yeto.war.fightcore.attr.constants.UnitType;
import com.yeto.war.fightcore.fight.state.FightStateEnum;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterKey;
import com.garowing.gameexp.game.rts.skill.constants.PropsFilterType;
import com.garowing.gameexp.game.rts.skill.filter.unit.CompositeFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.UnitPropsFilter;
import com.yeto.war.utils.StringUtil;
import commons.configuration.ConfigurableProcessor;

/**
 * 单位属性过滤器model
 * @author seg
 *
 */
@XmlRootElement(name = "props_filter")
@XmlAccessorType(XmlAccessType.NONE)
public class PropsFilterModel
{
	private static final Logger mLogger = Logger.getLogger(PropsFilterModel.class);
	
	/**
	 * id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 阵营类型 0-all 1-friend 2-enemy
	 */
	@XmlAttribute
	private int campType;
	
	/**
	 * 是否包含自己
	 */
	@XmlAttribute
	private boolean includeSelf;
	
	/**
	 * 允许部队类型
	 */
	@XmlAttribute
	private List<Integer> allowTroopTypes;
	
	/**
	 * 禁止的部队类型
	 */
	@XmlAttribute
	private List<Integer> forbidTroopTypes;
	
	
	/**
	 * 过滤器类型
	 */
	@XmlAttribute
	private int type;
	
	/**
	 * 组合ids,按顺序筛选
	 */
	@XmlAttribute
	private List<Integer> compose;
	
	/**
	 * 消耗能量
	 */
	@XmlAttribute
	private List<Integer> consumeEnergy;
	
	/**
	 * 允许状态集合
	 */
	@XmlAttribute
	private List<Integer> allowTroopStatus;
	
	/**
	 * 禁止状态集合
	 */
	@XmlAttribute
	private List<Integer> forbidTroopStatus;
	
	/**
	 * 部队id集合
	 */
	@XmlAttribute
	private List<Integer> troopIds;
	
	/**
	 * 属性过滤条件(eg: hp<=20%,hp<=100)
	 */
	@XmlAttribute
	private String attributes;
	
	/**
	 * 禁止的召唤类型 0-卡牌或脚本 1-怪物 2-将军
	 */
	@XmlAttribute
	private String forbidCallerType;
	
	/**
	 * 允许部队类型掩码
	 */
	@XmlTransient
	private int troopTypeMask;
	
	/**
	 * 过滤器实体
	 */
	@XmlTransient
	private UnitPropsFilter filter;
	
	/**
	 * 允许状态掩码
	 */
	@XmlTransient
	private int troopStatusMask;
	
	@SuppressWarnings("unchecked")
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		Set<Integer> allowTypes = new HashSet<Integer>(allowTroopTypes);
		if(allowTypes.isEmpty())
			this.troopTypeMask = 0xffffffff;
		
		Set<Integer> forbidTypes = new HashSet<Integer>(forbidTroopTypes);
		UnitType[] types = UnitType.values();
		for(UnitType type : types)
		{
			if(allowTypes.contains(type.getCode()))
				this.troopTypeMask |= type.getMask();
			
			if(forbidTypes.contains(type.getCode()))
				this.troopTypeMask &= ~type.getMask();
		}
		
		Set<Integer> allowStatus = new HashSet<Integer>(allowTroopStatus);
		if(allowTypes.isEmpty())
			this.troopStatusMask = 0xffffffff;
		
		Set<Integer> forbidStatus = new HashSet<>(forbidTroopStatus);
		FightStateEnum[] statuses = FightStateEnum.values();
		for(FightStateEnum status : statuses)
		{
			if(allowStatus.contains(status.code))
				this.troopStatusMask |= status.getStates();
			
			if(forbidStatus.contains(status.code))
				this.troopTypeMask &= ~status.getStates();
		}
		
		Map<String, String> initParams = new HashMap<String, String>();
		initParams.put(PropsFilterKey.CAMP_TYPE, campType + "");
		initParams.put(PropsFilterKey.INCLUDE_SELF, includeSelf + "");
		initParams.put(PropsFilterKey.TROOP_TYPE_MASK, troopTypeMask + "");
		initParams.put(PropsFilterKey.ALLOW_TROOP_TYPES, StringUtil.ListToXmlFormat(allowTroopTypes));
		initParams.put(PropsFilterKey.CONSUME_ENERGY, StringUtil.ListToXmlFormat(consumeEnergy));
		initParams.put(PropsFilterKey.TROOP_STATUS_MASK, troopStatusMask + "");
		initParams.put(PropsFilterKey.TROOP_IDS, StringUtil.ListToXmlFormat(troopIds));
		initParams.put(PropsFilterKey.ATTRIBUTES, attributes);
		initParams.put(PropsFilterKey.CALLER_TYPE, forbidCallerType);
		if(compose.isEmpty())
		{
			if(type == PropsFilterType.COMPOSITE.getCode())
			{
				mLogger.error("composite filter's parameters is invalid! id:[" + id + "]");
				return;
			}
			
			PropsFilterType filterType = PropsFilterType.getTypeById(type);
			if(filterType == null)
			{
				mLogger.error("filter condition is invalid! id:[" + id + "] condition:[" + type + "]");
				return;
			}
			
			try
			{
				filter = filterType.getFilterClass().newInstance();
				ConfigurableProcessor.process(filter, initParams);
			} catch (InstantiationException | IllegalAccessException e)
			{
				mLogger.error("filter init fail!", e);
			}
				
		}
		else
		{
			filter = new CompositeFilter();
			((CompositeFilter)filter).setFilterIds(compose);
		}
		
	}

	public int getId()
	{
		return id;
	}

	public List<Integer> getAllowTroopTypes()
	{
		return allowTroopTypes;
	}

	public List<Integer> getForbidTroopTypes()
	{
		return forbidTroopTypes;
	}

	public int getType()
	{
		return type;
	}

	public List<Integer> getCompose()
	{
		return compose;
	}

	public boolean isIncludeSelf()
	{
		return includeSelf;
	}

	public int getTroopTypeMask()
	{
		return troopTypeMask;
	}

	public UnitPropsFilter getFilter()
	{
		return filter;
	}
	
}
