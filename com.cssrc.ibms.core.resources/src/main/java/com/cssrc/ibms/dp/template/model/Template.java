package com.cssrc.ibms.dp.template.model;

/**
 * @description 模板管理model
 * @author xie chen
 * @date 2019年12月4日 下午6:58:58
 * @version V1.0
 */
public class Template {
	// 主键
	protected Long ID;
	// 模板名称
	protected String templateName;
	// 模板编号
	protected String templateCode;
	// 所属发次
	protected Long projectId;
	// 模板html
	protected String contents;
	// 备注
	protected String remark;
	// 文件夹id
	protected Long folderId;
	// 模板类别
	private String type;
	// 模板状态
	private String status;
	// 型号id
	private String moduleId;
	// 模板种类
	private String modelType;
	
	
	
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getContents(){
		return contents;
	}
	public void setContents(String contents){
		this.contents=contents;
	}
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public Long getFolderId() {
		return folderId;
	}
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

}
