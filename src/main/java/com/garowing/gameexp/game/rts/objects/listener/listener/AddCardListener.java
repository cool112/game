package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.AddCardEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

/**
 * 增加手牌监听
 */
public interface AddCardListener extends WarListener
{
	public void eventAddCard(AddCardEvent event);
}
