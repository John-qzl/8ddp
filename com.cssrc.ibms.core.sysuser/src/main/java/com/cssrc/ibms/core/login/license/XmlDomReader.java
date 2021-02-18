package com.cssrc.ibms.core.login.license;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;
public class XmlDomReader {
	/**
	 * 读取license.xml文件信息
	 * @param String fileName
	 * @return Map column
	 * @throws Exception
	 */
	public String readColumn(String fileName) throws Exception { 			
		InputStream fis =this.getClass().getClassLoader().getResourceAsStream(fileName);   
	    ObjectInputStream ois = new ObjectInputStream(fis);   
	    Document doc = (Document)ois.readObject();   
	    ois.close(); 
		Element root = doc.getRootElement();
		Iterator it = root.elementIterator();
		String key= "";
		String value = "";
		String info = "";
		while(it.hasNext()){
			Element element = (Element)it.next();
			key = element.getName();
			if(!"signature".equals(key)){
				value = element.getText();
				if(!"".equals(value)){
					info = info + key + "=" + value + ";";
				}
			}
		}
		return info.substring(0,info.length()-1);
	}
	
	public String readSignature(String fileName) throws Exception { 
		InputStream fis =this.getClass().getClassLoader().getResourceAsStream(fileName);   
        ObjectInputStream ois = new ObjectInputStream(fis);   
        Document doc = (Document)ois.readObject();   
        ois.close();
        
		// 轮循子节点
	    Element root = doc.getRootElement();
		Iterator it = root.elementIterator();
		String key= "";
		String value = "";
		String info = "";
		while(it.hasNext()){
			Element element = (Element)it.next();
			key = element.getName();
			if("signature".equals(key)){
				value = element.getText();
				if(!"".equals(value)){
					info = value;
				}
			}
		}
		return info;
	}
}
