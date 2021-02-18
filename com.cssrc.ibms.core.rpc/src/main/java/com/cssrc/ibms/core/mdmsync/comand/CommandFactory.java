package com.cssrc.ibms.core.mdmsync.comand;

import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;

/**
 * command 工厂类
 * 
 * @ClassName: CommandFactory
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月19日 上午8:27:11
 * 
 */
public class CommandFactory
{
    public enum CommandEmum
    {
        USER(ISysUser.class, UserSyncCommand.class), 
        ORG(ISysOrg.class, OrgSyncCommand.class), 
        ROLE(ISysRole.class, UserSyncCommand.class), 
        POSITION(IPosition.class, PositionSyncCommand.class);
        
        private Class<?> model;
        
        private Class<? extends SyncCommand<? extends ISyncMdmDataService>> command;
        
        CommandEmum(Class<?> model, Class<? extends SyncCommand<? extends ISyncMdmDataService>> command)
        {
            this.model = model;
            this.command = command;
        }
        
        public Class<?> getModel()
        {
            return model;
        }
        
        public void setModel(Class<?> model)
        {
            this.model = model;
        }
        
        public Class<?> getCommand()
        {
            return command;
        }
        
        public void setCommand(Class<? extends SyncCommand<? extends ISyncMdmDataService>> command)
        {
            this.command = command;
        }
        
        public static Class<? extends SyncCommand<? extends ISyncMdmDataService>> getCommand(Object model){
            CommandEmum[] commands=CommandEmum.values();
            for(CommandEmum com:commands){
                if(com.model.isInstance(model)){
                    return com.command;
                }
            }
            
            return null;
        }
        
    }
    
    public static SyncCommand<? extends ISyncMdmDataService> creatCommand(Object mdmData)
    {
        if (mdmData instanceof ISysUser)
        {
            return new UserSyncCommand(mdmData);
        }
        else if (mdmData instanceof ISysOrg)
        {
            return new OrgSyncCommand(mdmData);
        }
        else if (mdmData instanceof ISysRole)
        {
            return new RoleSyncCommand(mdmData);
        }
        else if (mdmData instanceof IPosition)
        {
            return new PositionSyncCommand(mdmData);
        }
        else if (mdmData instanceof IUserPosition)
        {
            return new UserPositionCommand(mdmData);
        }
        return null;
        
    }
}
