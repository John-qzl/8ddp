package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.model.IFlowNode;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormRightsService;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.NodeSetDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:NodeSetService类
 */
@Service
public class NodeSetService extends BaseService<NodeSet> implements
		INodeSetService {
	@Resource
	private NodeSetDao dao;
    
    @Autowired
    private TaskService taskService;
	   
	@Resource
	private IFormRightsService formRightsService;

	@Resource
	private DefinitionService definitionService;

	public NodeSetService() {
	}

	@Override
	protected IEntityDao<NodeSet, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 保存节点配置。
	 * 
	 * @param nodeList
	 * @throws Exception
	 */
	public void save(Long defId, List<NodeSet> nodeList) throws Exception {
		dao.delByStartGlobalDefId(defId);
		for (NodeSet node : nodeList) {
			if (node.getSetId() == null) {
				long setId = UniqueIdUtil.genId();
				node.setSetId(setId);
				dao.add(node);
			} else {
				dao.update(node);
				// 表单类型
				if (node.getFormType() == 0) {
					if (!(node.getOldFormKey() == 0)) {
						if ((node.getFormKey().equals(node.getOldFormKey())))
							continue;
						// 原来定义过表单权限，并且新定义的在线表单和原定义的表单不一致。
						// 删除原节点的权限定义
						formRightsService.delByFlowFormNodeId(
								node.getActDefId(), node.getNodeId());
					}
				} else {
					// 设置其他表单类型时，清空权限设置
					formRightsService.delByFlowFormNodeId(node.getActDefId(),
							node.getNodeId());
				}
			}
		}
	}

	/**
	 * 根据流程设置ID取流程节点设置
	 * 
	 * @param defId
	 * @return
	 */
	public List<NodeSet> getByDefId(Long defId) {
		List<NodeSet> list = dao.getByDefId(defId);
		handlerNodeList(defId, list);
		return list;
	}

	/**
	 * 根据流程设置ID取流程所有的节点设置
	 * 
	 * @param defId
	 * @return
	 */
	public List<NodeSet> getAllByDefId(Long defId) {
		List<NodeSet> list = dao.getAllByDefId(defId);
		handlerNodeList(defId, list);
		return list;
	}

	/**
	 * 根据流程定义id和节点id获取NodeSet对象。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @param nodeId
	 *            节点ID
	 * @return
	 */
	public NodeSet getByDefIdNodeId(Long defId, String nodeId) {
		NodeSet bpmNodeSet = dao.getByDefIdNodeId(defId, nodeId);
		// handlerNodeName(defId,bpmNodeSet);
		return bpmNodeSet;
	}

	public NodeSet getByDefIdNodeId(Long defId, String nodeId,
			String parentActDefId) {
		NodeSet bpmNodeSet = dao
				.getByDefIdNodeId(defId, nodeId, parentActDefId);
		// handlerNodeName(defId,bpmNodeSet);
		return bpmNodeSet;
	}

	/**
	 * 根据流程定义获取流程节点设置对象。
	 * 
	 * @param defId
	 * @return
	 */
	public Map<String, NodeSet> getMapByDefId(Long defId) {
		Map<String, NodeSet> map = dao.getMapByDefId(defId);
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			NodeSet bpmNodeSet = map.get(key);
			handlerNodeName(defId, bpmNodeSet);
		}
		return map;
	}

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体
	 * 
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	public NodeSet getByActDefIdNodeId(String actDefId, String nodeId) {
		NodeSet nodeSet = dao.getByActDefIdNodeId(actDefId, nodeId);
		Definition bpmDefinition = definitionService.getByActDefId(actDefId);
		// 节点未设置表单取全局表单
		if (isFormEmpty(nodeSet)) {
			NodeSet globalNodeSet = getBySetType(bpmDefinition.getDefId(),
					NodeSet.SetType_GloabalForm);
			if (BeanUtils.isNotEmpty(globalNodeSet)) {
				globalNodeSet.setIsHideOption(nodeSet==null?0:nodeSet.getIsHideOption());
				globalNodeSet.setIsHidePath(nodeSet==null?1:nodeSet.getIsHidePath());
				globalNodeSet.setInformType(nodeSet==null?"":nodeSet.getInformType());
				globalNodeSet.setIsAllowMobile(nodeSet==null?0:nodeSet.getIsAllowMobile());
				globalNodeSet.setIsJumpForDef(nodeSet==null?1:nodeSet.getIsJumpForDef());
				globalNodeSet.setJumpType(nodeSet==null?"1":nodeSet.getJumpType());
				handlerNodeNameByActDefId(actDefId, globalNodeSet);
				return globalNodeSet;
			}
		}
		handlerNodeNameByActDefId(actDefId, nodeSet);
		return nodeSet;
	}

	public NodeSet getByActDefIdNodeId(String actDefId, String nodeId,
			String parentActDefId) {
		NodeSet bpmNodeSet = dao.getByActDefIdNodeId(actDefId, nodeId,
				parentActDefId);
		Definition bpmDefinition = definitionService.getByActDefId(actDefId);

		if (isFormEmpty(bpmNodeSet)) {
			NodeSet globalNodeSet = getBySetType(bpmDefinition.getDefId(),
					NodeSet.SetType_GloabalForm, parentActDefId);
			if (BeanUtils.isNotEmpty(globalNodeSet)) {
				globalNodeSet.setIsHideOption(bpmNodeSet.getIsHideOption());
				globalNodeSet.setIsHidePath(bpmNodeSet.getIsHidePath());
				globalNodeSet.setInformType(bpmNodeSet.getInformType());
				globalNodeSet.setIsAllowMobile(bpmNodeSet.getIsAllowMobile());
				globalNodeSet.setIsJumpForDef(bpmNodeSet.getIsJumpForDef());
				globalNodeSet.setJumpType(bpmNodeSet.getJumpType());
				return globalNodeSet;
			}
		}
		return bpmNodeSet;
	}

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体(不用考虑是否有绑定表单)
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public NodeSet getByMyActDefIdNodeId(String actDefId, String nodeId) {
		NodeSet bpmNodeSet = dao.getByActDefIdNodeId(actDefId, nodeId);
		return bpmNodeSet;
	}

	public NodeSet getByMyActDefIdNodeId(String actDefId, String nodeId,
			String parentActDefId) {
		return this.dao.getByActDefIdNodeId(actDefId, nodeId, parentActDefId);
	}

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体(用于与“是否设置表单”无关的情况)
	 * 
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	public NodeSet getNodeSetByActDefIdNodeId(String actDefId, String nodeId) {
		return dao.getByActDefIdNodeId(actDefId, nodeId);
	}

	/**
	 * 判断表单是否为空。
	 * 
	 * @param bpmNodeSet
	 * @return
	 */
	private boolean isFormEmpty(NodeSet bpmNodeSet) {
	    if(bpmNodeSet==null) {
	        return true;
	    }
		short formType = bpmNodeSet.getFormType();
		Long formKey = bpmNodeSet.getFormKey();
		// 没有设置表单的情况
		if (formType == -1) {
			return true;
		}
		// 在线表单的情况
		if (formType == 0) {
			if (formKey == null || formKey == 0) {
				return true;
			}
		}
		// url表单的情况。
		else {
			String formUrl = bpmNodeSet.getFormUrl();
			if (StringUtil.isEmpty(formUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 取得某个流程定义中对应的某个节点为汇总节点的配置
	 * 
	 * @param actDefId
	 * @param joinTaskKey
	 * @return
	 */
	public NodeSet getByActDefIdJoinTaskKey(String actDefId, String joinTaskKey) {
		NodeSet bpmNodeSet = dao
				.getByActDefIdJoinTaskKey(actDefId, joinTaskKey);
		handlerNodeNameByActDefId(actDefId, bpmNodeSet);
		return bpmNodeSet;
	}

	/**
	 * 根据流程定义Id和 表单类型查询 默认表单和起始表单。
	 * 
	 * @param defId
	 * @param setType
	 *            值为(2，全局表单,3，流程业务表单)
	 * @return
	 */
	public NodeSet getBySetType(Long defId, Short setType) {
		NodeSet bpmNodeSet = dao.getBySetType(defId, setType);
		return bpmNodeSet;
	}
	
	
	 /**
     * 根据流程定义Id和 表单类型查询 默认表单和起始表单。
     * 
     * @param defId
     * @param setType
     *            值为(2，全局表单,3，流程业务表单)
     * @return
     */
    public NodeSet getBySetTypeAndActDefId(String actDefId, Short setType) {
        NodeSet bpmNodeSet = dao.getBySetTypeAndActDefId(actDefId, setType);
        return bpmNodeSet;
    }

	public NodeSet getBySetType(Long defId, Short setType, String parentActDefId) {
		NodeSet bpmNodeSet = dao.getBySetTypeAndParentActDefId(defId, setType,
				parentActDefId);
		return bpmNodeSet;
	}

	/**
	 * 根据流程定义获取当前的表单数据。
	 * 
	 * @param actDefId
	 * @return
	 */
	public List getByActDefId(String actDefId) {
		List<NodeSet> list = dao.getByActDefId(actDefId);
		for (NodeSet bpmNodeSet : list) {
			handlerNodeNameByActDefId(actDefId, bpmNodeSet);
		}
		return list;
	}

	/**
	 * 通过定义ID及节点Id更新isJumpForDef字段的设置
	 * 
	 * @param nodeId
	 * @param actDefId
	 * @param isJumpForDef
	 */
	public void updateIsJumpForDef(String nodeId, String actDefId,
			Short isJumpForDef) {
		dao.updateIsJumpForDef(nodeId, actDefId, isJumpForDef);
	}

	/**
	 * 国际化 节点名称
	 * 
	 * @param defId
	 * @param nodeSet
	 * @return
	 */
	private void handlerNodeName(Long defId, NodeSet nodeSet) {
		if (BeanUtils.isEmpty(nodeSet))
			return;
		String nodeId = nodeSet.getNodeId();

	}

	/**
	 * 国际化 节点名称
	 * 
	 * @param actDefId
	 * @param nodeSet
	 * @return
	 */
	private void handlerNodeNameByActDefId(String actDefId, NodeSet nodeSet) {
		if (BeanUtils.isEmpty(nodeSet))
			return;
		String nodeId = nodeSet.getNodeId();

	}

	private void handlerNodeList(Long defId, List<NodeSet> nodeList) {
		for (NodeSet bpmNodeSet : nodeList) {
			handlerNodeName(defId, bpmNodeSet);
		}
	}

	/**
	 * 
	 * @param defId
	 * @param parentActDefId
	 * @return
	 */
	public List<NodeSet> getByDefIdOpinion(Long defId, String parentActDefId) {
		return this.dao.getByDefIdOpinion(defId, parentActDefId);
	}

	// 获取流程定义的第一个节点。
	public NodeSet getStartBpmNodeSet(Long defId, String actDefId,
			String parentActDefId) {
		FlowNode flowNode = null;
		try {
			flowNode = NodeCache.getFirstNodeId(actDefId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String nodeId = "";
		if (flowNode != null) {
			nodeId = flowNode.getNodeId();
		}
		NodeSet firstNodeSet = this.dao.getByActDefIdNodeId(actDefId, nodeId,
				parentActDefId);
		if ((firstNodeSet != null)
				&& (!NodeSet.FORM_TYPE_NULL.equals(firstNodeSet.getFormType()))) {
			return firstNodeSet;
		}

		NodeSet globalNodeSet = this.dao.getByStartGlobal(defId);// ,
																	// parentActDefId
		if ((globalNodeSet != null)
				&& (!NodeSet.FORM_TYPE_NULL.equals(globalNodeSet.getFormType()))) {
			return globalNodeSet;
		}

		return null;
	}

	/**
	 * 根据actdefid 获取在线表单的数据。
	 * 
	 * @param actDefId
	 * @return
	 */
	@Override
	public List<? extends INodeSet> getOnlineFormByActDefId(String actDefId) {
		return this.dao.getBySqlKey("getOnlineFormByActDefId", actDefId);
	}

	/**
	 * 获取第一个节点，
	 * 
	 * @param actDefId
	 * @param actDefId
	 * @return
	 * @throws Exception
	 */
	@Override
	public FlowNode getFirstNodeIdFromCache(String actDefId) {
		try {
			return NodeCache.getFirstNodeId(actDefId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	@Override
	public NodeSet getByStartGlobal(Long defId) {
		return this.dao.getByStartGlobal(defId);
	}

	/**
	 * 根据formKey获取关联的NodeSet
	 * 
	 * @param formKey
	 * @return
	 */
	@Override
	public List<? extends INodeSet> getByFormKey(Long formKey) {
		return this.dao.getBySqlKey("getByFormKey", formKey);
	}

	/**
	 * 
	 * @param actDefId
	 * @param toFirstNode
	 * @return
	 * @throws Exception 
	 */
	public NodeSet getStartNodeSet(Long defId,String actDefId) throws Exception{
		FlowNode flowNode =NodeCache.getFirstNodeId(actDefId);
		String nodeId=flowNode.getNodeId();
		NodeSet globalNodeSet= this.dao.getByStartGlobal(defId);
		NodeSet firstNodeSet=dao.getByActDefIdNodeId(actDefId, nodeId);
		if(firstNodeSet!=null && !NodeSet.FORM_TYPE_NULL.equals(firstNodeSet.getFormType())){
			return firstNodeSet;
		}
		if(globalNodeSet!=null && !NodeSet.FORM_TYPE_NULL.equals(globalNodeSet.getFormType())){
			return globalNodeSet;
		}
		
		return null;
		
	}

	@Override
	public IFlowNode getNodeByActNodeIdFCache(String actDefId, String nodeId) {
		return NodeCache.getNodeByActNodeId(actDefId, nodeId);
	}

    /**
     * 获取所有节点
     * @param filter
     * @return
     */
    public List<?> getAllNodeSet(QueryFilter filter)
    {
        List<?> list=dao.getAllNodeSet(filter);
        filter.setForWeb();
        return list;
    }

    /**
     * @Title: updateUserLabel
     * @Description: TODO(跟新节点用户选择控件描述信息)
     * @param @param setId 节点ID
     * @param @param userLabel 用户选择控件描述信息
     * @return void 返回类型
     * @throws
     */
    public int updateUserLabel(String setId, String userLabel)
    {
        
        return dao.updateUserLabel(setId, userLabel);
    }
    
    /**
     * @Title: getJumpTypeNodeList
     * @Description: TODO(获取当前节点自定义跳转规则路径列表)
     * @param @param actdefId
     * @param @param nodeId
     * @param @return
     * @return List<Map<String,String>> 返回类型
     * @throws
     */
    public Object getJumpTypeNodeList(String actDefId, String nodeId)
    {
        
        Definition definition = this.definitionService.getById(Long.valueOf(actDefId));
        Map<String, FlowNode> map = NodeCache.getByActDefId(definition.getActDefId());
        NodeSet nodeset = this.getByDefIdNodeId(definition.getDefId(), nodeId);
        JSONArray checkedNodes = null;
        if (StringUtil.isNotEmpty(nodeset.getJumpSetting()))
        {
            checkedNodes = JSONArray.parseArray(nodeset.getJumpSetting());
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (String key : map.keySet())
        {
            FlowNode node = map.get(key);
            if (node.getNodeType().equals("startEvent") || node.getNodeType().equals("endEvent"))
            {
                continue;
            }
            else
            {
                Map<String, Object> node_ = new HashMap<String, Object>();
                String id = definition.getActDefKey() + "." + node.getNodeId();
                JSONObject checkNode = getChecked(checkedNodes, id);
                node_.put("id", id);
                //节点ID
                node_.put("nodeId", node.getNodeId());
                //节点名称
                node_.put("nodeName", node.getNodeName());
                //节点类型
                node_.put("nodeType", node.getNodeType());
                //节点对应流程key
                node_.put("flowkey", definition.getActDefKey());
                //如果是子流程节点，应该还有 所属callActivity节点key
                node_.put("callActivity", "");
                //父流程key
                node_.put("prentFlowKey", "");
                //是否已经审批
                node_.put("opinion", false);
                //流程对应的表单
                node_.put("relTab", "");
                //流程标题
                node_.put("subject", definition.getSubject());
                //是否选中
                node_.put("checked", checkNode != null ? true : false);
                //跳转名称
                node_.put("jumpName", checkNode != null ? checkNode.getString("jumpName") : "");
                if (node.getSubProcessNodes() != null && node.getSubProcessNodes().size() > 0)
                {
                    Definition subDefinition = this.definitionService.getByDefKey(node.getAttribute("subFlowKey"));
                    node_.put("children", getChildrenNode(node, definition,subDefinition, checkedNodes));
                }
                result.add(node_);
            }
        }
        return result;
    }
    
    private List<Map<String, Object>> getChildrenNode(FlowNode pnode, Definition pDefinition,Definition definition,
        JSONArray checkedNodes)
    {
        Map<String, FlowNode> map=pnode.getSubProcessNodes();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (String key : map.keySet())
        {
            FlowNode node = map.get(key);
            if (node.getNodeType().equals("startEvent") || node.getNodeType().equals("endEvent"))
            {
                continue;
            }
            else
            {
                Map<String, Object> node_ = new HashMap<String, Object>();
                String id = definition.getActDefKey() + "." + node.getNodeId();
                JSONObject checkNode = getChecked(checkedNodes, id);
                node_.put("id", id);
                //节点ID
                node_.put("nodeId", node.getNodeId());
                //节点名称
                node_.put("nodeName", node.getNodeName());
                //节点类型
                node_.put("nodeType", node.getNodeType());
                //节点对应流程key
                node_.put("flowkey", definition.getActDefKey());
                //如果是子流程节点，应该还有 所属callActivity节点key
                node_.put("callActivity", pnode.getNodeId());
                //父流程key
                node_.put("prentFlowKey", pDefinition.getActDefKey());
                //是否已经审批
                node_.put("opinion", false);
                //流程对应的表单
                node_.put("relTab", "");
                //流程标题
                node_.put("subject", definition.getSubject());
                //是否选中
                node_.put("checked", checkNode != null ? true : false);
                //跳转名称
                node_.put("jumpName", checkNode != null ? checkNode.getString("jumpName") : "");
                if (node.getSubProcessNodes() != null && node.getSubProcessNodes().size() > 0)
                {
                    Definition subDefinition = this.definitionService.getByDefKey(node.getAttribute("subFlowKey"));
                    node_.put("children", getChildrenNode(node, definition,subDefinition, checkedNodes));
                }
                result.add(node_);
            }
        }
        return result;
    }
    
    private JSONObject getChecked(JSONArray checkedNode, String id)
    {
        if(checkedNode==null){
            return null;
        }
        for (Object obj : checkedNode.toArray())
        {
            JSONObject _obj = (JSONObject)obj;
            if (_obj.getString("id").equals(id))
            {
                return _obj;
            }
        }
        return null;
    }
    
    /**
     * @Title: updateNodeJumpSetting
     * @Description: TODO(更新流程节点自定义跳转路径设置)
     * @param @param setId
     * @param @param jumpSetting
     * @return void 返回类型
     * @throws
     */
    public int updateNodeJumpSetting(String setId, String jumpSetting)
    {
        return dao.updateNodeJumpSetting(setId, jumpSetting);
        
    }

    /** 
    * @Title: getByTaskId 
    * @Description: TODO(根据流程taskid获取对应的nodeset) 
    * @param @param taskId
    * @param @return    
    * @return NodeSet    返回类型 
    * @throws 
    */
    public NodeSet getByTaskId(String taskId)
    {
        TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery()
            .taskId(taskId).singleResult();
        String actDefId = taskEntity.getProcessDefinitionId();
        String nodeId = taskEntity.getTaskDefinitionKey();
        NodeSet bpmNodeSet=this.getByActDefIdNodeId(actDefId, nodeId);
        return bpmNodeSet;
    }
}
