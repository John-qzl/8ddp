package com.cssrc.ibms.core.login.license;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;

public class ReadLicence {
	public Document getDocument(String fileName) throws Exception {
		InputStream fis =this.getClass().getClassLoader().getResourceAsStream("license.license");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Document doc = (Document) ois.readObject();
		ois.close();
		return doc;
	}

	public Map<String, String> getElementInfo(Element root) {
		Iterator it = root.elementIterator();
		Element element = null;
		Map<String, String> map = new HashMap<String, String>();
		String key, value;
		while (it.hasNext()) {
			element = (Element) it.next();
			key = element.getName();
			value = element.getText();
			map.put(key, value);
		}
		return map;
	}

	public Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	public Map<String, String> read(String fileName) throws Exception {
		Document doc = this.getDocument(fileName);
		Element root = this.getRootElement(doc);
		Map<String, String> map = this.getElementInfo(root);
		return map;
	}
}
