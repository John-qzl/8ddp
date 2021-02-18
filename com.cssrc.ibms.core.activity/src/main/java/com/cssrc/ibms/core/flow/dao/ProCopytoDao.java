package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.ProCopyto;

@Repository
public class ProCopytoDao extends BaseDao<ProCopyto> {
	@Override
	public Class<?> getEntityClass() {
		return ProCopyto.class;
	}

	public List<ProCopyto> getMyList(QueryFilter queryFilter) {
		return this.getBySqlKey("getMyList", queryFilter);
	}

	public List<ProCopyto> getByRunId(Long runId) {
		return this.getBySqlKey("getByRunId", runId);
	}

	public List<ProCopyto> getByActInstId(Long actDeployId) {
		return this.getBySqlKey("getByActInstId", actDeployId);
	}

	/**
	 * 显示某个流程实例的抄送人员数据。
	 * 
	 * @param queryFilter
	 * @return
	 */
	public List<ProCopyto> getUserInfoByRunId(QueryFilter queryFilter) {
		return this.getBySqlKey("getUserInfoByRunId", queryFilter);
	}

	/**
	 * 通过运行ID和用户ID 获取抄送记录
	 * 
	 * @param runId
	 * @param userId
	 * @return
	 */
	public List<ProCopyto> getByRunIdAndUserId(Long runId, Long userId) {
		Map<String, Long> param = new HashMap<String, Long>();
		param.put("runId", runId);
		param.put("userId", userId);
		return this.getBySqlKey("getByRunIdAndUserId", param);
	}

	public int delByRunId(Long runId) {
		return this.delBySqlKey("delByRunId", runId);
	}

	/**
	 * 通过抄送人id获得数量
	 * @author Yangbo 2016-7-22
	 * @param userId
	 * @return
	 */
	public Integer getCountByUser(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ccUid", userId);
		return (Integer) getOne("getCountByUser", params);
	}
	/**
	 * 抄送人未阅读的数量
	 * @param userId
	 * @return
	 */
	public Integer getCountNotReadByUser(Long userId)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ccUid", userId);
		return (Integer) getOne("getCountNotReadByUser", params);
	}
	
	/**
	 * 按分页显示抄送数据
	 * @param curUserId
	 * @param pb
	 * @return
	 */
	public List<ProCopyto> getMyProCopytoList(Long curUserId, PagingBean pb) {
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("userId", curUserId);
		return this.getBySqlKey("getMyProCopytoList",params, pb );
	}
}