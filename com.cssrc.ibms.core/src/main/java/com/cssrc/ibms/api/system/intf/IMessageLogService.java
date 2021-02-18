package com.cssrc.ibms.api.system.intf;

public interface IMessageLogService {

	/**
	 * 新增消息日志
	 * @param subject 主题
	 * @param receiver 接受者 ，多个人 ","分隔
	 * @param messageType 消息类型
	 * @param state 状态
	 */
	public abstract void addMessageLog(String subject, String receiver,
			Integer messageType, Integer state);

}