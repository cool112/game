package com.garowing.gameexp.game.rts.skill.handler.eventhandler;

import com.yeto.war.config.FightConfig;
import com.yeto.war.fightcore.fight.FightEngine;
import com.yeto.war.fightcore.fight.food.FoodAddType;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.event.AbstractSkillEvent;
import com.garowing.gameexp.game.rts.skill.handler.SkillEventHandler;
import com.garowing.gameexp.game.rts.model.WarControlModel;
import com.garowing.gameexp.game.rts.objects.AbsFightControl;

/**
 * 初始化控制器事件处理
 * @author seg
 * 2017年3月24日
 */
public class InitControlSkillEventHandler implements SkillEventHandler
{

	@Override
	public void handle(AbstractSkillEvent event, long currTime)
	{
		if(event.getEventType() != SkillEventType.INIT_CONTROL)
			return;
		
		AbsFightControl control = (AbsFightControl) event.getSource();
		WarControlModel controlModel = control.getModel();
		int addFood = control.getExtraInitPower();
		int initFood = FightConfig.FOOD_INIT + addFood;
		if (controlModel.getInitFood() > 0)
			initFood = controlModel.getInitFood() + addFood;
		else if (controlModel.getInitFood() < 0)
			initFood = 0;

		FightEngine.addFood(control, initFood, FoodAddType.INIT, currTime);
		control.getWar().handler().initFood(control);
	}

}
