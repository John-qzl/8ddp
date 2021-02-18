package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.NodeSet;

/**
 * 对象功能:节点设置DAO 开发人员:zhulongchao
 */
@Repository
public class NodeSetDao extends BaseDao<NodeSet>
{
    @SuppressWarnings("rawtypes")
    @Override
    public Class getEntityClass()
    {
        return NodeSet.class;
    }
    
    /**
     * 根据流程设置ID取流程节点设置
     * 
     * @param defId
     * @return
     */
    public List<NodeSet> getByDefId(Long defId)
    {
        return getBySqlKey("getByDefId", defId);
    }
    
    /**
     * 
     * 根据流程设置ID取流程节点设置(所有的)
     * 
     * @param defId
     * @return
     */
    public List<NodeSet> getAllByDefId(Long defId)
    {
        return getBySqlKey("getAllByDefId", defId);
    }
    
    /**
     * 根据流程的定义ID获取流程节点设置列表。
     * 
     * @param actDefId 流程定义ID。
     * @return
     */
    public List<NodeSet> getByActDef(String actDefId)
    {
        return this.getBySqlKey("getByActDef", actDefId);
    }
    
    public List<NodeSet> getByDefIdAndParentActDefId(Long defId, String parentActDefId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("parentActDefId", parentActDefId);
        return getBySqlKey("getByDefIdAndParentActDefId", params);
    }
    
    /**
     * 通过节点Id及流程定义Id获取节点设置实体
     * 
     * @param defId
     * @param nodeId
     * @return
     */
    public NodeSet getByDefIdNodeId(Long defId, String nodeId)
    {
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("nodeId", nodeId);
        
        return getUnique("getByDefIdNodeId", params);
    }
    
    public NodeSet getByDefIdNodeId(Long defId, String nodeId, String parentActDefId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("nodeId", nodeId);
        params.put("parentActDefId", parentActDefId);
        
        return getUnique("getByDefIdNodeIdAndParentActDefId", params);
    }
    
    /**
     * 通过流程发布ID及节点id获取流程设置节点实体
     * 
     * @param actDefId
     * @param nodeId
     * @return
     */
    public NodeSet getByActDefIdNodeId(String actDefId, String nodeId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actDefId", actDefId);
        params.put("nodeId", nodeId);
        
        return getUnique("getByActDefIdNodeId", params);
    }
    
    /**
     * 获取节点设置信息
     * 
     * @param actDefId 流程定义ID
     * @param nodeId 节点ID
     * @param parentActDefId 父流程定义ID
     * @return
     */
    public NodeSet getByActDefIdNodeId(String actDefId, String nodeId, String parentActDefId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actDefId", actDefId);
        params.put("nodeId", nodeId);
        params.put("parentActDefId", parentActDefId);
        return (NodeSet)getUnique("getByActDefIdNodeIdAndParentActDefId", params);
    }
    
    /**
     * 取得某个流程定义中对应的某个节点为汇总节点的配置
     * 
     * @param actDefId
     * @param joinTaskKey
     * @return
     */
    public NodeSet getByActDefIdJoinTaskKey(String actDefId, String joinTaskKey)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actDefId", actDefId);
        params.put("joinTaskKey", joinTaskKey);
        return getUnique("getByActDefIdJoinTaskKey", params);
    }
    
    /**
     * 根据formKey获取关联的NodeSet
     * 
     * @param formKey
     * @return
     */
    public List<NodeSet> getByFormKey(Long formKey)
    {
        return this.getBySqlKey("getByFormKey", formKey);
    }
    
    /**
     * 根据流程defId删除流程节点。
     * 
     * @param defId
     */
    public void delByDefId(Long defId)
    {
        this.delBySqlKey("delByDefId", defId);
    }
    
    /**
     * 根据流程定义Id和 表单类型查询 默认表单和起始表单。
     * 
     * @param defId
     * @param setType 值为(1，开始表单，2，全局表单)
     * @return
     */
    public NodeSet getBySetType(Long defId, Short setType)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("setType", setType);
        return this.getUnique("getBySetType", params);
    }
    
    public NodeSet getBySetTypeAndParentActDefId(Long defId, Short setType, String parentActDefId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("setType", setType);
        params.put("parentActDefId", parentActDefId);
        return (NodeSet)getUnique("getBySetTypeAndParentActDefId", params);
    }
    
    /**
     * 根据流程定义获取开始和全局表单的配置。
     * 
     * @param defId
     * @return
     */
    public NodeSet getByStartGlobal(Long defId)
    {
        List<NodeSet> list = this.getBySqlKey("getByStartGlobal", defId);
        if (list.size() == 0)
            return null;
        return list.get(0);
    }
    
    /**
     * 取得非节点的NODESET.
     * 
     * @param defId
     * @return
     */
    public List<NodeSet> getByOther(Long defId)
    {
        List<NodeSet> list = this.getBySqlKey("getByOther", defId);
        return list;
    }
    
    /**
     * 删除起始和缺省的表单。
     * 
     * @param defId
     */
    public void delByStartGlobalDefId(Long defId)
    {
        this.delBySqlKey("delByStartGlobalDefId", defId);
    }
    
    /**
     * 根据defid 获取节点数据，并转换为map。
     * 
     * @param defId
     * @return
     */
    public Map<String, NodeSet> getMapByDefId(Long defId)
    {
        Map<String, NodeSet> map = new HashMap<String, NodeSet>();
        List<NodeSet> list = getByDefId(defId);
        for (NodeSet bpmNodeSet : list)
        {
            map.put(bpmNodeSet.getNodeId(), bpmNodeSet);
        }
        return map;
    }
    
    /**
     * 根据actDefId获取流程节点数据。
     * 
     * <pre>
     * 这个关联了在线表单最新的表单id。
     * </pre>
     * 
     * @param actDefId
     * @return
     */
    public List<NodeSet> getByActDefId(String actDefId)
    {
        return this.getBySqlKey("getByActDefId", actDefId);
    }
    
    public List<NodeSet> getByActDefId(String actDefId, String parentActDefId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actDefId", actDefId);
        params.put("parentActDefId", parentActDefId);
        return getBySqlKey("getByActDefIdAndParentId", params);
    }
    
    /**
     * 根据actdefid 获取在线表单的数据。
     * 
     * @param actDefId
     * @return
     */
    public List<NodeSet> getOnlineFormByActDefId(String actDefId)
    {
        return this.getBySqlKey("getOnlineFormByActDefId", actDefId);
    }
    
    /**
     * 通过定义ID及节点Id更新isJumpForDef字段的设置
     * 
     * @param nodeId
     * @param actDefId
     * @param isJumpForDef
     */
    public void updateIsJumpForDef(String nodeId, String actDefId, Short isJumpForDef)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isJumpForDef", isJumpForDef);
        params.put("nodeId", nodeId);
        params.put("actDefId", actDefId);
        update("updateIsJumpForDef", params);
    }
    
    /**
     * @author yangbo 根据parentActDefId获取列表
     * @param defId
     * @param parentActDefId
     * @return
     */
    public List<NodeSet> getByDefIdOpinion(Long defId, String parentActDefId)
    {
        Map params = new HashMap();
        params.put("defId", defId);
        params.put("parentActDefId", parentActDefId);
        return getBySqlKey("getByDefIdOpinion", params);
    }
    
    /**
     * @author yangbo
     * @param actDefId 流程定义id
     * @return
     */
    public List<String> getOpinionFields(String actDefId)
    {
        Map params = new HashMap();
        params.put("actDefId", actDefId);
        return getBySqlKeyGenericity("getOpinionFields", params);
    }
    
    /**
     * 查找所有的nodeset
     * 
     * @param filter
     * @return
     */
    public List<?> getAllNodeSet(QueryFilter filter)
    {
        return this.getBySqlKey("getAllNodeSet", filter);
    }
    
    /**
     * 根据流程定义Id和 表单类型查询 默认表单和起始表单。
     * 
     * @param defId
     * @param setType 值为(1，开始表单，2，全局表单)
     * @return
     */
    public NodeSet getBySetTypeAndActDefId(String actDefId, Short setType)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actDefId", actDefId);
        params.put("setType", setType);
        return this.getUnique("getBySetTypeAndActDefId", params);
    }


    /** 
    * @Title: updateUserLabel 
    * @Description: TODO(跟新节点用户选择控件描述信息) 
    * @param @param setId 节点ID
    * @param @param userLabel    用户选择控件描述信息 
    * @return void    返回类型 
    * @throws 
    */
    public int updateUserLabel(String setId, String userLabel)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("setId", setId);
        params.put("userLabel", userLabel);
        return this.update("updateUserLabel", params);
    }

    /** 
    * @Title: updateNodeJumpSetting 
    * @Description: TODO(更新流程节点自定义跳转路径设置) 
    * @param @param setId
    * @param @param jumpSetting   
    * @return void    返回类型 
    * @throws 
    */
    public int updateNodeJumpSetting(String setId, String jumpSetting)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("setId", setId);
        params.put("jumpSetting", jumpSetting);
        return this.update("updateNodeJumpSetting", params);
    }
    
}