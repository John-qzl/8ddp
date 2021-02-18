package com.cssrc.ibms.api.activity.model;

import java.util.Date;

public interface IDefinition {

	/**
	 * 流程定义规则
	 */
	public static final String DefaultSubjectRule = "{流程标题:title}-{发起人:startUser}-{发起时间:startTime}";
	/**
	 * 默认测试状态的标签
	 */
	public static final String TEST_TAG = "测试状态";
	/**
	 * 为主流程 main=1
	 */
	public final static Short MAIN = 1;
	/**
	 * 非主流程，即为历史版本   main=0;
	 */
	public final static Short NOT_MAIN = 0;
	/**
	 * 不可用
	 */
	public final static Short STATUS_UNDEPLOYED = 0;
	/**
	 * 可用
	 */
	public final static Short STATUS_ENABLED = 1;
	/**
	 * 禁用
	 */
	public final static Short STATUS_DISABLED = 2;
	/**
	 *禁用流程实例
	 */
	public final static Short STATUS_INST_DISABLED = 3;
	/**
	 *试运行
	 */
	public final static Short STATUS_TEST = 4;
	/**
	 * 挂起
	 */
	public final static Short STATUS_SUSPEND = -1;
	/**
	 * 允许
	 */
	public final static Short ALLOW = 1;
	/**
	 * 不允许
	 */
	public final static Short NOT_ALLOW = 0;
	//表名称
	public final static String TABLE_NAME = "bpm_definition";
	
	
	Integer getDirectstart();


	Long getDefId();


	String getSubject();


	void setActDeployId(Long actDeployId);


	void setActDefKey(String actDefKey);


	void setActDefId(String actDefId);


	void setVersionNo(Integer version);


	void setDefId(Long defId);


	void setIsMain(Short isMain);


	void setCreatetime(Date createtime);


	void setUpdatetime(Date updatetime);


	void setToFirstNode(Short toFirstNode);


	void setInformStart(String informStart);


	void setStatus(Short status);


	Long getTypeId();
	
	public String getActDefId();

    String getActDefKey();
    String getPendingSetting();
    public String getKeyPath();
    String getDefKey();

}