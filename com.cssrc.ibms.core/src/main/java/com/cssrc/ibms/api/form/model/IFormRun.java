package com.cssrc.ibms.api.form.model;

public interface IFormRun {

	/**
	 * 表单类型：节点类型
	 */
	public static final short SETTYPE_NODE = 0;
	/**
	 * 表单类型：开始类型
	 */
	public static final short SETTYPE_START = 1;
	/**
	 * 表单类型：全类型
	 */
	public static final short SETTYPE_GOBAL = 2;
	Long getFormdefKey();

}