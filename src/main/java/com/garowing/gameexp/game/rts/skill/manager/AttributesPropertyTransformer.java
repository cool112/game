package com.garowing.gameexp.game.rts.skill.manager;

import java.lang.reflect.Field;

import com.yeto.war.fightcore.attr.AttrHelp;
import com.yeto.war.fightcore.attr.entity.AttrEntity;

import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

/**
 * 怪物属性转换器
 * @author seg
 *
 */
public class AttributesPropertyTransformer implements PropertyTransformer<AttrEntity>
{

	@Override
	public AttrEntity transform(String value, Field field) throws TransformationException
	{
		return AttrHelp.parseAttr(value);
	}

}
