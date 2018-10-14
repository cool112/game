package com.garowing.gameexp.game.rts.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 场景对象
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月13日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public abstract class WarSceneObject extends GameObject
{
	
	// 是否已产生
	private boolean birth;
	
	// 当前坐标xy,方向
	private float currentX;
	private float currentY;
	private float currentD;
	
	// 移动相关数据
	private boolean isMove;							// 是否在移动
	private final Lock moveLock;					// 对象移动锁
	private float currentTargetX;					// 最后目标的点x
	private float currentTargetY;					// 最后目标的点y
	private long lastMoveTime;						// 最终移动时间
	private final List<float[]> path;				// 移动路径
	
	private boolean isStop;							// 是否停止
	
	/**
	 * 能量场集合
	 * energyGridMap.gkey = energy
	 */
	private Map<Integer, Integer> energyGridMap = new HashMap<Integer, Integer>();
	
	public WarSceneObject(WarInstance war)
	{
		super(war);
		this.moveLock = new ReentrantLock();
		this.path = new ArrayList<>();
	}
	
	public void lockMove ()
	{
		moveLock.lock();
	}
	
	public void unlockMove ()
	{
		moveLock.unlock();
	}
	
	/**
	 * 获得对象的碰撞半径
	 * @return
	 */
	public abstract float getCollisionRadius ();
	
	/**
	 * 获得对象的移动速度
	 * @return
	 */
	public abstract float getSpeed();
	
	
	/**
	 * 获取模型类型
	 * @return
	 */
	public abstract int getModelType();
	
	/**
	 * 是否可以移动
	 * @return
	 */
	public boolean canMove()
	{
		return true;
	}
	

	public boolean isLive()
	{
		return false;
	}
	
	/******************** get/set方法 ********************/

	public float getX() {
		return currentX;
	}
	
	public void setX(float x) {
		this.currentX = x;
	}

	public float getY() {
		return currentY;
	}

	public void setY(float y) {
		this.currentY = y;
	}

	public float getD() {
		return currentD;
	}

	public void setD(float d) {
		this.currentD = d;
	}

	public float getTargetX() {
		return currentTargetX;
	}

	public void setTargetX(float targetX) {
		this.currentTargetX = targetX;
	}

	public float getTargetY() {
		return currentTargetY;
	}

	public void setTargetY(float targetY) {
		this.currentTargetY = targetY;
	}

	public long getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(long lastMovelTime) {
		this.lastMoveTime = lastMovelTime;
	}

	public List<float[]> getPath() {
		return path;
	}


	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public boolean isBirth() {
		return birth;
	}

	public void setBirth(boolean birth) {
		this.birth = birth;
	}

	public boolean isStop()
	{
		return isStop;
	}

	public void setStop(boolean isStop)
	{
		this.isStop = isStop;
	}
	
	/**
	 * 加入能量点
	 * @param gkey
	 * @param energy
	 */
	public void putEnergyGrid(int gkey, int energy)
	{
		this.energyGridMap.put(gkey, energy);
	}
	
	/**
	 * 清除能量点
	 */
	public void cleanEnergyGrid()
	{
		this.energyGridMap.clear();
	}
	
	public Integer getGridEnergy(int gKey)
	{
		return this.energyGridMap.get(gKey);
	}
}
