package com.cssrc.ibms.api.activity.model;

public interface IAgentSetting {
	/**
	 * 代理类型：全权代理
	 */
	public static int AUTHTYPE_GENERAL   	=0;
	/**
	 * 代理类型：部分代理
	 */
	public static int AUTHTYPE_PARTIAL   	=1;
	/**
	 * 代理类型：条件代理
	 */
	public static int AUTHTYPE_CONDITION   =2;
	
	/**
	 * 状态：启用
	 */
	public static Long ENABLED_YES 				= 1L;
	/**
	 * 状态：禁用
	 */
	public static Long ENABLED_NO 				= 0L;
	
	public Short getAuthtype();
	
	public void setAuthid(Long authid);
	/**
	 * 返回 授权人ID
	 * @return
	 */
	public Long getAuthid() ;

	public void setAuthname(String authname) ;
	
	/**
	 * 返回 授权人
	 * @return
	 */
	public String getAuthname();
	
	public Long getAgentid();
	
	public String getAgent();
}
