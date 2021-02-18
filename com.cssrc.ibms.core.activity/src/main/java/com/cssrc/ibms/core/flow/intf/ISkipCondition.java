package com.cssrc.ibms.core.flow.intf;
import org.activiti.engine.task.Task;
public abstract interface ISkipCondition {
	public abstract boolean canSkip(Task paramTask);

	public abstract String getTitle();
}
