package com.cssrc.ibms.core.form.xml.form;



 import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.model.QuerySql;
 
 @XmlRootElement(name="querySqlDefs")
 @XmlAccessorType(XmlAccessType.FIELD)
 public class SysQuerySqlDefXml
 {
 
   @XmlElement(name="querySql", type=QuerySql.class)
   private QuerySql querySql;
 
   @XmlElementWrapper(name="queryFields")
   @XmlElement(name="queryField", type=QueryField.class)
   private List<QueryField> queryFieldList;
 
   @XmlElementWrapper(name="querySettings")
   @XmlElement(name="querySetting", type=QuerySetting.class)
   private List<QuerySetting> querySettingList;

	public QuerySql getQuerySql() {
		return querySql;
	}
	
	public void setQuerySql(QuerySql querySql) {
		this.querySql = querySql;
	}
	
	public List<QueryField> getQueryFieldList() {
		return queryFieldList;
	}
	
	public void setQueryFieldList(List<QueryField> queryFieldList) {
		this.queryFieldList = queryFieldList;
	}
	
	public List<QuerySetting> getQuerySettingList() {
		return querySettingList;
	}
	
	public void setQuerySettingList(List<QuerySetting> querySettingList) {
		this.querySettingList = querySettingList;
	}
 

 }

