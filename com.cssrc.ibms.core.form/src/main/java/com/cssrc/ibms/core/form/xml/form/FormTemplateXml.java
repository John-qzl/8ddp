package com.cssrc.ibms.core.form.xml.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.cssrc.ibms.core.form.model.FormTemplate;

/**
 * <pre>
 * 对象功能:表单定义的XMl配置 
 * 开发人员:XieChen
 * </pre>
 */
@XmlRootElement(name = "formTemplates")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormTemplateXml {
	/**
	 * 自定义表单模板
	 */
	@XmlElement(name = "formTemplate", type = FormTemplate.class)
	private FormTemplate formTemplate;

	public FormTemplate getFormTemplate() {
		return formTemplate;
	}

	public void setFormTemplate(FormTemplate formTemplate) {
		this.formTemplate = formTemplate;
	}
}
