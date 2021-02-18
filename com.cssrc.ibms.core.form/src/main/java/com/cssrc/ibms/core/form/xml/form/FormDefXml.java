package com.cssrc.ibms.core.form.xml.form;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.BaseFormDefXml;
import com.cssrc.ibms.api.form.model.IFormDefXml;
import com.cssrc.ibms.api.system.model.BaseSysBusEvent;
import com.cssrc.ibms.api.system.model.BaseSysFile;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormDefTree;
import com.cssrc.ibms.core.form.model.FormRights;
import com.cssrc.ibms.core.form.xml.table.FormTableXml;

/**
 * <pre>
 * 对象功能:表单定义的XMl配置 
 * 开发人员:zhulongchao 
 * </pre>
 */
@XmlRootElement(name = "formDefs")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormDefXml extends BaseFormDefXml implements IFormDefXml{
	/**
	 * 自定义表单
	 */
	@XmlElement(name = "formDef", type = FormDef.class)
	private FormDef formDef;

	/**
	 * 其它版本的 自定义表单 List
	 */
	@XmlElementWrapper(name = "formDefList")
	@XmlElements({ @XmlElement(name = "formDefs", type = FormDefXml.class) })
	private List<FormDefXml> formDefXmlList;

	/**
	 * 表单权限List
	 */
	@XmlElementWrapper(name = "formRightsList")
	@XmlElements({ @XmlElement(name = "formRights", type = FormRights.class) })
	private List<FormRights> formRightsList;


	/**
	 * 对于的表
	 */
	@XmlElement(name = "formTable", type = FormTableXml.class)
	private FormTableXml formTableXml;
	
	/**
	 * 附件或者帮助文档
	 */
	@XmlElementWrapper(name = "sysFileList")
    @XmlElements({ @XmlElement(name = "sysFile")})
    private List<BaseSysFile> sysFileList;	
	/**
	 * 数据模版
	 */
	@XmlElement(name = "dataTemplate", type = DataTemplate.class)
	private DataTemplate dataTemplate;

	/**
	 * 业务保存设置
	 */
	@XmlElement(name = "sysBusEvent")
	private BaseSysBusEvent sysBusEvent;

	/**
	 * 树结构设置
	 */
	@XmlElement(name = "formDefTree", type = FormDefTree.class)
	private FormDefTree formDefTree;

	// ==========以下是getting和setting的方法================
	public FormDef getFormDef() {
		
		return formDef;
	}

	public void setFormDef(FormDef formDef) {
		this.formDef = formDef;
	}

	public List<FormDefXml> getFormDefXmlList() {
		return formDefXmlList;
	}

	public void setFormDefXmlList(List<FormDefXml> formDefXmlList) {
		this.formDefXmlList = formDefXmlList;
	}

	public List<FormRights> getFormRightsList() {
		return formRightsList;
	}

	public void setFormRightsList(List<FormRights> formRightsList) {
		this.formRightsList = formRightsList;
	}



	public FormTableXml getFormTableXml() {
		return formTableXml;
	}

	public void setFormTableXml(FormTableXml formTableXml) {
		this.formTableXml = formTableXml;
	}
	
	/**
	 * @return the sysFileList
	 */
	public List<BaseSysFile> getSysFileList() {
		return sysFileList;
	}

	/**
	 * @param sysFileList the sysFileList to set
	 */
	public void setSysFileList(List<BaseSysFile> sysFileList) {
		this.sysFileList = sysFileList;
	}

	public DataTemplate getDataTemplate() {
		return dataTemplate;
	}

	public void setDataTemplate(DataTemplate dataTemplate) {
		this.dataTemplate = dataTemplate;
	}

	public BaseSysBusEvent getSysBusEvent() {
		return this.sysBusEvent;
	}

	public void setSysBusEvent(BaseSysBusEvent sysBusEvent) {
		this.sysBusEvent = sysBusEvent;
	}

	public FormDefTree getFormDefTree() {
		return formDefTree;
	}

	public void setFormDefTree(FormDefTree formDefTree) {
		this.formDefTree = formDefTree;
	}
}
