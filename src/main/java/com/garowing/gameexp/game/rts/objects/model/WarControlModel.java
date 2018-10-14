package com.garowing.gameexp.game.rts.objects.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.yeto.war.fightcore.fight.model.TroopBornSpecialModel;
import com.yeto.war.module.scene.constants.AiGridConstants;
import com.yeto.war.module.troop.model.TroopBornModel;

/**
 * 控制器model
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年8月1日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@XmlRootElement(name = "control")
@XmlAccessorType(XmlAccessType.NONE)
public class WarControlModel 
{
	/**
	 * 控制器ID
	 * 0, 默认值, 不能使用
	 */
	@XmlAttribute(name = "control_id")
	private int controlId;
	
	/**
	 * 控制器类型 
	 * 1	阵营控制器
	 * 2	玩家控制器
	 * 3	副本控制
	 */
	@XmlAttribute(name = "control_type")
	private int controlType;
	
	/**
	 * 摄像头ID
	 */
	@XmlAttribute(name = "vidicon_id")
	private int vidiconId;
	
	// 初始化粮草点
	@XmlAttribute(name = "init_food")
	private int initFood;
	
	// 初始化粮草恢复
	@XmlAttribute(name = "init_food_sd")
	private float initFoodSD;

	/* 部队特殊产出 */
	@XmlElement(name = "troop_born_special")
	private List<TroopBornSpecialModel> specialBorns;
	
	/* 普通部队产出 */
	@XmlElement(name = "troop_born")
	private List<TroopBornModel> borns;
	
	/**
	 * 控制器技能列表
	 */
	@XmlAttribute
	private List<Integer> skills;
	
	/**
	 * 基地坐标
	 */
	@XmlTransient
	private float[] basePosition;
	
	/**
	 * ai使用的网格集合
	 */
	@XmlTransient
	private Map<Integer, Map<Integer, float[]>> aiGridMap;
	
	/**
	 * x轴标签
	 */
	@XmlTransient
	private float[] xAxis;
	
	/**
	 * y轴标签
	 */
	@XmlTransient
	private float[] yAxis;
	
	/**
	 * 边 0-左 1-右
	 */
	@XmlTransient
	private int side;

	public final static WarControlModel DefultCampControlModel = new WarControlModel("defult_camp_control");
	
	public WarControlModel (){}
	
	@SuppressWarnings("unchecked")
	public WarControlModel (String type)
	{
		this.controlType = 1;
		this.vidiconId = 0;
		this.specialBorns = new ArrayList<>();
		this.specialBorns = new ArrayList<>();
		this.skills = Collections.EMPTY_LIST;
	}
	
	void afterUnmarshal(Unmarshaller u, Object p)
	{
		if(specialBorns == null)
			return;
		
		basePosition = new float[2];
		basePosition[0] = specialBorns.get(0).getX();
		basePosition[1] = specialBorns.get(0).getY();
		side = basePosition[1] < AiGridConstants.MIDDLE_Y ? 0 : 1;
		yAxis = new float[AiGridConstants.MAX_Y_AXIS + 1];
		xAxis = new float[AiGridConstants.MAX_X_AXIS + 1];
		aiGridMap = new HashMap<Integer, Map<Integer, float[]>>();
		float[] originPoint = new float[2];
		float[] originVector = null;
		float yOffset = AiGridConstants.WIDTH;
		switch (side)
		{
		case 0:
			originVector = AiGridConstants.LEFT_ORIGIN_VECTOR;
			break;
		case 1:
			originVector = AiGridConstants.RIGHT_ORIGIN_VECTOR;
			yOffset = -yOffset;
			break;
		default:
			break;
		}
		
		originPoint[0] = basePosition[0] + originVector[0];
		originPoint[1] = basePosition[1] + originVector[1];
		yAxis[0] = originPoint[1];
		xAxis[0] = originPoint[0];
		for(int i = 0; i < AiGridConstants.MAX_Y_AXIS; i++)
		{
			yAxis[i+1] = yAxis[i] + yOffset;
			for(int j = 0; j < AiGridConstants.MAX_X_AXIS; j++)
			{
				xAxis[j + 1] = xAxis[j] - AiGridConstants.HEIGHT;
				Map<Integer, float[]> ySubMap = aiGridMap.get(i);
				if(ySubMap == null)
				{
					ySubMap = new HashMap<Integer, float[]>();
					aiGridMap.put(i, ySubMap);
				}
				
				float[] gridPoint = ySubMap.get(j);
				if(gridPoint == null)
				{
					gridPoint = new float[2];
					ySubMap.put(j, gridPoint);
				}
				
				gridPoint[0] = (xAxis[j] + xAxis[j + 1]) / 2;
				gridPoint[1] = (yAxis[i] + yAxis[i + 1]) / 2;
			}
		}
	}
	
	public int getControlId()
	{
		return controlId;
	}

	/**
	 * 控制器类型 
	 * 1	阵营控制器
	 * 2	玩家控制器
	 * 3	玩家机器人
	 */
	public int getControlType() {
		return controlType;
	}
	
	/**
	 * 获取ai网格中心点
	 * @param x
	 * @param y
	 * @return
	 */
	public float[] getAiGridPoint(int x, int y)
	{
		Map<Integer, float[]> ySubMap = aiGridMap.get(y);
		if(ySubMap == null)
			return null;
		
		return ySubMap.get(x);
	}

	public List<TroopBornSpecialModel> getSpecialBorns() {
		return specialBorns;
	}

	public List<TroopBornModel> getBorns() {
		return borns;
	}

	public int getVidiconId() {
		return vidiconId;
	}

	public int getInitFood() {
		return initFood;
	}

	public float getInitFoodSD() {
		return initFoodSD;
	}

	public List<Integer> getSkills()
	{
		return skills;
	}

	public float[] getBasePosition()
	{
		return basePosition;
	}

	public Map<Integer, Map<Integer, float[]>> getAiGridMap()
	{
		return aiGridMap;
	}

	public float[] getxAxis()
	{
		return xAxis;
	}

	public float[] getyAxis()
	{
		return yAxis;
	}

	public int getSide()
	{
		return side;
	}
	
	
	
}
