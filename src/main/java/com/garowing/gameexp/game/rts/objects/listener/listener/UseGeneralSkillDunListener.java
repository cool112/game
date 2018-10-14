package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.UseGeneralSkillWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 使用将军技能监听器接口
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年1月30日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public interface UseGeneralSkillDunListener extends WarListener
{
	public void eventUseGeneralSkill (UseGeneralSkillWarEvent event);
}
