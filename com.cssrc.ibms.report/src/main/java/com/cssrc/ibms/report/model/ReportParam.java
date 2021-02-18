package com.cssrc.ibms.report.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.cssrc.ibms.api.report.model.IReportParam;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class ReportParam extends BaseModel implements IReportParam
{
    private static final long serialVersionUID = 7964658761823301837L;
    
    // 主键
    private Long paramid;
    
    // 报表ID
    private Long reportid;
    
    // 报表参数名
    private String name;
    
    // 报表参数描述
    private String descp;
    
    // 报表参数描述
    private String value_;
    
    // 参数类型
    private String paramtype;
    
    // 参数长度
    private Long paramSize;
    
    // 报表参数创建时间
    private Date createtime;
    
    // 报表参数更新时间
    private Date updatetime;
    
    public ReportParam()
    {
    }
    
    public ReportParam(String name, String val)
    {
        this.name = name;
        this.value_ = val;
        this.paramtype="text";
    }
    
    public ReportParam(String name)
    {
        this.name = name;
        this.paramtype="text";
    }
    
    public Long getParamid()
    {
        return paramid;
    }
    
    public void setParamid(Long paramid)
    {
        this.paramid = paramid;
    }
    
    public Long getReportid()
    {
        return reportid;
    }
    
    public void setReportid(Long reportid)
    {
        this.reportid = reportid;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    public String getDescp()
    {
        return descp;
    }
    
    public void setDescp(String descp)
    {
        this.descp = descp == null ? null : descp.trim();
    }
    
    public Date getCreatetime()
    {
        return createtime;
    }
    
    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }
    
    public Date getUpdatetime()
    {
        return updatetime;
    }
    
    public void setUpdatetime(Date updatetime)
    {
        this.updatetime = updatetime;
    }
    
    public String getValue_()
    {
        return value_;
    }
    
    public void setValue_(String value_)
    {
        this.value_ = value_;
    }
    
    public String getParamtype()
    {
        return paramtype;
    }
    
    public void setParamtype(String paramtype)
    {
        this.paramtype = paramtype;
    }
    
    public Long getParamSize()
    {
        return paramSize;
    }
    
    public void setParamSize(Long paramSize)
    {
        this.paramSize = paramSize;
    }
    
    public boolean equals(Object obj)
    {
        
        
        if (this == obj)
        {
            return true;
        }
        if(obj==null){
            return false;
        }
        if(obj.getClass()!=this.getClass()){
            return false;
        }
        ReportParam o=(ReportParam)obj;
        if (o != null && this.name != null && o.getName() != null && o.getName().equals(this.name))
        {
            return true;
        }
        return new EqualsBuilder().append(this.paramid, o.paramid)
            .append(this.reportid, o.reportid)
            .append(this.name, o.name)
            .append(this.descp, o.descp)
            .append(this.value_, o.value_)
            .append(this.paramtype, o.paramtype)
            .append(this.paramSize, o.paramSize)
            .append(this.createtime, o.createtime)
            .append(this.updatetime, o.updatetime)
            .isEquals();
    }
    
}