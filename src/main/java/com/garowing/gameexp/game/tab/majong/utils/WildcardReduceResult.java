/**
 * 
 */
package com.garowing.gameexp.game.tab.majong.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjs
 *
 */
public class WildcardReduceResult implements Comparable<WildcardReduceResult> {
	/** 需要通配牌数 */
	private int needWildcard;
	/** 可扣的牌 */
	private List<Integer> cards = new ArrayList<Integer>();
	/** 牌值和 */
	private int sum;
	
	/**
	 * @param needWildcard
	 * @param sum
	 */
	public WildcardReduceResult(int needWildcard, int sum) {
		super();
		this.needWildcard = needWildcard;
		this.sum = sum;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(WildcardReduceResult o) {
		if(o == null)
			return 1;
		if(this.needWildcard > o.needWildcard)
			return 1;
		if(this.needWildcard < o.needWildcard)
			return -1;
		if(this.sum > o.sum)
			return 1;
		if(this.sum < o.sum)
			return -1;
		return 0;
	}
	
	public void addCard(int cardVal){
		this.cards.add(cardVal);
	}

	public int getNeedWildcard() {
		return needWildcard;
	}

	public void setNeedWildcard(int needWildcard) {
		this.needWildcard = needWildcard;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public List<Integer> getCards() {
		return cards;
	}

}
