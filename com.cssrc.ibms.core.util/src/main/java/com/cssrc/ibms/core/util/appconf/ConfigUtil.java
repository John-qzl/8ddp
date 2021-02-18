package com.cssrc.ibms.core.util.appconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;

public class ConfigUtil {
	private Document doc = null;
	private static ConfigUtil config = null;

	private static Lock lock = new ReentrantLock();

	private ConfigUtil(){
        InputStream is=this.getClass().getResourceAsStream("/viewconfig.xml");
		/*try {
			is = new FileInputStream(SysConfConstant.CONF_ROOT+File.separator+"conf/"+"viewconfig.xml");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		this.doc = Dom4jUtil.loadXml(is);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ConfigUtil getInstance() {
		if (config == null) {
			lock.lock();
			try {
				if (config == null)
					config = new ConfigUtil();
			} finally {
				lock.unlock();
			}
		}
		return config;
	}
	/**
	 * 通过category和id得到路劲跳转对应的页面
	 * @param category
	 * @param id
	 * @return
	 */
	public String getValue(String category, String id) {
		String template = "category[@id='%s']/view[@name='%s']";
		String filter = String.format(template, new Object[] { category, id });
		Element root = this.doc.getRootElement();
		Element el = (Element) root.selectSingleNode(filter);
		if (el != null)
			return el.attributeValue("value");
		return "";
	}
	/**
	 * 得到跳转页面地址
	 * @param category
	 * @param id
	 * @return
	 */
	public static String getVal(String category, String id){
		return getInstance().getValue(category, id);
	}
}
