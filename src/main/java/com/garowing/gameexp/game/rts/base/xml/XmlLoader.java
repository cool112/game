package com.garowing.gameexp.game.rts.base.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import commons.utils.XmlObjectUtil;

/**
 * xml加载器
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年7月22日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public final class XmlLoader 
{
	
	private static Logger log = Logger.getLogger(XmlLoader.class);

	private static final String _XML = "xml";
	private static final String LEFT_LINE = "/";
	
	private final Object lock = new Object();
	
	private final static XmlLoader instance = new XmlLoader();
	
	private XmlLoader(){}
	
	public static XmlLoader getInstance()
	{
		return instance;
	}
	
	// xml主目录地址
	private String XML_MAIN_DIR = "";
	
	public void loadClass (String mainDir, Class<? extends Object> clazz)
	{	
		this.XML_MAIN_DIR = mainDir;
		loadClass(clazz);
	}
	
	/**
	 * 对class对象进行xml解析
	 * @param clazz
	 * @create	2015年7月22日	darren.ouyang
	 */
	public void loadClass (Class<? extends Object> clazz)
	{
		synchronized (lock) 
		{
			Field[] fields = clazz.getDeclaredFields();
			List<Field> fieldList = Arrays.asList(fields);
			loadFields(fieldList);
		}
	}
	
	/**
	 * 加载多个xml
	 * @param clazz
	 * @param xmlNames
	 * @create	2015年7月22日	darren.ouyang
	 */
	public void loadXmls (Class<? extends Object> clazz, List<String> xmlNames)
	{
		synchronized (lock) 
		{
			List<Field> fields = new ArrayList<Field>();
			
			// 分析需要重新加载的字段
			Field[] allFields = clazz.getDeclaredFields();
			for (Field field : allFields)
			{
				String name = null;
				if(field.isAnnotationPresent(DataHolder.class))
				{
					DataHolder dataHolder = field.getAnnotation(DataHolder.class);
					name = dataHolder.name();
				}
				else if(field.isAnnotationPresent(DataHolderFolder.class))
				{
					DataHolderFolder dataHolderFolder = field.getAnnotation(DataHolderFolder.class);
					name = dataHolderFolder.fileName();
				}
				else
				{
					continue;
				}
				
				if(name != null && !name.equals("") && name.indexOf(_XML) > -1)
				{
					String xmlNname = name.substring(name.lastIndexOf(LEFT_LINE) + 1, name.lastIndexOf(_XML) - 1);
					
					if (xmlNames.contains(xmlNname))
						fields.add(field);
				}
			}
			
			// 加载需要的字段
			loadFields(fields);
		}
	}
	
	/**
	 * 加载读取xml的Class
	 * @param class1
	 * @create	2015年7月22日	darren.ouyang
	 */
	private void loadFields (List<Field> fields)
	{
		Map<Field, Object> fieldObjectCollection = new HashMap<Field, Object>();
		
		// 生成所有需要赋值属性集合
		for(Field field : fields)
		{
			// 只加载静态字段
			if(!Modifier.isStatic(field.getModifiers()))
				continue;
		
			// 只加载存在指定注解的字段
			AnnotationParser parser = new AnnotationParser();
			if(!parser.checkAnnotaion(field))
				continue;
		
			// 字段修饰类型不可以为final
			if (Modifier.isFinal(field.getModifiers()))
			{
				String errMsg = "加载xml的对象属性:["+field.getName()+"]修饰符有final,所以无法赋值";
				log.error(errMsg);
				throw new RuntimeException(errMsg);
			}
			
			boolean oldAccessible = field.isAccessible();
			field.setAccessible(true);
			
			// 解析当前字段的对象
			Object fieldObject = parser.parserObject(field);
			if (fieldObject != null)
			{
				if (fieldObject instanceof AbstractData)
					((AbstractData)fieldObject).initData();
				
				fieldObjectCollection.put(field, fieldObject);
			}
			field.setAccessible(oldAccessible);
		}
		
		// 遍历所有赋值属性进行赋值操作
		for (Entry<Field, Object> entry : fieldObjectCollection.entrySet())
		{
			Field field = entry.getKey();
			Object fieldObject = entry.getValue();
			boolean oldAccessible = field.isAccessible();
			field.setAccessible(true);
			
			try{
				field.set(null, fieldObject);
			}catch (Exception e) {
				log.error("xml赋值失败,类["+field.getDeclaringClass().getName()+"]属性["+field.getName()+"]", e);
			}
			
			field.setAccessible(oldAccessible);
		}
		
	}
	
	/**
	 * xml注解解析器
	 * @author      darren.ouyang <ouyang.darren@gmail.com>
	 * @date        2015年7月22日
	 * @version 	1.0
	 * @copyright Copyright (c) 2014, darren.ouyang
	 */
	private final class AnnotationParser
	{
		/**
		 * 检测注解枚举
		 * @param field
		 * @return
		 * @create	2015年7月22日	darren.ouyang
		 */
		public boolean checkAnnotaion (Field field)
		{
			if(!field.isAnnotationPresent(DataHolder.class) && !field.isAnnotationPresent(DataHolderFolder.class))
				return false;
			
			return true;
		}
		
		/**
		 * 解析对象
		 * @param field
		 * @return
		 * @create	2015年7月22日	darren.ouyang
		 */
		public Object parserObject (Field field)
		{
			Object obj = null;
			if (field.isAnnotationPresent(DataHolder.class))
				obj = dataHolderParser(field);
			else if (field.isAnnotationPresent(DataHolderFolder.class))
				obj = dataHolderFolderParser(field);
			return obj;
		}
		
		/**
		 * 单个xml文件转换对象
		 * @param field
		 * @return
		 * @create	2015年7月22日	darren.ouyang
		 */
		private Object dataHolderParser(Field field)
		{
			DataHolder dataholder = field.getAnnotation(DataHolder.class);
			// 判断路径是否合法
			String dataholderName = XML_MAIN_DIR + dataholder.name();
			if(dataholderName.isEmpty())
			{
				log.warn("DataHolder " + field.getName() + " class " + field.getDeclaringClass().getName() + " leng为0");
				return null;
			}
			
			File file = new File(dataholderName);
			if(!file.exists())
			{
				log.warn("类["+field.getDeclaringClass().getName()+"]属性["+field.getName()+"]的注解指定的文件["+dataholderName+"]不存在。");
				return null;
			}
			
			try{
				return XmlObjectUtil.XmlToJavaBean(file, field.getType());
			}catch (Exception e) {
				log.error("类["+field.getDeclaringClass().getName()+"]属性["+field.getName()+"]的注解指定的文件["+dataholderName+"]无法转换。", e);
				return null;
			}
		}
		
		/**
		 * 文件夹多个xml转换对象
		 * @param field
		 * @return
		 * @create	2015年7月22日	darren.ouyang
		 */
		private Object dataHolderFolderParser(Field field)
		{
			DataHolderFolder dataholder = field.getAnnotation(DataHolderFolder.class);
			
			// 判断目录路径是否合法
			String dataholderFolderName = XML_MAIN_DIR + dataholder.folderName();
			String dataholderFileName = XML_MAIN_DIR + dataholder.fileName();
			boolean recursive = dataholder.recursive();
			if(dataholderFolderName.isEmpty())
			{
				log.warn("DataHolderFolder " + field.getName() + " class " + field.getDeclaringClass().getName() + " 目录路径为空");
				return null;
			}
			
			// 判断最终文件路径是否合法
			if(dataholderFileName.isEmpty())
			{
				log.warn("DataHolderFolder " + field.getName() + " class " + field.getDeclaringClass().getName() + " 最终文件为空");
				return null;
			}
			
			File file = new File(dataholderFolderName);
			if(!file.exists())
			{
				log.warn("类["+field.getDeclaringClass().getName()+"]属性["+field.getName()+"]的注解指定的文件夹["+dataholderFolderName+"]不存在。");
				return null;
			}
			
			XmlRootElement rootElement = field.getType().getAnnotation(XmlRootElement.class);
			String qname = "root";
			if (rootElement != null && !rootElement.name().isEmpty())
				qname = rootElement.name();
			
			try{
				loadFolderToXmlFile(dataholderFolderName, dataholderFileName, qname, recursive);
				return XmlObjectUtil.XmlToJavaBean(dataholderFileName, field.getType());
			}catch (Exception e){
				log.error("类["+field.getDeclaringClass().getName()+"]属性["+field.getName()+"]的注解指定的文件夹["+
						dataholderFolderName+"]最终xml文件["+ dataholderFileName +"]无法转换。", e);
				return null;
			} 
		}
		
		/**
		 * 将目录转换生成最终xml文件
		 * @param folderName
		 * @param fileName
		 * @param qNameString
		 * @create	2015年7月22日	darren.ouyang
		 */
		private void loadFolderToXmlFile (String folderName, String fileName, String qNameString, boolean recursive)
		{
			XMLEventReader reader = null;
			XMLEventWriter writer = null;
			
			try {
				XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
				XMLInputFactory	 inputFactory = XMLInputFactory.newInstance();
				XMLEventFactory	 eventFactory = XMLEventFactory.newInstance();
				
				Collection<File> fileList = FileUtils.listFiles(new File(folderName), new String[]{"xml"}, recursive);
				
				writer = outputFactory.createXMLEventWriter(new BufferedWriter(new FileWriter(fileName, false)));
				XMLEvent event = null;
				writer.add(eventFactory.createStartDocument("UTF-8", "1.0"));
				writer.add(eventFactory.createCharacters("\n\t"));
				QName qname = new QName(qNameString);
				writer.add(eventFactory.createStartElement(qname, null, null));
				for(File f : fileList)
				{
					reader = inputFactory.createXMLEventReader(new FileReader(f));
					log.info(f.toString());
					while(reader.hasNext())
					{
						event = reader.nextEvent();
						if(event.isStartDocument())//7   <?xml version="1.0" encoding='null' standalone='no'?>
							continue;
						else if(event.isCharacters())
							continue;
						else if(event.isEndDocument())
							continue;
						else
							writer.add(eventFactory.createCharacters("\n\t"));
							writer.add(event);
					}
				}
				writer.add(eventFactory.createCharacters("\n\t"));
				writer.add(eventFactory.createEndElement(qname, null));
			} 
			catch (XMLStreamException e) 
			{
				String str = "路径："+ folderName + "出现错误。";
				log.error(str, e);
			}
			catch (IOException e)
			{
				String str = "路径："+ folderName + "出现错误。";
				log.error(str, e);
			}
			finally
			{
				try 
				{
					writer.close();
					reader.close();
				} 
				catch (XMLStreamException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
