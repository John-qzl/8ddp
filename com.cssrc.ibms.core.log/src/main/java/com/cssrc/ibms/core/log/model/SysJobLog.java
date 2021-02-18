package com.cssrc.ibms.core.log.model;

import java.util.Date;

import com.cssrc.ibms.api.log.model.ISysJobLog;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 后台任务执行日志
 * <p>
 * Title:SysJobLog
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-4下午05:19:01
 */
public class SysJobLog extends BaseModel implements ISysJobLog {
	protected Long logId;
	protected String jobName;
	protected String trigName;
	protected Date startTime;
	protected Date endTime;
	protected String content;
	protected int state;
	protected Long runTime;

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getLogId() {
		return this.logId;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setTrigName(String trigName) {
		this.trigName = trigName;
	}

	public String getTrigName() {
		return this.trigName;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}

	public Long getRunTime() {
		return this.runTime;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysJobLog)) {
			return false;
		}
		SysJobLog rhs = (SysJobLog) object;
		return new EqualsBuilder().append(this.logId, rhs.logId)
				.append(this.jobName, rhs.jobName)
				.append(this.trigName, rhs.trigName)
				.append(this.startTime, rhs.startTime)
				.append(this.endTime, rhs.endTime)
				.append(this.content, rhs.content)
				.append(this.state, rhs.state)
				.append(this.runTime, rhs.runTime).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.logId)
				.append(this.jobName).append(this.trigName)
				.append(this.startTime).append(this.endTime)
				.append(this.content).append(this.state).append(this.runTime)
				.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("logId", this.logId)
				.append("jobName", this.jobName)
				.append("trigName", this.trigName)
				.append("startTime", this.startTime)
				.append("endTime", this.endTime)
				.append("content", this.content).append("state", this.state)
				.append("runTime", this.runTime).toString();
	}
}
