package com.cssrc.ibms.core.flow.listener;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.model.INodeSql;
import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.core.activity.event.ProcessEndEvent;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.api.activity.intf.ITaskThreadService;
import com.cssrc.ibms.api.sysuser.intf.IEventUtilService;

/**
 * ProcessEndEventListener
 * @author liubo
 * @date 2017年5月31日
 */
@Service
public class ProcessEndEventListener implements ApplicationListener<ProcessEndEvent>, Ordered {
	
	public void onApplicationEvent(ProcessEndEvent event) {
		IProcessRun processRun = (IProcessRun)event.getSource();
		ExecutionEntity ent = event.getExecutionEntity();

		ITaskThreadService taskThreadService= AppUtil.getBean(ITaskThreadService.class);
		IProcessCmd processCmd = taskThreadService.getProcessCmdP();
		String actdefId = processCmd.getActDefId();
		String nodeId = event.getExecutionEntity().getActivityId();
		IEventUtilService eventUtilService= AppUtil.getBean(IEventUtilService.class);
		eventUtilService.publishNodeSqlEvent(actdefId, nodeId, INodeSql.ACTION_END, (Map)processCmd.getTransientVar("mainData"));
	}

	public int getOrder() {
		return 1;
	}
}