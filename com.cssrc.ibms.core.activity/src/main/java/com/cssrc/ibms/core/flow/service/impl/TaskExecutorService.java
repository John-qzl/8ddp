package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.model.IUserUnder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 用户组织岗位关系Service层
 * <p>Title:UserPositionService</p>
 * @author Yangbo 
 * @date 2016-8-2下午04:26:09
 */
@Service
public class TaskExecutorService{
	@Resource
	private IUserPositionService userPositionService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private IPositionService positionService;
	
	
	/**
	 * 获取用户userId的组织的直属领导
	 * @param userId
	 * @return
	 */
	public List<TaskExecutor> getLeaderByUserId(Long userId) {
		Long startUserId = UserContextUtil.getCurrentUserId();
		ISysOrg sysOrg = null;

		if (userId != startUserId) {
			////根据用户userid获取主组织
			sysOrg = this.sysOrgService.getPrimaryOrgByUserId(userId);
		} else{
			sysOrg = (ISysOrg) UserContextUtil.getCurrentOrg();
		}
			
		List list = new ArrayList();
		if (sysOrg == null) {
			return list;
		}

		Long orgId = sysOrg.getOrgId();

		IUserPosition up = this.userPositionService.getPrimaryUserPositionByUserId(userId);
		//当前用户已经是负责人，则找他上级组织的负责人 。
		if (up.getIsCharge() == IUserPosition.CHARRGE_YES.intValue()) {
			//找他的上级组织
			ISysOrg sysOrgParent = (ISysOrg) this.sysOrgService.getById(orgId);
			//找他的上级组织的负责人
			return getLeaderByOrgId(sysOrgParent.getOrgSupId());
		}
		//如果当前用户不是负责人，则查找当前组织的负责人。
		return getLeaderByOrgId(orgId);
	}
	
	/**
	 * 获取组织负责人
	 * @param orgId
	 * @return
	 */
	public List<TaskExecutor> getLeaderByOrgId(Long orgId) {
		//根据组织orgid获取组织负责人
		List<?extends IUserPosition> list = this.userPositionService.getChargeByOrgId(orgId);
		if (BeanUtils.isNotEmpty(list)) {
			List users = new ArrayList();
			for (IUserPosition userPosition : list) {
				// 获取任务用户。
				TaskExecutor taskExecutor = TaskExecutor.getTaskUser(userPosition.getUserId().toString(), userPosition.getFullname());
				users.add(taskExecutor);
			}
			return users;
		}
		//获取上级组织的负责人
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(orgId);
		if (sysOrg == null)
			return new ArrayList();
		Long parentOrgId = sysOrg.getOrgSupId();
		ISysOrg sysOrgParent = (ISysOrg) this.sysOrgService.getById(parentOrgId);
		if (sysOrgParent == null) {
			return new ArrayList();
		}
		
		return getLeaderByOrgId(parentOrgId);
	}
	
	/**
	 * 获得用户岗位主管人
	 * @param userId
	 * @return
	 */
	public String getLeaderPosByUserId(Long userId) {
		IUserPosition up = this.userPositionService.getPrimaryUserPositionByUserId(userId);
		Long uId = Long.valueOf(0L);
		if (up == null) {
			return null;
		}
		ISysOrg sysOrg = (ISysOrg) this.sysOrgService.getById(up.getOrgId());
		if (up.getIsCharge() == IUserPosition.CHARRGE_YES.intValue()) {
			List list = getLeaderByOrgId(sysOrg.getOrgSupId());

			if (BeanUtils.isNotEmpty(list)) {
				String tmpUserId = ((TaskExecutor) list.get(0)).getExecuteId();
				uId = Long.valueOf(Long.parseLong(tmpUserId));
				return this.positionService.getPosByUserId(uId).getPosName();
			}
		} else {
			List list = getLeaderByOrgId(up.getOrgId());
			if (BeanUtils.isNotEmpty(list)) {
				String tmpUserId = ((TaskExecutor) list.get(0)).getExecuteId();
				uId = Long.valueOf(Long.parseLong(tmpUserId));
				return this.positionService.getPosByUserId(uId).getPosName();
			}
		}

		return null;
	}
	/**
	 * 获得上司执行人列表
	 * @param userId
	 * @return
	 */
	public Set<TaskExecutor> getMyLeader(List<?extends IUserUnder> userList) {
		Set list = new HashSet();
		//List<UserUnder> userList = this.dao.getMyLeader(userId);
		for (IUserUnder user : userList) {
			list.add(TaskExecutor.getTaskUser(user.getUserid().toString(), user.getLeaderName()));
		}
		return list;
	}
}
