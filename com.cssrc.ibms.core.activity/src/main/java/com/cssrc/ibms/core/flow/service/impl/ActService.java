package com.cssrc.ibms.core.flow.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.activity.BaseProcessService;
import com.cssrc.ibms.core.flow.dao.DefVarDao;
import com.cssrc.ibms.core.flow.intf.IActService;
import com.cssrc.ibms.core.flow.model.DefVar;
import com.cssrc.ibms.core.flow.service.cmd.EndProcessCmd;
import com.cssrc.ibms.core.flow.service.cmd.GetExecutionCmd;


@Service
public class ActService extends BaseProcessService implements IActService {
	
	@Resource
	private DefVarDao dao;
	public ActService() {
		
	}
	
	@Override
	public List<DefVar> getVarsByFlowDefId(Long defId) {
		return dao.getVarsByFlowDefId(defId);
	}
	
	
	@Override
	public ExecutionEntity getExecution(String executionId) {
		return commandExecutor.execute(new GetExecutionCmd(executionId)) ;
	}
	
	/**
	 * 根据任务ID结束流程。
	 * @param taskId
	 */
	public void endProcessByTaskId(String taskId){
		EndProcessCmd cmd=new EndProcessCmd(taskId);
		commandExecutor.execute(cmd);
	}


}
