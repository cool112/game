package com.garowing.gameexp.game.rts.skill.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yeto.war.fightcore.GameObject;
import com.yeto.war.fightcore.GameObjectType;
import com.yeto.war.fightcore.attr.entity.AttrEntity;
import com.garowing.gameexp.game.rts.skill.Projectile;
import com.garowing.gameexp.game.rts.skill.constants.TriggerEventType;
import com.garowing.gameexp.game.rts.skill.filter.effect.EffectFilter;
import com.garowing.gameexp.game.rts.skill.filter.skill.SkillFilter;
import com.garowing.gameexp.game.rts.objects.WarInstance;
import com.garowing.gameexp.game.rts.objects.WarObjectInstance;
import com.yeto.war.module.troop.Troop;

/**
 * 游戏对象附加效果集合
 * @author seg
 *
 */
public class AttachEffectsList extends GameObject
{
	/**
	 * 临时效果集合
	 */
	private List<Integer> tempEffects = new ArrayList<Integer>();;
	
	/**
	 * 触发器集合
	 */
	private Map<TriggerEventType, Set<Integer>> triggersMap = new HashMap<TriggerEventType, Set<Integer>>();
	
	/**
	 * 效果附加属性集合，用于某些动态变更属性的效果
	 * effectAttrMap.effectId = AttrEntity
	 */
	private Map<Integer, AttrEntity> effectAttrMap = new HashMap<Integer, AttrEntity>();
	
	/**
	 * 效果过滤器集合
	 */
	private Map<Integer, EffectFilter> effectFilterMap = new HashMap<Integer, EffectFilter>();
	
	/**
	 * 效果护盾集合
	 * effectShieldMap.effectId = shieldValue
	 */
	private Map<Integer, Integer> effectShieldMap = new HashMap<Integer, Integer>();
	
	/**
	 * 召唤物集合
	 * children.effectModelId = troopIds
	 */
	private Map<Integer, Set<Integer>> children = new HashMap<Integer, Set<Integer>>();
	
	/**
	 * 技能过滤器集合
	 */
	private Map<Integer, SkillFilter> skillFilterMap = new HashMap<Integer, SkillFilter>();
	
	public AttachEffectsList(WarInstance war)
	{
		super(war);
	}
	
	/**
	 * 移除效果附加属性
	 * @param effectId
	 */
	public void removeEffectAttr(int effectId)
	{
		this.effectAttrMap.remove(effectId);
	}
	
	/**
	 * 设置效果附加属性
	 * @param effectId
	 * @param attrEntity
	 */
	public void setEffectAttr(int effectId, AttrEntity attrEntity)
	{
		this.effectAttrMap.put(effectId, attrEntity);
	}
	
	/**
	 * 获取效果附加属性
	 * @param effectId
	 * @return
	 */
	public AttrEntity getEffectAttr(int effectId)
	{
		return this.effectAttrMap.get(effectId);
	}
	
	/**
	 * 增加触发效果
	 * @param type
	 * @param trigger
	 */
	public void addTriggerEffect(TriggerEventType type, EffectEntity trigger)
	{
		Set<Integer> effectSet = this.triggersMap.get(type);
		if(effectSet == null)
		{
			effectSet = new HashSet<Integer>();
			this.triggersMap.put(type, effectSet);
		}
		
		effectSet.add(trigger.getObjectId());
	}
	
	/**
	 * 移除触发效果
	 * @param type
	 * @param effectId
	 */
	public void removeTriggerEffect(TriggerEventType type, int effectId)
	{
//		System.err.println("remove id" + effectId);
		Set<Integer> effectSet = this.triggersMap.get(type);
		if(effectSet == null)
			return;
		
		effectSet.remove(effectId);
	}

	/**
	 * 移除临时效果
	 * @param id
	 */
	public void removeTempEffect(int id)
	{
		this.tempEffects.remove(new Integer(id));
	}

	/**
	 * 增加临时效果
	 * @param id
	 */
	public void addTempEffect(int id)
	{
		this.tempEffects.add(id);
	}

	/**
	 * 获取临时效果
	 * @param parentId
	 * @param prototypeId
	 * @return
	 */
	public EffectEntity getTempEffect(int parentId, int prototypeId)
	{
		WarInstance war = getWar();
		Iterator<Integer> iterator = this.tempEffects.iterator();
		EffectEntity effectEntiry = null;
		while(iterator.hasNext())
		{
			Integer effectId = iterator.next();
			GameObject effect = war.getObject(effectId);
			if(effect == null || effect.getObjectType() != GameObjectType.SKILL_EFFECT)
			{
				iterator.remove();
				continue;
			}
			
			if(((EffectEntity)effect).getPrototypeId() != prototypeId)
				continue;
			
			if(((EffectEntity)effect).getParentId() != parentId)
				continue;
			
			effectEntiry = (EffectEntity) effect;
			break;
		}
		
		return effectEntiry;
	}
	
	/**
	 * 获取触发器
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EffectEntity> getTriggerEffect(TriggerEventType type)
	{
		Set<Integer> effectIds = this.triggersMap.get(type);
		if(effectIds == null)
			return Collections.EMPTY_LIST;
		
		WarInstance war = getWar();
		List<EffectEntity> effects = new ArrayList<EffectEntity>();
		for(Integer effectId : effectIds)
		{
			GameObject obj = war.getObject(effectId);
			if(obj == null || obj.getObjectType() != GameObjectType.SKILL_EFFECT)
				continue;
			
			effects.add((EffectEntity) obj);
		}
		
		return effects;
	}

	@Override
	public String getName()
	{
		return "attach effect list";
	}

	@Override
	public int getObjectType()
	{
		return GameObjectType.ATTACH_EFFECTS_LIST;
	}

	/**
	 * 获取临时效果集合
	 * @return
	 */
	public List<Integer> getTempEffects()
	{
		return tempEffects;
	}
	
	/**
	 * 获取效果过滤器
	 * @return
	 */
	public List<EffectFilter> getEffectFilters()
	{
		return new ArrayList<EffectFilter>(effectFilterMap.values());
	}
	
	/**
	 * 增加效果过滤器
	 * @param effect
	 * @param skillFilter
	 */
	public void addEffectFilter(EffectEntity effect, EffectFilter skillFilter)
	{
		effectFilterMap.put(effect.getObjectId(), skillFilter);
	}
	
	/**
	 * 移除效果过滤器
	 * @param effect
	 */
	public void rmoveEffectFilter(EffectEntity effect)
	{
		effectFilterMap.remove(effect.getObjectId());
	}
	
	/**
	 * 是否有效效果
	 * @param data
	 * @return
	 */
	public boolean isValidEffect(Projectile data)
	{
		for(EffectFilter filter : effectFilterMap.values())
		{
			if(!filter.isValid(data))
				return false;
		}
		
		return true;
	}
	
	/**
	 * 增加效果护盾
	 * @param effectId
	 * @param shield
	 */
	public void addEffecShield(int effectId, int shield)
	{
		this.effectShieldMap.put(effectId, shield);
	}
	
	/**
	 * 移除效果护盾
	 * @param effectId
	 */
	public void removeEffectShield(int effectId)
	{
		this.effectShieldMap.remove(effectId);
	}
	
	/**
	 * 增加召唤物
	 * @param effect 
	 * @param troop
	 */
	public void addChild(EffectEntity effect, Troop troop)
	{
		if(children == null)
			children = new HashMap<Integer, Set<Integer>>();
		
		int effectId = effect.getPrototypeId();
		Set<Integer> subSet = children.get(effectId);
		if(subSet == null)
		{
			subSet = new HashSet<Integer>();
			children.put(effectId, subSet);
		}
		
		subSet.add(troop.getObjectId());
	}
	
	/**
	 * 获取宠物数量
	 * @param effectModelId
	 * @return
	 */
	public int getChildrenCount(int effectModelId)
	{
		Set<Integer> subSet = children.get(effectModelId);
		if(subSet == null)
			return 0;
		
		WarInstance war = getWar();
		int total = 0;
		Iterator<Integer> iterator = subSet.iterator();
		while(iterator.hasNext())
		{
			Integer petId = iterator.next();
			GameObject obj = war.getObject(petId);
			if(obj == null || obj.getObjectType() != GameObjectType.TROOP)
			{
				iterator.remove();
				continue;
			}
			
			WarObjectInstance troop = (WarObjectInstance) obj;
			if(!troop.isLive())
			{
				iterator.remove();
				continue;
			}
			
			total++;
		}
		return total;
	}
	
	/**
	 * 获取技能过滤器
	 * @return
	 */
	public List<SkillFilter> getSkillFilters()
	{
		return new ArrayList<SkillFilter>(skillFilterMap.values());
	}
	
	/**
	 * 增加技能过滤器
	 * @param effect
	 * @param skillFilter
	 */
	public void addSkillFilter(EffectEntity effect, SkillFilter skillFilter)
	{
		skillFilterMap.put(effect.getObjectId(), skillFilter);
	}
	
	/**
	 * 移除技能过滤器
	 * @param effect
	 */
	public void rmoveSkillFilter(EffectEntity effect)
	{
		skillFilterMap.remove(effect.getObjectId());
	}
	
	/**
	 * 是否有效技能
	 * @param data
	 * @return
	 */
	public boolean isValidSkill(Projectile data)
	{
		for(SkillFilter filter : skillFilterMap.values())
		{
			if(!filter.isValid((SkillEntity) data))
				return false;
		}
		
		return true;
	}

	public Map<TriggerEventType, Set<Integer>> getTriggersMap()
	{
		return triggersMap;
	}

	public Map<Integer, Integer> getEffectShieldMap()
	{
		return effectShieldMap;
	}
	
	
}
