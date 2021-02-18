package com.cssrc.ibms.api.system.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class BaseSerialNumber extends BaseModel implements ISerialNumber
{
    private static final long serialVersionUID = -9020513527687363134L;
    
    protected Long id = Long.valueOf(0L);
    
    protected String name;
    
    protected String alias;
    
    protected String curDate = "";
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    public String getCurDate()
    {
        return curDate;
    }
    
    public void setCurDate(String curDate)
    {
        this.curDate = curDate;
    }
    
}
