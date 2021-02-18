package com.cssrc.ibms.core.flow.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.TaskVarsDao;
import com.cssrc.ibms.core.flow.model.TaskVars;


/**
 * 对象功能:流程变量定义 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class TaskVarsService extends BaseService<TaskVars>
{

	@Resource
	private TaskVarsDao dao;
	public TaskVarsService()
	{
	}
	
	@Override
	protected IEntityDao<TaskVars, Long> getEntityDao() 
	{
		return dao;
	}
    /**
     * 获取本任务下的所有流程变量
     * @param queryFilter
     * @return
     */
	public List<TaskVars> getVars(QueryFilter queryFilter){
	    return dao.getTaskVars(queryFilter);
	}
	
	public void delVarsByActInstId(String actInstId){
		dao.delVarsByActInstId(actInstId);
	}
}
