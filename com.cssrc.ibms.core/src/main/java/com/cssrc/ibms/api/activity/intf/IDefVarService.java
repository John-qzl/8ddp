package com.cssrc.ibms.api.activity.intf;

import java.util.List;

import com.cssrc.ibms.api.activity.model.IDefVar;

public interface IDefVarService {

	public abstract boolean isVarNameExist(String varName, String varKey,
			Long defId);

	/**
	 * 根据流程发布和节点id取得流程变量列表。
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	public abstract List<? extends IDefVar> getByDeployAndNode(String deployId,
			String nodeId);

	/**
	 *  根据流程定义ID获取流程变量。
	 * @param defId
	 * @return
	 */
	public abstract List<? extends IDefVar> getVarsByFlowDefId(long defId);

}