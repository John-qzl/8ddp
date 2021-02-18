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
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 节点人员脚本
 * <p>Title:NodeUserCalculationPersonScript</p>
 * @author Yangbo 
 * @date 2016-8-24上午11:12:22
 */
public class NodeUserCalculationPersonScript implements INodeUserCalculation {

	@Resource
	private ISysUserService sysUserService;

	@Resource
	private GroovyScriptEngine groovyScriptEngine;

	public List<ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		Long prevUserId = vars.getPrevExecUserId();
		Long startUserId = vars.getStartUserId();

		String script = bpmNodeUser.getCmpIds();
		List users = new ArrayList();
		Map grooVars = new HashMap();

		String actInstId = "";
		if (StringUtil.isNotEmpty(vars.getActInstId())) {
			actInstId = vars.getActInstId();
		}
		grooVars.put("actInstId", actInstId);
		if (vars.getVars().size() > 0) {
			grooVars.putAll(vars.getVars());
		}

		grooVars.put("startUser", startUserId);
		grooVars.put("prevUser", prevUserId);
		Object result = this.groovyScriptEngine.executeObject(script, grooVars);
		if (result == null) {
			return users;
		}
		Set set = (Set) result;
		for (Iterator it = set.iterator(); it.hasNext();) {
			String userId = (String) it.next();
			ISysUser sysUser = (ISysUser) this.sysUserService.getById(Long
					.valueOf(Long.parseLong(userId)));
			users.add(sysUser);
		}
		return users;
	}

	public String getTitle() {
		return "人员脚本";
	}

	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		int extractUser = bpmNodeUser.getExtractUser().shortValue();

		List sysUsers = getExecutor(bpmNodeUser, vars);
		Set uIdSet = NodeUserUtil.getExcutorsByUsers(sysUsers, extractUser);
		return uIdSet;
	}

	public boolean supportMockModel() {
		return true;
	}

	public List<INodeUserCalculation.PreViewModel> getMockModel(
			NodeUser bpmNodeUser) {
		List list = new ArrayList();

		INodeUserCalculation.PreViewModel startViewModel = new INodeUserCalculation.PreViewModel();
		startViewModel.setType(1);
		list.add(startViewModel);

		INodeUserCalculation.PreViewModel preViewModel = new INodeUserCalculation.PreViewModel();
		preViewModel.setType(2);
		list.add(preViewModel);

		return list;
	}

	public boolean supportPreView() {
		return true;
	}
}
