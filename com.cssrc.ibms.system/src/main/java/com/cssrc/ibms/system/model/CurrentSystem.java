package com.cssrc.ibms.system.model;

import com.cssrc.ibms.api.system.model.ICurrentSystem;

public class CurrentSystem implements ICurrentSystem{
	private String systemName;//系统名
	private String companyName;//公司名
	private String systemLog;//系统logo
	private String systemUrl;//系统访问页面
	private String desc;//描述
	private String skinStyle;//皮肤风格
	private String version; //系统版本
	
	
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSystemLog() {
		return systemLog;
	}
	public void setSystemLog(String systemLog) {
		this.systemLog = systemLog;
	}
	public String getSystemUrl() {
		return systemUrl;
	}
	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSkinStyle() {
		return skinStyle;
	}
	public void setSkinStyle(String skinStyle) {
		this.skinStyle = skinStyle;
	}
	
	
	

}
