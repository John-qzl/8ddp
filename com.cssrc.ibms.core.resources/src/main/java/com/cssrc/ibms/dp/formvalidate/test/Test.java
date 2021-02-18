package com.cssrc.ibms.dp.formvalidate.test;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.cssrc.ibms.dp.formvalidate.service.SimpleFormValidateService;
import com.google.code.yanf4j.util.ResourcesUtils;

/**
 * 校验功能测试
 * @author scc
 *
 */
public class Test {
	public static void main(String[] args) {
		SimpleFormValidateService service=new SimpleFormValidateService();
		try {
			File file=ResourcesUtils.getResourceAsFile("rowColSub-1.txt");
			SAXReader reader=new SAXReader();
			Document document=reader.read(file);
			String html= document.asXML();
			service.validate(html, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
