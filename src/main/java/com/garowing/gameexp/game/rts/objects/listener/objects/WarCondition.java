package com.garowing.gameexp.game.rts.listener.objects;
import com.garowing.gameexp.game.rts.listener.model.WarConditionModel;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 战争条件
 * @author John.zero
 */
public interface WarCondition 
{
	
	public boolean initConditionData (WarConditionModel warConditionModel);
	
	public boolean checkCondition (WarInstance war);
	
}
