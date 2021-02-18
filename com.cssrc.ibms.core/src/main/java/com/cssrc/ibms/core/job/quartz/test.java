/**
 * 
 */
package com.cssrc.ibms.core.job.quartz;

import org.quartz.JobExecutionContext;

/**
 *  所有定时计划类需要继承BaseJob基类，并实现executeJob方法即可
 * @see
 */
public class test extends BaseJob {

	@Override
	public void executeJob(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub
        System.out.print("dfs\n");
	}




}
