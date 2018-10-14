package com.garowing.gameexp.game.rts.ai.behaviac.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.garowing.gameexp.game.rts.ai.behaviac.entity.AbstractTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.ConstantTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.EnumTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.MethodTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.PropertyTag;

/**
 * 标签工具类
 * @author seg
 * 2017年1月6日
 */
public class TagUtils
{
	/**
	 * 属性正则
	 */
	private static final Pattern PROPERTY_REGEX = Pattern.compile("^(static )?([\\w<>]+) (\\w+)\\.((\\w+)::)?(\\w+)::([^\\(\\)]+)$|");
	
	/**
	 * 方法正则
	 */
	private static final Pattern METHOD_REGEX = Pattern.compile("^(\\w+)\\.((\\w+)::)?(\\w+)::(\\w+\\(.*\\))$");
	
	/**
	 * 参数正则
	 */
	private static final Pattern PARAM_REGEX = Pattern.compile("([\\d]+)|const ((\\w+)::)?(\\w+) ([^\\,]+)$|(static )?([\\w<>]+) (\\w+)\\.((\\w+)::)?(\\w+)::([^\\(\\)\\,]+)|(\\w+)\\.((\\w+)::)?(\\w+)::(\\w+\\([^\\,]*\\))|false|true");
	
	/**
	 * 常量正则
	 */
	private static final Pattern CONSTANT_REGEX = Pattern.compile("^const ((\\w+)::)?(\\w+) (\\S+)$|^([\\d]+)$");
	
	/**
	 * 转化方法标签
	 * @param desc
	 * @return
	 */
	public static MethodTag getMethodTag(String desc)
	{
		Matcher matcher = METHOD_REGEX.matcher(desc);
		if(!matcher.matches())
			return null;
		
		MethodTag methodTag = new MethodTag();
		methodTag.setInstance(matcher.group(1));
		methodTag.setClassName(matcher.group(4));
		String fullMethodName = matcher.group(5);
		String methodName = fullMethodName.substring(0, fullMethodName.indexOf("("));
		methodTag.setName(methodName);
		
		if(fullMethodName.length() == methodName.length() + 2)
			return methodTag;
		
		matcher = PARAM_REGEX.matcher(fullMethodName);
		while(matcher.find())
		{
			String param = matcher.group();
			AbstractTag paramTag = getParamTag(param);
			methodTag.addParam(paramTag);
		}
		
		return methodTag;
	}
	
	/**
	 * 获取参数标签，目前编辑器暂不支持方法返回值做参<br/>
	 * 并且目前方法做参数仅支持无参或单参数
	 * @param param
	 * @return
	 */
	public static AbstractTag getParamTag(String desc)
	{
		ConstantTag constTag = getConstantTag(desc);
		if(constTag != null)
			return constTag;

		PropertyTag propTag = getPropertyTag(desc);
		if(propTag != null)
			return propTag;
		
		MethodTag methodTag = getMethodTag(desc);
		if(methodTag != null)
			return methodTag;
		
		EnumTag enumTag = new EnumTag();
		enumTag.setValue(desc);
		
		return enumTag;
	}

	/**
	 * 转化属性标签
	 * @param desc
	 * @return
	 */
	public static PropertyTag getPropertyTag(String desc)
	{
		Matcher matcher = PROPERTY_REGEX.matcher(desc);
		if(!matcher.matches())
			return null;
		
		PropertyTag prop = new PropertyTag();
		if(matcher.group(1) != null)
			prop.setStatic(true);
		
		prop.setType(matcher.group(2));
		prop.setInstance(matcher.group(3));
		prop.setClassName(matcher.group(6));
		prop.setName(matcher.group(7));
		
		return prop;
	}
	
	/**
	 * 获取常量标签
	 * @param desc
	 * @return
	 */
	public static ConstantTag getConstantTag(String desc)
	{
		Matcher matcher = CONSTANT_REGEX.matcher(desc);
		if(!matcher.matches())
			return null;
		
		ConstantTag constTag = new ConstantTag();
		constTag.setConst(true);
		if(matcher.group(3) != null)
		{
			constTag.setType(matcher.group(3));
			constTag.setValue(matcher.group(4));
		}
		else
		{
			constTag.setType("float");
			constTag.setValue(matcher.group(5));
		}
			
		
		return constTag;
	}

	/**
	 * 获取参数数组
	 * @param params
	 * @return
	 */
	public static Object[] getParams(List<AbstractTag> params, Object obj)
	{
		if(params == null || params.isEmpty())
			return null;
		
		List<Object> paramList = new ArrayList<Object>();
		for(AbstractTag tag : params)
		{
			paramList.add(tag.rt(obj));
		}
		
		return paramList.toArray();
	}
	
	public static void main(String[] args)
	{
//		String d = "vector&lt;int&gt;";
//		Pattern p = Pattern.compile("[\\w\\;]+");
//		Matcher mat = p.matcher(d);
//		System.out.println(mat.matches());
//		String a = "float Self.TroopObject::targetX";
//		String b = "const float -1";
//		Matcher mat = PROPERTY_REGEX.matcher(b);
		
//		String c = "Self.TroopObject::move(float Self.TroopObject::targetX,float Self.TroopObject::targetY)";
		String c = "Self.com_rd_war_fightcore_behaviac_agent::BaseAIObj::findTarget(static int Self.com_rd_war_fightcore_behaviac_agent::BaseAIObj::CAMP_FRIEND,static vector<int> Self.com_rd_war_fightcore_behaviac_agent::BaseAIObj::TROOP_I_C_A,static int Self.com_rd_war_fightcore_behaviac_agent::BaseAIObj::FILTER_MIN_HP,0)";
//		Matcher mat = METHOD_REGEX.matcher(c);
//		System.out.println(mat.matches());
//		if(mat.matches())
//		{
//			System.out.println(mat.groupCount());
//			for(int i = 0; i <= mat.groupCount();i++)
//			{
//				System.out.println(mat.group(i));
//			}
//		}
		
//		if(!mat.group(5).isEmpty())
//		{
//			String method = mat.group(3);
//			Matcher mat2 = PARAM_REGEX.matcher(method);
//			while (mat2.find())
//			{
//				System.out.println(mat2.group());
//				String param = mat2.group();
//				Matcher mat3 = PROPERTY_REGEX.matcher(param);
//				if(mat3.matches())
//				{
//					System.out.println(mat3.groupCount());
//					for(int i = 0; i <= mat3.groupCount();i++)
//					{
//						System.out.println(mat3.group(i));
//					}
//				}
//				
//			}
//		}
		MethodTag methodTag = getMethodTag(c);
		System.out.println(methodTag);
	}
}
