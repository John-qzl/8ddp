package com.cssrc.ibms.core.form.model;

import java.util.Locale;

import com.cssrc.ibms.api.core.util.ContextUtil;

/**
 * 表单模版语言国际化
 * @author helh
 *
 */
public class FormLanguage {
	String add = "添加";
	String select = "选择";
	String reset = "重置";
	String uploadPicture = "上传图片";
	String delPicture = "删除图片";
	String unGrouped = "未分组";
	
	public static FormLanguage getFormLanguage(){
		Locale locale = ContextUtil.getLocale();
		FormLanguage formLanguage = new FormLanguage();
		formLanguage.setSelect("选择");
		formLanguage.setAdd("添加");
		formLanguage.setReset("重置");
		formLanguage.setUnGrouped("未分组");
		formLanguage.setDelPicture("删除图片");
		formLanguage.setUploadPicture("上传图片");
		return formLanguage;
	}

	public String getAdd() {
		return add;
	}

	public void setAdd(String add) {
		this.add = add;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getReset() {
		return reset;
	}

	public void setReset(String reset) {
		this.reset = reset;
	}

	public String getUnGrouped() {
		return unGrouped;
	}

	public void setUnGrouped(String unGrouped) {
		this.unGrouped = unGrouped;
	}

	public String getUploadPicture() {
		return uploadPicture;
	}

	public void setUploadPicture(String uploadPicture) {
		this.uploadPicture = uploadPicture;
	}

	public String getDelPicture() {
		return delPicture;
	}

	public void setDelPicture(String delPicture) {
		this.delPicture = delPicture;
	}
	
	
}
