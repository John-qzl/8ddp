package com.cssrc.ibms.core.job.scheduler;

import java.util.Date;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cssrc.ibms.api.log.intf.ISysJobLogService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
/**
 * 
 * <p>Title:BaseJob</p>
 * @author Yangbo 
 * @date 2016-8-4下午05:33:31
 */
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public abstract void executeJob(JobExecutionContext paramJobExecutionContext)
			throws Exception;

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String jobName = context.getJobDetail().getKey().getName();

		String trigName = "directExec";
		Trigger trig = context.getTrigger();
		if (trig != null)
			trigName = trig.getKey().getName();
		Date strStartTime = new Date();
		long startTime = System.currentTimeMillis();
		try {
			executeJob(context);
			long endTime = System.currentTimeMillis();
			Date strEndTime = new Date();

			long runTime = (endTime - startTime) / 1000L;
			addLog(jobName, trigName, strStartTime, strEndTime, runTime,
					"任务执行成功!", 1);
		} catch (Exception ex) {
			long endTime = System.currentTimeMillis();
			Date strEndTime = new Date();
			long runTime = (endTime - startTime) / 1000L;
			try {
				addLog(jobName, trigName, strStartTime, strEndTime, runTime, ex
						.toString(), 0);
			} catch (Exception e) {
				e.printStackTrace();
				this.log.error("执行任务出错:" + e.getMessage());
			}
		}
	}

	private void addLog(String jobName, String trigName, Date strStartTime,
			Date strEndTime, long runTime, String content, int state)
			throws Exception {
		ISysJobLogService logService = (ISysJobLogService) AppUtil
				.getBean(ISysJobLogService.class);
		logService.addLog(jobName, trigName,
				strStartTime, strEndTime, runTime, content, state);
	}
}
