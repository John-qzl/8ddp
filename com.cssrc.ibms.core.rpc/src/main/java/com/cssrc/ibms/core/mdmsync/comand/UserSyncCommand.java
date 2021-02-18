package com.cssrc.ibms.core.mdmsync.comand;

import com.cssrc.ibms.core.mdmsync.service.SysUserSyncService;

/**
 * 用户 数据同步 command 执行类
 * 
 * @ClassName: UserSyncCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:27:45
 * 
 */
public class UserSyncCommand extends SyncCommand<SysUserSyncService>
{
    
    public UserSyncCommand()
    {
        super(SysUserSyncService.class);
    }
    
    public UserSyncCommand(Object data)
    {
        super(SysUserSyncService.class, data);
    }
    
    @Override
    public Integer[] getConType()
    {
        return USER_SELECTOT;
    }
    
}
