package com.cssrc.ibms.api.rpc.intf;

import java.util.HashMap;

public class CommonParamBean extends HashMap<String, Object> implements ICommonParam
{
    
    private static final long serialVersionUID = 5945672382635218930L;
    
    public CommonParamBean()
    {
        
    }
    public CommonParamBean(long userId)
    {
        this.addUser(userId);
    }
    public CommonParamBean(long userId,String userField)
    {
        this.addUser(userId);
        this.addUserField(userField);
    }
    
    public CommonParamBean(long userId,String userField,String alias)
    {
        this.addUser(userId);
        this.addUserField(userField);
        this.addDataTemplateAlias(alias);
    }
    
    public void addUser(Long userId)
    {
        this.put(ICommonParam.KEY_USER, userId);
    }
    
    public void addUserField(String userField)
    {
        this.put(ICommonParam.KEY_USERFIELD, userField);
        
    }
    
    public void addDataTemplateAlias(String templateAlias)
    {
        this.put(ICommonParam.KEY_TEMPLATEALIAS, templateAlias);
        
    }
    
    @Override
    public Object getUser()
    {
        return this.get(ICommonParam.KEY_USER);
    }
    @Override
    public Object getUserField()
    {
        return this.get(ICommonParam.KEY_USERFIELD);
    }
    @Override
    public Object getTemplate()
    {
        return this.get(ICommonParam.KEY_TEMPLATEALIAS);
    }
    
}
