package com.cssrc.ibms.api.job.intf;
import java.util.List;

import com.cssrc.ibms.api.job.model.IJob;

public interface IJobService{

	/**
	 * 删除职务同时相应用户的此岗位职务删除
	 * @param id
	 */
	public abstract void deleteByUpdateFlag(Long id);

	/**
	 * 判断职务名称是否存在
	 * @param jobCode
	 * @return
	 */
	public abstract boolean isExistJobCode(String jobCode);

	public abstract boolean isExistJobCodeForUpd(String jobCode, Long jobId);

	public abstract IJob getByJobCode(String jobCode);

	public abstract IJob getById(Long jobId);

	public abstract List<?extends IJob> getAll();

}