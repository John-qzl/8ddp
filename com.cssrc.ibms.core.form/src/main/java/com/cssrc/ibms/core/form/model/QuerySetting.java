package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.IQuerySetting;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;

@SuppressWarnings("serial")
@XmlRootElement(name = "querySetting")
@XmlAccessorType(XmlAccessType.NONE)
public class QuerySetting extends BaseModel implements IQuerySetting{
	@XmlAttribute
	protected Long id;
	@XmlAttribute
	protected Long sqlId;
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected String alias;
	@XmlAttribute
	protected Short style;
	@XmlAttribute
	protected Short needPage;
	@XmlAttribute
	protected Short pageSize;
	@XmlAttribute
	protected Short isQuery;
	@XmlAttribute
	protected String templateAlias;
	@XmlAttribute
	protected String templateHtml;
	@XmlAttribute
	protected String displayField;
	@XmlAttribute
	protected String filterField;
	@XmlAttribute
	protected String conditionField;
	@XmlAttribute
	protected String sortField;
	@XmlAttribute
	protected String exportField;
	@XmlAttribute
	protected String manageField;
	private List<Map<String, Object>> list = new ArrayList();
	private PagingBean pageBean;

	public List<Map<String, Object>> getList() {
		return this.list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public PagingBean getPageBean() {
		return this.pageBean;
	}

	public void setPageBean(PagingBean pageBean) {
		this.pageBean = pageBean;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setSqlId(Long sqlId) {
		this.sqlId = sqlId;
	}

	public Long getSqlId() {
		return this.sqlId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}

	public String getTemplateAlias() {
		return this.templateAlias;
	}

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	public String getTemplateHtml() {
		return this.templateHtml;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getDisplayField() {
		return this.displayField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	public String getFilterField() {
		return this.filterField;
	}

	public void setConditionField(String conditionField) {
		this.conditionField = conditionField;
	}

	public String getConditionField() {
		return this.conditionField;
	}

	public Short getStyle() {
		return this.style;
	}

	public void setStyle(Short style) {
		this.style = style;
	}

	public Short getNeedPage() {
		return this.needPage;
	}

	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}

	public Short getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Short pageSize) {
		this.pageSize = pageSize;
	}

	public Short getIsQuery() {
		return this.isQuery;
	}

	public void setIsQuery(Short isQuery) {
		this.isQuery = isQuery;
	}

	public String getSortField() {
		return this.sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getExportField() {
		return this.exportField;
	}

	public void setExportField(String exportField) {
		this.exportField = exportField;
	}

	public String getManageField() {
		return this.manageField;
	}

	public void setManageField(String manageField) {
		this.manageField = manageField;
	}
}
