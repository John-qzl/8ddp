package com.cssrc.ibms.api.activity.model;

public interface INodeSet {

	/**
	 * 没有设置表单
	 */
	public static final Short FORM_TYPE_NULL = -1;
	/**
	 * 在线表单
	 */
	public static final Short FORM_TYPE_ONLINE = 0;
	/**
	 * URL表单
	 */
	public static final Short FORM_TYPE_URL = 1;

	/**
	 * 普通任务节点
	 */
	public static final Short NODE_TYPE_NORMAL = 0;
	/**
	 * 分发任务节点
	 */
	public static final Short NODE_TYPE_FORK = 1;
	/**
	 * 汇集任务节点
	 */
	public static final Short NODE_TYPE_JOIN = 2;

	/**
	 * 允许回退 =1
	 */
	public static final Short BACK_ALLOW = 1;
	/**
	 * 隐藏意见表单
	 */
	public static final Short HIDE_OPTION = 1;
	/**
	 * 隐藏执行路径
	 */
	public static final Short HIDE_PATH = 1;
	// 不隐藏路径
	public static final Short NOT_HIDE_PATH = 0;
	public static final Short NOT_HIDE_OPTION = 0;

	/**
	 * 正常跳转=1
	 */
	public static final Short JUMP_TYPE_NORMAL = 1;
	/**
	 * 选择跳转=2
	 */
	public static final Short JUMP_TYPE_SELECT = 2;
	/**
	 * 自由跳转=3
	 */
	public static final Short JUMP_TYPE_FREE = 3;
	/**
	 * 跳转回本节点=4
	 */
	public static final Short JUMP_TYPE_SELF = 4;

	/**
	 * 允许回退到流程发起人。
	 */
	public static final Short BACK_ALLOW_START = 1;
	/**
	 * 不允许回退=0
	 */
	public static final Short BACK_DENY = 0;

	/**
	 * 任务节点
	 */
	public static final Short SetType_TaskNode = 0;

	/**
	 * 开始表单
	 */
	public static final Short SetType_StartForm = 1;
	/**
	 * 全局表单
	 */
	public static final Short SetType_GloabalForm = 2;
	/**
	 * 业务表单
	 */
	public static final Short SetType_BpmForm = 3;
	/**
	 * 规则不符合条件时，任务按定义正常跳转
	 */
	public static final Short RULE_INVALID_NORMAL = 1;

	/**
	 * 规则不符合条件时，任务仅是完成当前节点，不作跳转处理
	 */
	public static final Short RULE_INVALID_NO_NORMAL = 0;
	String getActDefId();
	String getNodeId();
	Long getFormDefId();
	Long getFormKey();
	Short getFormType();
	String getFormUrl();
	Short getSetType();
	Object getSetId();
	void setSetId(Long setId);
	String getInitScriptHandler();
	String getDetailUrl();
	String getFormDefName();
	Long getDefId();
	String getNodeName();
    String getBeforeHandler();
    String getAfterHandler();
    Long getTableId();

}