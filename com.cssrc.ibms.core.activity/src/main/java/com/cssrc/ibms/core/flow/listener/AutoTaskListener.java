package com.cssrc.ibms.core.flow.listener;

import org.activiti.engine.delegate.DelegateExecution;

import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.ProStatusService;
import com.cssrc.ibms.core.util.appconf.AppUtil;


public class AutoTaskListener extends BaseNodeEventListener {

	@Override
	protected void execute(DelegateExecution execution, String actDefId,
			String nodeId) {
		ProStatusService bpmProStatusService=(ProStatusService)AppUtil.getBean("proStatusService");
		Long actInstanceId=Long.parseLong(execution.getProcessInstanceId());
		/**
		 * 记录节点执行状态。
		 */
		bpmProStatusService.addOrUpd(actDefId, actInstanceId,nodeId,TaskOpinion.STATUS_EXECUTED);
		
	}

	@Override
	protected Integer getScriptType() {
		
		return BpmConst.StartScript;
	}

}
