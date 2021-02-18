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
 * 根据节点用户设置为“用户属性”，计算执行人员。
 * 
 * @author Raise
 */
@Deprecated
public class NodeUserCalculationUserAttr implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;

	@Override
	public List<ISysUser> getExecutor(NodeUser NodeUser, CalcVars vars) {
		List<ISysUser> users = new ArrayList<ISysUser>();
		try {
			//users = sysUserService.getByUserParam(NodeUser.getCmpIds());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public String getTitle() {
		return "用户属性";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser NodeUser, CalcVars vars) {
		int extractUser=NodeUser.getExtractUser();
		List<ISysUser> sysUsers= getExecutor(NodeUser,vars);
		Set<TaskExecutor> uIdSet =NodeUserUtil.getExcutorsByUsers(sysUsers, extractUser);
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
		return true;
	}

	

}
