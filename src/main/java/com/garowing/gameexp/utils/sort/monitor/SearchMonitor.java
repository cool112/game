package com.garowing.gameexp.utils.sort.monitor;

import java.util.ArrayList;
import java.util.List;

import com.garowing.gameexp.utils.sort.DeapSort;

/**
 * 搜索监控
 * 
 * @author Administrator
 * 
 */
public class SearchMonitor {
	/**
	 * 分割点集合
	 */
	private static List<Integer> pivotList = new ArrayList<Integer>();

	/**
	 * 新增分割点
	 * 
	 * @param pivot
	 * @param focus
	 */
	public static void addPivot(int pivot) {
		if (pivotList.isEmpty()) {
			pivotList.add(pivot);
			return;
		}

		int low = 0;
		int high = pivotList.size();
		int mid = high / 2;
		if (pivotList.get(mid) > pivot) {
			while (mid - 1 >= 0 && pivotList.get(mid) > pivot)
				mid--;
		} else {
			while (mid + 1 < high && pivotList.get(mid) < pivot)
				mid++;
		}
		pivotList.add(mid, pivot);

	}

	public static void captureArray(int[] array, int start, int end, int focus) {
		System.out.println("start:" + start + ", end:" + end);
		DeapSort.findFocus(array, start, end, focus);
	}
}
