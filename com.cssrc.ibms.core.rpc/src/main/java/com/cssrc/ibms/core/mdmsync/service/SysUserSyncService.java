package com.cssrc.ibms.core.mdmsync.service;


import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

@Service("sysUserSyncService")
public class SysUserSyncService extends AbsSyncMdmDataService<ISysUser> implements ISyncMdmDataService<ISysUser>
{
    
    public String getFiledIdVal(ISysUser user)
    {
        return user.getUserId().toString();
    }
    
    public String getFiledNameVal(ISysUser user)
    {
        return user.getFullname().toString();
    }
    
    
}
