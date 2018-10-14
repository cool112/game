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
 * 节点model
 * @author seg
 * 2017年1月5日
 */
@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeModel extends AbstractData
{
	/**
	 * 节点类型
	 */
	@XmlAttribute(name = "class")
	private String className;
	
	/**
	 * id
	 */
	@XmlAttribute
	private int id;
	
	/**
	 * 子节点集合
	 */
	@XmlElement(name = "node")
	private List<NodeModel> children;
	
	/**
	 * 属性集合
	 */
	@XmlElement(name = "property")
	private List<PropertyModel> props;
	
	/**
	 * 附件集合
	 */
	@XmlElement(name = "attachment")
	private List<AttachmentModel> attachments;
	
	/**
	 * 节点映射
	 * nodeMap.id = node
	 */
	@XmlTransient
	private Map<Integer, NodeModel> nodeMap;
	
	/**
	 * 属性映射
	 */
	@XmlTransient
	private Map<String, PropertyModel> propMap;
	
	/**
	 * 附件映射
	 */
	@XmlTransient
	private Map<Integer, AttachmentModel> attachMap;

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
		
		if(children != null)
		{
			for(NodeModel node : children)
			{
				if(nodeMap == null)
					nodeMap = new HashMap<Integer, NodeModel>();
				
				nodeMap.put(node.id, node);
			}
		}
		
		if(attachments != null)
		{
			for(AttachmentModel attchment : attachments)
			{
				if(attachMap == null)
					attachMap = new HashMap<Integer, AttachmentModel>();
				
				attachMap.put(attchment.getId(), attchment);
			}
		}
	}

	@Override
	protected void initData()
	{
		
	}
	
	/**
	 * 根据id获取节点
	 * @param id
	 * @return
	 */
	public NodeModel getNode(int id)
	{
		if(nodeMap == null)
			return null;
		
		return nodeMap.get(id);
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

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public List<NodeModel> getChildren()
	{
		return children;
	}

	public void setChildren(List<NodeModel> children)
	{
		this.children = children;
	}

	public List<PropertyModel> getProps()
	{
		return props;
	}

	public void setProps(List<PropertyModel> props)
	{
		this.props = props;
	}

	public Map<Integer, NodeModel> getNodeMap()
	{
		return nodeMap;
	}

	public void setNodeMap(Map<Integer, NodeModel> nodeMap)
	{
		this.nodeMap = nodeMap;
	}

	public Map<String, PropertyModel> getPropMap()
	{
		return propMap;
	}

	public void setPropMap(Map<String, PropertyModel> propMap)
	{
		this.propMap = propMap;
	}

	public Map<Integer, AttachmentModel> getAttachMap()
	{
		return attachMap;
	}
	
	
	
}
