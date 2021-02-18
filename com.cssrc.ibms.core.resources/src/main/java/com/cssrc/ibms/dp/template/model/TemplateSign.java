package com.cssrc.ibms.dp.template.model;

/**
 * @description 模板签署定义model
 * @author xie chen
 * @date 2019年12月5日 上午9:19:13
 * @version V1.0
 */
public class TemplateSign {
	
	protected Long id;
	
	protected String name;
	
	protected String order;
	
	protected String templateId;

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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}
