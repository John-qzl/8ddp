package com.cssrc.ibms.core.form.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

import java.util.Date;
/**
 * 自定义表单历史记录表
 * @author YangBo
 *
 */
public class FormDefHi extends BaseModel {
	private static final long serialVersionUID = 1L;
	protected Long hisId;
	protected Long formDefId;
	protected Long categoryId;
	protected String formKey;
	protected String subject;
	protected String formDesc;
	protected String html;
	protected String template;
	protected Short isDefault;
	protected Integer versionNo;
	protected Long tableId;
	protected Short isPublished;
	protected String publishedBy;
	protected Date publishTime;
	protected String tabTitle;
	protected Integer designType;
	protected String templatesId;
	protected Long createBy;
	protected String creator;
	protected Date createTime;
	// 表单设计中自定义js，css文件
	protected String headHtml;
	public FormDefHi() {
	}

	public FormDefHi(FormDef formDef) {
		this.formDefId = formDef.getFormDefId();
		this.categoryId = formDef.getCategoryId();
		this.formKey = formDef.getFormKey().toString();
		this.subject = formDef.getSubject();
		this.formDesc = formDef.getFormDesc();
		this.html = formDef.getHtml();
		this.template = formDef.getTemplate();
		this.isDefault = formDef.getIsDefault();
		this.versionNo = formDef.getVersionNo();
		this.tableId = formDef.getTableId();
		this.isPublished = formDef.getIsPublished();
		this.publishedBy = formDef.getPublishedBy();
		this.publishTime = formDef.getPublishTime();
		this.tabTitle = formDef.getTabTitle();
		this.designType = Integer.valueOf(formDef.getDesignType());
		this.templatesId = formDef.getTemplatesId();
		this.createBy = formDef.getCreateBy();
		this.creator = formDef.getCreator();
		this.createtime = formDef.getCreatetime();
		this.headHtml = formDef.getHeadHtml();
	}

	public Long getHisId() {
		return this.hisId;
	}

	public void setHisId(Long hisId) {
		this.hisId = hisId;
	}

	public Long getFormDefId() {
		return this.formDefId;
	}

	public void setFormDefId(Long formDefId) {
		this.formDefId = formDefId;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getFormKey() {
		return this.formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFormDesc() {
		return this.formDesc;
	}

	public void setFormDesc(String formDesc) {
		this.formDesc = formDesc;
	}

	public String getHtml() {
		return this.html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Short getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(Short isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getVersionNo() {
		return this.versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Long getTableId() {
		return this.tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Short getIsPublished() {
		return this.isPublished;
	}

	public void setIsPublished(Short isPublished) {
		this.isPublished = isPublished;
	}

	public String getPublishedBy() {
		return this.publishedBy;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	public Date getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getTabTitle() {
		return this.tabTitle;
	}

	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}

	public Integer getDesignType() {
		return this.designType;
	}

	public void setDesignType(Integer designType) {
		this.designType = designType;
	}

	public String getTemplatesId() {
		return this.templatesId;
	}

	public void setTemplatesId(String templatesId) {
		this.templatesId = templatesId;
	}

	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHeadHtml() {
		return headHtml;
	}

	public void setHeadHtml(String headHtml) {
		this.headHtml = headHtml;
	}
}
