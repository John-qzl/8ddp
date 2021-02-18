package com.cssrc.ibms.core.mdmsync.comand;

import com.cssrc.ibms.core.mdmsync.service.SysPositionSyncService;

/**
 * 岗位 数据同步 command 执行类
 * 
 * @ClassName: PositionSyncCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:26:51
 * 
 */
public class PositionSyncCommand extends SyncCommand<SysPositionSyncService>
{
    
    public PositionSyncCommand()
    {
        super(SysPositionSyncService.class);
    }
    
    public PositionSyncCommand(Object data)
    {
        super(SysPositionSyncService.class, data);
    }
    
    @Override
    public Integer[] getConType()
    {
        return POS_SELECTOT;
    }
    
}
