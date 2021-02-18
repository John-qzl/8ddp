package com.cssrc.ibms.core.flow.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.dao.NodePrivilegeDao;
import com.cssrc.ibms.core.flow.dao.NodeSignDao;
import com.cssrc.ibms.core.flow.model.NodePrivilege;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.service.impl.TaskExecutorService;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:会签任务投票规则 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class NodeSignService extends BaseService<NodeSign> {

	@Resource
	private NodeSignDao dao;

	@Resource
	private GroovyScriptEngine groovyScriptEngine;
	/**
	 * 管理会签特权
	 */
	@Resource
	private NodePrivilegeDao privilegeDao;

	@Resource
	private IUserPositionService userPositionService;

	public NodeSignService() {
	}

	@Override
	protected IEntityDao<NodeSign, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 根据发布id和节点id获取会签设置对象。
	 * 
	 * @param actDefId
	 *            流程定义id
	 * @param nodeId
	 *            流程节点id
	 * @return
	 */
	public NodeSign getByDefIdAndNodeId(String actDefId, String nodeId) {
		return dao.getByDefIdAndNodeId(actDefId, nodeId);
	}

	/**
	 * 根据发布id和节点id获取会签设置对象。
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<NodePrivilege> getPrivilegesByDefIdAndNodeId(
			String actDefId, String nodeId) {
		return privilegeDao.getPrivilegesByDefIdAndNodeId(actDefId, nodeId);
	}

	/**
	 * 保存会签配置
	 * 
	 * @param bpmNodeSign
	 * @param list
	 */
	public void addOrUpdateSignAndPrivilege(NodeSign bpmNodeSign,
			List<NodePrivilege> list) {
		// 先处理BPM_NODE_SIGN表
		NodeSign nodeSign = dao.getByDefIdAndNodeId(
				bpmNodeSign.getActDefId(), bpmNodeSign.getNodeId());
		if (nodeSign == null) {
			bpmNodeSign.setSignId(UniqueIdUtil.genId());
			dao.add(bpmNodeSign);
		} else {
			dao.update(bpmNodeSign);
		}

		// 处理BPM_NODE_PRIVILEGE表
		// 先删除原有的
		privilegeDao.delByDefIdAndNodeId(bpmNodeSign.getActDefId(),bpmNodeSign.getNodeId());

		// 添加本次提交的
		for (NodePrivilege vo : list) {
			vo.setPrivilegeid(UniqueIdUtil.genId());
			vo.setActdefid(bpmNodeSign.getActDefId());
			vo.setNodeid(bpmNodeSign.getNodeId());
			privilegeDao.add(vo);
		}

	}
	
	private boolean containList(List<String> list,String id){
		for(String str:list){
			if(str.equals(id)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前用户(或组织,角色等)是否满足特权要求
	 * @param actDefId
	 * @param nodeId
	 * @param type
	 * @return
	 */
	public boolean checkNodeSignPrivilege(String actDefId, String nodeId,NodePrivilegeType type,Long userId,Long orgId) {
		List<NodePrivilege> list = privilegeDao.getPrivilegesByDefIdAndNodeIdAndMode(actDefId, nodeId,type.getMode());

		// 没有配置特权
		if (BeanUtils.isEmpty(list)) {
			return false;
		}

		// 逐条校验特权规则
		for (NodePrivilege rule : list) {
			switch (rule.getUsertype().intValue()) {
			// 指定用户
			case NodeSignPrivilegeRuleChecker.ASSIGN_TYPE_USER: {
					if (!StringUtil.isEmpty(rule.getCmpids())) {
						List<String> allowList = Arrays.asList(rule.getCmpids().split(","));
						boolean rtn= containList(allowList,String.valueOf(userId));
						if(rtn){
							return true;
						}
						
					}
				}
				break;
			// 指定组织
			case NodeSignPrivilegeRuleChecker.ASSIGN_TYPE_ORG: {
					if (!StringUtil.isEmpty(rule.getCmpids())) {
						List<String> allowList = Arrays.asList(rule.getCmpids().split(","));
						boolean rtn= containList(allowList,String.valueOf(orgId));
						if(rtn){
							return true;
						}
					}
				}
				break;
			// 按组织负责人过滤用户
			case NodeSignPrivilegeRuleChecker.ASSIGN_TYPE_ORG_CHARGE: {
					if (!StringUtil.isEmpty(rule.getCmpids())) {
						List<String> allowList = Arrays.asList(rule.getCmpids().split(","));
						for (String currentOrgId : allowList) {
							/*List<SysUser> userIdList = sysUserOrgService.getUsersByOrgIdandIsCharge(new Long(currentOrgId));*/
							List<?extends IUserPosition> userIdList = this.userPositionService.getChargeByOrgId(new Long(currentOrgId));
							if(BeanUtils.isEmpty(userIdList)){
								continue;
							}
							for (IUserPosition userOrg : userIdList) {
								if (userId.equals(userOrg.getUserId())) {
									return true;
								}
							}
						}
					}
				}
				break;
			// 根据脚本获取用户。
			case NodeSignPrivilegeRuleChecker.ASSIGN_TYPE_SCRIPT: {
					Map<String, Object> vars = new HashMap<String, Object>();
					Object result = groovyScriptEngine.executeObject(rule.getCmpnames(), vars);
					if (result != null && result instanceof Set) {
						Set<String> set = (Set<String>) result;
						if (set.contains(String.valueOf(userId))) {
							return true;
						}
					}
				}
				break;
			}
		}

		return false;
	}

	/**
	 * 会签节点特权规则
	 * 
	 */
	private static class NodeSignPrivilegeRuleChecker {
		
		/**
		 * 用户=1
		 */
		public static final short ASSIGN_TYPE_USER = 1;
	
		/**
		 * 组织=3
		 */
		public static final short ASSIGN_TYPE_ORG = 3;
		/**
		 * 组织负责人=4
		 */
		public static final short ASSIGN_TYPE_ORG_CHARGE = 4;
		
		/**
		 * 脚本
		 */
		public static final short ASSIGN_TYPE_SCRIPT = 12;
	
	}

	/**
	 * 节点特权类型
	 * 
	 * @author wwz
	 * 
	 */
	public static enum NodePrivilegeType {
		/**
		 * 0:"拥有所有特权"
		 */
		DEFAULT(0L),

		/**
		 * 1:"允许直接处理"
		 */
		ALLOW_DIRECT(1L),

		/**
		 * 2:"允许一票制"
		 */
		ALLOW_ONE_VOTE(2L),

		/**
		 * 3:"允许补签"
		 */
		ALLOW_RETROACTIVE(3L);

		private Long mode;

		private NodePrivilegeType(Long mode) {
			this.mode = mode;
		}

		/**
		 * 获取特权模式
		 * 
		 * @return
		 */
		private Long getMode() {
			return mode;
		}
	}

}
