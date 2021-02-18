package com.cssrc.ibms.skin.model;

import java.util.Date;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class SysSkin extends BaseModel {

	private static final long serialVersionUID = 1L;

	// 主键
	private Long id;
	// 皮肤名称
	private String skinName;
	//皮肤文件路径
	private String skinFilePath;
	//描述
	private String description;
	//创建人
	private String creator;
	//创建人ID
	private Long creatorId;
	//创建时间
	private Date datetime;
	//皮肤应用
	private Long appId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSkinName() {
		return skinName;
	}
	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}
	
	public String getSkinFilePath() {
		return skinFilePath;
	}
	public void setSkinFilePath(String skinFilePath) {
		this.skinFilePath = skinFilePath;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
}
