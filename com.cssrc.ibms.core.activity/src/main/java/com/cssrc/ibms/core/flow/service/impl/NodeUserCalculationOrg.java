package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 根据节点用户设置为“组织”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationOrg implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private IUserPositionService userPositionService;
	@Override
	public List<ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		List<ISysUser> users = new ArrayList<ISysUser>();
		String uids = bpmNodeUser.getCmpIds();
		if (StringUtil.isEmpty(uids)) {
			return users;
		}
		 List list = StringUtil.getListByStr(uids);
		 List userList = this.sysUserService.getByOrgIds(list);
		return userList;
	}

	@Override
	public String getTitle() {
		//组织
		return "组织";
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
			//不抽取的情况
			case TaskExecutor.EXACT_NOEXACT:
				String[] orgIds = bpmNodeUser.getCmpIds().split("[,]");
				String[] orgNames = bpmNodeUser.getCmpNames().split("[,]");
				for (int i = 0; i < orgIds.length; i++) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskOrg(orgIds[i].toString(),orgNames[i]) ;
					
					userIdSet.add(taskExecutor);
				}
				break;
			//抽取用户
			case TaskExecutor.EXACT_EXACT_USER:
				 List<?extends IUserPosition> userOrgList = this.userPositionService.getUserByOrgIds(bpmNodeUser.getCmpIds());
				 for (IUserPosition userPosition : userOrgList)  {
					 TaskExecutor taskExecutor = TaskExecutor.getTaskUser(userPosition.getUserId().toString(), userPosition.getFullname());
					userIdSet.add(taskExecutor);
				}
				break;
			//二次抽取
			case TaskExecutor.EXACT_EXACT_SECOND:
				String[] aryOrgIds = bpmNodeUser.getCmpIds().split("[,]");
				String[] aryOrgNames = bpmNodeUser.getCmpNames().split("[,]");
				for (int i = 0; i < aryOrgIds.length; i++) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskOrg(aryOrgIds[i].toString(),aryOrgNames[i]) ;
					taskExecutor.setExactType(TaskExecutor.EXACT_EXACT_SECOND);
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
