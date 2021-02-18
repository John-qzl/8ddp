
package com.cssrc.ibms.core.flow.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.ProStatus;
/**
 * 对象功能:流程节点状态 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class ProStatusDao extends BaseDao<ProStatus>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return ProStatus.class;
	}
	
	/**
	 * 根据流程实例ID获取流程状态数据。
	 * @param instanceId	流程实例ID。
	 * @return
	 */
	public List<ProStatus> getByActInstanceId(String instanceId){
		List<ProStatus> list=this.getBySqlKey("getByActInstanceId", instanceId);
		return list;
	}
	
	/**
	 * 根据流程实例ID判断是否状态是否存在。
	 * @param instanceId
	 * @return
	 */
	public boolean isExistByInstanceId(Long instanceId){
		Integer rtn=(Integer)this.getOne("isExistByInstanceId", instanceId);
		return rtn>0;
	}
	
	/**
	 * 根据runId和taskId获取任务的状态
	 * @param runId
	 * @param nodeId
	 * @return
	 */
	public Integer getStatusByRunidNodeid(String runId,String nodeId){
		Map map = new HashMap();		
		map.put("runId", runId);
		map.put("nodeId", nodeId);
		Integer rtn=(Integer)this.getOne("getStatusByRunidNodeid", map);
		return rtn;
	}
	
	/**
	 * 更新节点的状态。
	 * @param actInstanceId		流程实例ID。
	 * @param nodeId			流程节点ID。
	 * @param status			状态。
	 */
	public void updStatus(Long actInstanceId,String nodeId,Short status){
		ProStatus bpmProStatus=new ProStatus();
		bpmProStatus.setActinstid(actInstanceId);
		bpmProStatus.setNodeid(nodeId);
		bpmProStatus.setStatus(status);
		bpmProStatus.setLastupdatetime(new Date());
		this.update("updStatus", bpmProStatus);
	}
	
	/**
	 * 根据流程实例和任务节点Id获取流程状态。
	 * @param instanceId
	 * @param nodeId
	 * @return
	 */
	public ProStatus getByInstNodeId(Long instanceId,String nodeId)
	{
		Map params=new HashMap();
		params.put("actinstid", instanceId);
		params.put("nodeid", nodeId);
		ProStatus rtn = (ProStatus)getOne("getByInstNodeId", params);
	    return rtn;
	}
	
	/**
	 * 根据act流程定义Id删除流程节点状态
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}

	public void delByActInstId(Long actInstId) {
		delBySqlKey("delByActInstId", actInstId);
		
	}
	
	/**
        * 描述:  根据流程实例删除数据
        * @param actInstId  
        * @author wzh  
		* @version 1.0  
        * @since 2013年11月21日 下午2:24:26
	 */
	public void delByProcInstId(Long procInstId) {
		delBySqlKey("delByProcInstId", procInstId);		
	}
}