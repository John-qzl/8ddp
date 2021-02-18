package com.cssrc.ibms.api.form.model;

import java.util.List;

import com.cssrc.ibms.api.system.model.BaseSerialNumber;

public class BaseFormTableXml implements IFormTableXml
{
    private List<BaseSerialNumber> serialNumberList;
    
    public List<BaseSerialNumber> getSerialNumberList()
    {
        return serialNumberList;
    }
    
    public void setSerialNumberList(List<BaseSerialNumber> serialNumberList)
    {
        this.serialNumberList = serialNumberList;
    }
    
}
