package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.TaskExe;

/**
 *<pre>
 * 对象功能:任务转办代理 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class TaskExeDao extends BaseDao<TaskExe>
{
	@Override
	public Class<?> getEntityClass()
	{
		return TaskExe.class;
	}

	/**
	 * 根据任务ID获得任务转办代理
	 * @param taskId 任务ID
	 * @return
	 */
	public List<TaskExe> getByTaskId(Long taskId) {
		return this.getBySqlKey("getByTaskId", taskId);	
	}

	/**
	 * 根据任务ID获得任务转办代理
	 * @param taskId 任务ID
	 * @return
	 */
	public TaskExe getByTaskIdStatus(Long taskId, Short status) {
		Map<String,Object> map =  new HashMap<String,Object>();
		map.put("taskId", taskId);
		map.put("status", status);
		return this.getUnique("getByTaskIdStatus", map);
	}

	public List<TaskExe> getByRunId(Long runId) {
		return this.getBySqlKey("getByRunId", runId);
	}

	public List<TaskExe> getByActInstId(Long actInstId) {
		return this.getBySqlKey("getByActInstId", actInstId);
	}

	public List<TaskExe> accordingMattersList(QueryFilter filter) {
		return getBySqlKey("accordingMattersList", filter);
	}
	
	/**
	 * 根据任务id获取是否已经转办。
	 * @param taskId
	 * @return
	 */
	public Integer getByIsAssign(Long taskId){
		return (Integer) this.getOne("getByIsAssign", taskId);
	}

	public int delByRunId(Long runId) {
		return this.delBySqlKey("delByRunId", runId);
	}
	
	public List<TaskExe> accordingMattersList(Long ownerId, PagingBean pb) {
		Map params = new HashMap();
		params.put("ownerId", ownerId);
		return getBySqlKey("accordingMattersList", params, pb);
	}

}