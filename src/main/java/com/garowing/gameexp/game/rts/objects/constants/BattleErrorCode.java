package com.garowing.gameexp.game.rts.objects.constants;

/**
 * 战斗错误码类型
 * @author seg
 *
 */
public enum BattleErrorCode
{
	APPLY_FAIL				(-1, "申请失败"),
	CREATE_FAIL 			(0, "创建失败"),
	LINK_FAIL				(1, "LINK失败"),
	NO_LINK_FAIL			(2, "无LINK"),
	APPLY_OUT_TIME			(3, "申请超时"),
	BATTLE_OUT_TIME			(4, "战斗超时"),
	BATTLE_SERVER_CLOSE		(5, "战斗服关闭"),
	BATTLE_CREATE_SUCCESS 	(100, "战斗申请创建成功"),
	;
	
	private int code;
	
	private String info;
	
	BattleErrorCode (int code, String info)
	{
		this.code = code;
		this.info = info;
	}

	public int getCode()
	{
		return code;
	}

	public String gainInfo()
	{
		return info;
	}
	
	public static BattleErrorCode gainErrorCode (int code)
	{
		for(BattleErrorCode errorCode : BattleErrorCode.values())
		{
			if(errorCode.getCode() == code)
				return errorCode;
		}
		
		return null;
	}
}
