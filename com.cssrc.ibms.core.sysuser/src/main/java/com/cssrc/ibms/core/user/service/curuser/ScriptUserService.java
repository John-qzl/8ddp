package com.cssrc.ibms.core.user.service.curuser;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.login.model.CurrentUser;

import java.util.List;

import javax.annotation.Resource;

public class ScriptUserService implements ICurUserService {

	@Resource
	private GroovyScriptEngine groovyScriptEngine;

	public List<Long> getByCurUser(CurrentUser currentUser) {
		return null;
	}

	public String getKey() {
		return "user";
	}
	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
	public String getTitle() {
		return "人员脚本";
	}
}
