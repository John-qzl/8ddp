package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * 新闻评论Model层
 * @author YangBo
 *
 */
public class InsNewsCm {
	@SysFieldDescription(detail="评论Id")
	private Long commId; //评论Id
	@SysFieldDescription(detail="新闻Id")
	private Long newId; //新闻Id
	@SysFieldDescription(detail="评论人名")
	private String fullName; //评论人名
	@SysFieldDescription(detail="内容")
	private String content;
	@SysFieldDescription(detail="赞同与顶次数")
	private Integer agreeNums; //赞数
	@SysFieldDescription(detail="反对与鄙视次数")
	private Integer refuseNums; //踩数
	@SysFieldDescription(detail="是否为回复",maps="{\"1\":\"是\",\"0\":\"否\"}")
	private String isReply;
	@SysFieldDescription(detail="回复评论Id")
	private Long repId;//回复评论Id
	@SysFieldDescription(detail="组织ID")
	private String orgId;
	@SysFieldDescription(detail="创建人")
	private String createBy;
	@SysFieldDescription(detail="创建时间")
	private Date createTime;
	@SysFieldDescription(detail="更新人")
	private String updateBy;
	@SysFieldDescription(detail="更新时间")
	private Date updateTime;
	@SysFieldDescription(detail="所属新闻标题")
	protected String newsTitle;//所属新闻标题

	public Long getCommId() {
		return commId;
	}

	public void setCommId(Long commId) {
		this.commId = commId;
	}
	
	public Serializable getPkId()
	{
		return this.commId;
	}

	public void setPkId(Serializable pkId)
	{
		this.commId = ((Long)pkId);
	}

	public Long getNewId() {
		return newId;
	}

	public void setNewId(Long newId) {
		this.newId = newId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName == null ? null : fullName.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public Integer getAgreeNums() {
		return agreeNums;
	}

	public void setAgreeNums(Integer agreeNums) {
		this.agreeNums = agreeNums;
	}

	public Integer getRefuseNums() {
		return refuseNums;
	}

	public void setRefuseNums(Integer refuseNums) {
		this.refuseNums = refuseNums;
	}

	public String getIsReply() {
		return isReply;
	}

	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}

	public Long getRepId() {
		return repId;
	}

	public void setRepId(Long repId) {
		this.repId = repId;
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
	
	

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof InsNewsCm)) {
			return false;
		}
		InsNewsCm rhs = (InsNewsCm)object;
		return new EqualsBuilder().append(this.commId, rhs.commId).append(this.fullName, rhs.fullName).append(this.content, rhs.content).append(this.agreeNums, rhs.agreeNums).append(this.refuseNums, rhs.refuseNums).append(this.isReply, rhs.isReply).append(this.repId, rhs.repId).append(this.orgId, rhs.orgId).append(this.createBy, rhs.createBy)
				.append(this.createTime, rhs.createTime).append(this.updateBy, rhs.updateBy).append(this.updateTime, rhs.updateTime).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973).append(this.commId).append(this.fullName).append(this.content).append(this.agreeNums).append(this.refuseNums).append(this.isReply).append(this.repId).append(this.orgId).append(this.createBy).append(this.createTime).append(this.updateBy).append(this.updateTime).toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("commId", this.commId).append("fullName", this.fullName).append("content", this.content).append("agreeNums", this.agreeNums).append("refuseNums", this.refuseNums).append("isReply", this.isReply).append("repId", this.repId).append("orgId", this.orgId).append("createBy", this.createBy).append("createTime", this.createTime)
				.append("updateBy", this.updateBy).append("updateTime", this.updateTime).toString();
	}

}