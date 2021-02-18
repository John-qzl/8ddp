package com.cssrc.ibms.api.form.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;


public class BaseFormRights extends BaseModel implements IFormRights
{
    private static final long serialVersionUID = -5869757199383433517L;
    protected Long id;
    protected Long formDefId;
    protected String actDefId = "";
    protected short type = 1;
    protected String permission = "";

    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    public Long getFormDefId()
    {
        return formDefId;
    }
    public void setFormDefId(Long formDefId)
    {
        this.formDefId = formDefId;
    }
    public String getActDefId()
    {
        return actDefId;
    }
    public void setActDefId(String actDefId)
    {
        this.actDefId = actDefId;
    }
    public short getType()
    {
        return type;
    }
    public void setType(short type)
    {
        this.type = type;
    }
    public String getPermission()
    {
        return permission;
    }
    public void setPermission(String permission)
    {
        this.permission = permission;
    }

    
}
