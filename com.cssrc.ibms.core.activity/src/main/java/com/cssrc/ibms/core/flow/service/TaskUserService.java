package com.cssrc.ibms.core.flow.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.job.intf.IJobService;
import com.cssrc.ibms.api.job.model.IJob;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.TaskUserDao;
import com.cssrc.ibms.core.flow.model.TaskUser;
import com.cssrc.ibms.core.util.bean.BeanUtils;


@Service
public class TaskUserService extends BaseService<TaskUser>{	
	@Resource
	private TaskUserDao taskUserDao;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private IPositionService positionService;
	@Resource
	private ISysRoleService sysRoleService; 
	@Resource
	private IJobService jobService;
	
	@Override
	protected IEntityDao<TaskUser, Long> getEntityDao(){
		return taskUserDao;
	}
	
	public List<TaskUser> getByTaskId(String taskId){
		
		return taskUserDao.getByTaskId(taskId);
	}
	
	/**
	 * 取得任务的候选用户
	 * @param taskId
	 * @return
	 */
	public Set<TaskExecutor> getCandidateExecutors(String taskId){
		
		Set<TaskExecutor> taskUserSet=new HashSet<TaskExecutor>();
		List<TaskUser> taskUsers=getByTaskId(taskId);
		for(TaskUser taskUser:taskUsers){
			if(taskUser.getUserId()!=null){
				ISysUser sysUser=sysUserService.getById(new Long(taskUser.getUserId()));
				if(BeanUtils.isNotEmpty(sysUser))
				taskUserSet.add(TaskExecutor.getTaskUser(taskUser.getUserId(), sysUser.getFullname()));
			}else if(taskUser.getGroupId()!=null){
				String tmpId=taskUser.getGroupId();
				if(TaskExecutor.USER_TYPE_ORG.equals(taskUser.getType())){//组织下的用户
					ISysOrg sysOrg=sysOrgService.getById(new Long(tmpId));
					taskUserSet.add(TaskExecutor.getTaskOrg(tmpId, sysOrg.getOrgName()));
				}else if(TaskExecutor.USER_TYPE_POS.equals(taskUser.getType())){//岗位下的用户
					IPosition position=positionService.getById(new Long(tmpId));
					taskUserSet.add(TaskExecutor.getTaskPos(tmpId, position.getPosName()));
				}else if(TaskExecutor.USER_TYPE_ROLE.equals(taskUser.getType())){//角色下的用户
					ISysRole sysRole=sysRoleService.getById(new Long(tmpId));
					taskUserSet.add(TaskExecutor.getTaskRole(tmpId, sysRole.getRoleName()));
				}else if(TaskExecutor.USER_TYPE_JOB.equals(taskUser.getType())){//职务下的用户
					IJob job=jobService.getById(new Long(tmpId));
					taskUserSet.add(TaskExecutor.getTaskJob(tmpId, job.getJobname()));
				}
			}
		}
		return taskUserSet;
	}
	
	/**
	 * 根据任务Id获取任务候选人。
	 * @param taskId
	 * @return
	 */
	public Set<?extends ISysUser> getCandidateUsers(String taskId){
		Set<ISysUser> taskUserSet=new HashSet<ISysUser>();
		List<TaskUser> taskUsers=getByTaskId(taskId);
		for(TaskUser taskUser:taskUsers){
			if(taskUser.getUserId()!=null){
				ISysUser sysUser=sysUserService.getById(Long.parseLong( taskUser.getUserId()));
				taskUserSet.add(sysUser);
			}else if(taskUser.getGroupId()!=null){
				Long tmpId=Long.parseLong(taskUser.getGroupId());
				if(TaskExecutor.USER_TYPE_ORG.equals(taskUser.getType())){//组织下的用户
					List<?extends ISysUser> userList= sysUserService.getByOrgId(tmpId);
					taskUserSet.addAll(userList);
				}else if(TaskExecutor.USER_TYPE_POS.equals(taskUser.getType())){//岗位下的用户
					List<?extends ISysUser> userList= sysUserService.getByPosId(tmpId);
					taskUserSet.addAll(userList);
				}else if(TaskExecutor.USER_TYPE_ROLE.equals(taskUser.getType())){//角色下的用户
					List<?extends ISysUser> userList=sysUserService.getByRoleId(tmpId);
					taskUserSet.addAll(userList);
				}
			}
		}
		return taskUserSet;
	}
}
