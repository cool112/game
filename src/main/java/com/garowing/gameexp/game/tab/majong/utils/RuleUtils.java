/**
 * 
 */
package com.garowing.gameexp.game.tab.majong.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

//import com.jm.logic.game.base.OperCardsType;
//import com.jm.logic.game.base.OperInfo.OperType;
 
/**
 * 规则工具类
 * @author gjs
 *
 */
public class RuleUtils {
	
	/**
	 * 通配牌标记
	 */
	private static final int WILDCARD = -1;
	
	/**
	 * 牌数计数
	 * @param cards
	 * @return map.cardVal = total
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Integer> countCards(int[] cards){
		if(cards == null || cards.length == 0)
			return Collections.EMPTY_MAP;
		
		Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
		for (int i = 0; i < cards.length; i++) {
			int j = cards[i];
			Integer count = counter.get(j);
			if(count == null)
				counter.put(j, 1);
			else
				counter.put(j, ++count);
		}
		return counter;
	}
	
	/**
	 * 牌数计数,有序树
	 * @param cards
	 * @return map.cardVal = total
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Integer> countCardsByOrder(int[] cards){
		if(cards == null || cards.length == 0)
			return Collections.EMPTY_MAP;
		
		Map<Integer, Integer> counter = new TreeMap<Integer, Integer>();
		for (int i = 0; i < cards.length; i++) {
			int j = cards[i];
			Integer count = counter.get(j);
			if(count == null)
				counter.put(j, 1);
			else
				counter.put(j, ++count);
		}
		return counter;
	}
	
	/**
	 * 牌型(对2\大对3\杠4)计数
	 * @param cards
	 * @return map.cardRepeatNum = total
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Integer> countCardRepeat(int[] cards){
		Map<Integer, Integer> cardCounter = countCards(cards);
		if(cardCounter == null || cardCounter.isEmpty())
			return Collections.EMPTY_MAP;
		
		Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
		for (Integer i : cardCounter.values()) {
			Integer count = counter.get(i);
			if(count == null)
				counter.put(i, 1);
			else
				counter.put(i, ++count);
		}
		return counter;
	}
	
	/**
	 * 是否杠操作
	 * @param oper
	 * @return
	 */
//	public static boolean isGangOper(OperCardsType oper){
//		int code = oper.getOperType();
//		return isGangOper(code);
//	}
	
	/**
	 * 是否杠操作
	 * @param code
	 * @return
	 */
//	public static boolean isGangOper(int code){
//		return code == OperType.GANG.getCode() || code == OperType.GANG_IN.getCode() 
//				|| code == OperType.GANG_ALL_IN.getCode();
//	}
	
	/**
	 * 检查听牌,需要包含定掌或可能为将的牌
	 * @param cards13
	 * @param ALL_CARDS
	 * @param fixedJiang 固定将牌 定掌机制
	 * @param specialHuRule 特殊胡牌规则,优先计算
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> checkListenCards(int[] cards13, final int[] ALL_CARDS, int fixedJiang, SpecialHuRule specialHuRule){
		if(cards13 == null || cards13.length % 3 != 1)
			return Collections.EMPTY_LIST;
		
		if(ALL_CARDS == null)
			return Collections.EMPTY_LIST;
		
		Map<Integer, Integer> counter = countCards(cards13);
		int[] temp = Arrays.copyOf(cards13, cards13.length + 1);
		ArrayList<Integer> listenCards = new ArrayList<Integer>();
		for(int i = 0 ; i < ALL_CARDS.length ; i++){
			int testCard = ALL_CARDS[i];
			Integer existsCount = counter.get(testCard);
			if(existsCount != null && existsCount >= 4)
				continue;
			
			temp[cards13.length] = testCard;
			if((specialHuRule != null && specialHuRule.checkHu(temp)) 
					|| checkHu(temp, fixedJiang))
				listenCards.add(testCard);
				
		}
		return listenCards;
	
	}
	
	/**
	 * 检查正在听的牌,需要包含定掌或可能为将的牌
	 * @param cards13
	 * @param ALL_CARDS
	 * @param fixedJiang 固定将牌 定掌机制
	 * @return
	 */
	public static List<Integer> checkListenCards(int[] cards13, final int[] ALL_CARDS, int fixedJiang){
		return checkListenCards(cards13, ALL_CARDS, fixedJiang, null);
	}
	
	/**
	 * 检查标准胡牌
	 * @param cards
	 * @param fixedJiang
	 * @return
	 */
	public static boolean checkHu(int[] cards, int fixedJiang){
		Map<Integer, Integer> counter = countCardsByOrder(cards);
		if(fixedJiang > 0){
			Integer dingCount = counter.get(fixedJiang);
			if(dingCount == null || dingCount < 2)
				return false;
			
			counter.put(fixedJiang, dingCount - 2);
			return checkHuNoJiang(counter);
		}
		else {
			for(Entry<Integer, Integer> entry : counter.entrySet()){
				if(entry.getValue() < 2)
					continue;
				
				Map<Integer, Integer> copyCounter = new TreeMap<Integer, Integer>(counter);
				copyCounter.put(entry.getKey(), entry.getValue() - 2);
				if(checkHuNoJiang(copyCounter))
					return true;
			}
		}
			
		return false;
	}
	
	/**
	 * 检查无将的牌是否能胡
	 * @param counter
	 * @return
	 */
	private static boolean checkHuNoJiang(Map<Integer, Integer> counter){
		int remain = 0;
		for(Integer count : counter.values()){
			remain += count;
		}
		for(Entry<Integer, Integer> entry : counter.entrySet()){
			if(remain == 0)
				return true;
			
			Integer cardVal = entry.getKey();
			if(cardVal.equals(WILDCARD))
				continue;
			
			Integer count = entry.getValue();
			if(count <= 0)
				continue;
			
			while(entry.getValue() > 0){
				int result = 0;
				if(counter.containsKey(WILDCARD))
					result = check3NWithWildcard(counter, cardVal);
				else
					result = check3N(counter, cardVal);
				if(result == 0)//这里做万能牌消耗检查,前提是先把万能牌扣除,把已能组队的牌先组, -1当万能牌
					return false;
				
				remain -= 3;
			}
				
		}
		if(remain == 0)
			return true;
		
		return false;
	}
	
	

	/**
	 * 有通配情况检查3n组合
	 * @param counter
	 * @param cardVal
	 * @return
	 */
	private static int check3NWithWildcard(Map<Integer, Integer> counter,
			int cardVal) {
		if(counter == null)
			return 0;
		Integer count = counter.get(cardVal);
		if(count == null || count <= 0)
			return 0;
		if(reduceShunWithWildcard(counter, cardVal)){
			counter.put(cardVal, --count);
			return 1;
		}
		if(count >= 3){//顺都够不成,可能需要检查两张凑刻的情况
			counter.put(cardVal, count - 3);
			return 2;
		}
		Integer wildcardCount = counter.get(WILDCARD);
		if(count == 2 && wildcardCount > 0){
			counter.put(count, 0);
			counter.put(WILDCARD, --wildcardCount);
			return 2;
		}
			
		return 0;
	}

	/**
	 * 检查目标是否是顺或刻的成员
	 * @param counter 已排除将牌
	 * @param cardVal 待测试的牌
	 * @return 0-非顺非刻 1-顺 2-刻
	 */
	public static int check3N(Map<Integer, Integer> counter, int cardVal){
		if(counter == null)
			return 0;
		Integer count = counter.get(cardVal);
		if(count == null || count <= 0)
			return 0;
		if(reduceShun(counter, cardVal)){
			counter.put(cardVal, --count);
			return 1;
		}
		if(count >= 3){
			counter.put(cardVal, count - 3);
			return 2;
		}
		//如果有通配牌,可以进行尝试,从少到多提供通配牌
			
		return 0;
	}

	/**
	 * 减少一个顺子,默认目标卡数量已检查,按从小到大优先组合
	 * @param counter
	 * @param cardVal
	 */
	private static boolean reduceShun(Map<Integer, Integer> counter, int cardVal){
		switch(cardVal % 10){
		case 2:
			if(checkAndReduce(counter, cardVal - 1, cardVal + 1))
				return true;
			//直达1
		case 1:
			if(checkAndReduce(counter, cardVal + 1, cardVal + 2))
				return true;
			break;
		case 8:
			if(checkAndReduce(counter, cardVal - 1, cardVal - 2))
				return true;
			if(checkAndReduce(counter, cardVal - 1, cardVal + 1))
				return true;
			break;
		case 9:
			if(checkAndReduce(counter, cardVal - 1, cardVal - 2))
				return true;
			break;
		default:
			if(checkAndReduce(counter, cardVal - 1, cardVal - 2))
				return true;
			if(checkAndReduce(counter, cardVal - 1, cardVal + 1))
				return true;
			if(checkAndReduce(counter, cardVal + 1, cardVal + 2))
				return true;
		}
		
		return false;
	}
	
	/**
	 * 减少一个顺子,默认目标卡数量已检查,按从小到大优先组合,可以使用通配牌
	 * @param counter
	 * @param cardVal
	 * @param widecardLimit
	 */
	private static boolean reduceShunWithWildcard(Map<Integer, Integer> counter, int cardVal){
		WildcardReduceResult tmp = null;
		TreeSet<WildcardReduceResult> reset = new TreeSet<WildcardReduceResult>();
		switch(cardVal % 10){
		case 2:
			tmp = checkAndReduceWithWildcard(counter, cardVal - 1, cardVal + 1);
			if(checkWildcardResult(tmp, reset))
				return true;
			//直达1
		case 1:
			tmp = checkAndReduceWithWildcard(counter, cardVal + 1, cardVal + 2);
			if(checkWildcardResult(tmp, reset))
				return true;
			break;
		case 8:
			tmp = checkAndReduceWithWildcard(counter, cardVal - 1, cardVal - 2);
			if(checkWildcardResult(tmp, reset))
				return true;
			tmp = checkAndReduceWithWildcard(counter, cardVal - 1, cardVal + 1);
			if(checkWildcardResult(tmp, reset))
				return true;
			break;
		case 9:
			tmp = checkAndReduceWithWildcard(counter, cardVal - 1, cardVal - 2);
			if(checkWildcardResult(tmp, reset))
				return true;
			break;
		default:
			tmp = checkAndReduceWithWildcard(counter, cardVal - 1, cardVal - 2);
			if(checkWildcardResult(tmp, reset))
				return true;
			tmp = checkAndReduceWithWildcard(counter, cardVal - 1, cardVal + 1);
			if(checkWildcardResult(tmp, reset))
				return true;
			tmp = checkAndReduceWithWildcard(counter, cardVal + 1, cardVal + 2);
			if(checkWildcardResult(tmp, reset))
				return true;
		}
		if(reset.isEmpty())
			return false;
		
		tmp = reset.iterator().next();
		Integer wildcardCount = counter.get(WILDCARD);
		if(wildcardCount < tmp.getNeedWildcard())
			return false;
		
		counter.put(WILDCARD, wildcardCount - tmp.getNeedWildcard());
		for(Integer needCard : tmp.getCards()){
			Integer count = counter.get(needCard);
			counter.put(needCard, --count);
		}
		return true;
	}

	/**
	 * 检查扣牌结果
	 * @param result
	 * @param reset
	 * @return
	 */
	private static boolean checkWildcardResult(WildcardReduceResult result, Set<WildcardReduceResult> reset){
		if(result != null){
			if(result.getNeedWildcard() == 0)
				return true;
			else
				reset.add(result);
		}
		return false;
	}
	/**
	 * 检查并减少牌数
	 * @param counter
	 * @param more1Count
	 * @param more2Count
	 * @return
	 */
	private static boolean checkAndReduce(Map<Integer, Integer> counter,
			Integer... cardArr) {
		if(cardArr == null)
			return false;
		
		for(Integer cardVal : cardArr){
			Integer count = counter.get(cardVal);
			if(count == null || count <= 0)
				return false;
		}
		
		for(Integer cardVal : cardArr){
			Integer count = counter.get(cardVal);
			counter.put(cardVal, --count);
		}
		return true;
	}
	
	/**
	 * 检查并减少牌数
	 * @param counter
	 * @param more1Count
	 * @param more2Count
	 * @return
	 */
	private static WildcardReduceResult checkAndReduceWithWildcard(Map<Integer, Integer> counter,
			Integer... cardArr) {
		if(cardArr == null)
			return null;
		
		int needWildcard = 0;
		int sum = 0;
		WildcardReduceResult result = new WildcardReduceResult(Integer.MAX_VALUE, 0);
		for(Integer cardVal : cardArr){
			Integer count = counter.get(cardVal);
			if(count == null || count <= 0)
				++needWildcard;
			else
				result.addCard(cardVal);
			sum += cardVal;
		}
		result.setNeedWildcard(needWildcard);
		result.setSum(sum);
		if(needWildcard == 0){
			for(Integer cardVal : cardArr){
				Integer count = counter.get(cardVal);
				counter.put(cardVal, --count);
			}
		}
		return result;
	}
	
	/**
	 * 听牌检查,考虑通配牌
	 * @param hands
	 * @param ALL_CARDS
	 * @param fixedJiang
	 * @param specialHuRule
	 * @param wildcard
	 * @return 通配牌情况返回值可能有重复值
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> checkListenCards(int[] hands, final int[] ALL_CARDS, 
			int fixedJiang, SpecialHuRule specialHuRule, int wildcard){
		if(hands == null || hands.length % 3 != 1)
			return Collections.EMPTY_LIST;
		if(ALL_CARDS == null)
			return Collections.EMPTY_LIST;
		Map<Integer, Integer> counter = countCards(hands, wildcard);
		ArrayList<Integer> listenCards = new ArrayList<Integer>();
		if(counter.get(WILDCARD) == null){
			int[] temp = Arrays.copyOf(hands, hands.length + 1);
			for(int i = 0 ; i < ALL_CARDS.length ; i++){
				int testCard = ALL_CARDS[i];
				if(testCard == wildcard)
					continue;
				Integer existsCount = counter.get(testCard);
				if(existsCount != null && existsCount >= 4)
					continue;
				
				temp[hands.length] = testCard;
				if((specialHuRule != null && specialHuRule.checkHu(temp)) 
						|| checkHu(temp, fixedJiang))
					listenCards.add(testCard);
			}
		}
		else if(counter.get(WILDCARD) == 1){
			int[] temp = Arrays.copyOf(hands, hands.length + 1);
			checkListenCardsWithWildcard(hands.length, ALL_CARDS, fixedJiang,
					specialHuRule, wildcard, counter, listenCards, temp);
		}
		else{
			int[] temp = new int[hands.length + 1];
			for(Integer key : counter.keySet()){
				if(!key.equals(WILDCARD)){
					int index = 0;
					for(Entry<Integer, Integer> entry : counter.entrySet()){
						int cardVal = entry.getKey();
						int count = entry.getValue();
						if(cardVal == WILDCARD){
							--count;
							cardVal = wildcard;
						}
						for (int i = 0; i < count; ++i) {
							temp[index] = cardVal;
							++index;
						}
					}
					temp[hands.length - 1] = key;
				}
				else{//不做改变
					temp = Arrays.copyOf(hands, hands.length + 1);
				}
					
				checkListenCardsWithWildcard(hands.length, ALL_CARDS, fixedJiang,
						specialHuRule, wildcard, counter, listenCards, temp);
			}
		
		}
		
		return listenCards;
	}

	/**
	 * 处理通配牌的听牌检查
	 * @param hands
	 * @param ALL_CARDS
	 * @param fixedJiang
	 * @param specialHuRule
	 * @param wildcard
	 * @param counter
	 * @param listenCards
	 * @param temp
	 */
	private static void checkListenCardsWithWildcard(int handsLen,
			final int[] ALL_CARDS, int fixedJiang, SpecialHuRule specialHuRule,
			int wildcard, Map<Integer, Integer> counter,
			ArrayList<Integer> listenCards, int[] temp) {
		for(int i = 0 ; i < ALL_CARDS.length ; i++){
			int testCard = ALL_CARDS[i];
			if(testCard == wildcard)
				continue;
			Integer existsCount = counter.get(testCard);
			if(existsCount != null && existsCount >= 4)
				continue;
			
			temp[handsLen] = testCard;
			if ((specialHuRule != null && specialHuRule.checkHu(temp))
					|| checkHuWithWildcard(temp, fixedJiang, wildcard))
				listenCards.add(testCard);
			
		}
	}

	/**
	 * 卡牌计数,带通配牌
	 * @param hands
	 * @param wildcard
	 * @return
	 */
	private static Map<Integer, Integer> countCards(int[] hands,
			int wildcard) {
		Map<Integer, Integer> counter = countCards(hands);
		if(wildcard > 0){
			Integer wildcardCount = counter.get(wildcard);
			if(wildcardCount != null){
				counter.remove(wildcard);
				counter.put(WILDCARD, wildcardCount);
			}
		}
		return counter;
	}
	
	/**
	 * 卡牌计数,带通配牌,排序
	 * @param hands
	 * @param wildcard
	 * @return
	 */
	private static Map<Integer, Integer> countCardsByOrder(int[] hands,
			int wildcard) {
		Map<Integer, Integer> counter = countCardsByOrder(hands);
		if(wildcard > 0){
			Integer wildcardCount = counter.get(wildcard);
			if(wildcardCount != null){
				counter.remove(wildcard);
				counter.put(WILDCARD, wildcardCount);
			}
		}
		return counter;
	}
	/**
	 * 检查通配牌胡牌
	 * @param cards
	 * @param fixedJiang
	 * @param wildcard 
	 * @return
	 */
	private static boolean checkHuWithWildcard(int[] cards, int fixedJiang, int wildcard) {
		Map<Integer, Integer> counter = countCardsByOrder(cards, wildcard);
		if(fixedJiang > 0){
			Integer dingCount = counter.get(fixedJiang);
			Integer wildcardCount = counter.get(WILDCARD);
			if(dingCount == null)
				dingCount = 0;
			int minDingCount = Math.min(2, dingCount);
			int needWildcard = 2 - dingCount;
			if(wildcardCount < needWildcard)
				return false;
			
			counter.put(WILDCARD, wildcardCount - needWildcard);
			counter.put(fixedJiang, dingCount - minDingCount);
			return checkHuNoJiang(counter);
		}
		else {
			for(Entry<Integer, Integer> entry : counter.entrySet()){
				if(entry.getValue() < 2)
					continue;
				
				Map<Integer, Integer> copyCounter = new TreeMap<Integer, Integer>(counter);
				copyCounter.put(entry.getKey(), entry.getValue() - 2);
				if(checkHuNoJiang(copyCounter))
					return true;
			}
		}
			
		return false;
	}
	
}
