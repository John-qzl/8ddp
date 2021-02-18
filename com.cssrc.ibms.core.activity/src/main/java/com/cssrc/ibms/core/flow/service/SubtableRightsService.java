package com.cssrc.ibms.core.flow.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.ISubtableRightsService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.SubtableRightsDao;
import com.cssrc.ibms.core.flow.model.SubtableRights;

/**
 *<pre>
 * 对象功能:子表权限 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class SubtableRightsService extends BaseService<SubtableRights> implements ISubtableRightsService
{
	@Resource
	private SubtableRightsDao dao;
	
	public SubtableRightsService()
	{
	}
	
	@Override
	protected IEntityDao<SubtableRights, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public SubtableRights getByDefIdAndNodeId(String actDefId, String nodeId,Long tableId, String parentActDefId){
		return dao.getByDefIdAndNodeId(actDefId, nodeId, tableId,parentActDefId);
	}
	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	public SubtableRights getByDefIdAndNodeId(String actDefId, String nodeId,Long tableId){
		return dao.getByDefIdAndNodeId(actDefId, nodeId, tableId);
	}

	@Override
	public void delByActDefId(String actDefId) {
		this.dao.delByActDefId(actDefId);
	}
}
