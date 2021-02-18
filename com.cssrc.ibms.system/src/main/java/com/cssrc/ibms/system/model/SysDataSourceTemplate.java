package com.cssrc.ibms.system.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * SysDataSourceTemplate--数据源模板
 * @author liubo
 * @date 2017年4月14日
 */
public class SysDataSourceTemplate extends BaseModel{
	// 主键Id
	protected Long id;
	//数据源模板名称
    protected String name;
	// 数据源模板别名
    protected String alias;
    //模板路径
    protected String classPath;
    //设置的链接信息
    protected String settingJson;
    //是否系统默认
    protected Integer isSystem;
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
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getSettingJson() {
		return settingJson;
	}
	public void setSettingJson(String settingJson) {
		this.settingJson = settingJson;
	}
	public Integer getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
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
		if (!(object instanceof SysDataSourceTemplate)) {
			return false;
		}
		SysDataSourceTemplate rhs = (SysDataSourceTemplate)object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name)
				.append(this.classPath, rhs.classPath).append(this.settingJson, rhs.settingJson)
				.append(this.initMethod, rhs.initMethod).append(this.isSystem, rhs.isSystem)
				.append(this.closeMethod, rhs.closeMethod).isEquals();
	}
	public int hashCode(){
		return new HashCodeBuilder(-82280557, -700257973).append(this.id).append(this.name)
				.append(this.classPath).append(this.settingJson).append(this.initMethod)
				.append(this.isSystem).append(this.closeMethod).toHashCode();
	}
	public String toString(){
		return new ToStringBuilder(this).append("id", this.id).append("name", this.name)
				.append("classPath", this.classPath).append("settingJson", this.settingJson)
				.append("initMethod", this.initMethod).append("isSystem", this.isSystem)
				.append("closeMethod", this.closeMethod).toString();
	}
}
