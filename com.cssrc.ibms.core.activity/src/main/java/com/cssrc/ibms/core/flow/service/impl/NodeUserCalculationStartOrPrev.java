package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.intf.IUserUnderService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;

import net.sf.json.JSONObject;


/**
 * 根据发起人或上一任务执行人计算人员。
 * <pre>
 * 人员的JSON格式如下：
 * {type:'director',userType:'start',varName:'name'}
 * type:可能的值为
 * 	director,部门负责人
 * 	leader,领导
 * userType:可能的值为 start、prev
 * </pre>
 * @author zhulongchao
 *
 */
public class NodeUserCalculationStartOrPrev implements INodeUserCalculation { 
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private IUserUnderService userUnderService;
	@Resource
	private IUserPositionService userPositionService; 
	
	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		Long userId = 0L;//发起人或上一任务发起人
		Long orgId=0L;
		List<?extends ISysUser> sysUserList=new ArrayList<ISysUser>();
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		String type=jsonObject.getString("type");
		String userType=jsonObject.getString("userType");
		
		if("start".equals(userType)){//发起人
			userId=vars.getStartUserId();
		}
        if("prev".equals(userType)){//上一任务执行人
        	userId=vars.getPrevExecUserId();
          	if(userId==null||userId==0L)userId=vars.getStartUserId();
		}
        if(userId==null  || userId.intValue()==0) 
        	return new ArrayList<ISysUser>();
        
       /* 
        * 负责人 首先要找到用户所在的组织岗位，通过组织岗位找到用户组织，用户组织有负责人信息
        * 最后获取组织负责人
        */
		if ("director".equals(type)){//负责人			
			IUserPosition userPosition= userPositionService.getPrimaryByUserId(userId);
			if(userPosition!=null){
				orgId=userPosition.getOrgId();
			}
			sysUserList=this.sysUserService.getDirectLeaderByOrgId(orgId);
		}
		/*
		 * 领导 --领导就是用户上一级用户 ，
		 * 在用户管理里编辑用户可以设置用户的上一级用户
		 * */
		if ("leader".equals(type)){
			
			sysUserList = this.sysUserService.getUserByUnderUserId(userId);
		}
		return sysUserList;
	}
	
	
	

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser,CalcVars vars) {
		Set<TaskExecutor> userSet = new LinkedHashSet<TaskExecutor>();
		List<?extends ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
		for (ISysUser sysUser : sysUsers) {
			userSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),sysUser.getFullname()));
		}
		return userSet;
	}

	

	@Override
	public String getTitle() {
		return "发起人或上一任务执行人的领导或负责人";
	}

	@Override
	public boolean supportMockModel() {
		
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser bpmNodeUser) {
		List<PreViewModel> list=new ArrayList<PreViewModel>();
		PreViewModel preViewModel=new PreViewModel();
		preViewModel.setType(PreViewModel.START_USER);
		list.add(preViewModel);
			
		return list;
	}

	@Override
	public boolean supportPreView() {
		return true;
	}

}