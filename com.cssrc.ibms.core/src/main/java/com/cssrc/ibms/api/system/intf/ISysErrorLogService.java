package com.cssrc.ibms.api.system.intf;


/**
 * 操作错误日志
 * @author liubo
 * @date 2017年8月31日下午6:05:39
 */
public interface ISysErrorLogService{

	public abstract Long addError(String name, String account, String ip, String error,
			String errorUrl);

}