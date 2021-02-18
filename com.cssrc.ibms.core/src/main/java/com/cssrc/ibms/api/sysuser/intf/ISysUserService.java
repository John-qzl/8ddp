package com.cssrc.ibms.api.sysuser.intf;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

public interface ISysUserService{

	public abstract ISysUser findByUserId(Long userId);

	public abstract ISysUser getByUsername(String username);

	/**
	 * 未删除的用户
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getUserByQuery(QueryFilter queryFilter);

	/**
	 * 字段查询用户列表
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getUsersByQuery(QueryFilter queryFilter);

	/**
	 * roleId查找用户
	 * @param roleId
	 * @return
	 */
	public abstract List<Long> getUserIdsByRoleId(Long roleId);

	/**
	 * 具有某角色下的所有用户
	 * @param roleId
	 * @return
	 */
	public abstract List<?extends ISysUser> getByRoleId(Long roleId);

	/**
	 * no主要岗位的用户
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getUserNoOrg(QueryFilter queryFilter);

	/**
	 * 获得指定username的用户信息列表
	 * @param accounts
	 * @return
	 */
	public abstract List<?extends ISysUser> getByUsernames(String usernames);

	/**
	 * 某岗位下所有用户
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getDistinctUserByPosPath(
			QueryFilter queryFilter);

	/**
	 * 通过isprimary获得相关组织用户
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getDistinctUserByOrgPath(
			QueryFilter queryFilter);

	/**
	 * 判断用户是否存在
	 * @param username
	 * @return
	 */
	public abstract boolean isUsernameExist(String username);

	/**
	 * 是否存在一个用户多条数据
	 * @param userId
	 * @param account
	 * @return
	 */
	public abstract boolean isUsernameExistForUpd(Long userId, String username);

	/**
	 * 用户自定义参数查询
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	public abstract List<?extends ISysUser> getByUserParam(String userParam)
			throws Exception;

	/**
	 * 组织自定义参数查询
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	public abstract List<?extends ISysUser> getByOrgParam(String userParam)
			throws Exception;


	/**
	 * 用户id获取其上下级信息
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysUser> getByUserIdAndUplow(long userId);

	/**
	 * userid该集合范围内存在的用户
	 * @param idSet
	 * @return
	 */
	public abstract List<?extends ISysUser> getByIdSet(Set uIds);
	/**
     * ids范围内存在的用户,ID 之间用， 分割
     * @param idSet
     * @return
     */
	public abstract List<?extends ISysUser> getByIdSet(String ids);


	public abstract ISysUser getByMail(String address);

	public abstract List<?extends ISysUser> getAllIncludeOrg();

	/**
	 * 修改用户密码
	 * @param userId
	 * @param pwd
	 */
	public abstract void updPwd(Long userId, String pwd);

	/**
	 * 更新状态(status:1=激活 0=禁用 2=离职)
	 * @param userId
	 * @param status
	 */
	public abstract void updStatus(Long userId,Long currentUserId, Short status);

	/**	
	 * 更新status状态
	 * (status:1=激活 0=禁用 2=离职)
	 * @param username
	 * @param status
	 */
	public abstract void updStatus(String username,Long currentUserId, Short status);

	/**
	 * 查询某角色下的用户列表
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getUserByRoleId(QueryFilter queryFilter);

	/**
	 * 担任某组织岗位和具有某角色身份的用户(包括之前担任的)
	 * @param roleId
	 * @param orgId
	 * @return
	 */
	public abstract List<?extends ISysUser> getUserByRoleIdOrgId(Long roleId, Long orgId);

	/**
	 * 担任某岗位的用户(posid已经唯一)
	 * @param orgId
	 * @param posId
	 * @return
	 */
	public abstract List<?extends ISysUser> getByOrgIdPosId(Long orgId, Long posId);

	/**
	 * 某组织下的用户列表
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getDistinctUserByOrgId(QueryFilter queryFilter);

	/**
	 * 根据岗位id查询用户
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysUser> getDistinctUserByPosId(QueryFilter queryFilter);

	/**
	 * 职务id查询用户列表
	 * @param jobId
	 * @return
	 */
	public abstract List<?extends ISysUser> getUserListByJobId(Long jobId);

	/**
	 * posid的岗位下用户
	 * @param posId
	 * @return
	 */
	public abstract List<?extends ISysUser> getUserListByPosId(Long posId);

	/**
	 * email作为查询条件
	 * @param email
	 * @return
	 */
	public abstract List<?extends ISysUser> findLinkMan(String email);
	

	/**
	 * 根据组织id获取所有用户
	 * @param orgId
	 * @return
	 */
	public abstract List<?extends ISysUser> getByOrgId(Long orgId);

	/**
	 * 获得某岗位下所有用户
	 * @param posId
	 * @return
	 */
	public abstract List<?extends ISysUser> getByPosId(Long posId);
	/**
	 * 某流程所有执行人的用户信息
	 * @param actInstId
	 * @return
	 */
	public abstract List<?extends ISysUser> getExeUserByInstnceId(Long actInstId);

	/**
	 * 角色集合内相关用户
	 * @param list
	 * @return
	 */
	public abstract List<?extends ISysUser> getByRoleIds(List list);
	/**
	 * 担任某些岗位主要负责人并作为主岗位的用户
	 * @param list
	 * @return
	 */
	public abstract List<?extends ISysUser> getByPos(List list);
	/**
	 * 组织集合相关用户
	 * @param list
	 * @return
	 */
	public abstract List<?extends ISysUser> getByOrgIds(List list);
	/**
	 * 组织集合相关的组织直接领导人
	 * @param list
	 * @return
	 */
	public abstract List<?extends ISysUser> getMgrByOrgIds(List list);

	/**
	 * 
	 * @param orgId
	 * @return
	 */
	public abstract List<?extends ISysUser> getDirectLeaderByOrgId(Long orgId);

	/**
	 * 与发起人相同职务的用户
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysUser> getSameJobUsersByUserId(Long userId);
	/**
	 * 与发起人相同岗位的用户
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysUser> getSamePositionUsersByUserId(Long userId);
	/**
	 * 发起人或上一任务发起人节点用户上下级用户获取
	 * @param userId
	 * @param nodeUser
	 * @return
	 */
	public abstract List<?extends ISysUser> getByUserIdAndUplow(Long userId,
			String cmpIds);

	/**
	 * 担任主要岗位的相关组织的
	 * @param userId
	 * @return
	 */
	public abstract List getOrgMainUser(Long userId);
	/**
	 * 对应公司组织主管用户
	 * @param companyId
	 * @param roleId
	 * @param isCharge
	 * @return
	 */
	public abstract List<?extends ISysUser> getByCompanyRole(Long companyId, Long roleId,
			boolean isCharge);
	/**
	 * 同时在某组织下任职和担任某岗位的用户
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public abstract List getByOrgRole(Long orgId, Long roleId);

	public abstract ISysUser getById(Long userId);
	/**
	 * 获得上级用户列表
	 * @param userId
	 * @return
	 */
	public abstract List<? extends ISysUser> getUserByUnderUserId(Long userId);

	public abstract List<? extends ISysUser> getAll();
	
	/**
	 * 更新sysuser出错
	 * @param sysUser
	 */
	public abstract void updateFailure(Long userId, String loginFailures,Date lastFailureTime);
	
	/**
	 * 更新sysuser锁定
	 * @param sysUser
	 */
	public abstract void updLock(Long userId, String loginFailures,short lockState,Date lastFailureTime,Date lockTime);

    /**
     * 查找 某个组织下某个职务的用户
     * @param orgId 组织ID
     * @param jobId 职务ID
     * @return
     */
    public abstract List<? extends ISysUser> getSameOrgJobUsers(Long orgId, Long jobId);

    /**
     * 构造一个user
     * @param userId
     * @return
     */
    public abstract ISysUser newUser(Long userId);

    /**
     * 获得某职务下所有用户
     * @param posId
     * @return
     */
    List<? extends ISysUser> getByJobId(Long jobId);
    
    /**
     * 将所有用户放到redis中
     */
    public void setAllSysUserToRedis();

    /**
     * 通过中文名来返回对应的记录
     * @param fullname 中文名
     * @return
     */
    public List<? extends ISysUser> getByFullname(String fullname);
    
    

    /**
     * 担任posid岗位的用户名数组
     * @param posId
     * @return
     */
    public abstract String getUsernamesByPosId(Long posId);
    
}