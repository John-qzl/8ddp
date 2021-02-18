package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;

/** 
 * 目前执行人都是直接抽取
* @ClassName: NodeUserCalculationOrgLeader 
* @Description: 发起人或上一级任务执行人 所属组织 的分管领导人员查找
* @author zxg 
* @date 2017年3月28日 上午11:34:05 
*  
*/
public class NodeUserCalculationOrgLeader implements INodeUserCalculation
{
    @Resource
    private IUserPositionService userPositionService;
    
    @Resource
    private ISysOrgService sysOrgService;
    
    @Override
    public List<? extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars)
    {
        Long userId = 0L;// 发起人或上一任务发起人
        String cmpIds = bpmNodeUser.getCmpIds();
        JSONObject jsonObject = JSONObject.fromObject(cmpIds);
        // 领导类型 所有分管领导，主领导，副领导
        String type = jsonObject.getString("type");
        // 用户类型，发起人或者上一节点审批人
        String userType = jsonObject.getString("userType");
        // 查找策略
        String strategy = jsonObject.getString("strategy");

        if ("start".equals(userType))
        {
            // 发起人
            userId = vars.getStartUserId();
        }else if ("prev".equals(userType))
        {
            // 上一任务执行人
            userId = vars.getPrevExecUserId();
        }else{
            return new ArrayList<ISysUser>();
        }
        IUserPosition userPosition = userPositionService.getPrimaryByUserId(userId);
        List<? extends ISysUser> sysUserList=sysOrgService.getLeaderUserByOrgId(userPosition.getOrgId(),type,"1".equals(strategy));
        return sysUserList;
    }
    
    @Override
    public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars)
    {
        Set<TaskExecutor> userSet = new LinkedHashSet<TaskExecutor>();
        List<? extends ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
        for (ISysUser sysUser : sysUsers)
        {
            userSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getFullname()));
        }
        return userSet;
    }
    
    @Override
    public String getTitle()
    {
        return "发起人或上一任务执行人的所属组织分管领导";
    }
    
    @Override
    public boolean supportMockModel()
    {
        return true;
    }
    
    @Override
    public List<PreViewModel> getMockModel(NodeUser bpmNodeUser)
    {
        List<PreViewModel> list = new ArrayList<PreViewModel>();
        PreViewModel preViewModel = new PreViewModel();
        preViewModel.setType(PreViewModel.START_USER);
        list.add(preViewModel);
        return list;
    }
    
    @Override
    public boolean supportPreView()
    {
        return true;
    }
    
}
