package com.cssrc.ibms.core.flow.service;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.NodeWebServiceDao;
import com.cssrc.ibms.core.flow.model.NodeWebService;


/**
 * <pre>
 * 对象功能:流程WebService节点 Service类 
 * 开发人员:zhulongchao 
 * </pre>
 */
@Service
public class NodeWebServiceService extends BaseService<NodeWebService> {
	@Resource
	private NodeWebServiceDao dao;

	public NodeWebServiceService() {
	}

	@Override
	protected IEntityDao<NodeWebService, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 通过节点ID和流程节点ID和服务类型获得流程服务设置信息
	 * 
	 * @param nodeId
	 *            节点ID
	 * @param actDefId
	 *            流程节点ID
	 * @return
	 */
	public NodeWebService getByNodeIdActDefId(String nodeId, String actDefId) {
		return dao.getByNodeIdActDefId(nodeId, actDefId);
	}
	
	/**
	 * 保存 流程WebService节点
	 * 
	 * @param nodeId
	 *            节点ID
	 * @param actDefId
	 *            流程节点ID
	 * @param json
	 * @throws Exception
	 */
	public void save(String nodeId, String actDefId, String json) throws Exception {
		NodeWebService bpmNodeWebService = new NodeWebService();
		bpmNodeWebService.setId(UniqueIdUtil.genId());
		bpmNodeWebService.setNodeId(nodeId);
		bpmNodeWebService.setActDefId(actDefId);
		bpmNodeWebService.setDocument(json);
		dao.add(bpmNodeWebService);
	}

	/**
	 * 取得 NodeWebService 实体
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	protected NodeWebService getFormObject(String nodeId, String actDefId, JSONObject jsonObject) throws Exception {
		Long id = (Long) jsonObject.get("wsid");
		NodeWebService bpmNodeWebService = new NodeWebService();
		bpmNodeWebService.setId(id);
		bpmNodeWebService.setNodeId(nodeId);
		bpmNodeWebService.setActDefId(actDefId);
		return bpmNodeWebService;
	}
}
