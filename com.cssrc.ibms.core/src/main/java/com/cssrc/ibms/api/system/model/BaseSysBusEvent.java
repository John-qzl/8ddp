package com.cssrc.ibms.api.system.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;


public class BaseSysBusEvent extends BaseModel implements ISysBusEvent
{
    private static final long serialVersionUID = 7406830006768117964L;
    private Long id;
    private String formkey;
    private String preScript;
    private String afterScript;
    
    public Long getId()
    {
        return id;
    }
    public void setFormkey(String formkey)
    {
        this.formkey = formkey;
    }
    public String getFormkey()
    {
        return formkey;
    }
    public String getPreScript()
    {
        return preScript;
    }
    public String getAfterScript()
    {
        return afterScript;
    }

}
