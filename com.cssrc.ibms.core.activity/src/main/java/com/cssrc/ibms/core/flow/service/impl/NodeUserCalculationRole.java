package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserRoleService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserRole;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 根据节点用户设置为“角色”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationRole implements INodeUserCalculation { 
	@Resource
	private IUserRoleService userRoleService;
	@Resource
	private ISysUserService sysUserService;

	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		List<ISysUser> users = new ArrayList<ISysUser>();
		String roleIds = bpmNodeUser.getCmpIds();
		if (StringUtil.isEmpty(roleIds)) {
			return users;
		}
		List list = StringUtil.getListByStr(roleIds);
		return this.sysUserService.getByRoleIds(list);
	}

	@Override
	public String getTitle() { 
		return "角色";
	}

	
	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		int extractUser=bpmNodeUser.getExtractUser();
		Set<TaskExecutor> userIdSet = new LinkedHashSet<TaskExecutor>();
		String uids=bpmNodeUser.getCmpIds();
		if(StringUtil.isEmpty(uids)){
			return userIdSet;
		}
		switch (extractUser) {
			case TaskExecutor.EXACT_NOEXACT:
				String[] roleIds = bpmNodeUser.getCmpIds().split("[,]");
				String[] roleNames = bpmNodeUser.getCmpNames().split("[,]");
				for (int i = 0; i < roleIds.length; i++) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskRole(roleIds[i],roleNames[i]) ;
					userIdSet.add(taskExecutor);
				}
				break;
			case TaskExecutor.EXACT_EXACT_USER:
				List<?extends IUserRole> userList = this.userRoleService.getUserByRoleIds(bpmNodeUser.getCmpIds());
				for (IUserRole user : userList) {
					TaskExecutor taskExecutor = TaskExecutor.getTaskUser(user.getUserId().toString(), user.getFullname());
					userIdSet.add(taskExecutor);
				}
				break;
			case TaskExecutor.EXACT_EXACT_SECOND:
				String[] aryRoleIds = bpmNodeUser.getCmpIds().split("[,]");
				String[] aryRoleNames = bpmNodeUser.getCmpNames().split("[,]");
				for (int i = 0; i < aryRoleIds.length; i++) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskRole(aryRoleIds[i],aryRoleNames[i]) ;
					taskExecutor.setExactType(extractUser);
					userIdSet.add(taskExecutor);
				}
				break;
			
		}
		return userIdSet;
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
		return true;
	}

	

}
