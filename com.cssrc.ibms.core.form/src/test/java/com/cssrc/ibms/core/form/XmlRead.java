package com.cssrc.ibms.core.form;

import java.io.FileInputStream;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;

import com.cssrc.ibms.core.util.xml.Dom4jUtil;

public class XmlRead {
	private final static String xmlPath = 
			"C:\\Users\\user\\Desktop\\TDM数据结构迁移至IBMS方案\\hksoa1.xml";
	
	public static void main(String[] args) throws Exception{
		FileInputStream inputFile = new FileInputStream(xmlPath);
		Document doc = Dom4jUtil.loadXml(inputFile,"GBK");
		document_T(doc);
	}
	public static void document_T(Document doc){
		Element element = doc.getRootElement();
		Attribute attri = element.attribute(1);
		String name = attri.getName();
		QName qname = attri.getQName();
		
	}
	
}
