package com.garowing.gameexp.game.rts.ai.behaviac.constants;

/**
 * 属性dom关键字，一个property只有一个关键字
 * @author seg
 * 2017年1月9日
 */
public enum PropertyModelKey
{
	/**
	 * 运算符
	 */
	OPERATOR("Operator", false),
	
	/**
	 * 运算左值
	 */
	OPL("Opl", true),
	
	/**
	 * 运算右值
	 */
	OPR("Opr", true),
	
	/**
	 * 运算右值2
	 */
	OPR2("Opr2", true),
	
	/**
	 * 方法
	 */
	METHOD("Method", true),
	
	/**
	 * 返回类型
	 */
	RESULT_OPTION("ResultOption", true),
	
	/**
	 * 引用的文件（行为树）
	 */
	REFERENCE_FILENAME("ReferenceFilename", false),
	
	/**
	 * 引用的行为树
	 */
	REFERENCE_BEHAVIOR("ReferenceBehavior", true),
	
	/**
	 * 子节点结束策略
	 */
	CHILD_FINISH_POLICY("ChildFinishPolicy", true),
	
	/**
	 * 退出策略
	 */
	EXIT_POLICY("ExitPolicy", true),
	
	/**
	 * 失败策略
	 */
	FAILURE_POLICY("FailurePolicy", true),
	
	/**
	 * 成功策略
	 */
	SUCCESS_POLICY("SuccessPolicy", true),
	
	/**
	 *计数
	 */
	COUNT("Count", true),
	
	/**
	 * 子节点结束时
	 */
	DECORATE_WHEN_CHILD_ENDS("DecorateWhenChildEnds", true),
	
	/**
	 * 直到子节点返回某个值
	 */
	UNTIL("Until", true),
	
	/**
	 * 二元运算符，出现多层逻辑运算时
	 */
	BINARY_OPERATOR("BinaryOperator", false),
	
	/**
	 * 阶段
	 */
	PHASE("Phase", false),
	
	/**
	 * 结果函数
	 */
	RESULT_FUNCTOR("ResultFunctor", true);
	
	/**
	 * 属性名
	 */
	private String name;
	
	/**
	 * 是否需要转化
	 */
	private boolean needTransition;
	
	PropertyModelKey(String name, boolean needTransition)
	{
		this.name = name;
		this.needTransition = needTransition;
	}
	
	/**
	 * 根据属性名获取枚举
	 * @param name
	 * @return
	 */
	public static PropertyModelKey getKey(String name)
	{
		if(name == null)
			return null;
		
		for(PropertyModelKey key : values())
		{
			if(name.equals(key.name))
				return key;
		}
		
		return null;
	}

	public String getName()
	{
		return name;
	}

	public boolean isNeedTransition()
	{
		return needTransition;
	}
	
	
}
