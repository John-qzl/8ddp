package com.cssrc.ibms.api.activity.model;


public interface IProcessRun {
	/** 挂起状态 */
	public static final Short STATUS_SUSPEND = 0;
	/** 运行状态 */
	public static final Short STATUS_RUNNING = 1;
	/** 结束状态 */
	public static final Short STATUS_FINISH = 2;
	/** 人工终止 */
	public static final Short STATUS_MANUAL_FINISH = 3;
	/** 草稿状态 */
	public static final Short STATUS_FORM=4;
	/** 已撤销 */
	public static final Short STATUS_RECOVER = 5;
	/** 已驳回 */
	public static final Short STATUS_REJECT = 6;
	/** 已追回 */
	public static final Short STATUS_REDO = 7;
	/** 逻辑删除 */
	public static final Short STATUS_DELETE = 10;
	
	
	/**试运行**/
	public static final Short TEST_RUNNING=0;
	/**正常运行**/
	public static final Short FORMAL_RUNNING=1;
	/**不追回*/
	public static final Short RECOVER_NO = 0;
	/**追回*/
	public static final Short RECOVER = 1;
	Long getDefId();
	Long getRunId();
	String getActInstId();
	String getActDefId();
	String getBusinessKey();
	Short getFormType();
	String getFormKeyUrl();
	java.util.Date getCreatetime();
	Long getTypeId();
	public String getSubject();
	String getFlowKey();
	Long getFormDefId();
	public String getCreator();
    void setBusinessKey(String businessKey);
    Short getStatus();
}