package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 根据节点用户设置为“用户”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationUser implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;

	@Override
	public List<ISysUser> getExecutor(NodeUser NodeUser, CalcVars vars) {
		List<ISysUser> sysUsers = new ArrayList<ISysUser>();
		String uids = NodeUser.getCmpIds();
		if (StringUtil.isEmpty(uids)) {
			return sysUsers;
		}
		String[] aryUid = uids.split("[,]"); 
		for(String userId : aryUid){
			sysUsers.add(sysUserService.getById(Long.valueOf(userId)));
		}
		return sysUsers;
	}

	@Override
	public String getTitle() { 
		return "用户";
	}
	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser NodeUser, CalcVars vars) {
		int extraceUser=NodeUser.getExtractUser();
		List<ISysUser> sysUsers = this.getExecutor(NodeUser, vars);
		Set<TaskExecutor> uIdSet=NodeUserUtil.getExcutorsByUsers(sysUsers, extraceUser);
		return uIdSet;
	}

	@Override
	public boolean supportMockModel() {
		
		return false;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser NodeUser) {
	
		return null;
	}

	@Override
	public boolean supportPreView() {
		// TODO Auto-generated method stub
		return true;
	}

	
	
	
}