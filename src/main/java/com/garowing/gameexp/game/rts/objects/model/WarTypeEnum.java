package com.garowing.gameexp.game.rts.objects.model;

import com.garowing.gameexp.game.rts.objects.handler.WarHandler;

/**
 * 战争种类
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月10日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public enum WarTypeEnum 
{
//	ZHENG_FA_ZHANG				(10001, 10001, new QualifyingWarHandler(), "排位赛"),
//	HAO_YOU_QIE_CUO				(10002, 10002, new FriendWarHandler(), "好友切磋"),
//	HUN_LUAN_ZHI_DI				(10003, 10003, new ChaosWarHandler(), "混乱之地"),
//	DI_TU_SHUA_BING				(10005, 10005, new MissionBattleHander(), "任务剧情战斗"),
//	ZHENG_FA_BU_LUO				(10006, 10006, new QuartsBattleHander(), "征伐部落战"),
//	
//	CHENG_SHI_GUAN_KA			(10009, 10009, new CityWarHandler(), "城市关卡"),
//	ZHENG_FA_ZHANG_AI			(10010, 10010, new QualifyingRobotWarHandler(), "征伐战机器人"),
//	CHENG_ZHANG					(10011, 10011, new CityRobotWarHandler(), "城战机器人"),
//	FIRST_BATTLE				(10012, 10012, new NewGuidBattleHandler(), "第一场, 首秀"),
//	CHANNEL_BATTLE				(10013, 10013, new ChannelWarHandler(), "航道"),
//	BATTLE_PYRAMID				(10014, 10014, new PyramidWarHandler(), "金字塔"),
//	BATTLE_COLOSSEUM			(10015, 10015, new ColosseumWarHandler(), "斗兽场"),
//	BATTLE_ESCORT				(10016, 10016, new EscortWarHandler(), "护送"),
//	BATTLE_DIG_DIAMOND			(10017, 10017, new DigDiamondWarHandler(), "挖钻石"),
//	BATTLE_GANG_WAR_PVP			(10018, 10018, new GangWarPvpHandler(), "公会战PVP"),
//	BATTLE_GANG_WAR_PVE			(10019, 10019, new GangWarPveHandler(), "公会战PVE"),
//	BATTLE_WAR_GOD_PVE			(10020, 10020, new WarGodDungeonWarHandler(), "战神传记"),	
//	BATTLE_CONTEST				(10021, 10021, new ContestWarHandler(), "比武大会"),
//	BATTLE_FAIR_PLAY			(10022, 10022, new FairPlayWarHandler(), "公平竞技"), 
//	BATTLE_GALAXIES_CONQUEST	(20001, 20001, new GalaxiesConquestWarHandler(), "星系远征征服战"),
//	BATTLE_CHALLENGE			(10024, 10024, new ChallengeWarHandler(), "金币钻石副本"),
//	BATTLE_GANG_BOSS			(10025, 10025, new GangBossWarHandler(), "公会BOSS"),
//	ARENA						(10026, 10026, new ArenaWarHandler(), "竞技场"),
//	TOWER						(10027, 10027, new TowerWarHandler(), "地宫"),
//	CHAPTER_DUNGEON				(10028, 10028, new ChapterDungeonWarHandler(), "章节副本"),
//	BATTLE_GALAXIES_CUSTOM_DECK	(10029, 10029, new GalaxiesCustomDeckWarHandler(), "星系远征预置卡组战"),
//	QUALIFYING_GUIDE			(10030, 10030, new QualifyingGuideWarHandler(), "征伐战引导机器人"),
	;
	
	public int code;
	public int clientCode;
	public WarHandler handler;
	public String info;
	
	WarTypeEnum (int code, int clientCode, WarHandler handler, String info)
	{
		this.code = code;
		this.clientCode = clientCode;
		this.handler = handler;
		this.info = info;
	}
	
	/**
	 * 获得枚举
	 * @param code
	 * @return
	 * @create	2015年4月15日	darren.ouyang
	 */
	public static WarTypeEnum getEnum (int code)
	{
		for (WarTypeEnum warEnum : WarTypeEnum.values())
		{
			if(warEnum.code == code)
				return warEnum;
		}
		
		return null;
	}
}
