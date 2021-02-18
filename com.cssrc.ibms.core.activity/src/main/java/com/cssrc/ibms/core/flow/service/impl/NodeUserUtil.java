package com.cssrc.ibms.core.flow.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;

public class NodeUserUtil {
	
	/**
	 * 根据用户返回执行人员。
	 * @param sysUsers
	 * @param extraceUser
	 * @return
	 */
	public static Set<TaskExecutor> getExcutorsByUsers(List<?extends ISysUser> sysUsers,int extraceUser){
		Set<TaskExecutor> uIdSet = new LinkedHashSet<TaskExecutor>();
		if(BeanUtils.isEmpty(sysUsers))
			return uIdSet;
		if(sysUsers.size()==0)
			return uIdSet;
		if(sysUsers.size()==1){
			ISysUser sysUser=sysUsers.get(0);
			uIdSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),sysUser.getFullname()));
			return uIdSet;
		}
		//用户组
		if(extraceUser==TaskExecutor.EXACT_USER_GROUP){
			String userIds="";
			String userNames="";
			for(int i=0;i<sysUsers.size();i++){
				if(i>0){
					userIds+=",";
					userNames+=",";
				}
				userIds+=sysUsers.get(i).getUserId();
				userNames+=sysUsers.get(i).getFullname();
			}
			TaskExecutor executor=TaskExecutor.getTaskUserGroup(userIds, userNames);
			uIdSet.add(executor);
		}
		else{
			for (ISysUser sysUser : sysUsers) {
				uIdSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),sysUser.getFullname()));
			}
		}
		return uIdSet;
	}
	
	/**
	 * 根据taskExecutor 获取执行人员。
	 * @param taskExecutor
	 * @return
	 */
	public static List<?extends ISysUser> getUserListByExecutor(TaskExecutor taskExecutor){
		ISysUserService sysUserService=(ISysUserService)AppUtil.getBean(ISysUserService.class);
		List<?extends ISysUser> userList=null;
		if(TaskExecutor.USER_TYPE_ROLE.equals(taskExecutor.getType())){
			userList= sysUserService.getByRoleId(Long.parseLong(taskExecutor.getExecuteId()));
		}
		else if(TaskExecutor.USER_TYPE_ORG.equals(taskExecutor.getType())){
			userList= sysUserService.getByOrgId(Long.parseLong(taskExecutor.getExecuteId()));
		}
		else if(TaskExecutor.USER_TYPE_POS.equals(taskExecutor.getType())){
			userList= sysUserService.getByPosId(Long.parseLong(taskExecutor.getExecuteId()));
		}
		
		return userList;
	}
	
	

}
