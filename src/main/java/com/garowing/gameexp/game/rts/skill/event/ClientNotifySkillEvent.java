package com.garowing.gameexp.game.rts.skill.event;

import java.util.List;

import com.yeto.war.fightcore.GameObject;
import com.garowing.gameexp.game.rts.skill.constants.SkillEventType;
import com.garowing.gameexp.game.rts.skill.entity.SkillParams;
import com.garowing.gameexp.game.rts.objects.WarInstance;

/**
 * 客户端通知事件
 * @author seg
 *
 */
public class ClientNotifySkillEvent extends AbstractSkillEvent
{
	/**
	 * 通知类型
	 */
	private int noticeType;
	
	private SkillParams params;
	
	public ClientNotifySkillEvent(WarInstance war, int noticeType, SkillParams params)
	{
		super(war);
		this.noticeType = noticeType;
		this.params = params;
	}

	@Override
	public SkillEventType getEventType()
	{
		return SkillEventType.CLIENT_NOTIFY;
	}

	@Override
	public GameObject getSource()
	{
		return getWar().getObject(params.getCasterId());
	}

	@Override
	public GameObject getTarget()
	{
		List<Integer> targets = params.getTargetIds();
		if(targets == null || targets.isEmpty())
			return null;
		
		return getWar().getObject(params.getTargetIds().get(0));
	}

	@Override
	public String getName()
	{
		return "client notify event type[" + noticeType + "] params[" + params + "]";
	}

	public int getNoticeType()
	{
		return noticeType;
	}

	public void setNoticeType(int noticeType)
	{
		this.noticeType = noticeType;
	}

	public SkillParams getParams()
	{
		return params;
	}

	public void setParams(SkillParams params)
	{
		this.params = params;
	}

}
