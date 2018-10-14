package com.garowing.gameexp.game.rts.objects.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.garowing.gameexp.game.rts.objects.listener.model.WarListenerModel;
import com.garowing.gameexp.game.rts.objects.listener.objects.VisualType;
import com.yeto.war.fightcore.fight.model.BattleModel;
import com.yeto.war.module.fight.model.MaskModel;

/**
 * 战争原形
 * 
 * @author darren.ouyang <ouyang.darren@gmail.com>
 * @date 2015年4月10日
 * @version 1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class WarModel
{
	// 战争模型ID
	private final int id;

	// 战争地图ID
	private final int mapID;

	// 战争存活时间
	private final long survivalTime;

	// 战争类型
	private final WarTypeEnum warType;

	// 等级
	private final int level;

	// 战争信息
	private final String info;

	// 引导卡牌集合 1001,1002
	private final String guideCards;

	// 所有阵营
	private final List<WarCampModel> camps;

	// 战争监听事件集合
	private final List<WarListenerModel> listenerCollection;
	
	//战场中出现的部队ID集合
	private final Set<Integer> troopIds;

	public WarModel(BattleModel battleModel, MaskModel maskModel)
	{

		this.id = battleModel.getId();
		this.level = battleModel.getLevel();
		this.mapID = battleModel.getMapId();
		this.survivalTime = battleModel.getDurationTime();
		this.warType = WarTypeEnum.getEnum(battleModel.getScriptId());
		this.info = battleModel.getInfo();
		this.guideCards = battleModel.getGuideCards();
		this.camps = battleModel.getCamps();
		this.troopIds = battleModel.getVisualIds(VisualType.TROOP.ordinal());

		if (battleModel.getDungeonModel() != null)
			this.listenerCollection = battleModel.getDungeonModel().getListeners();
		else
			this.listenerCollection = new ArrayList<>();
	}

	public int getLevel()
	{
		return level;
	}

	public int getId()
	{
		return id;
	}

	public int getMapID()
	{
		return mapID;
	}

	public long getSurvivalTime()
	{
		return survivalTime;
	}

	public WarTypeEnum getWarType()
	{
		return warType;
	}

	public String getInfo()
	{
		return info;
	}

	public String getGuideCards()
	{
		return guideCards;
	}

	public List<WarCampModel> getCamps()
	{
		return camps;
	}

	public List<WarListenerModel> getListeners()
	{
		return listenerCollection;
	}

	public Set<Integer> getTroopIds()
	{
		return troopIds;
	}

	
}
