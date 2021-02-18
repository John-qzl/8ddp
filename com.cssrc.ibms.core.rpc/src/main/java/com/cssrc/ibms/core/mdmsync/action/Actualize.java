package com.cssrc.ibms.core.mdmsync.action;

import com.cssrc.ibms.core.mdmsync.comand.CommandQueueIntf;

/**
 * 命令执行封装类
 * 
 * @ClassName: Actualize
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:40:30
 * 
 */
public class Actualize
{
    private CommandQueueIntf queue;
    
    public Actualize()
    {
    }
    
    /**
     * 设置命令执行队列
     * 
     * @param queue
     */
    public void setCommandQueue(CommandQueueIntf queue)
    {
        this.queue = queue;
    }
    
    /**
     * 执行命令
     * 
     * @param action
     */
    public void exeAction(SyncActionCommandIntf action)
    {
        action.actionPerformed(queue);
    }
    
}
