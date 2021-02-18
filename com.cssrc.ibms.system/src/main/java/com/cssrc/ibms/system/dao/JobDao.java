package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.Job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 职务DAO
 * <p>Title:JobDao</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:15:52
 */
@Repository
public class JobDao extends BaseDao<Job> {
	public Class<?> getEntityClass() {
		return Job.class;
	}
	//删除职务(弱删除，isdelete变成1)
	public void deleteByUpdateFlag(Long id) {
		update("deleteByUpdateFlag", id);
	}
	//获得该用户所有职务
	public List<Job> getByUserId(Long userId) {
		return getBySqlKey("getByUserId", userId);
	}
	public Job getByJobCode(String jobCode) {
		return (Job) getUnique("getByJobCode", jobCode);
	}
	//判断职务名称是否存在
	public boolean isExistJobCode(String jobCode) {
		Integer count = (Integer) getOne("isExistJobCode", jobCode);
		return count.intValue() > 0;
	}

	public boolean isExistJobCodeForUpd(String jobCode, Long jobId) {
		Map params = new HashMap();
		params.put("jobCode", jobCode);
		params.put("jobid", jobId);
		Integer count = (Integer) getOne("isExistJobCodeForUpd", params);
		return count.intValue() > 0;
	}
}
