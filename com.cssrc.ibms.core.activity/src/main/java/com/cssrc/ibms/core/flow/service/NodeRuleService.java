package com.cssrc.ibms.core.flow.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.NodeRuleDao;
import com.cssrc.ibms.core.flow.model.NodeRule;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:流程节点规则 Service类 
 * 开发人员:zhulongchao
 */
@Service
public class NodeRuleService extends BaseService<NodeRule> {
	@Resource
	private NodeRuleDao dao;

	public NodeRuleService() {
	}

	@Override
	protected IEntityDao<NodeRule, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 获取所有的任务定义
	 * 
	 * @param actDefId
	 * @param ruleId
	 * @return
	 */
	public List<NodeRule> getByDefIdNodeId(String actDefId, String nodeId) {
		return dao.getByDefIdNodeId(actDefId, nodeId);
	}

	/**
	 * 规则重新排序
	 * 
	 * @param ruleIds
	 */
	public void reSort(String ruleIds) {
		if (StringUtil.isEmpty(ruleIds))
			return;
		String[] aryRuleIds = ruleIds.split(",");
		for (int i = 0; i < aryRuleIds.length; i++) {
			Long ruleId = Long.parseLong(aryRuleIds[i]);
			dao.reSort(ruleId, (long) i);
		}
	}

	/**
	 * 获取所有的任务定义
	 * 
	 * @param actDefId
	 * @param ruleId
	 * @return
	 */
	public List<NodeRule> getByActDefId(String actDefId) {
		return dao.getByDefIdNodeId(actDefId, null);
	}

}
