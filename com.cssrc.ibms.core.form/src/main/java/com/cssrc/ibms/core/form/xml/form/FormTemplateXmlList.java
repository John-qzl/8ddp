package com.cssrc.ibms.core.form.xml.form;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "form")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormTemplateXmlList {
	@XmlElements({ @javax.xml.bind.annotation.XmlElement(name = "formTemplates", type = FormTemplateXml.class) })
	private List<FormTemplateXml> FormTemplateXmlList;

	public List<FormTemplateXml> getFormTemplateXmlList() {
		return FormTemplateXmlList;
	}

	public void setFormTemplateXmlList(List<FormTemplateXml> formTemplateXmlList) {
		FormTemplateXmlList = formTemplateXmlList;
	}

}
