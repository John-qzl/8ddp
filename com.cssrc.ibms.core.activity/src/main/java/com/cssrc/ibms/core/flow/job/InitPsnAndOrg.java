package com.cssrc.ibms.core.flow.job;

import com.cssrc.ibms.core.job.quartz.BaseJob;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

/**
 * User :  sgl.
 * Date : 2019/2/19.
 * Time : 14:35.
 */
public class InitPsnAndOrg extends BaseJob {
    private static final String TAG = "InitPsnAndOrg";

    private Log logger = LogFactory.getLog(ReminderJob.class);

    @Override
    public void executeJob(JobExecutionContext context) throws Exception {


    }
}
