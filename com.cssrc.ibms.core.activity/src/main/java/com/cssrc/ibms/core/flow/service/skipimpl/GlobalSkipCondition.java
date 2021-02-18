package com.cssrc.ibms.core.flow.service.skipimpl;

import org.activiti.engine.task.Task;

import com.cssrc.ibms.core.flow.intf.ISkipCondition;


public class GlobalSkipCondition implements ISkipCondition {
	public boolean canSkip(Task task) {
		return true;
	}

	public String getTitle() {
		return "全部跳过";
	}
}
