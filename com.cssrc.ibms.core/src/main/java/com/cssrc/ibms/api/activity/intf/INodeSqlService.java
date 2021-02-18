package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import com.cssrc.ibms.api.activity.model.INodeSql;

public interface INodeSqlService {

	/**
	 * 通过节点id、流程id以及触发时机找到对应的节点sql记录列表
	 * @param nodeId
	 * @param actdefId
	 * @param action
	 * @return
	 */
	public abstract List<? extends INodeSql> getByNodeIdAndActdefIdAndAction(String nodeId, String actdefId, String action);

}