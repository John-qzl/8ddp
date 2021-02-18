package com.cssrc.ibms.core.flow.job;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.flow.dao.ReminderStateDao;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;

public class RemindSettingImpl extends IRemindMsgSend
{
    
    @Override
    public void handMsg(ProcessTask task, TaskReminder taskReminder) throws Exception
    {           
        
        Set<Long> userSet = getUserByTask(task);
        Date curDate = new Date();
        ReminderStateDao reminderStateDao = AppUtil.getBean(ReminderStateDao.class);
        ICalendarAssignService calendarAssignService =
            (ICalendarAssignService)AppUtil.getBean(ICalendarAssignService.class);
        String taskId = task.getId();
        // 需要提醒的次数
        int needRemindTimes = taskReminder.getTimes().intValue();
        // 催办开始时间(相对于任务创建时间，多少工作日)
        int reminderStart = taskReminder.getReminderStart().intValue();
        // 催办持续时间(相对于催办开始时间) 这里其实是催办发送的间隔时间
        int interval = taskReminder.getReminderEnd().intValue();
        // 结束时间是按照
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
            // 已经催办了用户次数统计
            int reminderTimes =
                reminderStateDao.getAmountByUserTaskId(Long.parseLong(taskId), userId.longValue(), 1).intValue();
            // 超过设置的催办次数
            if (reminderTimes >= needRemindTimes)
            {
                continue;
            }
            if(!this.exeCondition(taskReminder.getCondExp(),task,userId)) {
                logger.debug("Skip TaskReminder :" + taskReminder.getTaskDueId() + "," + taskReminder.getName());
                return;
            }
            // 根据已经催办的次数意见催办间隔时间，计算当期催办发送时间与任务创建时间的时间差
            int start = reminderStart + interval * reminderTimes;
            // 当期催办消息发送时间
            Date dueDate = null;
            if (new Integer(1).equals(taskReminder.getRelativeTimeType()))
            {
                // 相对时间类型，1为日历日
                dueDate = new Date(TimeUtil.getNextTime(1, start, startTime.getTime()));
            }
            else
            {
                // 相对时间类型，0为工作日
                dueDate = calendarAssignService.getTaskTimeByUser(startTime, start, userId.longValue());
            }
            if (dueDate.compareTo(curDate) > 0)
            {
                // 还没到催办时间
                continue;
            }
            Date completeDate = null;
            
            // 计算催办结束时间，相对于任务创建时间或者任务结束时间
            int deadLine = taskReminder.getCompleteTime().intValue();
            if (new Integer(1).equals(taskReminder.getRelativeTimeType()))
            {
                completeDate = new Date(TimeUtil.getNextTime(1, deadLine, startTime.getTime()));
            }
            else
            {
                completeDate = calendarAssignService.getTaskTimeByUser(startTime, deadLine, userId.longValue());
            }
            /* 任务过期 不发送催办 */
            if (deadLine != 0 && completeDate.compareTo(curDate) < 0)
            {
                // 已经过了结束时间
                continue;
            }
            Map<String, Object> vars=this.getVars(task);
            // 发送消息
            sendMsg(userId, taskReminder, task, completeTime, vars);
            // 添加催办记录
            saveReminderState(taskId, actDefId, actInstanceId, userId, 3, dueDate);
        }
    }
   
}
