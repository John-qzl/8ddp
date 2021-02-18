package com.cssrc.ibms.core.form.xml.table;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre> 
 * 对象功能:自定义表List的XMl配置 
 * </pre>
 */
@XmlRootElement(name = "bpm")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormTableXmlList {
	
	
	@XmlElements({ @XmlElement(name = "tables", type = FormTableXml.class) })
	private List<FormTableXml> formTableXmlList;

	public List<FormTableXml> getFormTableXmlList() {
		return formTableXmlList;
	}

	public void setFormTableXmlList(List<FormTableXml> bpmFormTableXmlList) {
		this.formTableXmlList = bpmFormTableXmlList;
	}

}
