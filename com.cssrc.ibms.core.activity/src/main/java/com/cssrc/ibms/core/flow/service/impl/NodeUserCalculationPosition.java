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
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 根据节点用户设置为“岗位”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationPosition implements INodeUserCalculation { 
	
	@Resource
	private ISysUserService sysUserService;
	
	@Override
	public List<ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		List<ISysUser> users = new ArrayList<ISysUser>();
		String uids = bpmNodeUser.getCmpIds();
		if (StringUtil.isEmpty(uids)) {
			return users;
		}
		return getByCmpIds(uids);
	}
	
	private List<ISysUser> getByCmpIds(String cmpIds){
		List list = StringUtil.getListByStr(cmpIds);
		List<?extends ISysUser> sysUsers = this.sysUserService.getByPos(list);
		List<ISysUser> sysUserList = new ArrayList();
		for (ISysUser user : sysUsers) {
			if (!sysUserList.contains(user)) {
				sysUserList.add(user);
			}
		}
		 return sysUserList;
	}

	@Override
	public String getTitle() {
		return "岗位";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		int extractUser=bpmNodeUser.getExtractUser().intValue();
		
		Set<TaskExecutor> userIdSet = new LinkedHashSet<TaskExecutor>();
		String uids=bpmNodeUser.getCmpIds();
		if(StringUtil.isEmpty(uids)){
			return userIdSet;
		}
		switch (extractUser) {
			case TaskExecutor.EXACT_NOEXACT:
				String[] aryId = bpmNodeUser.getCmpIds().split("[,]");
				String[] aryName = bpmNodeUser.getCmpNames().split("[,]");
				for (int i = 0; i < aryId.length; i++) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskPos(aryId[i].toString(),aryName[i]) ;
					userIdSet.add(taskExecutor);
				}
				break;
			case TaskExecutor.EXACT_EXACT_USER:
				List<ISysUser> userList= getByCmpIds(uids);
				for (ISysUser user : userList) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskUser(user.getUserId().toString(),user.getFullname()) ;
					userIdSet.add(taskExecutor);
				}
				break;
			case TaskExecutor.EXACT_EXACT_SECOND:
				String[] aryPosId = bpmNodeUser.getCmpIds().split("[,]");
				String[] aryPosName = bpmNodeUser.getCmpNames().split("[,]");
				for (int i = 0; i < aryPosId.length; i++) {
					TaskExecutor taskExecutor=TaskExecutor.getTaskPos(aryPosId[i].toString(),aryPosName[i]) ;
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
	public List< PreViewModel> getMockModel(NodeUser bpmNodeUser) {
	
		return null;
	}
	
	@Override
	public boolean supportPreView() {
		return true;
	}

	


}
