package com.cssrc.ibms.core.mdmsync.service;

import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;

@Service("userPositionSyncService")
public class UserPositionSyncService extends AbsSyncMdmDataService<IUserPosition> implements ISyncMdmDataService<IUserPosition>
{

    @Override
    public String getFiledIdVal(IUserPosition userPosition)
    {
        return userPosition.getUserId().toString();
    }

    @Override
    public String getFiledNameVal(IUserPosition userPosition)
    {
        return null;
    }
    
    
    @Override
    public String getUpDateSql(IFormTable formTable, IFormField f, IUserPosition mdmData,String id,String[] fnameVals)
    {
        if(filter!=null){
            return filter.getUpDateSql(formTable,f,mdmData,id,fnameVals);
        }else{
            return "";
        }
       
    }
}
