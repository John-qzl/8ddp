package com.cssrc.ibms.core.flow.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IDefVarService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.DefVarDao;
import com.cssrc.ibms.core.flow.model.DefVar;


/**
 * 对象功能:流程变量定义 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class DefVarService extends BaseService<DefVar> implements IDefVarService
{
	@Resource
	private DefVarDao dao;
	

	
	public DefVarService()
	{
	}
	
	@Override
	protected IEntityDao<DefVar, Long> getEntityDao() 
	{
		return dao;
	}
	
	public boolean isVarNameExist(String varName,String varKey,Long defId){
		return dao.isVarNameExist(varName,varKey,defId);
	}
	
	/**
	 * 根据流程发布和节点id取得流程变量列表。
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	public List<DefVar> getByDeployAndNode(String deployId,String nodeId){
		return dao.getByDeployAndNode(deployId, nodeId);
		
	}
	
	/**
	 *  根据流程定义ID获取流程变量。
	 * @param defId
	 * @return
	 */
	public List<DefVar> getVarsByFlowDefId(long defId){
		return dao.getVarsByFlowDefId(defId);
	}
	

	
}
