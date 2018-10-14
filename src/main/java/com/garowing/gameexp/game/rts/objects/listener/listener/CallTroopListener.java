package com.garowing.gameexp.game.rts.listener.listener;

import com.garowing.gameexp.game.rts.listener.event.CallTroopWarEvent;
import com.garowing.gameexp.game.rts.listener.objects.WarListener;

public interface CallTroopListener extends WarListener
{
	public void eventCallTroop (CallTroopWarEvent event);
}
