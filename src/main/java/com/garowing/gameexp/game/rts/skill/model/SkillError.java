package com.garowing.gameexp.game.rts.skill.model;

/**
 * 技能错误枚举
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年3月23日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public enum SkillError 
{
	SUCCESS	("成功"),
	ERROR_NO_SKILL_MODEL 			("不存在的技能Model"),
	ERROR_NO_TRIGGER_EFFECT_MODEL 	("不存在的触发效果(技能)Model"),
	ERROR_EFFECT_NO_ATTACK 			("效果没有拥有者,效果失败"),
	ERROR_EFFECT_NO_TARGET 			("效果没有目标,效果失败"),
	ERROR_BUFF_NO_TARGET 			("效果BUFF没有目标,效果失败"),
	ERROR_TARGGER_NO_TARGET			("触发没有目标,效果失败"),
	ERROR_ROOT_SKILL_NO_CONTROL		("首层技能没有控制器,效果失败"),
	ERROR_ATTACH_NOT_DY_AUREOLE		("效果依附对象不是动态光环类型失败"),
	ERROR_ATTACH_NOT_ST_AUREOLE		("效果依附对象不是静态光环类型失败"),
	ERROR_ST_ATTACH_POINT_NO_TARGET	("静态光环生成时,不存在目标对象导致找不到目标点释放失败"),
	ERROR_EFF_CALL_TROOP_NO_MODEL	("召唤部队技能找不到部队ID"),
	ERROR_EFF_CALL_TROOP_NO_CONTROL	("召唤部队技能找不到最终归属阵营"),
	ERROR_EFF_CONTROL_NOT_PLAYER	("控制器攻击对象不是玩家,不存在粮草"),
	ERROR_EFF_NO_FOOD				("粮草在效果中消耗不足"),
	ERROR_FIGHT_STATE_REPETION		("战斗状态叠加重复"),
	ERROR_FIGHT_STATE_IMMUN			("战斗状态免疫"),
	ERROR_FIGHT_STATE_INVINCIBLE	("无敌状态"),
	ERROR_DYNAMIC_AUREOLE_NO_TARGET	("动态光环没有目标"),
	ERROR_DISPLACEMENT_NO_TYPE		("位移技能找不到配置类型"),
	ERROR_TARGET_TEMP_CHECK_FALSE	("目标模板参数检测失败"),
	ERROR_ATTR_BUFF_NO_DATA			("属性加成不存在属性失败"),
	ERROR_DISPLACE_BUILD			("位移不能是建筑"),
	ERROR_NOCONTROL_CONTROL_EFF		("不存在控制器"),
	ERROR_NOTFIGHT_CONTROL_EFF		("非战斗控制器"),
	ERROR_RATION_FAILED				("随机概率失败"),
	ERROR_TRIGGER_NO_TYPE			("没有触发类型"),
	ERROR_CHILD_SUM_NO_DATA			("召唤小兵脚本没找到中间Map数据"),
	ERROR_CHILD_SUM_NO_CHILD		("召唤小兵脚本召唤未能成功"),
	ERROR_CHAOS_STATE				("眩晕状态不能放能"),
	ERROR_BASE_DATA_ERROR			("基础配置错误"),	
	ERROR_INVALID_STATE				("当前状态技能失效"),	
	
	ERROR_NO_TARGET					("技能不存在目标"),	
	ERROR_CD_TIME					("技能cd"),
	ERROR_CASTER_CAN_NOT_ACT		("施法者不能行动")
	;
	
	public final String info; 
	SkillError (String info)
	{
		this.info = info;
	}
}
