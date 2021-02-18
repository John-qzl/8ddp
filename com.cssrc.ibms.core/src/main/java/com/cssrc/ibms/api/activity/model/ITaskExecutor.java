package com.cssrc.ibms.api.activity.model;

public interface ITaskExecutor {
	
	/**
	 * 默认不抽取。
	 */
	public static final int EXACT_NOEXACT=0;
	
	/**
	 * 抽取用户
	 */
	public static final int EXACT_EXACT_USER=1;
	
	/**
	 * 二级抽取,这个适用于会签任务，
	 * 就是角色先不抽取，到具体任务时，在进行人员抽取。
	 */
	public static final int EXACT_EXACT_SECOND=2;
	
	/**
	 * 用户分组，这个适用于会签任务。
	 * 比如某个用户规则选出人员来后，将人员进行分组。
	 * 
	 */
	public static final int EXACT_USER_GROUP=3;
	
	/**
	 * 把任务直接授权给一个或多个用户
	 */
	public static final String USER_TYPE_USER="user";
	/**
	 * 把任务直接授权给一个或多个组织
	 */
	public static final String USER_TYPE_ORG="org";
	/**
	 * 把任务直接授权给一个或多个角色
	 */
	public static final String USER_TYPE_ROLE="role";
	/**
	 * 把任务直接授权给一个或多个岗位
	 */
	public static final String USER_TYPE_POS="pos";
	
	/**
	 * 把任务直接授权给一个或多个职务
	 */
	public static final String USER_TYPE_JOB="job";
	
	
	
	/**
	 * 用户分组。
	 */
	public static final String USER_TYPE_USERGROUP="group";
	
}