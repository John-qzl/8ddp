package com.cssrc.ibms.api.form.model;

public interface IFormModel {

	String getFormHtml();

	void setFormHtml(String formHtml);

	void setFormType(int urlform);

	void setFormUrl(String formUrl);

	/**
	 * 返回自定义js
	 * by zmz 20200910
	 * @return
	 */
	String getHeadHtml();
	void setHeadHtml(String headHtml);

	boolean isFormEmpty();

	int getFormType();

	String getFormUrl();

	boolean isValid();

	String getDetailUrl();
	
	
}