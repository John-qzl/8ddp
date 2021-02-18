package com.cssrc.ibms.core.flow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
/**
 * 任务分配时执行事件
 * @author zhulongchao
 *
 */
public class TaskAssignListener extends BaseTaskListener {
	
	
	
	private TaskOpinionService taskOpinionService=(TaskOpinionService)AppUtil.getBean(TaskOpinionService.class);
	
	@Override
	protected void execute(DelegateTask delegateTask, String actDefId,
			String nodeId) {
		String userId=delegateTask.getAssignee();
		logger.debug("任务ID:" + delegateTask.getId());
		TaskOpinion taskOpinion=taskOpinionService.getByTaskId(new Long(delegateTask.getId()));
		if(taskOpinion!=null){
			logger.debug("update taskopinion exe userId" + userId);
			
			taskOpinion.setExeUserId(Long.parseLong(userId));
			taskOpinionService.update(taskOpinion);
		}
		
		delegateTask.setOwner(userId);

		
	}

	@Override
	protected int getScriptType() {
		 
		return BpmConst.AssignScript;
	}

    @Override
    protected void handlerExtTaskExeCallBack(TaskEntity taskEnt)
    {
        
    }

}
