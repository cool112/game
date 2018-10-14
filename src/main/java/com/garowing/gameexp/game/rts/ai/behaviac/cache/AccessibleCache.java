package com.garowing.gameexp.game.rts.ai.behaviac.cache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.garowing.gameexp.game.rts.ai.behaviac.agent.Agent;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentClass;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentMethod;
import com.garowing.gameexp.game.rts.ai.behaviac.annotation.AgentProperty;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.MethodTag;
import com.garowing.gameexp.game.rts.ai.behaviac.entity.PropertyTag;
import com.garowing.gameexp.utils.ClassUtils;
import com.garowing.gameexp.utils.ClassUtils.ClassFilter;


/**
 * 访问对象静态缓存
 * @author seg
 * 2017年1月6日
 */
public class AccessibleCache
{
	private static final Logger mLogger = LogManager.getLogger(AccessibleCache.class);
	
	/**
	 * 类名映射
	 * classMap.shortNmae = Class
	 */
	private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	
	/**
	 * 方法映射
	 * methodMap.Class.methodName = Method
	 */
	private static Map<Class<?>, Map<String, Method>> methodMap = new HashMap<Class<?>, Map<String, Method>>();
	
	/**
	 * 成员映射，方便起见，约定都没有访问限制
	 * fieldMap.Class.fieldName = Field
	 */
	private static Map<Class<?>, Map<String, Field>> fieldMap = new HashMap<Class<?>, Map<String, Field>>();
	
	/**
	 * 扫描包名
	 */
	private static String SCAN_PACKAGE = "com.garowing.gameexp.game.rts.ai.behaviac.agent";
	
	static
	{
		autoRegister(SCAN_PACKAGE, new ClassFilter(){

			@SuppressWarnings("rawtypes")
			@Override
			public boolean accept(Class clazz)
			{
				if(!ClassUtils.isSubclass(clazz, Agent.class))
					return false;
				
				if(ClassUtils.isAbstract(clazz))
					return false;
				
				return true;
			}
			
		});
	}
	
	/**
	 * 增加类
	 * @param name
	 * @param clazz
	 */
	public static void addClass(String name, Class<?> clazz)
	{
		if(clazz == null)
			return;
		
		classMap.putIfAbsent(name, clazz);
	}
	
	/**
	 * 自动注册
	 * @param packageName
	 * @param classFilter
	 */
	private static void autoRegister(String packageName, ClassFilter classFilter)
	{
		Collection<Class<Agent>> classes = ClassUtils.scanPackage(packageName, classFilter);
		for(Class<Agent> clazz : classes)
		{
			AgentClass agentClass = clazz.getAnnotation(AgentClass.class);
			if(agentClass == null)
				continue;
			
			if("##default".equals(agentClass.name()))
				addClass(clazz);
			else
				addClass(agentClass.name(), clazz);
			
			Collection<AccessibleObject> members = getAnnotatedMembers(clazz, AgentProperty.class, AgentMethod.class);
			for(AccessibleObject member : members)
			{
				if(member instanceof Method)
					addMethod(clazz, (Method) member);
				else
					addField(clazz, (Field) member);
			}
		}
	}
	

	/**
	 * 获取指定注解的成员，递归
	 * @param clazz
	 * @param annos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<AccessibleObject> getAnnotatedMembers (Class<?> clazz, Class<?>...annos)
	{
		if(clazz == null)
			return Collections.EMPTY_LIST;
		
		List<AccessibleObject> fieldsAll = new ArrayList<AccessibleObject>();
		
		for(; clazz != Object.class; clazz = clazz.getSuperclass())
		{
			Field [] fields = clazz.getDeclaredFields();
			for(Field field : fields)
			{
				boolean originalAccessible = field.isAccessible();
				field.setAccessible(true);
				
				if (isAnnotatedBy(field, annos))
					fieldsAll.add(field);
				
				field.setAccessible(originalAccessible);
			}
			
			Method[] methods = clazz.getDeclaredMethods();
			for(Method method : methods)
			{
				boolean originalAccessible = method.isAccessible();
				method.setAccessible(true);
				
				if (isAnnotatedBy(method, annos))
					fieldsAll.add(method);
				
				method.setAccessible(originalAccessible);
			}
		}
		
		return fieldsAll;
	}
	
	/**
	 * 是否拥有注解
	 * @param annos
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean isAnnotatedBy(AccessibleObject member, Class<?>...annos)
	{
		if(annos == null || annos.length == 0)
			return true;
		
		for(Class anno : annos)
		{
			if(member.isAnnotationPresent(anno))
				return true;
		}
		
		return false;
	}

	/**
	 * 增加类
	 * @param clazz
	 */
	public static void addClass(Class<?> clazz)
	{
		if(clazz == null)
			return;
		
		classMap.putIfAbsent(clazz.getSimpleName(), clazz);
	}
	
	/**
	 * 获取类
	 * @param name
	 * @return
	 */
	public static Class<?> getClass(String name)
	{
		return classMap.get(name);
	}
	
	/**
	 * 增加方法
	 * @param method
	 */
	public static void addMethod(Method method)
	{
		Class<?> clazz = method.getDeclaringClass();
		addMethod(clazz, method);
	}
	
	/**
	 * 增加方法，处理继承的问题
	 * @param clazz
	 * @param method
	 */
	@SuppressWarnings("rawtypes")
	public static void addMethod(Class clazz, Method method)
	{
		Map<String, Method> methodSubMap = methodMap.get(clazz);
		if(methodSubMap == null)
		{
			Map<String, Method> newMap = new HashMap<String, Method>();
			methodMap.put(clazz, newMap);
			methodSubMap = methodMap.get(clazz);
		}
		
		methodSubMap.putIfAbsent(method.getName(), method);
	}
	
	/**
	 * 获取方法
	 * @param methodTag
	 * @return
	 */
	public static Method getMethod(MethodTag methodTag)
	{
		Class<?> clazz = classMap.get(methodTag.getClassName());
		if(clazz == null)
			return null;
		
		Map<String, Method> methodSubMap = methodMap.get(clazz);
		if(methodSubMap == null)
		{
			mLogger.warn("method not found in Class: " + clazz+" method:"+methodTag);
			return null;
		}
		
		Method method = methodSubMap.get(methodTag.getName());
		if(method == null)
		{
			mLogger.warn("method not found in Class: [" + clazz + "] method: [" + methodTag.getName() + "]");
			return null;
		}
		
		return method;
	}
	
	/**
	 * 增加属性
	 * @param field
	 */
	public static void addField(Field field)
	{
		Class<?> clazz = field.getDeclaringClass();
		addField(clazz, field);
	}
	
	/**
	 * 增加属性，处理继承
	 * @param clazz
	 * @param field
	 */
	private static void addField(Class<?> clazz, Field field)
	{
		Map<String, Field> fieldSubMap = fieldMap.get(clazz);
		if(fieldSubMap == null)
		{
			Map<String, Field> newMap = new HashMap<String, Field>();
			fieldMap.put(clazz, newMap);
			fieldSubMap = fieldMap.get(clazz);
		}
		
		field.setAccessible(true);
		fieldSubMap.putIfAbsent(field.getName(), field);
	}
	
	/**
	 * 获取属性
	 * @param fieldTag
	 * @return
	 */
	public static Field getField(PropertyTag fieldTag)
	{
		Class<?> clazz = classMap.get(fieldTag.getClassName());
		if(clazz == null)
			return null;
		
		Map<String, Field> fieldSubMap = fieldMap.get(clazz);
		if(fieldSubMap == null)
		{
			mLogger.warn("field not found in Class: " + clazz);
			return null;
		}
		
		Field field = fieldSubMap.get(fieldTag.getName());
		if(field == null)
		{
			mLogger.warn("field not found in Class: [" + clazz + "] field: [" + fieldTag.getName() + "]");
			return null;
		}
		
		return field;
	}

}
