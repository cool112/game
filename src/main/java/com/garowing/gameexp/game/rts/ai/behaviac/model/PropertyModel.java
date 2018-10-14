package com.garowing.gameexp.game.rts.ai.behaviac.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

import com.garowing.gameexp.game.rts.ai.behaviac.constants.PropertyModelKey;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.AbstractTag;
import com.garowing.gameexp.game.rts.ai.behaviac.utils.TagUtils;
import com.garowing.gameexp.game.rts.base.xml.AbstractData;

/**
 * property Model
 * @author seg
 * 2017年1月5日
 */
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyModel extends AbstractData
{
	/**
	 * 属性集合
	 */
	@XmlAnyAttribute
	private Map<QName, String> attrMap;
	
	/**
	 * 标签集合
	 * tagMap.attr = tag
	 */
	@XmlTransient
	private Map<String, AbstractTag> tagMap;
	
	@Override
	protected void loadData()
	{
		for(Entry<QName, String> prop : attrMap.entrySet())
		{
			PropertyModelKey key = PropertyModelKey.getKey(prop.getKey().getLocalPart());
			if(key == null)
				continue;
			
			if(key.isNeedTransition())
			{
				AbstractTag tag = TagUtils.getParamTag(prop.getValue());
				if(tagMap == null)
					tagMap = new HashMap<String, AbstractTag>();
				
				tagMap.put(key.getName(), tag);
			}
		}
	}

	@Override
	protected void initData()
	{
		
	}
	
	/**
	 * 由于通常一个property只有一个属性，返回第一个key
	 * @return
	 */
	public String getKey()
	{
		if(attrMap.isEmpty())
			return "";
		
		return attrMap.keySet().iterator().next().getLocalPart();
	}
	
	/**
	 * 返回第一个属性值
	 * @return
	 */
	public Object getValue()
	{
		if(tagMap != null && !tagMap.isEmpty())
			return tagMap.values().iterator().next();
		
		if(attrMap.isEmpty())
			return "";
		
		return attrMap.values().iterator().next();
	}
}
