package com.cssrc.ibms.api.system.model;

public interface IMessageLog {
	/**
	 * 消息的类型
	 */
	/**
	 * 邮件信息=1
	 */
	public final static Integer MAIL_TYPE = 1;
	/**
	 * 手机短信
	 */
	public final static Integer MOBILE_TYPE = 2;
	/**
	 * 站内消息
	 */
	public final static Integer INNER_TYPE = 3;
	/**
	 * RTX即时消息
	 */
	public final static Integer RTX_TYPE = 4;

	/**
	 * 成功
	 */
	public final static Integer STATE_SUCCESS = 1;
	/**
	 * 失败
	 */
	public final static Integer STATE_FAIL = 0;
}