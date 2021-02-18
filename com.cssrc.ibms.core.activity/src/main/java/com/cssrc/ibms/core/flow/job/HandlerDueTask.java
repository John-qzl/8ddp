package com.cssrc.ibms.core.flow.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.dao.ReminderStateDao;
import com.cssrc.ibms.core.flow.intf.IActService;
import com.cssrc.ibms.core.flow.model.ReminderState;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;

public class HandlerDueTask
{
    private static Logger logger = Logger.getLogger(HandlerDueTask.class);

    /**
     * 任务到期动作处理
     * @param task
     * @param taskReminder
     * @param userSet
     * @throws Exception
     */
    public void handlerDueTask(ProcessTask task, TaskReminder taskReminder, Set<Long> userSet)
        throws Exception
    {
        ICalendarAssignService calendarAssignService =
            (ICalendarAssignService)AppUtil.getBean(ICalendarAssignService.class);
        ReminderStateDao reminderStateDao = (ReminderStateDao)AppUtil.getBean(ReminderStateDao.class);
        GroovyScriptEngine scriptEngine = (GroovyScriptEngine)AppUtil.getBean("scriptEngine");
        IActService processService = (IActService)AppUtil.getBean("actService");
        ProcessRunService processRunService = (ProcessRunService)AppUtil.getBean(ProcessRunService.class);
        String taskId = task.getId();
        String actDefId = task.getProcessDefinitionId();
        String actInstanceId = task.getProcessInstanceId();
        Date relativeStartTime = calendarAssignService
            .getRelativeStartTime(actInstanceId, taskReminder.getRelativeNodeId(), taskReminder.getRelativeNodeType());
        
        if (relativeStartTime == null)
        {
            return;
        }
        
        if (new Integer(0).equals(taskReminder.getRelativeTimeType()))
        {
            Date dueDate = new Date(
                TimeUtil.getNextTime(1, taskReminder.getCompleteTime().intValue(), relativeStartTime.getTime()));
            Date curDate = new Date();
            if (dueDate.compareTo(curDate) <= 0)
            {
                handlerAction(taskReminder, processRunService, task, processService, scriptEngine);
                saveReminderState(taskId, actDefId, actInstanceId, null, 2, null);
            }
        }
        else if (userSet.size() > 0)
        {
            for (Iterator<Long> it = userSet.iterator(); it.hasNext();)
            {
                Long userId = it.next();
                
                int reminderTimes = reminderStateDao.getAmountByTaskId(Long.parseLong(taskId), 2).intValue();
                Date dueDate = calendarAssignService.getTaskTimeByUser(relativeStartTime,
                    taskReminder.getCompleteTime().intValue(),
                    userId.longValue());
                
                if (reminderTimes > 0)
                {
                    continue;
                }
                
                Date curDate = new Date();
                if (dueDate.compareTo(curDate) > 0)
                    continue;
                handlerAction(taskReminder, processRunService, task, processService, scriptEngine);
                
                saveReminderState(taskId, actDefId, actInstanceId, userId, 2, null);
                break;
            }
        }
        else
        {
            int reminderTimes = reminderStateDao.getAmountByTaskId(Long.parseLong(taskId), 2).intValue();
            if (reminderTimes == 0)
            {
                Date dueDate = calendarAssignService
                    .getTaskTimeByUser(relativeStartTime, taskReminder.getCompleteTime().intValue(), 0L);
                
                Date curDate = new Date();
                if (dueDate.compareTo(curDate) <= 0)
                {
                    handlerAction(taskReminder, processRunService, task, processService, scriptEngine);
                    
                    saveReminderState(taskId, actDefId, actInstanceId, null, 2, null);
                }
            }
        }
    }
    
    /**
     * 执行到期动作
     * @param taskReminder
     * @param processRunService
     * @param task
     * @param processService
     * @param scriptEngine
     * @throws Exception
     */
    private void handlerAction(TaskReminder taskReminder, ProcessRunService processRunService, ProcessTask task,
        IActService processService, GroovyScriptEngine scriptEngine)
        throws Exception
    {
        Integer action = taskReminder.getAction();
        String taskId = task.getId();
        ProcessCmd processCmd = new ProcessCmd();
        processCmd.setTaskId(taskId);
        switch (action.intValue())
        {
            case 1:
                processCmd.setVoteAgree(new Short("1"));
                processRunService.nextProcess(processCmd, true);
                logger.debug("对该任务执行同意操作");
                break;
            case 2:
                processCmd.setVoteAgree(new Short("2"));
                processRunService.nextProcess(processCmd, false);
                logger.debug("对该任务执行反对操作");
                break;
            case 3:
                processCmd.setVoteAgree(new Short("3"));
                processCmd.setBack(Integer.valueOf(1));
                processRunService.nextProcess(processCmd, false);
                logger.debug("对该任务执行驳回操作");
                break;
            case 4:
                processCmd.setVoteAgree(new Short("3"));
                processCmd.setBack(Integer.valueOf(2));
                processRunService.nextProcess(processCmd, false);
                logger.debug("对该任务执行驳回到发起人操作");
                break;
            case 6:
                processService.endProcessByTaskId(task.getId());
                logger.debug("结束流程");
                break;
            case 7:
                String script = taskReminder.getScript();
                Map<String,Object> vars = new HashMap<String,Object>();
                vars.put("task", task);
                scriptEngine.execute(script, vars);
                logger.debug("执行指定的脚本");
            case 5:
        }
    }
    
    /**
     * 添加催办记录
     * @param taskId
     * @param actDefId
     * @param actInstanceId
     * @param userId
     * @param remindType
     * @throws Exception
     */
    private void saveReminderState(String taskId, String actDefId, String actInstanceId, Long userId, int remindType,
        Date dueDate)
        throws Exception
    {
        ReminderStateDao reminderStateDao = (ReminderStateDao)AppUtil.getBean(ReminderStateDao.class);
        ReminderState reminderState = new ReminderState();
        reminderState.setId(Long.valueOf(UniqueIdUtil.genId()));
        reminderState.setActInstanceId(actInstanceId);
        reminderState.setUserId(userId);
        reminderState.setActDefId(actDefId);
        reminderState.setTaskId(taskId);
        reminderState.setRemindType(remindType);
        reminderState.setCreatetime(new Date());
        reminderState.setReminderTime(dueDate);
        reminderStateDao.add(reminderState);
    }
}
