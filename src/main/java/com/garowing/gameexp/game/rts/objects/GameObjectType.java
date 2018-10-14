package com.garowing.gameexp.game.rts.objects;

/**
 * 游戏对象类型
 * @author seg
 * 2017年3月24日
 */
public interface GameObjectType
{
	/**
	 * 战场
	 */
	int WAR = 1;
	
	/**
	 * 阵营管理
	 */
	int CAMP = 2;
	
	/**
	 * 控制器
	 */
	int CONTROLLOR = 3;
	
	/**
	 * 技能管理器
	 */
	int SKILL_MGR = 4;
	
	/**
	 * 场景管理器
	 */
	int SCENE = 5;
	
	/**
	 * 生命单位
	 */
	int TROOP = 6;
	
	/**
	 * 技能
	 */
	int SKILL = 7;
	
	/**
	 * 技能触发事件
	 */
	int SKILL_EVENT = 8;
	
	/**
	 * 技能效果
	 */
	int SKILL_EFFECT= 9;
	
	/**
	 * 卡牌
	 */
	int CARD = 10;
	
	/**
	 * 将军
	 */
	int STRATEGOS = 11;
	
	/**
	 * 关注区域
	 */
	int AOI = 12;

	/**
	 * 人工智能
	 */
	int AI = 13;

	/**
	 * 战场事件
	 */
	int WAR_EVENT = 14;

	/**
	 * 附加效果列表
	 */
	int ATTACH_EFFECTS_LIST = 15;

	/**
	 * 客户端通知集合
	 */
	int CLIENT_NOTIFICATION_MAP = 16;

	/**
	 * 点对象
	 */
	int POINT = 17;

	/**
	 * 表情
	 */
	int EMOJI =18;
}
