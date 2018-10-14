package com.garowing.gameexp.utils.sort;

import com.garowing.gameexp.utils.sort.monitor.SearchMonitor;

/**
 * 快速排序
 * @author seg
 *
 */
public class FastSort {
	
	/**
	 * 排序
	 * @param array
	 * @return
	 */
	public static int[] sort(int[] array, int low, int high)
	{
		if(array == null || array.length < 2)
			return array;
		
		if(low < high)
		{
			int pivotIndex = partition(array, low, high);
			sort(array, low, pivotIndex -1);
			sort(array, pivotIndex + 1, high);
		}
		return array;
	}
	/**
	 * 焦点排序
	 * @param array
	 * @param low
	 * @param high
	 * @param focus
	 * @return
	 */
	public static int[] focusSort(int[] array, int low, int high, int focus)
	{
		if(array == null || array.length < 2)
			return array;
		if(low < high)
		{
			if(focus < low || high < focus)
				return array;
			
			if(high - low < array.length / 5)
			{
				SearchMonitor.captureArray(array, low, high, focus);
				return array;
			}
				
			int pivotIndex = partition(array, low, high);
			SearchMonitor.addPivot(pivotIndex);
			if(pivotIndex == focus)
				System.out.println("focus:" + array[pivotIndex]);
			focusSort(array, low, pivotIndex -1, focus);
			focusSort(array, pivotIndex + 1, high, focus);
		}
		return array;
	}
	
	/**
	 * 区域快速排序
	 * @param array
	 * @param low
	 * @param high
	 * @return
	 */
	private static int partition(int[] array, int low, int high)
	{
		int pivot = array[low];
		while(low < high)
		{
			while(low < high && array[high] >= pivot) 
				high--;
			array[low] = array[high];
			while(low < high && array[low] <= pivot)
				low++;
			array[high] = array[low];
		}
		array[low] = pivot;
		return low;
	}

}
