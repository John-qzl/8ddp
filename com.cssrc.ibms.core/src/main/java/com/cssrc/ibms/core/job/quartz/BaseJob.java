package com.cssrc.ibms.core.job.quartz;

import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cssrc.ibms.api.log.intf.IQuartzLogService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 任务执行基类，这个类采用模版模式进行实现。 <br>
 * 子类继承这个类后，任务执行的日志就会自动记录下来， <br>
 * 不需要在子类中在进行记录。
 * 
 */
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private String jobName = "";
	private String trigName = "";
	private Date strStartTime;

	public BaseJob() {
	}

	public abstract void executeJob(JobExecutionContext context)
			throws Exception;

	/**
	 * trigger执行方法入口
	 * */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// JobDataMap map= context.getJobDetail().getJobDataMap();
		jobName = context.getJobDetail().getKey().getName();
		trigName = "directExec";
		Trigger trig = context.getTrigger();
		if (trig != null)
			trigName = trig.getKey().getName();
		strStartTime = new Date();
		try {
			executeJob(context);
			// 记录日志
			addTriggerLog(jobName, trigName, strStartTime,"任务执行成功!", 1);
		} catch (Exception ex) {
            log.error("执行任务出错:" + ex.getMessage());
			try {
				addTriggerLog(jobName, trigName, strStartTime,"任务执行失败："+ex.toString(), 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("执行任务出错:" + e.getMessage());
			}
		}
	}

	/**
	 * 记录trigger日志
	 * ***/
	protected void addTriggerLog(String jobName, String trigName, Date strStartTime,String content, int state) throws Exception {
		Date strEndTime = new Date();
		long runTime = (strEndTime.getTime() - strStartTime.getTime()) / 1000;
		IQuartzLogService quartzLogService = (IQuartzLogService) AppUtil
				.getBean(IQuartzLogService.class);
		quartzLogService.addLog(content, trigName, jobName, runTime, strEndTime,
				state, strEndTime);
	}

}
