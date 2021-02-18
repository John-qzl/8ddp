package com.cssrc.ibms.core.mdmsync.service;


import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;

@Service("sysOrgSyncService")
public class SysOrgSyncService extends AbsSyncMdmDataService<ISysOrg> implements ISyncMdmDataService<ISysOrg>
{
    
    @Override
    public String getFiledIdVal(ISysOrg org)
    {
        return org.getOrgId().toString();
    }
    
    @Override
    public String getFiledNameVal(ISysOrg org)
    {
        return org.getOrgName().toString();
    }
    
    
}
