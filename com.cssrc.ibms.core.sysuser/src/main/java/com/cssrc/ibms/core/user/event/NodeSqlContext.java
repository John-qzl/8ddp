package com.cssrc.ibms.core.user.event;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * <p>Title:NodeSqlContext</p>
 * @author Yangbo 
 * @date 2016-8-18下午03:52:53
 */
public class NodeSqlContext {
	private Long runId = Long.valueOf(0L);

	private Long businessKey = Long.valueOf(0L);

	private Map<String, Object> dataMap = new HashMap();

	private String actdefId = "";

	private String nodeId = "";

	private String action = "";

	public Long getRunId() {
		return this.runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Long getBusinessKey() {
		return this.businessKey;
	}

	public void setBusinessKey(Long businessKey) {
		this.businessKey = businessKey;
	}

	public Map<String, Object> getDataMap() {
		return this.dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public String getActdefId() {
		return this.actdefId;
	}

	public void setActDefId(String actdefId) {
		this.actdefId = actdefId;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
