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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 根据节点用户设置为“上下级”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationUpLow implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;

	@Override
	public List<?extends ISysUser> getExecutor(NodeUser NodeUser, CalcVars vars) {
		Long userId = 0L;//发起人或上一任务发起人
		String cmpIds=NodeUser.getCmpIds();
		//JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		//必须用JSONArray来转，用JSONObject会出现以下错误信息：  
        // A JSONObject text must begin with '{' at character 1 of
		
		JSONArray jsonArray=  JSONArray.fromObject(cmpIds);
		JSONObject jsonObject=jsonArray.getJSONObject(0);
		String userType=jsonObject.getString("userType");
		
		if("start".equals(userType)){//发起人
			userId=vars.getStartUserId();
		}
        if("prev".equals(userType)){//上一任务执行人
        	userId=vars.getPrevExecUserId();
          	if(userId==null||userId==0L)userId=vars.getStartUserId();
		}
        if(userId==null  || userId.intValue()==0) 
        	return new ArrayList<ISysUser>();
        
		List<?extends ISysUser> users = new ArrayList<ISysUser>();
		users = sysUserService.getByUserIdAndUplow(userId, NodeUser.getCmpIds());
		return users;
	}

	@Override
	public String getTitle() {
		return "上下级";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser NodeUser, CalcVars vars) {
		int extractUser=NodeUser.getExtractUser();
		List<?extends ISysUser> sysUsers = this.getExecutor(NodeUser, vars);
		Set<TaskExecutor> uIdSet =NodeUserUtil.getExcutorsByUsers(sysUsers, extractUser);
		return uIdSet;
	}

	@Override
	public boolean supportMockModel() {
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser NodeUser) {
		List< PreViewModel> list=new ArrayList<PreViewModel>();
		PreViewModel preViewModel=new PreViewModel();
		preViewModel.setType(PreViewModel.START_USER);
		
		list.add(preViewModel);
		
		return list;
	}

	@Override
	public boolean supportPreView() {
		return true;
	}

	
}
