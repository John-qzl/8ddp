package com.cssrc.ibms.core.form.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IPkValue;
import com.cssrc.ibms.api.form.model.IRelTable;
import com.cssrc.ibms.api.form.model.ISubTable;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.sync.intf.ISyncRelTableDataService;

@Service("syncRelTableDataService")
public class SyncRelTableDataService implements ISyncRelTableDataService
{
    @Resource
    private IMessageProducer messageProducer;

    @Override
    public void handlerSyncData(IFormData formData)
    {
        IPkValue pkValue=formData.getPkValue();
        boolean isAdd=pkValue.getIsAdd();
        if(isAdd){
            return;
        }
        HashMap<String,Object> topicData=new HashMap<String,Object>();
        //仅构建需要的数据，避免消息数据过多无用数据
        List<?extends ISubTable> subTables=formData.getSubTableList();
        List<?extends IRelTable> relTables=formData.getRelTableList();
        //子表需要同步的数据
        for(ISubTable st:subTables){
            topicData.put(st.getTableId()+"."+st.getTableName(), st.getDataList());
        }
        //关联表表需要同步的数据
        for(IRelTable rt:relTables){
            topicData.put(rt.getRelFormTable().getTableId()+"."+rt.getRelTableName(), rt.getRelTableDataList());
        }
        //主表需要同步的数据
        topicData.put(formData.getTableId()+"."+formData.getTableName(), formData.getMainFields());
        try{
            messageProducer.sendTopic(topicData);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void handlerSyncData(String tableId, String tableName, Map<String, Object> formData)
    {
        HashMap<String,Object> topicData=new HashMap<String,Object>();
        topicData.put(tableId+"."+tableName, formData);
        messageProducer.sendTopic(topicData);
    }

    @Override
    public void handlerSyncData(IFormTable table, Map<String, Object> formData)
    {
        HashMap<String,Object> topicData=new HashMap<String,Object>();
        topicData.put(table.getTableId()+"."+table.getTableName(), formData);
        messageProducer.sendTopic(topicData);
    
    }
    
    
    
}
