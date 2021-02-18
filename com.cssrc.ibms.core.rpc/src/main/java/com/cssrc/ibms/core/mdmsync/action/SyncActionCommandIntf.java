package com.cssrc.ibms.core.mdmsync.action;

import com.cssrc.ibms.core.mdmsync.comand.CommandQueueIntf;

/**
 * 命令队列执行接口
 * 
 * @ClassName: SyncActionCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:40:46
 * 
 */
public interface SyncActionCommandIntf
{
    public void actionPerformed(CommandQueueIntf queue);
}
