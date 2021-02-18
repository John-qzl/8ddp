package com.cssrc.ibms.core.user.event;

import com.cssrc.ibms.api.activity.model.IProcessCmd;
/**
 * 
 * <p>Title:TriggerNewFlowModel</p>
 * @author Yangbo 
 * @date 2016-8-18下午03:51:01
 */
public class TriggerNewFlowModel {
	private String action;
	private IProcessCmd processCmd;
	private String nodeId;

	public TriggerNewFlowModel(String action, String nodeId,
			IProcessCmd processCmd) {
		this.nodeId = nodeId;
		this.action = action;
		this.processCmd = processCmd;
	}

	public TriggerNewFlowModel() {
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public IProcessCmd getProcessCmd() {
		return this.processCmd;
	}

	public void setProcessCmd(IProcessCmd processCmd) {
		this.processCmd = processCmd;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
}
