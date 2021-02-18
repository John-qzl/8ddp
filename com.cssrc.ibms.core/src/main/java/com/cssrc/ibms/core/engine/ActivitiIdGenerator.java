package com.cssrc.ibms.core.engine;

import org.activiti.engine.impl.cfg.IdGenerator;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;

/**
 * 使用分布式的方式产生流程id。
 * @author zhulongchao
 *
 */
public class ActivitiIdGenerator implements IdGenerator {

	@Override
	public String getNextId() {
		return String.valueOf(UniqueIdUtil.genId());
		
	}

}

