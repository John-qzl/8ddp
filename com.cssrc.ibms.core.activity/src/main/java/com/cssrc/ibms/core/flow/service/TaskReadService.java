package com.cssrc.ibms.core.flow.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.TaskReadDao;
import com.cssrc.ibms.core.flow.model.TaskRead;


/**
 *<pre>
 * 对象功能:任务是否已读 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class TaskReadService extends BaseService<TaskRead>
{
	@Resource
	private TaskReadDao dao;
	
	
	
	public TaskReadService()
	{
	}
	
	@Override
	protected IEntityDao<TaskRead, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 添加查看记录
	 * @param actInstId		流程实例ID
	 * @param taskId		任务ID
	 */
	public void saveReadRecord(Long actInstId,Long taskId){
		ISysUser sysUser=(ISysUser)UserContextUtil.getCurrentUser();
		Long userId=sysUser.getUserId();
		if(dao.isTaskRead(taskId, userId)) return;
		
		TaskRead taskRead=new TaskRead();
		taskRead.setId(Long.valueOf(UniqueIdUtil.genId()));
		taskRead.setActinstid(actInstId);
		taskRead.setTaskid(taskId);
		taskRead.setUserid(userId);
		taskRead.setUsername(sysUser.getFullname());
		taskRead.setCreatetime(new Date());
		dao.add(taskRead);
	}
	
	/**
	 * 判断任务是否已读
	 * @param taskId 任务ID
	 * @param userId 用户ID
	 * @return
	 */
	public boolean isTaskRead(Long taskId,Long userId){
		return dao.isTaskRead(taskId, userId);
	}
	
	public List<TaskRead> getTaskRead(Long actInstId,Long taskId){
		return dao.getTaskRead(actInstId,taskId);
	}

	public void delByActInstId(Long actInstId) {
		dao.delByActInstId(actInstId);
	}
}
