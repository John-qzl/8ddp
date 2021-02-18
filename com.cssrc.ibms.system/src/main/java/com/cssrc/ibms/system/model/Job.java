package com.cssrc.ibms.system.model;

import java.util.Date;

import com.cssrc.ibms.api.job.model.IJob;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 职务
 * <p>Title:Job</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:00:52
 */
public class Job extends BaseModel implements IJob{
	@SysFieldDescription(detail="职务ID")
	protected Long jobid;
	@SysFieldDescription(detail="职务名称")
	protected String jobname;
	@SysFieldDescription(detail="职务代码")
	protected String jobcode;
	@SysFieldDescription(detail="职务描述")
	protected String jobdesc;
	@SysFieldDescription(detail="设置码，对应组织ID")
	protected Long setid = Long.valueOf(0L);
	@SysFieldDescription(detail="是否删除",maps="{\"1\":\"已删除\",\"0\":\"未删除\"}")
	protected Long isdelete = Long.valueOf(0L);
	@SysFieldDescription(detail="是否公共职务",maps="{\"1\":\"独有职务\",\"0\":\"公共职务\"}")
	protected Short grade = Short.valueOf((short) 0); //默认为0是公共职务（例如每个部门都有老板这个职务），1是该组织独有职务
	@SysFieldDescription(detail="创建人ID")
	protected Long job_creatorId;// 创建人ID
	@SysFieldDescription(detail="创建时间")
	protected Date job_createTime;// 创建时间
	@SysFieldDescription(detail="更改人ID")
	protected Long job_updateId;// 更改人ID
	@SysFieldDescription(detail="更改时间")
	protected Date job_updateTime;// 更改时间
	
	public Long getJob_creatorId() {
		return job_creatorId;
	}

	public void setJob_creatorId(Long job_creatorId) {
		this.job_creatorId = job_creatorId;
	}

	public Date getJob_createTime() {
		return job_createTime;
	}

	public void setJob_createTime(Date job_createTime) {
		this.job_createTime = job_createTime;
	}

	public Long getJob_updateId() {
		return job_updateId;
	}

	public void setJob_updateId(Long job_updateId) {
		this.job_updateId = job_updateId;
	}

	public Date getJob_updateTime() {
		return job_updateTime;
	}

	public void setJob_updateTime(Date job_updateTime) {
		this.job_updateTime = job_updateTime;
	}

	public void setJobid(Long jobid) {
		this.jobid = jobid;
	}

	public Long getJobid() {
		return this.jobid;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobname() {
		return this.jobname;
	}

	public void setJobcode(String jobcode) {
		this.jobcode = jobcode;
	}

	public String getJobcode() {
		return this.jobcode;
	}

	public void setJobdesc(String jobdesc) {
		this.jobdesc = jobdesc;
	}

	public String getJobdesc() {
		return this.jobdesc;
	}

	public void setSetid(Long setid) {
		this.setid = setid;
	}

	public Long getSetid() {
		return this.setid;
	}

	public void setIsdelete(Long isdelete) {
		this.isdelete = isdelete;
	}

	public Long getIsdelete() {
		return this.isdelete;
	}

	public Short getGrade() {
		return this.grade;
	}

	public void setGrade(Short grade) {
		this.grade = grade;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Job)) {
			return false;
		}
		Job rhs = (Job) object;
		return new EqualsBuilder().append(this.jobid, rhs.jobid).append(
				this.jobname, rhs.jobname).append(this.jobcode, rhs.jobcode)
				.append(this.jobdesc, rhs.jobdesc)
				.append(this.setid, rhs.setid).append(this.isdelete,
						rhs.isdelete).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.jobid)
				.append(this.jobname).append(this.jobcode).append(this.jobdesc)
				.append(this.setid).append(this.isdelete).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("jobid", this.jobid).append(
				"jobname", this.jobname).append("jobcode", this.jobcode)
				.append("jobdesc", this.jobdesc).append("setid", this.setid)
				.append("isdelete", this.isdelete).toString();
	}
}
