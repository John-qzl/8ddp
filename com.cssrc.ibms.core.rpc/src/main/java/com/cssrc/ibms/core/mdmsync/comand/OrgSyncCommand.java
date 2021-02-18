package com.cssrc.ibms.core.mdmsync.comand;

import com.cssrc.ibms.core.mdmsync.service.SysOrgSyncService;

/**
 * 组织 数据同步 command 执行类
 * 
 * @ClassName: OrgSyncCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:26:26
 * 
 */
public class OrgSyncCommand extends SyncCommand<SysOrgSyncService>
{
    
    public OrgSyncCommand()
    {
        super(SysOrgSyncService.class);
    }
    
    public OrgSyncCommand(Object data)
    {
        super(SysOrgSyncService.class, data);
    }
    
    @Override
    public Integer[] getConType()
    {
        return ORG_SELECTOT;
    }
    
}
