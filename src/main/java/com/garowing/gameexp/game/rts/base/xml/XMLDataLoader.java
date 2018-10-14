package com.garowing.gameexp.game.rts.base.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

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


/**
 * 将指定目录下的XML整合成一个XML
 * @author      darren.ouyang <ouyang.darren@gmail.com>
 * @date        2015年4月11日
 * @version 	1.0
 * @copyright Copyright (c) 2014, darren.ouyang
 */
public class XMLDataLoader
{
	private Logger log = Logger.getLogger(XMLDataLoader.class);
	
	private XMLInputFactory		inputFactory	= XMLInputFactory.newInstance();
	private XMLOutputFactory	outputFactory	= XMLOutputFactory.newInstance();
	private XMLEventFactory		eventFactory	= XMLEventFactory.newInstance();
	
	public void doupdate(String folderName,String fileName, String qNameString, boolean recursive)
	{
		
		XMLEventReader reader = null;
		XMLEventWriter writer = null;
		
		try {
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
