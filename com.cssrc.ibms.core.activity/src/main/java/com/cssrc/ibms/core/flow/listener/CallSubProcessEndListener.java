package com.cssrc.ibms.core.flow.listener;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.transformer.StringToInteger;
import org.activiti.engine.runtime.ProcessInstance;

import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.dao.ProStatusDao;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 外部子流程启动事件监听器。
 * <pre>
 * 	1.用于修改子流程节点的状态为正在结束。
 *  2.在子流程调用任务结束后，将人员变量删除。
 * </pre>
 * @author zhulongchao
 *
 */
public class CallSubProcessEndListener extends BaseNodeEventListener
{
    
    private ProStatusDao bpmProStatusDao = AppUtil.getBean(ProStatusDao.class);
    
    // private ExecutionDao executionDao=(ExecutionDao)AppUtil.getBean(ExecutionDao.class);
    private RuntimeService runtimeService = AppUtil.getBean(RuntimeService.class);
    
    private DefinitionService definitionService = AppUtil.getBean(DefinitionService.class);
    
    @Override
    protected void execute(DelegateExecution execution, String actDefId, String nodeId)
    {
        Long processInstanceId = new Long(execution.getProcessInstanceId());
        ExecutionEntity ent = (ExecutionEntity)execution;
        String varName = ent.getActivityId() + "_" + BpmConst.SUBPRO_EXT_MULTI_USERIDS;
        
        boolean rtn = BpmUtil.isMuiltiExcetion(ent);
        if (rtn)
        {
            int completeInstance = (Integer)execution.getVariable("nrOfCompletedInstances");
            int nrOfInstances = (Integer)execution.getVariable("nrOfInstances");
            if (completeInstance == nrOfInstances)
            {
                bpmProStatusDao.updStatus(processInstanceId, nodeId, TaskOpinion.STATUS_AGREE);
                execution.removeVariable(varName);
                // executionDao.delById(processInstanceId.toString());
                /*
                 * 这里为什么删除子流程实例， 子流程实例必须结束才会到主流程， 就算删除，代码也有bug，如果两个分支都有子流程，其中一个结束，会把另外一个子流程的所有实例都删除，
                 * 所以需要加上子流程的actdefId过滤删除，但是如果加上过滤， 查找子流程实例应该是空的，所以应该不需要删除。
                 */
                // deleteInstanceBySupperInstanceId(execution, actDefId, nodeId);
            }
        }
        else
        {
            bpmProStatusDao.updStatus(processInstanceId, nodeId, TaskOpinion.STATUS_AGREE);
            execution.removeVariable(varName);
            /*
             * 这里为什么删除子流程实例， 子流程实例必须结束才会到主流程， 就算删除，代码也有bug，如果两个分支都有子流程，其中一个结束，会把另外一个子流程的所有实例都删除，
             * 所以需要加上子流程的actdefId过滤删除，但是如果加上过滤， 查找子流程实例应该是空的，所以应该不需要删除。
             */
            // deleteInstanceBySupperInstanceId(execution, actDefId, nodeId);
        }
        
    }
    
    private void deleteInstanceBySupperInstanceId(DelegateExecution execution, String actDefId, String nodeId)
    {
        Long processInstanceId = new Long(execution.getProcessInstanceId());
        String subDefKey = NodeCache.getNodeByActNodeId(actDefId, nodeId).getAttribute("subFlowKey");
        String subActDefId = definitionService.getByDefKey(subDefKey).getActDefId();
        List<ProcessInstance> subInstances = runtimeService.createProcessInstanceQuery()
            .superProcessInstanceId(processInstanceId.toString())
            .processDefinitionKey(subActDefId)
            .list();
        for (ProcessInstance instance : subInstances)
        {
            runtimeService.deleteProcessInstance(instance.getProcessInstanceId(), "结束子流程");
        }
    }
    
    @Override
    protected Integer getScriptType()
    {
        // TODO Auto-generated method stub
        return BpmConst.EndScript;
    }
    
}
