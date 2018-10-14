package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants.AttrType;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.constants.Condition;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter.AbstractAttrConditionFilter;
import com.garowing.gameexp.game.rts.skill.filter.unit.attrcondition.filter.IAttrConditionFilter;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * 属性条件集合转换器
 * 
 * @author seg
 *
 */
public class ListAttrCondPropertyTransformer implements PropertyTransformer<List<IAttrConditionFilter>>
{
	private static final Logger mLogger = Logger.getLogger(ListAttrCondPropertyTransformer.class);
	
	/**
	 * 表达式正则
	 */
	public static Pattern PATTERN_CONDITION = Pattern.compile("^(\\w+)([<=>]+)([\\d\\.]+%?)$");

	@SuppressWarnings("unchecked")
	@Override
	public List<IAttrConditionFilter> transform(String value, Field field) throws TransformationException
	{
		if (value == null || value.equals("") || value.equals("NULL") || value.equals("null"))
			return Collections.EMPTY_LIST;

		String[] parts = value.split("\\,");
		List<IAttrConditionFilter> filterList = new ArrayList<IAttrConditionFilter>();
		for (String attrCondStr : parts)
		{
			attrCondStr = attrCondStr.trim();
			Matcher matcher = PATTERN_CONDITION.matcher(attrCondStr);
			if (matcher.matches())
			{
				try
				{
					String type = matcher.group(1);
					String condStr = matcher.group(2);
					String val = matcher.group(3);
					
					AttrType attrType = AttrType.getTypeByCode(type);
					if(attrType == null)
					{
						mLogger.warn("invalid attr type [" + type + "]");
						continue;
					}
						
					
					Condition condition = Condition.getConditionByCode(condStr);
					if(condition == null)
					{
						mLogger.warn("invalid attr condition [" + condStr + "]");
						continue;
					}
						
					
					boolean isPercent = false;
					if(val.indexOf("%") > -1)
					{
						isPercent = true;
						val  = val.substring(0, val.length() - 1);
					}
					
					float condVal = Float.valueOf(val);
					Class<? extends AbstractAttrConditionFilter> clazz = attrType.getHandlerClass();
					Constructor<? extends AbstractAttrConditionFilter> constructor = clazz.getConstructor(Condition.class, boolean.class, float.class);
					AbstractAttrConditionFilter filter = constructor.newInstance(condition, isPercent, condVal);
					if(filter != null)
						filterList.add(filter);
					
				} catch (Exception e)
				{
					mLogger.error("create attr filter fail!", e);
				}
			}
		}
		return filterList;
	}

}
