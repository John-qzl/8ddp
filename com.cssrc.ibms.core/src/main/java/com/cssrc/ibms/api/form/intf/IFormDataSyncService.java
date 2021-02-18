package com.cssrc.ibms.api.form.intf;

import java.util.Map;

public interface IFormDataSyncService
{
    
    /**
     * 根据map数据进行数据关联数据的更新
     * @param data
     */
    public void handelData(Map<String,Object> data);
}
