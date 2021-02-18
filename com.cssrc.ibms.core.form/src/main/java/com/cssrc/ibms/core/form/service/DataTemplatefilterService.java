package com.cssrc.ibms.core.form.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.cssrc.ibms.api.form.intf.IDataTemplatefilterService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.intf.IUserUnderService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.model.IUserUnder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service("dataTemplatefilterService")
public class DataTemplatefilterService implements IDataTemplatefilterService
{
    @Resource
    ISysOrgService sysOrgService;
    
    @Resource
    ISysUserService sysUserService;
    
    @Resource
    IUserUnderService userUnderService;
    
    @Resource
    ISysRoleService sysRoleService;
    
    @Resource
    IUserPositionService userPositionService;
    
    static
    {
        usermethods.put("5", new Filter("所属组织负责人为当前用户", "getUserByUser", true));
        usermethods.put("6", new Filter("所属组织的分管领导为当前用户", "getUserByLeaderUser", true));
        usermethods.put("7", new Filter("所属组织为当前用户组织的子组织", "getUserByUserOrg", true));
        usermethods.put("8", new Filter("所属角色和当前用户角色相同", "getUserByUserRole", true));
        usermethods.put("9", new Filter("所属角色包含", "getUserByUserRole", true));
        usermethods.put("10", new Filter("上级领导为当前用户", "getUnderUser", true));
        usermethods.put("11", new Filter("所属组织为当前用户所属组织(可指定职务)", "getOrgJobUser", true));
        usermethods.put("12", new Filter("岗位为当前用户岗位", "getUserByUserPos", true));
        usermethods.put("13", new Filter("职务为当前用户职务", "getUserByUserJob", true));
        usermethods.put("14", new Filter("包含职务", "getUserByUserJob", true));
        usermethods.put("15", new Filter("包含岗位", "getUserByUserPos", true));
        usermethods.put("16", new Filter("包含组织", "getUserByOrg", true));
        usermethods.put("17", new Filter("上级组织负责人为当前用户", "getOrgUnderUser", true));

    }
    
    public List invoke(String judgeCon, String judgeVal, Long userId)
    {
        try
        {
            Filter filter = (Filter)usermethods.get(judgeCon);
            Method method = this.getClass().getMethod(filter.getMethod(), String.class, Long.class);
            List result = (List)method.invoke(this, judgeVal, userId);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取用户组织下所有用户
     * 
     * @param userId
     * @param pwd
     */
    
    public List<?> getUserByUser(String judgeVal, Long userId)
    {
        JSONObject juge = JSONObject.fromObject(judgeVal);
        Object recurssion = juge.get("recurssion");
        List users = new ArrayList<>();
        if (recurssion == null)
        {
            return users;
        }
        List<Long> orgs = sysOrgService.getChargeOrg(userId);
        for (Long org : orgs)
        {
            if ("0".equals(recurssion.toString()))
            {
                // 不递归
                users.addAll(sysUserService.getByOrgId(org));
            }
            else
            {
                getUserByOrg(org, users);
            }
            
        }
        if (users.size() == 0)
        {
            users.add(UserContextUtil.getCurrentUser());
        }
        return users;
    }
    
    /**
     * 根据组织查询所有子组织下的用户
     * 
     * @param org
     * @param users
     */
    public void getUserByOrg(Long org, List users)
    {
        users.addAll(sysUserService.getByOrgId(org));
        List<? extends ISysOrg> orgs = this.sysOrgService.getByParentId(org, null);
        if (orgs != null)
        {
            for (ISysOrg sysOrg : orgs)
            {
                getUserByOrg(sysOrg.getOrgId(), users);
            }
        }
        
    }
    
    /**
     * 获取分管领导下部门的所有用户
     * 
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    @Override
    public List<?> getUserByLeaderUser(String judgeVal, Long userId)
    {
        JSONObject juge = JSONObject.fromObject(judgeVal);
        Object recurssion = juge.get("recurssion");
        Object leadertype = juge.get("leadertype");
        List<Long> orgs = null;
        List users = new ArrayList<>();
        
        if (recurssion == null || leadertype == null)
        {
            return users;
        }
        
        if ("all".equals(leadertype))
        {
            // 所有领导
            orgs = this.sysOrgService.getByAllLeader(userId);
        }
        else if ("leader".equals(leadertype))
        {
            // 分管主领导
            orgs = this.sysOrgService.getByLeader(userId);
            
        }
        else if ("viceLeader".equals(leadertype))
        {
            // 分管副领导
            orgs = this.sysOrgService.getByViceLeader(userId);
            
        }
        for (Long org : orgs)
        {
            if ("0".equals(recurssion.toString()))
            {
                // 不递归
                users.addAll(sysUserService.getByOrgId(org));
            }
            else
            {
                getUserByOrg(org, users);
            }
            
        }
        //users.add(sysUserService.newUser(userId));
        return users;
        
    }
    
    /**
     * 查询当前用户所属组织子组织下的所有用户
     * 
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    @Override
    public List<?> getUserByUserOrg(String judgeVal, Long userId)
    {
        JSONObject juge = JSONObject.fromObject(judgeVal);
        Object recurssion = juge.get("recurssion");
        Object demensionId = juge.get("demensionId");
        
        List users = new ArrayList<>();
        if (demensionId == null)
        {
            return null;
        }
        if (recurssion == null)
        {
            recurssion = "0";
        }
        List<? extends ISysOrg> orgs = sysOrgService.getByUserIdAndDemId(userId, Long.valueOf(demensionId.toString()));
        
        List<? extends ISysOrg> subOrgs = new ArrayList<>();
        for (ISysOrg sysOrg : orgs)
        {
            subOrgs.addAll(sysOrgService.getByParentId(sysOrg.getOrgId(), null));
        }
        if (orgs != null)
        {
            for (ISysOrg sysOrg : subOrgs)
            {
                if ("0".equals(recurssion.toString()))
                {
                    // 不递归
                    users.addAll(sysUserService.getByOrgId(sysOrg.getOrgId()));
                }
                else
                {
                    getUserByOrg(sysOrg.getOrgId(), users);
                }
            }
        }
        users.add(sysUserService.newUser(userId));
        return users;
    }
    
    /**
     * 1所属角色和当前用户角色相同
     * 2包含角色
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    @Override
    public List getUserByUserRole(String judgeVal, Long userId)
    {
        List users = new ArrayList();
        
        if (StringUtil.isEmpty(judgeVal))
        {
            //所属角色和当前用户角色相同
            List<? extends ISysRole> roles = this.sysRoleService.getByUserId(userId);
            for (ISysRole role : roles)
            {
                users.addAll(sysUserService.getByRoleId(role.getRoleId()));
            }
            users.add(sysUserService.newUser(userId));
        }
        else
        {
            //包含角色
            String[] roles = judgeVal.split(",");
            for (String roleId : roles)
            {
                users.addAll(sysUserService.getByRoleId(Long.valueOf(roleId)));
            }
        }
        return users;
    }
    
    /**
     * 获取所有下属用户
     * 
     * @param judgeVal
     * @param currentUserId
     * @return
     */
    @Override
    public List<?> getUnderUser(String judgeVal, Long userId)
    {
        JSONObject juge = JSONObject.fromObject(judgeVal);
        Object recurssion = juge.get("recurssion");
        List users = new ArrayList<>();
        if (recurssion == null)
        {
            recurssion = "0";
        }
        List<? extends IUserUnder> underUser = new ArrayList<>();
        this.getUnderUser(underUser, userId, recurssion.toString());
        for (IUserUnder user : underUser)
        {
            users.add(sysUserService.newUser(user.getUnderuserid()));
        }
        users.add(sysUserService.newUser(userId));
        return users;
    }
    
    /**
     * 递归获取用户所有下属用户
     * 
     * @param result
     * @param userId
     * @param recurssion
     * @return
     */
    private List<? extends IUserUnder> getUnderUser(List result, Long userId, String recurssion)
    {
        List<? extends IUserUnder> underUsers = userUnderService.getMyUnderUser(userId);
        result.addAll(underUsers);
        if ("0".equals(recurssion.toString()))
        {
            // 不递归
            return result;
        }
        for (IUserUnder user : underUsers)
        {
            getUnderUser(result, user.getUnderuserid(), recurssion);
        }
        return result;
    }
    
    /**
     * 相同组织并且指定职务 
     * @param judgeVal
     * @param userId
     * @return
     */
    public List<?> getOrgJobUser(String judgeVal, Long userId)
    {
        
        List users = new ArrayList();
        String jobId=null;
        try
        {
            JSONObject juge = JSONObject.fromObject(judgeVal);
            jobId = juge.getString("jobId");
        }
        catch (Exception e)
        {
            
        }
        List<Long> orgs = userPositionService.getOrgIdByUserAndJob(userId, jobId);
        for (Long org : orgs)
        {
            users.addAll(sysUserService.getByOrgId(org));
        }
        users.add(sysUserService.newUser(userId));
        return users;
    }
    
    
    /**
     * 与人员有相同岗位 的相关人员
     * 包含岗位
     * @param judgeVal
     * @param userId
     * @return
     */
    public List<?> getUserByUserPos(String judgeVal, Long userId)
    {
        List users = new ArrayList();
        
        if (StringUtil.isEmpty(judgeVal))
        {
            //与人员有相同岗位 的相关人员
            List<? extends IUserPosition> pos = this.userPositionService.getByUserId(userId);
            for (IUserPosition po : pos)
            {
                users.addAll(sysUserService.getByPosId(po.getPosId()));
            }
            users.add(sysUserService.newUser(userId));
        }
        else
        {
            // 包含岗位
            String[] posIds = judgeVal.split(",");
            for (String posId : posIds)
            {
                users.addAll(sysUserService.getByPosId(Long.valueOf(posId)));
            }
        }
        return users;
    }
    
    /**
     * 职务为当前用户职务
     * 包含职务
     * @param judgeVal
     * @param userId
     * @return
     */
    public List<?> getUserByUserJob(String judgeVal, Long userId)
    {
        List users = new ArrayList();
        
        if (StringUtil.isEmpty(judgeVal))
        {
            //职务为当前用户职务
            List<? extends ISysUser> user = this.sysUserService.getSameJobUsersByUserId(userId);
            users.addAll(user);
            users.add(sysUserService.newUser(userId));
        }
        else
        {
            // 包含职务
            String[] jobIds = judgeVal.split(",");
            for (String jobId : jobIds)
            {
                users.addAll(sysUserService.getByJobId(Long.valueOf(jobId)));
            }
        }
        return users;
    }
    
    
    public List<?> getUserByOrg(String judgeVal, Long userId)
    {
        List users = new ArrayList();
        
        String[] orgIds = judgeVal.split(",");
        for (String org : orgIds)
        {
            users.addAll(sysUserService.getByOrgId(Long.valueOf(org)));
        }
        return users;
    }
    
    
    public List<?>  getOrgUnderUser(String judgeVal, Long userId)
    {
        List users = new ArrayList();
        //{"demId":"10000002540020","stategy":"0","orgType":""}
        Long demId=null;
        int stategy=0;
        String orgType=null;
        try
        {
            JSONObject juge = JSONObject.fromObject(judgeVal);
            demId = juge.getLong("demId");
            stategy = juge.getInt("stategy");
            orgType = juge.getString("orgType");
        }
        catch (Exception e)
        {
            
        }
        List<Long> orgs = sysOrgService.getChargeOrg(userId);
        List<? extends ISysOrg> subOrgs = new ArrayList<>();
        for (Long orgid : orgs)
        {
            subOrgs.addAll(sysOrgService.getByParentIdAndType(orgid, orgType));
        }
        if (orgs != null)
        {
            for (ISysOrg sysOrg : subOrgs)
            {
                if (stategy==0)
                {
                    // 不递归
                    users.addAll(sysUserService.getByOrgId(sysOrg.getOrgId()));
                }
                else
                {
                    getUserByOrgAndType(sysOrg.getOrgId(), users);
                }
            }
        }
        users.add(sysUserService.newUser(userId));
        
        return users;
    }
    
    /**
     * 根据组织查询所有子组织下的用户，指定组织类型
     * @param org
     * @param users
     */
    public void getUserByOrgAndType(Long org, List users)
    {
        users.addAll(sysUserService.getByOrgId(org));
        List<? extends ISysOrg> orgs = this.sysOrgService.getByParentId(org, null);
        if (orgs != null)
        {
            for (ISysOrg sysOrg : orgs)
            {
                getUserByOrg(sysOrg.getOrgId(), users);
            }
        }
        
    }
    
    public static class Filter
    {
        
        private String text;
        
        private String method;
        
        private boolean select;
        
        public Filter()
        {
        }
        
        public Filter(String text, String method, boolean select)
        {
            this.text = text;
            this.method = method;
            this.select = select;
        }
        
        public String getText()
        {
            return text;
        }
        
        public void setText(String text)
        {
            this.text = text;
        }
        
        public String getMethod()
        {
            return method;
        }
        
        public void setMethod(String method)
        {
            this.method = method;
        }
        
        public boolean isSelect()
        {
            return select;
        }
        
        public void setSelect(boolean select)
        {
            this.select = select;
        }
        
    }
}
