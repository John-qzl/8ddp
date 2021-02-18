package com.cssrc.ibms.core.mdmsync;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SyncRpcMdmDataMsgRecive
{
    
    protected Logger logger = LoggerFactory.getLogger(SyncRpcMdmDataMsgRecive.class);
    
    public void syncData(Serializable mdmData)
    {        
        
        SyncCommand<? extends ISyncMdmDataService> com=CommandFactory.creatCommand(mdmData);
        ExecuteCommand.exe(com);
    
    }

   
}
