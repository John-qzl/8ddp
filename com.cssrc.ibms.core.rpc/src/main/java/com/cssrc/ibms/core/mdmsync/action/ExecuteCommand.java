package com.cssrc.ibms.core.mdmsync.action;

import com.cssrc.ibms.core.mdmsync.comand.CommandQueue;
import com.cssrc.ibms.core.mdmsync.comand.SyncCommand;
import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/**
 * 命令执行工具类
 * 
 * @ClassName: ExecuteCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:41:48
 * 
 */
public class ExecuteCommand
{
    public static void exe(SyncCommand<? extends ISyncMdmDataService> com)
    {
        Actualize actu = new Actualize();
        CommandQueue queue = new CommandQueue();
        queue.addCommand(com);
        actu.setCommandQueue(queue);
        actu.exeAction(new SyncActionCommand());
    }
}
