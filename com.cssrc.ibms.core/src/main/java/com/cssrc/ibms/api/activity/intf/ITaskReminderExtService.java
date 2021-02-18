package com.cssrc.ibms.api.activity.intf;

import java.util.Date;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public interface ITaskReminderExtService
{

    /** 
    * @Title: sendMsgExtCallBack 
    * @Description: TODO(task 催办消息发送后回调接口) 
    * @param @param excution 流程实例
    * @param @param taskEnt task 任务实例
    * @param @param businessKey 业务数据主键
    * @param @param userId 催办人
    * @param @param remindType 消息类型 1：邮件 3:站内信 4:手机短信
    * @param @param dueDate  发送时间
    * @return void    返回类型 
    * @throws 
    */
    void sendMsgExtCallBack(ExecutionEntity excution, TaskEntity taskEnt, String businessKey, Long userId,
        int remindType, Date dueDate);
    

}



