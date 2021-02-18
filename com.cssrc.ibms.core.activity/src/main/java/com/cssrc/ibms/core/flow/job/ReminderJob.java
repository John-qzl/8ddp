package com.cssrc.ibms.core.flow.job;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.flow.dao.ReminderStateDao;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.dao.TaskReminderDao;
import com.cssrc.ibms.core.flow.model.TaskReminder;
import com.cssrc.ibms.core.job.quartz.BaseJob;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 催办任务
 * ReminderJob
 * @author liubo
 * @date 2017年5月10日
 */
public class ReminderJob extends BaseJob
{
    public static final int Remind = 1;
    
    public static final int Complete = 2;
    
    private Log logger = LogFactory.getLog(ReminderJob.class);
    
    public void executeJob(JobExecutionContext context)
        throws Exception
    {
        TaskReminderDao taskReminderDao = AppUtil.getBean(TaskReminderDao.class);
        ReminderStateDao reminderStateDao = AppUtil.getBean(ReminderStateDao.class);
        TaskDao taskDao = AppUtil.getBean(TaskDao.class);
        reminderStateDao.delExpiredTaskReminderState();
        List<ProcessTask> list = taskDao.getReminderTask();
        if (list.size() == 0)
        {
            this.logger.debug("没有获取到催办任务!");
            return;
        }
        // 循环所有的催办任务进行相对应的处理
        for (ProcessTask task : list)
        {
            String actDefId = task.getProcessDefinitionId();
            String nodeId = task.getTaskDefinitionKey();
            List<TaskReminder> taskReminders = taskReminderDao.getByActDefAndNodeId(actDefId, nodeId);
            for (TaskReminder taskReminder : taskReminders)
            {
                IRemindMsgSend msgSend=null;
                if (StringUtil.isNotEmpty(taskReminder.getQuarzCron()))
                {
                    msgSend=new RemindCronImpl();
                }
                else if (taskReminder.getTimes().intValue() > 0)
                {
                    msgSend=new RemindSettingImpl();
                }
                
                msgSend.sendMsg(task, taskReminder);
            }
        }
    }
    
  
}