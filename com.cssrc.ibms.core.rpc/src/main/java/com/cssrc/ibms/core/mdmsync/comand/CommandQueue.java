package com.cssrc.ibms.core.mdmsync.comand;

import java.util.ArrayList;
import java.util.List;

import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/**
 * 数据同步命令 执行队列 实现类
 * 
 * @ClassName: CommandQueueImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:25:56
 * 
 */
public class CommandQueue implements CommandQueueIntf
{
    private List<SyncCommand<? extends ISyncMdmDataService>> queue = new ArrayList<SyncCommand<?>>();
    
    public List<SyncCommand<? extends ISyncMdmDataService>> getQueue()
    {
        return queue;
    }
    
    public void setQueue(List<SyncCommand<? extends ISyncMdmDataService>> queue)
    {
        this.queue = queue;
    }
    
    @Override
    public void addCommand(SyncCommand<? extends ISyncMdmDataService> command)
    {
        queue.add(command);
    }
    
    @Override
    public void moveCommand(SyncCommand<? extends ISyncMdmDataService> command)
    {
        queue.remove(command);
    }
    
}
