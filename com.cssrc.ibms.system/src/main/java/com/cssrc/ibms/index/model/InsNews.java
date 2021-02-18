package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * 新闻公告Model层
 * @author YangBo
 *
 */
public class InsNews {
	@SysFieldDescription(detail="ID")
    private Long newId;
	@SysFieldDescription(detail="标题")
    private String subject;
	@SysFieldDescription(detail="标签")
    private String tag;
	@SysFieldDescription(detail="关键字")
    private String keywords;
	@SysFieldDescription(detail="是否为图片新闻",maps="{\"1\":\"是\",\"0\":\"否\"}")
    private String isImg;
	@SysFieldDescription(detail="图片文件ID")
    private String imgFileId;
	@SysFieldDescription(detail="阅读次数")
    private Integer readTimes;
	@SysFieldDescription(detail="作者")
    private String author;
	@SysFieldDescription(detail="是否允许评论",maps="{\"1\":\"允许\",\"0\":\"不允许\"}")
    private String allowCmt;
	@SysFieldDescription(detail="状态")
    private String status;
	@SysFieldDescription(detail="组织ID")
    private String orgId;
	@SysFieldDescription(detail="创建人ID")
    private String createBy;
	@SysFieldDescription(detail="创建时间")
    private Date createTime;
	@SysFieldDescription(detail="更改人")
    private String updateBy;
	@SysFieldDescription(detail="更改时间")
    private Date updateTime;
	@SysFieldDescription(detail="内容")
    private String content;

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId ;
    }
    
    public Serializable getPkId()
    {
    	return this.newId;
    }

    public void setPkId(Serializable pkId)
    {
    	this.newId = ((Long)pkId);
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getIsImg() {
        return isImg;
    }

    public void setIsImg(String isImg) {
        this.isImg = isImg == null ? null : isImg.trim();
    }

    public String getImgFileId() {
        return imgFileId;
    }

    public void setImgFileId(String imgFileId) {
        this.imgFileId = imgFileId == null ? null : imgFileId.trim();
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getAllowCmt() {
        return allowCmt;
    }

    public void setAllowCmt(String allowCmt) {
        this.allowCmt = allowCmt == null ? null : allowCmt.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
    
    
    public boolean equals(Object object)
    {
    	if (!(object instanceof InsNews)) {
    		return false;
    	}
    	InsNews rhs = (InsNews)object;
    	return new EqualsBuilder().append(this.newId, rhs.newId).append(this.subject, rhs.subject).append(this.content, rhs.content).append(this.imgFileId, rhs.imgFileId).append(this.readTimes, rhs.readTimes).append(this.author, rhs.author).append(this.status, rhs.status).append(this.orgId, rhs.orgId).append(this.createBy, rhs.createBy)
    			.append(this.createTime, rhs.createTime).append(this.updateBy, rhs.updateBy).append(this.updateTime, rhs.updateTime).isEquals();
    }

    public int hashCode()
    {
    	return new HashCodeBuilder(-82280557, -700257973).append(this.newId).append(this.subject).append(this.content).append(this.imgFileId).append(this.readTimes).append(this.author).append(this.status).append(this.orgId).append(this.createBy).append(this.createTime).append(this.updateBy).append(this.updateTime).toHashCode();
    }

    public String toString()
    {
    	return new ToStringBuilder(this).append("newId", this.newId).append("subject", this.subject).append("content", this.content).append("imgFileId", this.imgFileId).append("readTimes", this.readTimes).append("author", this.author).append("status", this.status).append("orgId", this.orgId).append("createBy", this.createBy).append("createTime", this.createTime)
    			.append("updateBy", this.updateBy).append("updateTime", this.updateTime).toString();
    }
}