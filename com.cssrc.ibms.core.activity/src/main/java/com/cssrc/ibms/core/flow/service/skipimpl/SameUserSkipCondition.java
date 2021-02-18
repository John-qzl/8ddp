package com.cssrc.ibms.core.flow.service.skipimpl;

import org.activiti.engine.task.Task;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.flow.intf.ISkipCondition;


public class SameUserSkipCondition implements ISkipCondition {
	public boolean canSkip(Task task) {
		ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();
		String assignee = task.getAssignee();
		String curUserId = sysUser.getUserId().toString();

		return curUserId.equals(assignee);
	}

	public String getTitle() {
		return "相同执行人跳过";
	}
}
