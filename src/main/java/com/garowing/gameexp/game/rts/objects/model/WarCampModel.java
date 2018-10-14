package com.garowing.gameexp.game.rts.objects.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 战场阵营Model
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2014年2月27日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
@XmlRootElement(name = "camp")
@XmlAccessorType(XmlAccessType.NONE)
public class WarCampModel
{
	/* 阵营号 */
	@XmlAttribute(name = "camp_numb")
	private int campNumb;

	/* 是否可供玩家选择 */
	@XmlAttribute(name = "is_player_chose")
	private boolean isPlayerChose;
	
	/** 
	 * 阵营类型 
	 * 1 玩家, 2 副本, 3 中立
	 */
	@XmlAttribute(name = "camp_type")
	private int campType;

	/* 产兵出生点x坐标 */
	@XmlAttribute(name = "born_x")
	private int bornX;
	
	/* 产兵出生点y坐标*/
	@XmlAttribute(name = "born_y")
	private int bornY;
	
	/* 产兵出生点半径 */
	@XmlAttribute(name = "born_r")
	private int bornR;
	
	/* 部队特殊产出 */
	@XmlElement(name = "control")
	private List<WarControlModel> controls;
	
	/**
	 * 阵营方向
	 */
	@XmlAttribute(name = "d")
	private int direction;

	public int getCampNumb() {
		return campNumb;
	}

	public boolean isPlayerChose() {
		return isPlayerChose;
	}

	/** 
	 * 阵营类型 
	 * 1 玩家, 2 副本, 3 中立
	 * @create	2014年7月30日	darren.ouyang
	 */
	public int getCampType() {
		return campType;
	}

	public int getBornX() {
		return bornX;
	}

	public int getBornY() {
		return bornY;
	}

	public List<WarControlModel> getControls() {
		return controls;
	}

	public int getBornR() {
		return bornR;
	}

	public int getDirection()
	{
		return direction;
	}
	
	

}
