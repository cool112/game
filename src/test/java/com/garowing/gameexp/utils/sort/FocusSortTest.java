package com.garowing.gameexp.utils.sort;

import com.garowing.gameexp.utils.sort.generator.RandomIntGenerator;

import junit.framework.TestCase;

public class FocusSortTest extends TestCase {
	public void testFocusSort() {
		int MAX_LENGTH = 100000000;
		int RANDOM_CEIL = Integer.MAX_VALUE;
		int[] array = new int[MAX_LENGTH];
		boolean flag = RandomIntGenerator.initRandomArray(array, MAX_LENGTH, RANDOM_CEIL);
		if(!flag)
			return;
		
//		System.out.println(Arrays.toString(array));
//		FastSort.sort(array, 0, array.length - 1);
//		DeapSort.sort(array, 0, MAX_LENGTH, new Comparator<Integer>() {
//			
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				return o1 - o2;
//			}
//		});
		System.err.println("start:"+System.currentTimeMillis());
		FastSort.focusSort(array, 0, MAX_LENGTH - 1, 10000000);
		System.err.println("end:"+System.currentTimeMillis());
//		System.out.println(Arrays.toString(array));
	}
}
