
package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.RunLog;
/**
 * 对象功能:流程运行日志 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class RunLogDao extends BaseDao<RunLog>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return RunLog.class;
	}
	
	/**
	 * 通过用户ID获取用户操作的流程日志
	 * @param userId 用户ID
	 * @return
	 */
	public List<RunLog> getByUserId(Long userId){		
		List list=getBySqlKey("getByUserId",userId);
		return list;
	}
	
	/**
	 * 通过流程运行ID获取流程的操作日志
	 * @param runId 流程运行ID
	 * @return
	 */
	public List<RunLog> getByRunId(Long runId){		
		List list=getBySqlKey("getByRunId",runId);
		return list;
	}
	
	/**
	 * 根据流程运行ID删除流程操作日志
	 * @param runId
	 */
	public void delByRunId(Long runId) {
		this.delBySqlKey("delByRunId", runId);
	}
}