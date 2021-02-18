package com.cssrc.ibms.core.mdmsync.comand;

import com.cssrc.ibms.core.mdmsync.service.SysRoleSyncService;

/**
 * 
 * 角色 数据同步 command 执行类
 * 
 * @ClassName: RoleSyncCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:27:10
 * 
 */
public class RoleSyncCommand extends SyncCommand<SysRoleSyncService>
{
    
    public RoleSyncCommand()
    {
        super(SysRoleSyncService.class);
    }
    
    public RoleSyncCommand(Object data)
    {
        super(SysRoleSyncService.class, data);
    }
    
    @Override
    public Integer[] getConType()
    {
        return ROLE_SELECTOT;
    }
    
}
