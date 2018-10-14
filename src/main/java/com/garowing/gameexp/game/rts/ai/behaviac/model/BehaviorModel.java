package com.garowing.gameexp.game.rts.ai.behaviac.model;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 行为model
 * @author seg
 * 2017年1月9日
 */
@XmlRootElement(name = "behavior")
@XmlAccessorType(XmlAccessType.FIELD)
public class BehaviorModel
{
	/**
	 * 名称
	 */
	@XmlAttribute
	private String name;
	
	/**
	 * agent类型
	 */
	@XmlAttribute
	private String agenttype;
	
	/**
	 * 版号
	 */
	@XmlAttribute
	private String version;
	
	/**
	 * 根节点
	 */
	@XmlElement(name = "node")
	private NodeModel root;
	
	/**
	 * 短agent类型
	 */
	@XmlTransient
	private String shortAgentType;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		this.shortAgentType = agenttype.substring(agenttype.indexOf("::") + 2);
	}

	public String getName()
	{
		return name;
	}

	public String getAgenttype()
	{
		return agenttype;
	}

	public String getVersion()
	{
		return version;
	}

	public NodeModel getRoot()
	{
		return root;
	}

	/**
	 * 获取短类型名
	 * @return
	 */
	public String getShortAgenTtype()
	{
		return shortAgentType;
	}
}
