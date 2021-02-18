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
 * 根据节点用户设置为“组织负责人”，计算执行人员。 
 * @author zhulongchao
 */
public class NodeUserCalculationOrgCharge implements INodeUserCalculation { 


	@Resource 
	private ISysUserService sysUserService;
	
	@Override
	public List<ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		List<ISysUser> users = new ArrayList<ISysUser>();

		String uids = bpmNodeUser.getCmpIds();
		if (StringUtil.isEmpty(uids)) {
			return users;
		}
		 List list = StringUtil.getListByStr(uids);
		 List userList = this.sysUserService.getMgrByOrgIds(list);
		 return userList;
		
	}

	@Override
	public String getTitle() { 
		return "组织负责人";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		int extraceUser=bpmNodeUser.getExtractUser();
		List<ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
		Set<TaskExecutor> uIdSet=NodeUserUtil.getExcutorsByUsers(sysUsers, extraceUser);
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
