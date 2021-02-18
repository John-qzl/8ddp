package com.cssrc.ibms.core.flow.service.skipimpl;

import javax.annotation.Resource;
import org.activiti.engine.task.Task;
import com.cssrc.ibms.core.flow.intf.ISkipCondition;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;

/**
 * @ClassName: ApproveUserSkipCondition
 * @Description: TODO(之前审批过后跳过)
 * @author zxg
 * @date 2017年7月13日 下午6:12:01
 * 
 */
public class ApproveUserSkipCondition implements ISkipCondition
{
    @Resource
    TaskOpinionService taskOpinionService;
    
    public boolean canSkip(Task task)
    {
        String assignee = task.getAssignee();
        
        String actInstId = task.getProcessInstanceId();
        String taskKey = task.getTaskDefinitionKey();
        TaskOpinion opinion = this.taskOpinionService.getLatestTaskOpinion(Long.valueOf(actInstId), taskKey);
        if (opinion != null && assignee.equals(opinion.getExeUserId().toString()))
        {
            Short status = opinion.getCheckStatus();
            switch (status.shortValue())
            {
                case 3://拒绝
                    return false;
                case 1://同意
                case 0://弃权
                case 2://反对
                    return true;
            }
            
        }
        
        return false;
    }
    
    public String getTitle()
    {
        return "之前审批过后跳过";
    }
}
