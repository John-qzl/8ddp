package com.cssrc.ibms.api.sync.intf;

import java.util.Map;

import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormTable;

public interface ISyncRelTableDataService
{
    public void handlerSyncData(IFormData formData);
    
    public void handlerSyncData(String tableId,String tableName,Map<String,Object> formData);
   
    public void handlerSyncData(IFormTable table,Map<String,Object> formData);

}
