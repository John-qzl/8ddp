package com.cssrc.ibms.core.flow.service;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.ProStatusDao;
import com.cssrc.ibms.core.flow.model.ProStatus;
import com.cssrc.ibms.core.flow.model.TaskOpinion;

/**
 * 对象功能:流程节点状态 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class ProStatusService extends BaseService<ProStatus> {
	@Resource
	private ProStatusDao dao;

	public ProStatusService() {
	}

	@Override
	protected IEntityDao<ProStatus, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 添加流程数据状态。
	 * 
	 * @param actDefId
	 *            流程定义ID
	 * @param processInstanceId
	 *            流程实例ID
	 * @param nodeId
	 *            节点ID
	 */
	public void addOrUpd(String actDefId, Long processInstanceId, String nodeId) {
		addOrUpd(actDefId,processInstanceId,nodeId,TaskOpinion.STATUS_CHECKING);
	}
	
	/**
	 * 添加或修改节点的执行状态。
	 * @param actDefId
	 * @param processInstanceId
	 * @param nodeId
	 * @param status
	 */
	public void addOrUpd(String actDefId, Long processInstanceId, String nodeId,Short status) {
		ProStatus bpmProStatus = this.dao.getByInstNodeId(processInstanceId, nodeId);
		if (bpmProStatus == null) {
			Map<String, FlowNode> mapNode = NodeCache.getByActDefId(actDefId);
			ProStatus tmp = new ProStatus();
			tmp.setId(UniqueIdUtil.genId());
			tmp.setActdefid(actDefId);
			tmp.setActinstid(processInstanceId);
			tmp.setLastupdatetime(new Date());
			tmp.setNodeid(nodeId);
			tmp.setStatus(status);
			FlowNode flowNode = mapNode.get(nodeId);
			tmp.setNodename(flowNode.getNodeName());
			dao.add(tmp);
		} else {
			this.dao.updStatus(processInstanceId, nodeId, status);
		}
	}

	/**
	 * 更新节点的状态。
	 * @param actInstanceId		流程实例ID。
	 * @param nodeId			流程节点ID。
	 * @param status			状态。
	 */
	public void updStatus(Long actInstanceId, String nodeId, Short status) {

		this.dao.updStatus(actInstanceId, nodeId, status);
	}
}
