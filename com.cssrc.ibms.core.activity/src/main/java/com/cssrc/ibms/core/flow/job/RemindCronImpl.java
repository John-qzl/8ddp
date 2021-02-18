package com.cssrc.ibms.core.flow.job;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.quartz.impl.triggers.CronTriggerImpl;

import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.flow.dao.ReminderStateDao;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;

public class RemindCronImpl extends IRemindMsgSend
{
    
    @Override
    public void handMsg(ProcessTask task, TaskReminder taskReminder) throws Exception
    {
        //需要发送 消息 的用户
        Set<Long> userSet = getUserByTask(task);

        //流程变量
        Map<String, Object> vars=this.getVars(task);
        
        ICalendarAssignService calendarAssignService =AppUtil.getBean(ICalendarAssignService.class);
        String taskId = task.getId();
        //催办开始时间(相对于任务创建时间，多少工作日)
        //结束时间
        String completeTime = taskReminder.getCompleteTime() + "分钟";
        String actDefId = task.getProcessDefinitionId();
        String actInstanceId = task.getProcessInstanceId();
        // 节点任务创建时间
        Date startTime = calendarAssignService
            .getRelativeStartTime(actInstanceId, taskReminder.getRelativeNodeId(), taskReminder.getRelativeNodeType());
        if (startTime == null)
        {
            return;
        }
        
        for (Long userId : userSet)
        {
            if(userId==null) {
                continue;
            }
            if(!this.exeCondition(taskReminder.getCondExp(), task,userId)) {
                logger.debug("Skip TaskReminder :" + taskReminder.getTaskDueId() + "," + taskReminder.getName());
                continue;
            }
            Date nextDate=this.getNextSendTime(taskReminder, task, userId);
            // 根据已经催办的次数意见催办间隔时间，计算当期催办发送时间与任务创建时间的时间差
            nextDate = DateUtil.parseDate(nextDate, "yyyy-MM-dd hh:mm");
            Date curDate = DateUtil.getCurrentDateDate("yyyy-MM-dd hh:mm");
            if (nextDate.compareTo(curDate) > 0)
            {
                // 还没到催办时间
                continue;
            }
            // 发送消息
            sendMsg(userId, taskReminder, task, completeTime, vars);
            // 添加催办记录
            saveReminderState(taskId, actDefId, actInstanceId, userId, 1, nextDate);
            //发送消息后扩展接口
            this.sendMsgExtCallBack(taskId, actDefId, actInstanceId, userId, 1, nextDate);
        }
        
    }
    


    public Date getNextSendTime(TaskReminder taskReminder,ProcessTask task,Long userId) {
        try
        {
            ReminderStateDao reminderStateDao =AppUtil.getBean(ReminderStateDao.class);
            //催办开始时间(相对于任务创建时间，多少工作日)
            int reminderStart = taskReminder.getReminderStart().intValue();
            String quarzCron = taskReminder.getQuarzCron();
            Long taskId=Long.parseLong(task.getId());
            Date nextDate = null;
            CronTriggerImpl cronTrigger = new CronTriggerImpl();
            cronTrigger.setCronExpression(quarzCron);
            Date lastReminder = reminderStateDao.getLastByUserTaskId(taskId, userId, 1);
           
            Date curDate = DateUtil.getCurrentDateDate("yyyy-MM-dd hh:mm");

            if (lastReminder != null)
            {
                nextDate = cronTrigger.getFireTimeAfter(lastReminder);
            }
            else
            {
                //还没有发送催办消息
                Date now = DateUtil.getCurrentDateDate("yyy-MM-dd 00:00:00");
                nextDate = cronTrigger.getFireTimeAfter(now);
                Date start_ = new Date(TimeUtil.getNextTime(1, reminderStart, curDate.getTime()));
                if (nextDate.compareTo(start_) > 0)
                {
                    //已经过了催办而且没有发送催办消息，第一次需要补上发送催办消息，并且发送时间设定为当前时间。
                    nextDate = curDate;
                }
            }
            
            return nextDate;
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
}
