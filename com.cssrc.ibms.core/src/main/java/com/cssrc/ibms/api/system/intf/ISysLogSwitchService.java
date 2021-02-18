package com.cssrc.ibms.api.system.intf;

import com.cssrc.ibms.api.system.model.ISysLogSwitch;

/**
 * 日志开关
 * @author liubo
 * @date 2017年8月31日下午6:09:36
 */
public interface ISysLogSwitchService {
	
	public abstract ISysLogSwitch getById(Long id);
}