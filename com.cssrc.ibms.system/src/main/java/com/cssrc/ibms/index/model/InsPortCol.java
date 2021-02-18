package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 布局显示管理Model
 * @author YangBo
 *
 */
public class InsPortCol {
    private Long confId;

    private Long portId;

    private Long colId;

    private Integer width;

    private Integer height;

    private String widthUnit;

    private String heightUnit;

    private Integer colNum;

    private Integer sn;

    private String orgId;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    public Long getConfId() {
        return confId;
    }

    public void setConfId(Long confId) {
        this.confId = confId ;
    }
    
    public Serializable getPkId()
    {
    	return this.confId;
    }

    public void setPkId(Serializable pkId)
    {
    	this.confId = ((Long)pkId);
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId ;
    }

    public String getColId() {
        if(colId!=null){
            return colId.toString();
        }else{
            return null;
        }
    }

    public void setColId(Long colId) {
        this.colId = colId;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(String widthUnit) {
        this.widthUnit = widthUnit == null ? null : widthUnit.trim();
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit == null ? null : heightUnit.trim();
    }

    public Integer getColNum() {
        return colNum;
    }

    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
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
    
    public InsPortCol(){
    	
    }
    public InsPortCol(InsPortal insPortal){
    	this.portId = insPortal.getPortId();
    	this.createBy = insPortal.getCreateBy();
    	this.createTime = new Date();
    	this.updateBy = insPortal.getCreateBy();
    	this.updateTime = new Date();
    	this.orgId = insPortal.getOrgId();
    }
    public boolean equals(Object object)
    {
    	if (!(object instanceof InsPortCol)) {
    		return false;
    	}
    	InsPortCol rhs = (InsPortCol)object;
    	return new EqualsBuilder().append(this.confId, rhs.confId)
    			.append(this.width, rhs.width).append(this.height, rhs.height)
    			.append(this.widthUnit, rhs.widthUnit)
    			.append(this.heightUnit, rhs.heightUnit)
    			.append(this.sn, rhs.sn).append(this.orgId, rhs.orgId)
    			.append(this.createBy, rhs.createBy)
    			.append(this.createTime, rhs.createTime)
    			.append(this.updateBy, rhs.updateBy)
    			.append(this.updateTime, rhs.updateTime).isEquals();
    }

    public int hashCode()
    {
    	return new HashCodeBuilder(-82280557, -700257973).append(this.confId)
    			.append(this.width).append(this.height).append(this.widthUnit)
    			.append(this.heightUnit).append(this.sn).append(this.orgId)
    			.append(this.createBy).append(this.createTime)
    			.append(this.updateBy).append(this.updateTime).toHashCode();
    }

    public String toString()
    {
    	return new ToStringBuilder(this).append("confId", this.confId)
    			.append("width", this.width).append("height", this.height)
    			.append("widthUnit", this.widthUnit)
    			.append("heightUnit", this.heightUnit).append("sn", this.sn)
    			.append("orgId", this.orgId)
    			.append("createBy", this.createBy)
    			.append("createTime", this.createTime)
    			.append("updateBy", this.updateBy)
    			.append("updateTime", this.updateTime).toString();
    }
}