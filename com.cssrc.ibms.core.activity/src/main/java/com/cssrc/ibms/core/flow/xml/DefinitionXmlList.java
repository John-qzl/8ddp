package com.cssrc.ibms.core.flow.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>
 * 对象功能:流程定义List的XMl配置 
 * </pre>
 */
@XmlRootElement(name = "flow")
@XmlAccessorType(XmlAccessType.FIELD)
public class DefinitionXmlList {
	/**
	 * 流程定义列表
	 */
	@XmlElements({ @XmlElement(name = "definitions", type = DefinitionXml.class) })
	private List<DefinitionXml> definitionXmlList;

	public List<DefinitionXml> getDefinitionXmlList() {
		return definitionXmlList;
	}

	public void setDefinitionXmlList(
			List<DefinitionXml> bpmDefinitionXmlList) {
		this.definitionXmlList = bpmDefinitionXmlList;
	}

}
