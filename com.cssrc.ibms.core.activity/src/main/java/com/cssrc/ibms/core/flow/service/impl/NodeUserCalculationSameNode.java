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
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.util.bean.BeanUtils;

/**
 * 根据节点用户设置为“与其他节点相同执行人”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationSameNode implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private TaskOpinionService taskOpinionService;

	@Override
	public List<ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		String actInstId = vars.getActInstId();
		List<ISysUser> users = new ArrayList<ISysUser>();
		String nodeId = bpmNodeUser.getCmpIds();
		TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(new Long( actInstId), nodeId);
		if (taskOpinion != null) {
			if(taskOpinion.getExeUserId()!=null){
				ISysUser user = sysUserService.getById(taskOpinion.getExeUserId());
				users.add(user);
			}
		}
		return users;
	}

	@Override
	public String getTitle() { 
		return "与已执行节点相同执行人";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		Set<TaskExecutor> uIdSet = new LinkedHashSet<TaskExecutor>();
		List<ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
		if(BeanUtils.isNotEmpty(sysUsers)){
			for (ISysUser sysUser : sysUsers) {
				if(BeanUtils.isNotEmpty(sysUser)){
					uIdSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),sysUser.getFullname()));
				}
			}
		}
		
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
		return false;
	}

}
