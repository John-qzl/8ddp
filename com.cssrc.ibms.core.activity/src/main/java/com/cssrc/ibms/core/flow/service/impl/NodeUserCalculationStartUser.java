package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;

/**
 * 根据节点用户设置为“发起人”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationStartUser implements INodeUserCalculation {

	@Resource
	private ISysUserService sysUserService;

	@Override
	public List<ISysUser> getExecutor(NodeUser NodeUser, CalcVars vars) {
		Long startUserId = vars.getStartUserId();
		if(startUserId==null || startUserId.intValue()==0) return new ArrayList<ISysUser>();
		
		List<ISysUser> sysUsers = new ArrayList<ISysUser>();
		ISysUser sysUser = sysUserService.getById(startUserId);
		sysUsers.add(sysUser);
		return sysUsers;
	}

	@Override
	public String getTitle() { 
		return "发起人";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser NodeUser, CalcVars vars) {
		Set<TaskExecutor> uIdSet = new LinkedHashSet<TaskExecutor>();
		List<ISysUser> sysUsers = this.getExecutor(NodeUser, vars);
		for (ISysUser sysUser : sysUsers) {
			uIdSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),sysUser.getFullname()));
		}
		return uIdSet;
	}

	@Override
	public boolean supportMockModel() {
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser NodeUser) {
		List< PreViewModel> list=new ArrayList<PreViewModel>();
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
