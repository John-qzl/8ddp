package com.cssrc.ibms.core.user.event;

import org.springframework.context.ApplicationEvent;

import com.cssrc.ibms.api.sysuser.model.IUserPosition;

public class UserPositionEvent extends ApplicationEvent
{
    private static final long serialVersionUID = 5571590748478367778L;
    
    public static int ACTION_UPD = 1;
    
    public static int ACTION_DEL = 2;
    
    public static int ACTION_ADD = 0;
    
    private Long posId = Long.valueOf(0L);
    
    private int action = ACTION_UPD;
    
    private IUserPosition userPosition;
    
    public UserPositionEvent(Long posId)
    {
        super(posId);
        this.posId = posId;
    }
    
    public Long getPosId()
    {
        return posId;
    }
    
    public void setPosId(Long posId)
    {
        this.posId = posId;
    }
    
    public int getAction()
    {
        return action;
    }
    
    public void setAction(int action)
    {
        this.action = action;
    }
    
    public IUserPosition getUserPosition()
    {
        return userPosition;
    }
    
    public void setUserPosition(IUserPosition userPosition)
    {
        this.userPosition = userPosition;
    }
    
    public String getActionMsg()
    {
        if (ACTION_UPD == action)
        {
            return "update ";
        }
        else if (ACTION_DEL == action)
        {
            return "delete ";
        }
        else if (ACTION_ADD == action)
        {
            return "add ";
        }
        return "action error";
    }
    
}
