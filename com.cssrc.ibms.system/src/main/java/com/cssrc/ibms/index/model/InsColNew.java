package com.cssrc.ibms.index.model;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 新闻栏目关联信息Model层
 * @author YangBo
 *
 */
public class InsColNew {
	private Long id;

	private Long colId;

	private Long newId;

	private Integer sn;

	private Date startTime;

	private Date endTime;

	private String isLongValid = "20";

	private String orgId;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;
	
	public InsColNew(){
		
	}
	
		
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getColId() {
		return colId;
	}


	public void setColId(Long colId) {
		this.colId = colId;
	}


	public Long getNewId() {
		return newId;
	}


	public void setNewId(Long newId) {
		this.newId = newId;
	}


	public Serializable getPkId()
	{
		return this.id;
	}

	public void setPkId(Serializable pkId)
	{
		this.id = ((Long)pkId);
	}

	

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIsLongValid() {
		return isLongValid;
	}

	public void setIsLongValid(String isLongValid) {
		this.isLongValid = isLongValid == null ? null : isLongValid.trim();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof InsColNew)) {
			return false;
		}
		InsColNew rhs = (InsColNew)object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.sn, rhs.sn).append(this.startTime, rhs.startTime).append(this.endTime, rhs.endTime).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973).append(this.id).append(this.sn).append(this.startTime).append(this.endTime).toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("id", this.id).append("sn", this.sn).append("startTime", this.startTime).append("endTime", this.endTime).toString();
	}
}