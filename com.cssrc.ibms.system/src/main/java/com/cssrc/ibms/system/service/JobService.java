package com.cssrc.ibms.system.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.job.intf.IJobService;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.JobDao;
import com.cssrc.ibms.system.model.Job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
/**
 * 职务业务层
 * <p>Title:JobService</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:17:51
 */
@Service
public class JobService extends BaseService<Job> implements IJobService{

	@Resource
	private JobDao dao;

	@Resource
	private IUserPositionService userPositionService;

	@Resource
	private IPositionService positionService;

	protected IEntityDao<Job, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 删除职务同时相应用户的此岗位职务删除
	 * @param id
	 */
	public void deleteByUpdateFlag(Long id) {
		Long currentUserId = UserContextUtil.getCurrentUserId();
		this.dao.deleteByUpdateFlag(id);
		this.userPositionService.delByJobId(id);
		this.positionService.deleByJobId(id,currentUserId);
	}
	/**
	 * 判断职务名称是否存在
	 * @param jobCode
	 * @return
	 */
	public boolean isExistJobCode(String jobCode) {
		return this.dao.isExistJobCode(jobCode);
	}

	public boolean isExistJobCodeForUpd(String jobCode, Long jobId) {
		return this.dao.isExistJobCodeForUpd(jobCode, jobId);
	}

	public Job getByJobCode(String jobCode) {
		return this.dao.getByJobCode(jobCode);
	}
}
