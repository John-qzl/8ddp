package com.cssrc.ibms.dp.template.model;

/**
 * @description 模板实例model
 * @author xie chen
 * @date 2019年12月5日 下午3:59:18
 * @version V1.0
 */
public class TemplateInstance {
	// 主键
	protected Long id;
	// 实例名称
	protected String instanceName;
	// 实例编号
	protected String instanceCode;
	// 实例状态
	protected String status;
	// 实例HTML
	protected String content;
	/*上传时间*/
	protected String uploadtime;
	// 所属模板id
	protected String templateId;
	/*所属工作项目*/
	protected String taskId;
	// 版本
	protected String version;
	// 开始检查时间
	protected String startTime;
	// 检查结束时间
	protected String endTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getInstanceCode() {
		return instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this.instanceCode = instanceCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
