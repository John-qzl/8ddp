package com.cssrc.ibms.core.activity;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.ServiceImpl;

import org.springframework.beans.factory.InitializingBean;

/**
 * 获取命令执行对象，将其注入到当前对象。
 * @author zhulongchao
 *
 */
public class BaseProcessService extends ServiceImpl implements InitializingBean{
	
	@Resource 
	ProcessEngine processEngine;

	@Override
	public void afterPropertiesSet() throws Exception {
		 ProcessEngineImpl engine = (ProcessEngineImpl)processEngine;
		
		 //需要测试
		 this.setCommandExecutor(engine.getProcessEngineConfiguration().getCommandExecutor());
	}

	

}
