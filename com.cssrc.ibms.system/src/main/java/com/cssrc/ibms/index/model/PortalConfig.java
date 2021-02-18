package com.cssrc.ibms.index.model;

public class PortalConfig {
	private String templateId;
	private String templateName;
	private String service;

	public PortalConfig()
	{
	}

	public PortalConfig(String templateId, String templateName, String service)
	{
		this.templateId = templateId;
		this.templateName = templateName;
		this.service = service;
	}

	public String getTemplateId() {
		return this.templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return this.templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getService() {
		return this.service;
	}
	public void setService(String service) {
		this.service = service;
	}


}
