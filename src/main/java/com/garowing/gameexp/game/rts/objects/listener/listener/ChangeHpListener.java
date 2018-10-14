package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.ChangeHpWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

public interface ChangeHpListener extends WarListener
{
	public void eventChangeHp (ChangeHpWarEvent event);
}

