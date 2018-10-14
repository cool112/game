package com.garowing.gameexp.utils.sort;

import java.util.Comparator;

/**
 * 堆排序
 * @author seg
 *
 */
public class DeapSort {

	/**
	 * 堆排序，自定义比较器
	 * @param array
	 * @param start inclusive
	 * @param end exclusive
	 * @param comparator
	 * @return
	 */
	public static int[] sort(int[] array, int start, int end, Comparator<Integer> comparator)
	{
		if(array == null || array.length < end || start > end)
			return array;
		
		int[] newArray = new int[end - start + 1];
		System.arraycopy(array, start, newArray, 1, end - start);
		newArray = buildNewDump(newArray, comparator);
		StringBuilder sb = new StringBuilder(newArray[1] + "");
		sb.append(",");
		for(int i = newArray.length - 1; i > 1 ; i--)
		{
			int temp = newArray[i];
			newArray[i] = newArray[1];
			newArray[1] = temp;
			
			adjustNode(newArray, 1, i - 1, comparator);
			sb.append(newArray[1]).append(",");
		}
		System.out.println(sb.toString());
		return newArray;
	}
	
	/**
	 * 建立新的堆结构
	 * @param array
	 * @param comparator
	 * @return
	 */
	private static int[] buildNewDump(int[] array, Comparator<Integer> comparator)
	{
		for(int i = (array.length - 1) / 2; i > 0; i--)
			adjustNode(array, i, array.length - 1, comparator);
		
		return array;
	}
	
	/**
	 * 调整节点
	 * @param array
	 * @param start
	 * @param end
	 */
	private static void adjustNode(int[] array, int start, int end, Comparator<Integer> comparator)
	{
		int val = array[start];
		for(int i = start * 2; i <= end; i *= 2)
		{
			if(i < end && comparator.compare(array[i], array[i + 1]) < 0)
				i += 1;
			if(comparator.compare(val, array[i]) >= 0)
				break;
			
			array[start] = array[i];
			start = i;
		}
		array[start] = val;
	}

	/**
	 * 搜寻焦点目标
	 * @param array
	 * @param start
	 * @param focus 
	 * @param end 
	 */
	public static void findFocus(int[] array, int start, int end, int focus) {
		if(array == null || array.length < end || start > end)
			return;
		
		int[] newArray = new int[end - start + 1];
		System.arraycopy(array, start, newArray, 1, end - start);
		Comparator<Integer> comparator;
		if(focus - start > end - focus)
			comparator = new Comparator<Integer>() {
				
				public int compare(Integer o1, Integer o2) {
					return o1 - o2;
				}
			};
		else
			comparator = new Comparator<Integer>() {
				
				public int compare(Integer o1, Integer o2) {
					return o2 - o1;
				}
			};
		newArray = buildNewDump(newArray, comparator);
		int distance = Math.min(focus - start, end - focus);
		for(int i = newArray.length - 1; i > newArray.length - distance - 1 ; i--)
		{
			int temp = newArray[i];
			newArray[i] = newArray[1];
			newArray[1] = temp;
			
			adjustNode(newArray, 1, i - 1, comparator);
		}
		System.out.println(newArray[1]);
		return;
		
	}
}
