package com.cssrc.ibms.core.flow.service.impl;

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
 * 当前流程实例审批人。
 * @author zhulongchao
 *
 */
public class NodeUserCalculationApprove implements INodeUserCalculation {

	@Resource
	private ISysUserService sysUserService; 
	
	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		String actInstId= vars.getActInstId(); 
		List<?extends ISysUser> list=sysUserService.getExeUserByInstnceId(Long.valueOf(Long.parseLong(actInstId)));
		
		return list;
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser,
			CalcVars vars) {
		int extraceUser=bpmNodeUser.getExtractUser();
		
		List<?extends ISysUser> list= getExecutor(bpmNodeUser, vars);
		Set<TaskExecutor> set=NodeUserUtil.getExcutorsByUsers(list, extraceUser);
		return set;
	}

	@Override
	public String getTitle() { 
		return "当前流程实例审批人";
	}

	@Override
	public boolean supportMockModel() {
		
		return false;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser bpmNodeUser) {
		
		return null;
	}

	@Override
	public boolean supportPreView() {
	
		return false;
	}

	

	

	
}
