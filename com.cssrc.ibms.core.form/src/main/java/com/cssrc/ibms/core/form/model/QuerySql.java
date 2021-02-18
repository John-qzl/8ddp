package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.api.form.model.IQuerySql;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

@SuppressWarnings("serial")
@XmlRootElement(name = "querySql")
@XmlAccessorType(XmlAccessType.NONE)
public class QuerySql extends BaseModel implements IQuerySql{

	@XmlAttribute
	protected Long id;
	@XmlAttribute
	protected String sql;
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected String dsname; //数据源
	@XmlAttribute
	protected String alias; //别名
	@XmlAttribute
	private Long categoryId;
	@XmlAttribute
	private String categoryName;
	@XmlAttribute
	protected String urlParams;
	@XmlElement
	protected List<QueryField> queryFields = new ArrayList();
	@XmlElement
	protected List<QuerySetting> querySettingList = new ArrayList();
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSql() {
		return this.sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getUrlParams() {
		return this.urlParams;
	}

	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
	}

	public List<QueryField> getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(List<QueryField> queryFields) {
		this.queryFields = queryFields;
	}

	public List<QuerySetting> getQuerySettingList() {
		return querySettingList;
	}

	public void setQuerySettingList(List<QuerySetting> querySettingList) {
		this.querySettingList = querySettingList;
	}
	
	
}