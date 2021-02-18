package com.cssrc.ibms.core.flow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.INodeSqlService;
import com.cssrc.ibms.api.activity.model.INodeSql;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.NodeSqlDao;
import com.cssrc.ibms.core.flow.model.NodeSql;
import com.cssrc.ibms.core.util.string.StringUtil;
 
/**
 * NodeSqlService
 * @author liubo
 * @date 2017年2月16日
 */
@Service
public class NodeSqlService extends BaseService<NodeSql> implements INodeSqlService
{
	@Resource
	private NodeSqlDao dao;
	
	protected IEntityDao<NodeSql, Long> getEntityDao(){
		return this.dao;
	}
	
	/**
	 * 通过节点id与流程id找到对应的节点sql记录
	 * @param nodeId
	 * @param actdefId
	 * @return
	 */
	public NodeSql getByNodeIdAndActdefId(String nodeId, String actdefId) {
		List bpmNodeSqls = getByNodeIdAndActdefIdAndAction(nodeId, actdefId, null);
		if (bpmNodeSqls.isEmpty())
			return null;
			return (NodeSql)bpmNodeSqls.get(0);
		}
		 
	/**
	 * 通过节点id、流程id以及触发时机找到对应的节点sql记录列表
	 * @param nodeId
	 * @param actdefId
	 * @param action
	 * @return
	 */
	public List<NodeSql> getByNodeIdAndActdefIdAndAction(String nodeId, String actdefId, String action) {
		Map map = new HashMap();
		if (StringUtil.isNotEmpty(action)) {
			map.put("action", action);
		}
		if (StringUtil.isNotEmpty(nodeId)) {
			map.put("nodeId", nodeId);
		}
		if (StringUtil.isNotEmpty(actdefId)) {
			map.put("actdefId", actdefId);
		}
		return this.dao.getList("getByNodeIdAndActdefIdAndAction", map);
	}
	 
	/**
	 * 设置候选数据
	 * @param processCmd
	 * @param bpmFormData
	 */
	public static void handleData(ProcessCmd processCmd, IFormData bpmFormData){
		Map map = new HashMap();
		Map formDataMap = processCmd.getFormDataMap();
		Map data = new HashMap();
		data.putAll(formDataMap);
		data.putAll(bpmFormData.getMainFields());
		data.remove("formData");
		processCmd.addTransientVar("bpmFormData", bpmFormData);
		processCmd.addTransientVar("mainData", data);
	}
}
