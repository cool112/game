package com.garowing.gameexp.game.rts.ai.behaviac.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.garowing.gameexp.game.rts.base.xml.AbstractData;

/**
 * 附件
 * @author seg
 * 2017年1月9日
 */
@XmlRootElement(name = "attachment")
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentModel extends AbstractData
{
	/**
	 * 类型名
	 */
	@XmlAttribute(name = "class")
	private String className;
	
	/**
	 * id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 标志，似乎没用
	 */
	@XmlAttribute
	private String flag;
	
	/**
	 * 属性集合
	 */
	@XmlElement(name = "property")
	private List<PropertyModel> props;
	
	/**
	 * 属性映射
	 * propMap.name = propertyModel
	 */
	@XmlTransient
	private Map<String, PropertyModel> propMap;

	@Override
	protected void loadData()
	{
		if(props != null)
		{
			for(PropertyModel prop : props)
			{
				if(propMap == null)
					propMap = new HashMap<String, PropertyModel>();
				
				propMap.put(prop.getKey(), prop);
			}
		}
	}

	@Override
	protected void initData()
	{
		
	}
	
	/**
	 * 返回属性值
	 * @param key
	 * @return
	 */
	public Object getPropValue(String key)
	{
		PropertyModel propModel = getProp(key);
		if(propModel == null)
			return null;
		
		return propModel.getValue();
	}
	
	/**
	 * 获取属性model
	 * @param key
	 * @return
	 */
	public PropertyModel getProp(String key)
	{
		if(propMap == null)
			return null;
		
		return propMap.get(key);
	}

	public String getClassName()
	{
		return className;
	}

	public int getId()
	{
		return id;
	}

	public String getFlag()
	{
		return flag;
	}

	public List<PropertyModel> getProps()
	{
		return props;
	}
	
}
