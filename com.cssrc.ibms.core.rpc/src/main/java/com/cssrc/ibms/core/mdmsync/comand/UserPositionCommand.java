package com.cssrc.ibms.core.mdmsync.comand;

import com.cssrc.ibms.core.mdmsync.service.UserPositionSyncService;

public class UserPositionCommand extends SyncCommand<UserPositionSyncService>
{
    
    public UserPositionCommand(Class<UserPositionSyncService> t)
    {
        super(t);
    }
    
    public UserPositionCommand(Object data)
    {
        super(UserPositionSyncService.class, data);
    }
    
    @Override
    public Integer[] getConType()
    {
        return USER_SELECTOT;
        
    }
    
}
