package com.cssrc.ibms.core.mdmsync.action;

import java.util.List;

import com.cssrc.ibms.core.mdmsync.comand.CommandQueueIntf;
import com.cssrc.ibms.core.mdmsync.comand.SyncCommand;
import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/**
 * 命令队列执行接口 实现类
 * 
 * @ClassName: SyncActionCommandImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:41:10
 * 
 */
public class SyncActionCommand implements SyncActionCommandIntf
{
    
    public SyncActionCommand()
    {
    }
    
    @Override
    public void actionPerformed(CommandQueueIntf queue)
    {
        List<SyncCommand<? extends ISyncMdmDataService>> list = queue.getQueue();
        for (SyncCommand<? extends ISyncMdmDataService> command : list)
        {
            command.sync();
        }
    }
    
}
