package com.cssrc.ibms.api.activity.intf;

import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.cssrc.ibms.api.activity.model.ITaskExecutor;

public interface IExtTaskExecuteService
{
    /** 
    * @Title: createTaskCallBack 
    * @Description: TODO(task 创建 成功后回调接口) 
    * @param @param excution 流程实例信息
    * @param @param taskEnt task 任务实例信息
    * @param @param businessKey 业务数据主键
    * @param @return     
    * @return List<? extends ITaskExecutor>    返回类型 
    * @throws 
    */
    public List<? extends ITaskExecutor> createTaskCallBack(ExecutionEntity excution, TaskEntity taskEnt,
        String businessKey);

    /** 
    * @Title: completeTaskCallBack 
    * @Description: TODO(task 任务完成 扩展接口) 
    * @param @param excution 当前流程实例
    * @param @param taskEnt 当前task 任务实例
    * @param @param businessKey 当前业务数据主键
    * @param @param status 操作类型，比如驳回，同意等
    * @param @param destNode 当前任务完成后下一任务节点Id,比如 task2 驳回 task1 当前任务为task2 ,destNode就是 task1
    * @return void    返回类型 
    * @throws 
    */
    public void completeTaskCallBack(ExecutionEntity excution, TaskEntity taskEnt, String businessKey, Short status,
        List<String> destNode);
}
