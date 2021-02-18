package com.cssrc.ibms.api.login.model;

public interface ILoginLog {
	public static final Short SUCCESS = Short.valueOf((short) 0); //登陆成功

	public static final Short VCODE_ERR = Short.valueOf((short) -1); //验证码错误（功能有待开发）

	public static final Short ACCOUNT_PWD_EMPTY = Short.valueOf((short) -2); //账户密码为空

	public static final Short ACCOUNT_PWD_ERR = Short.valueOf((short) -3); //账户密码错误

	public static final Short ACCOUNT_LOCKED = Short.valueOf((short) 1); //账户锁死

	public static final Short ACCOUNT_DISABLED = Short.valueOf((short) 2); //账户禁用

	public static final Short ACCOUNT_OVERDUE = Short.valueOf((short) 3); //账户过期（离职或游客）

	public static final Short PWDSTRATEGY_UNPASS = Short.valueOf((short) 4); //登录输入次数太多，验证码记录
	
	public static final Short ACCOUNT_DELETE = Short.valueOf((short) 5); //账户被删除.
	
	
	Short getStatus();
	String getDesc();
	void setAccount(String username);
	void setIp(String ipAddr);
	void setDesc(String string);
	void setStatus(Short accountPwdErr);
}
