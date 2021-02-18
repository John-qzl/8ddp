package com.cssrc.ibms.core.mdmsync.comand;

import java.util.List;

import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/**
 * 数据同步命令 执行队列 接口类
 * 
 * @ClassName: CommandQueue
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:25:38
 * 
 */
public interface CommandQueueIntf
{
    public void addCommand(SyncCommand<? extends ISyncMdmDataService> command);
    
    public void moveCommand(SyncCommand<? extends ISyncMdmDataService> command);
    
    public List<SyncCommand<? extends ISyncMdmDataService>> getQueue();
    
}
