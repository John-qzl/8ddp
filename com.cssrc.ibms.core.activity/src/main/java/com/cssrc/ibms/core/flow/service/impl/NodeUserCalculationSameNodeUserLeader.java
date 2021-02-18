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
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 根据节点用户设置为“指定节点的执行人的负责人”，计算执行人员。
 * 
 * @author sc
 */
public class NodeUserCalculationSameNodeUserLeader implements
		INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private IUserPositionService userPositionService; 
	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		String actInstId = vars.getActInstId();
		List<?extends ISysUser> users = new ArrayList<ISysUser>();
		String nodeId = bpmNodeUser.getCmpIds();
		TaskOpinion taskOpinion = taskOpinionService.getLatestTaskOpinion(new Long( actInstId), nodeId);
		if (taskOpinion != null) {
			if(taskOpinion.getExeUserId()!=null){
				IUserPosition userPosition= userPositionService.getPrimaryByUserId(taskOpinion.getExeUserId());
				Long orgId=0L;
				if(userPosition!=null){
					orgId=userPosition.getOrgId();
				}
				users=this.sysUserService.getDirectLeaderByOrgId(orgId);
			}
		}
		return users;
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		Set<TaskExecutor> uIdSet = new LinkedHashSet<TaskExecutor>();
		List<?extends ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
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
	public String getTitle() {
		return "指定节点的执行人的负责人";
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
