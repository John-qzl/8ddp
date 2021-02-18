package com.cssrc.ibms.api.form.intf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDataTemplatefilterService
{
    public static Map<String,Object> usermethods=new HashMap<String,Object>();
    /**
     * 根据用户查询 用户负责组织下的所有用户
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    public abstract List<?> getUserByUser(String judgeVal,Long currentUserId);

    /**
     * 获取分管领导下部门的所有用户
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    public abstract List<?> getUserByLeaderUser(String judgeVal, Long currentUserId);

    /**
     * 查询当前用户所属组织子组织下的所有用户
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    public abstract List<?> getUserByUserOrg(String judgeVal, Long currentUserId);

    /**
     * 所属角色和当前用户角色相同
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    public abstract List<?> getUserByUserRole(String judgeVal, Long currentUserId);

    /**
     * 获取所有下属用户
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    public abstract List<?> getUnderUser(String judgeVal, Long userId);
    
    
    public List invoke(String judgeCon, String judgeVal,Long userId);
}
