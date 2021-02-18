package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 布局管理对象
 * @author YangBo
 *
 */
public class InsPortal {
    private Long portId;

    private String name;

    private String key;

    private Integer colNums;

    private String colWidths; //栏目宽

    private String isDefault;

    private String desc;

    private String userId;

    private String orgId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    protected String orgName;

    private String layoutInfo;

	public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId ;
    }
    
    public Serializable getPkId()
    {
    	return this.portId;
    }

    public void setPkId(Serializable pkId)
    {
    	this.portId = ((Long)pkId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    public Integer getColNums() {
        return colNums;
    }

    public void setColNums(Integer colNums) {
        this.colNums = colNums;
    }

    public String getColWidths() {
        return colWidths;
    }

    public void setColWidths(String colWidths) {
        this.colWidths = colWidths == null ? null : colWidths.trim();
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? null : isDefault.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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
    
    
    public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getLayoutInfo() {
		return layoutInfo;
	}

	public void setLayoutInfo(String layoutInfo) {
		this.layoutInfo = layoutInfo;
	}

	public boolean equals(Object object)
    {
    	if (!(object instanceof InsPortal)) {
    		return false;
    	}
    	InsPortal rhs = (InsPortal)object;
    	return new EqualsBuilder().append(this.portId, rhs.portId)
    			.append(this.name, rhs.name).append(this.key, rhs.key)
    			.append(this.isDefault, rhs.isDefault)
    			.append(this.desc, rhs.desc)
    			.append(this.orgId, rhs.orgId)
    			.append(this.createBy, rhs.createBy)
    			.append(this.createTime, rhs.createTime)
    			.append(this.updateBy, rhs.updateBy)
    			.append(this.orgName, rhs.orgName)
    			.append(this.updateTime, rhs.updateTime)
    			.append(this.layoutInfo, rhs.layoutInfo).isEquals();
    }

    public int hashCode()
    {
    	return new HashCodeBuilder(-82280557, -700257973).append(this.portId)
    			.append(this.name).append(this.key).append(this.isDefault)
    			.append(this.desc).append(this.orgId).append(this.createBy)
    			.append(this.createTime).append(this.updateBy).append(this.orgName)
    			.append(this.updateTime).append(this.layoutInfo).toHashCode();
    }

    public String toString()
    {
    	return new ToStringBuilder(this).append("portId", this.portId)
    			.append("name", this.name).append("key", this.key)
    			.append("isDefault", this.isDefault).append("desc", this.desc)
    			.append("orgId", this.orgId)
    			.append("createBy", this.createBy)
    			.append("createTime", this.createTime)
    			.append("updateBy", this.updateBy)
    			.append("orgName", this.orgName)
    			.append("updateTime", this.updateTime)
    			.append("layoutInfo", this.layoutInfo).toString();
    }
}