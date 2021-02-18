package com.cssrc.ibms.core.mdmsync.service;


import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.sysuser.model.ISysRole;

@Service("sysRoleSyncService")
public class SysRoleSyncService extends AbsSyncMdmDataService<ISysRole> implements ISyncMdmDataService<ISysRole>
{
    
    @Override
    public String getFiledIdVal(ISysRole role)
    {
        return role.getRoleId().toString();
    }
    
    @Override
    public String getFiledNameVal(ISysRole role)
    {
        return role.getRoleName().toString();
    }
    
}
