/**
 * 
 */
package com.jm.logic.util;

/**
 * 特殊胡牌规则
 * @author gjs
 *
 */
public interface SpecialHuRule {
	/**
	 * 检查胡牌
	 * @param cards
	 * @return
	 */
	public boolean checkHu(int[] cards);
}
