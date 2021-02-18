/**
 * 
 */
package com.cssrc.ibms.core.job.quartz;

/**
 * trigger 列表对象
 * @see
 */
public class TriggerObject implements java.io.Serializable{
    /**
     * trigger name
     * */
	private String name;
  
    /**
     * trgger cron
     * */
	private String cron;
  
	/**
	 * trgger status
	 * */
	private String status;
  
	/**
	 * trgger group
	 * */
	private String group;
	
	/**
	 * job name
	 * */
	private String jobName;
	
	/**
	 * job group
	 * */
	private String jobGroup;
	
	/**
	 * cron表达式的描述
	 * */
	private String cronDescription;

	public String getCronDescription() {
		return cronDescription;
	}

	public void setCronDescription(String cronDescription) {
		this.cronDescription = cronDescription;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cron
	 */
	public String getCron() {
		return cron;
	}

	/**
	 * @param cron the cron to set
	 */
	public void setCron(String cron) {
		this.cron = cron;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * @return the jobGroup
	 */
	public String getJobGroup() {
		return jobGroup;
	}

	/**
	 * @param jobGroup the jobGroup to set
	 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
  
  
}
