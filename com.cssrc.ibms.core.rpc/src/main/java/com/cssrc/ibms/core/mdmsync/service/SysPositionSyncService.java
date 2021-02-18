package com.cssrc.ibms.core.mdmsync.service;

import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.sysuser.model.IPosition;

@Service("sysPositionSyncService")
public class SysPositionSyncService extends AbsSyncMdmDataService<IPosition> implements ISyncMdmDataService<IPosition>
{
    
    @Override
    public String getFiledIdVal(IPosition pos)
    {
        return pos.getPosId().toString();
    }
    
    @Override
    public String getFiledNameVal(IPosition pos)
    {
        return pos.getPosName().toString();
    }
    
}
