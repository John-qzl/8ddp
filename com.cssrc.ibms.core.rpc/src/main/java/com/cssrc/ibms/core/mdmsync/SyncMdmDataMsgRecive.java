package com.cssrc.ibms.core.mdmsync;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.rpc.intf.IDubboInterfaceService;
import com.cssrc.ibms.core.mdmsync.action.ExecuteCommand;
import com.cssrc.ibms.core.mdmsync.comand.CommandFactory;
import com.cssrc.ibms.core.mdmsync.comand.SyncCommand;
import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/** 
 * mq 同步消息处理类
* @ClassName: SyncMdmDataMsgRecive 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author zxg 
* @date 2017年5月19日 下午3:00:29 
*  
*/
public class SyncMdmDataMsgRecive
{
    
    @Resource
    IDubboInterfaceService dubboInterfaceService;
    
    /**
     * 数据同步类型 0：本地同步 1：rpc同步 2：本地 rpc 都同步
     */
    private int syncType = 0;
    
    protected Logger logger = LoggerFactory.getLogger(SyncMdmDataMsgRecive.class);
    
    public void syncData(Serializable mdmData)
    {
        logger.info("invoking dubbo service sync data---syncType:"+syncType);
        
        if (syncType == 0)
        {
            // 只同步本地的数据
            localSync(mdmData);
        }
        else if (syncType == 1)
        {
            // 只同步 rpc 远程系统的数据
            rpcSync(mdmData);
        }
        else if (syncType == 2)
        {
            // 本地 rpc 都同步
            allSync(mdmData);
        }
        
        
    }

    private void localSync(Serializable mdmData)
    {
        SyncCommand<? extends ISyncMdmDataService> com=CommandFactory.creatCommand(mdmData);
        ExecuteCommand.exe(com);
    }
    
    private void allSync(Serializable mdmData)
    {
        localSync(mdmData);
        rpcSync(mdmData);
    }
    
    private void rpcSync(Serializable mdmData)
    {
        // 获取所有dubbo服务。
        List<CommonService> services = dubboInterfaceService.getReferenceServiceList(CommonService.class, false);
        for (CommonService service : services)
        {
            //通过 dubbo 服务 远程发送 mq消息。
            service.sendSyncMdmData(mdmData);
        }
    }

    public int getSyncType()
    {
        return syncType;
    }

    public void setSyncType(int syncType)
    {
        this.syncType = syncType;
    }
    
}
