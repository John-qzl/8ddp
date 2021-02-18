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

/**
 * 根据节点用户设置为“组织属性”，计算执行人员。
 * 
 * @author Raise
 */
public class NodeUserCalculationOrgAttr implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;

	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		List<?extends ISysUser> users = new ArrayList<ISysUser>();
		try {
			users = this.sysUserService.getByOrgParam(bpmNodeUser.getCmpIds());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public String getTitle() {
		return "组织属性";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		int extraceUser=bpmNodeUser.getExtractUser().intValue();
		List<?extends ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
		Set<TaskExecutor> uIdSet = NodeUserUtil.getExcutorsByUsers(sysUsers, extraceUser);
		return uIdSet;
	}

	@Override
	public boolean supportMockModel() {
		return false;
	}

	@Override
	public List< PreViewModel> getMockModel(NodeUser bpmNodeUser) {
		return null;
	}
	
	@Override
	public boolean supportPreView() {
		return true;
	}


}
