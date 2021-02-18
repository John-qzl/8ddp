package com.cssrc.ibms.core.form.xml.table;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.BaseFormTableXml;
import com.cssrc.ibms.api.form.model.IFormTableXml;
import com.cssrc.ibms.api.system.model.BaseSerialNumber;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;

/**
 * <pre>
 * 对象功能:自定义表的XMl配置
 * </pre>
 */
@XmlRootElement(name = "tables")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormTableXml extends BaseFormTableXml implements IFormTableXml {
	
	/**
	 * 自定义表
	 */
	@XmlElement(name = "table", type = FormTable.class)
	private FormTable formTable;

	/**
	 * 自定义表字段列表
	 */
	@XmlElementWrapper(name = "fields")
	@XmlElements({ @XmlElement(name = "field", type = FormField.class) })
	private List<FormField> formFieldList;

	/**
	 * 流水号
	 */
	@XmlElementWrapper(name = "identitys")
	@XmlElements({ @XmlElement(name = "identity") })
	private List<BaseSerialNumber> serialNumberList;

	/**
	 * 存在子表则循环
	 */
	@XmlElementWrapper(name = "subTables")
	@XmlElements({ @XmlElement(name = "subTable", type = FormTableXml.class) })
	private List<FormTableXml> formTableXmlList;

	public FormTable getFormTable() {
		return formTable;
	}

	public void setFormTable(FormTable bpmFormTable) {
		this.formTable = bpmFormTable;
	}

	public List<FormField> getFormFieldList() {
		return formFieldList;
	}

	public void setFormFieldList(List<FormField> bpmFormFieldList) {
		this.formFieldList = bpmFormFieldList;
	}

	public List<FormTableXml> getFormTableXmlList() {
		return formTableXmlList;
	}

	public void setFormTableXmlList(List<FormTableXml> bpmFormTableXmlList) {
		this.formTableXmlList = bpmFormTableXmlList;
	}

	
	public List<BaseSerialNumber> getSerialNumberList() {
		return serialNumberList;
	}

	public void setSerialNumberList(List<BaseSerialNumber> serialNumberList) {
		this.serialNumberList = serialNumberList;
	}

}
