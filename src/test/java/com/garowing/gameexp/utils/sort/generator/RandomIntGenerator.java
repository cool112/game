package com.garowing.gameexp.utils.sort.generator;

import java.util.Random;

/**
 * 随机整数生成器
 * @author Administrator
 *
 */
public class RandomIntGenerator {
	/**
	 * 实例
	 */
	private static final Random R = new Random();
	
	/**
	 * 初始化数组
	 * @param array 原始数组
	 * @param n 随机数长度
	 * @param ceil 
	 * @return
	 */
	public static boolean initRandomArray(int[] array, int n, int ceil)
	{
		if(array == null || array.length < n)
			return false;
		
		for(int i = 0; i < n ; i++)
			array[i] = R.nextInt(ceil);
		
		return true;
	}
}
