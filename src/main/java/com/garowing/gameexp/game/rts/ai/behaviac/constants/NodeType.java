package com.garowing.gameexp.game.rts.ai.behaviac.constants;

/**
 * 节点类型
 * @author seg
 * 2017年1月9日
 */
public interface NodeType
{
	/**
	 *根节点,封装节点 
	 */
	String TREE_ROOT = "Root";
	
	/**
	 * 条件执行
	 */
	String IF_ELSE = "IfElse";
	
	/**
	 * 与
	 */
	String AND = "And";
	
	/**
	 * 条件
	 */
	String CONDITION = "Condition";
	
	/**
	 * 动作
	 */
	String ACTION = "Action";
	
	/**
	 * 或
	 */
	String OR = "Or";
	
	/**
	 * 选择，遇true则返回
	 */
	String SELECTOR = "Selector";
	
	/**
	 * 序列，遇false则返回
	 */
	String SEQUENCE = "Sequence";
	
	/**
	 * 引用行为树
	 */
	String REFERENCED_BEHAVIOR = "ReferencedBehavior";
	
	/**
	 * 并行，全部执行，多种返回策略
	 */
	String PARALLEL = "Parallel";
	
	/**
	 * 赋值
	 */
	String ASSIGNMEN = "Assignment";
	
	/**
	 * 循环直到子节点返回xx
	 */
	String DECORATOR_LOOP_UNTIL = "DecoratorLoopUntil";
	
	/**
	 * 效果器
	 */
	String EFFECTOR = "Effector";
	
}
