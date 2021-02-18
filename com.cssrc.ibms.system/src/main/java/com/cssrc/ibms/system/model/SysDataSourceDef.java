package com.cssrc.ibms.system.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * SysDataSourceDef-=-数据源信息
 * @author liubo
 * @date 2017年4月14日
 */
public class SysDataSourceDef extends BaseModel implements ISysDataSourceDef{
	// 主键Id
	protected Long id;
	// 数据源名称
	protected String name;
	// 数据源别名
    protected String alias;
    //数据库类型
    protected String dbType;
    //数据库设置信息
    protected String settingJson;
    //是否初始化容器
    protected Integer initContainer;
    //是否生效
    protected Integer isEnabled;
    //选择的模板路径
    protected String classPath;
    //初始化方法
    protected String initMethod;
    //关闭方法
    protected String closeMethod;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getSettingJson() {
		return settingJson;
	}
	public void setSettingJson(String settingJson) {
		this.settingJson = settingJson;
	}

	public Integer getInitContainer() {
		return initContainer;
	}
	public void setInitContainer(Integer initContainer) {
		this.initContainer = initContainer;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getInitMethod() {
		return initMethod;
	}
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}
	public String getCloseMethod() {
		return closeMethod;
	}
	public void setCloseMethod(String closeMethod) {
		this.closeMethod = closeMethod;
	}
	public boolean equals(Object object){
		if (!(object instanceof SysDataSourceDef)) {
			return false;
		}
		SysDataSourceDef rhs = (SysDataSourceDef)object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name)
				.append(this.alias, rhs.alias).append(this.dbType, rhs.dbType)
				.append(this.settingJson, rhs.settingJson).append(this.initContainer, rhs.initContainer)
				.append(this.isEnabled, rhs.isEnabled).append(this.classPath, rhs.classPath)
				.append(this.initMethod, rhs.initMethod).append(this.closeMethod, rhs.closeMethod).isEquals();
	}
	      
	public int hashCode(){
		return new HashCodeBuilder(-82280557, -700257973).append(this.id).append(this.name)
				.append(this.alias).append(this.dbType).append(this.settingJson)
				.append(this.initContainer).append(this.isEnabled).append(this.classPath)
				.append(this.initMethod).append(this.closeMethod).toHashCode();
	}
	      
	public String toString(){
		return new ToStringBuilder(this).append("id", this.id).append("name", this.name)
				.append("alias", this.alias).append("dbType", this.dbType)
				.append("settingJson", this.settingJson).append("initContainer", this.initContainer)
				.append("isEnabled", this.isEnabled).append("classPath", this.classPath)
				.append("initMethod", this.initMethod).append("closeMethod", this.closeMethod).toString();
	}
}
