package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 根据节点用户设置为“脚本”，计算执行人员。
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationScript implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;

	@SuppressWarnings("unchecked")
	@Override
	public List<ISysUser> getExecutor(NodeUser NodeUser, CalcVars vars) {
		Long prevUserId = vars.getPrevExecUserId();
		Long startUserId = vars.getStartUserId();
		String script = NodeUser.getCmpNames();
		List<ISysUser> users = new ArrayList<ISysUser>();
		Map<String, Object> grooVars = new HashMap<String, Object>();
		grooVars.put(BpmConst.PrevUser, prevUserId);
		grooVars.put(BpmConst.StartUser, startUserId.toString());
		String actInstId="";
		if(StringUtil.isNotEmpty(vars.getActInstId()) ){
			actInstId=vars.getActInstId();
		}
		grooVars.put("actInstId", actInstId);
		if(vars.getVars().size()>0){
			grooVars.putAll(vars.getVars());
		}
		Object result = groovyScriptEngine.executeObject(script, grooVars);
		if (result == null) {
			return users;
		}
		Set<String> set = (Set<String>) result;
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String userId = it.next();
			if(StringUtil.isNotEmpty(userId) && !"null".equals(userId)){
				ISysUser sysUser = sysUserService.getById(Long.parseLong(userId));
				users.add(sysUser);
			}
		}
		return users;
	}

	@Override
	public String getTitle() { 
		return "脚本";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser NodeUser, CalcVars vars) {
		int extractUser=NodeUser.getExtractUser();
		
		List<ISysUser> sysUsers = this.getExecutor(NodeUser, vars);
		Set<TaskExecutor> uIdSet = NodeUserUtil.getExcutorsByUsers(sysUsers, extractUser);
		return uIdSet;
	
	}

	@Override
	public boolean supportMockModel() {
		
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser NodeUser) {
		List<PreViewModel> list=new ArrayList<PreViewModel>();
		
		PreViewModel startViewModel=new PreViewModel();
		startViewModel.setType(PreViewModel.START_USER);
		list.add(startViewModel);
	
		PreViewModel preViewModel=new PreViewModel();
		preViewModel.setType(PreViewModel.PRE_VIEW_USER);
		list.add(preViewModel);
		
		return list;
	}
	
	@Override
	public boolean supportPreView() {
		return true;
	}


}
