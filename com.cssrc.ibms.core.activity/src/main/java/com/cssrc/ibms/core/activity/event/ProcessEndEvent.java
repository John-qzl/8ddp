package com.cssrc.ibms.core.activity.event;
 
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.context.ApplicationEvent;
import com.cssrc.ibms.api.activity.model.IProcessRun;

/**
 * ProcessEndEvent
 * @author liubo
 * @date 2017年5月31日
 */
public class ProcessEndEvent extends ApplicationEvent{
	private ExecutionEntity executionEntity;
	private static final long serialVersionUID = -3737454342482409864L;

	public ProcessEndEvent(IProcessRun source) {
		super(source);
	}

	public void setExecutionEntity(ExecutionEntity ent) {
		this.executionEntity = ent;
	}

	public ExecutionEntity getExecutionEntity() {
		return this.executionEntity;
	}
}