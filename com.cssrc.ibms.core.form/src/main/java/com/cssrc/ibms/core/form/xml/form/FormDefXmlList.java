package com.cssrc.ibms.core.form.xml.form;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>
 * 对象功能:表单定义List的XMl配置 
 * </pre>
 */
@XmlRootElement(name = "form")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormDefXmlList {
	/**
	 * 表单定义xml List
	 */
	@XmlElements({ @XmlElement(name = "formDefs", type = FormDefXml.class) })
	private List<FormDefXml> formDefXmlList;

	// ==========以下是getting和setting的方法================
	public List<FormDefXml> getFormDefXmlList() {
		return formDefXmlList;
	}

	public void setFormDefXmlList(List<FormDefXml> formDefXmlList) {
		this.formDefXmlList = formDefXmlList;
	}
	 

}
