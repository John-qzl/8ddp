package com.cssrc.ibms.core.user.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 用户DAO
 * <p>Title:SysUserDao</p>
 * @author Yangbo 
 * @date 2016-8-18下午03:06:05
 */
@Repository
public class SysUserDao extends BaseDao<SysUser> implements UserDetailsService {

	@Resource
	SysRoleDao sysRoleDao;

	public Class getEntityClass() {
		return SysUser.class;
	}
	/**
	 * 登陆用户
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		SysUser sysUser = getByUsername(username);
		if (sysUser == null)
			throw new UsernameNotFoundException("用户不存在");
		return sysUser;
	}

	public SysUser getByUsername(String username) {
		SysUser sysUser = (SysUser) getUnique("getByUsername", username);
		return sysUser;
	}
	/**
	 * 没有主要岗位的用户（有些用户有多个岗位但是并没有固定的）
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserNoOrg(QueryFilter queryFilter) {
		return getBySqlKey("getUserNoOrg", queryFilter);
	}
	/**
	 * 通过sql获得所有组织相关用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserByOrgId(QueryFilter queryFilter) {
		return getBySqlKey("getUserByOrgId", queryFilter);
	}
	/**
	 * 获得某组织所有岗位下的用户
	 * @param orgId
	 * @return
	 */
	public List<SysUser> getByOrgId(Long orgId) {
		return getBySqlKey("getByOrgId", orgId);
	}
	/**
	 * 获得某岗位下所有用户
	 * @param posId
	 * @return
	 */
	public List<SysUser> getByPosId(Long posId) {
		return getBySqlKey("getByPosId", posId);
	}
	/**
	 * 担任某职务的所有用户
	 * @param jobId
	 * @return
	 */
	public List<SysUser> getByJobId(Long jobId) {
		return getBySqlKey("getByJobId", jobId);
	}
	/**
	 * 具有某角色下的所有用户
	 * @param roleId
	 * @return
	 */
	public List<SysUser> getByRoleId(Long roleId) {
		return getBySqlKey("getByRoleId", roleId);
	}

	public List<SysUser> getUserByPath(String path) {
		Map param = new HashMap();
		param.put("path", path);
		return getBySqlKey("getUserByOrgId", param);
	}
	/**
	 * 未删除的用户 delFlag=0,status=1
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserByQuery(QueryFilter queryFilter) {
		if ((getDbType().equals("oracle")) || (getDbType().equals("mssql"))
				|| (getDbType().equals("mysql"))) {
			return getBySqlKey("getUserByQuery", queryFilter);
		}
		return getBySqlKey("getUserByQuery", queryFilter);
	}
	
	/**
	 * 过滤已选择的用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getByIdsFilter(QueryFilter queryFilter)
	{
		if ((getDbType().equals("oracle")) || (getDbType().equals("mssql"))
				|| (getDbType().equals("mysql"))) {
			return getBySqlKey("getByIdsFilter", queryFilter);
		}
		return getBySqlKey("getByIdsFilter", queryFilter);
	}
	
	/**
	 * 查看用户id的用户列
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getByIds(QueryFilter queryFilter)
	{
		if ((getDbType().equals("oracle")) || (getDbType().equals("mssql"))
				|| (getDbType().equals("mysql"))) {
			return getBySqlKey("getByIds", queryFilter);
		}
		return getBySqlKey("getByIds", queryFilter);
	}
	
	/**
	 * 字段查询用户列表(fullname,username,sex,delFlag,status,accessionTime)
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUsersByQuery(QueryFilter queryFilter) {
		if ((getDbType().equals("oracle")) || (getDbType().equals("mssql"))
				|| (getDbType().equals("mysql"))) {
			return getBySqlKey("getUsersByQuery", queryFilter);
		}
		return getBySqlKey("getUsersByQuery", queryFilter);
	}
	/**
	 * 获得指定角色的所有用户id
	 * @param roleId
	 * @return
	 */
	public List<Long> getUserIdsByRoleId(Long roleId) {
		String statement = getIbatisMapperNamespace() + ".getUserIdsByRoleId";

		List list = getSqlSessionTemplate().selectList(statement, roleId);

		return list;
	}
	/**
	 * 根据角色id查询
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserByRoleId(QueryFilter queryFilter) {
		return getBySqlKey("getUserByRoleId", queryFilter);
	}
	/**
	 * 某岗位下所有用户(该岗位包括isdelete=1)
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByPosPath(QueryFilter queryFilter) {
		return getBySqlKey("getDistinctUserByPosPath", queryFilter);
	}
	/**
	 * 通过isprimary获得相关组织用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByOrgPath(QueryFilter queryFilter) {
		return getBySqlKey("getDistinctUserByOrgPath", queryFilter);
	}
	/**
	 * 是否存在该用户
	 * @param username
	 * @return
	 */
	public boolean isUsernameExist(String username) {
		Integer rtn = (Integer) getOne("isUsernameExist", username);
		return rtn.intValue() > 0;
	}
	/**
	 * 是否存在一个用户多条数据
	 * @param userId
	 * @param username
	 * @return
	 */
	public boolean isUsernameExistForUpd(Long userId, String username) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("username", username);
		Integer rtn = (Integer) getOne("isUsernameExistForUpd", map);
		return rtn.intValue() > 0;
	}
	/**
	 * 根据连接的数据库类型获得有参数的用户
	 * @param property
	 * @return
	 */
	public List<SysUser> getByUserOrParam(Map<String, String> property) {
		List list = getBySqlKey("getByUserOrParam_" + getDbType(), property);
		return list;
	}
	/**
	 * 根据存在param的组织查询用户列表
	 * @param property
	 * @return
	 */
	public List<SysUser> getByOrgOrParam(Map<String, String> property) {
		return getBySqlKey("getByOrgOrParam_" + getDbType(), property);
	}
	/**
	 * 存在posid的情况
	 * @param p
	 * @return
	 */
	public List<SysUser> getUpLowPost(Map<String, Object> p) {
		return getBySqlKey("getUpLowPost", p);
	}
	/**
	 * 存在orgid的情况
	 * @param p
	 * @return
	 */
	public List<SysUser> getUpLowOrg(Map<String, Object> p) {
		return getBySqlKey("getUpLowOrg", p);
	}
	/**
	 * idset范围内存在的用户
	 * @param idSet
	 * @return
	 */
	public List<SysUser> getByIdSet(Set idSet) {
		Map params = new HashMap();
		if ((idSet == null) || (idSet.size() == 0))
			params.put("ids", Integer.valueOf(-1));
		else {
			params.put("ids", StringUtil.getSetAsString(idSet));
		}
		return getBySqlKey("getByIdSet", params);
	}
	
	/**
     * ids范围内存在的用户,ID 之间用， 分割
     * @param idSet
     * @return
     */
    public List<SysUser> getByIdSet(String ids) {
        Map params = new HashMap();
        params.put("ids", ids);
        return getBySqlKey("getByIdSet", params);
    }
	/**
	 * 根据邮件查找用户
	 * @param address
	 * @return
	 */
	public SysUser getByMail(String address) {
		return (SysUser) getUnique("getByMail", address);
	}
	/**
	 * 更新密码
	 * @param userId
	 * @param pwd
	 */
	public void updPwd(Long userId, String pwd) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("password", pwd);
		map.put("passwordSetTime", new Date());
		update("updPwd", map);
	}
	/**
	 * 锁定与解锁
	 * @param userId
	 * @param pwd
	 */
	public void updLock(Long userId, String loginFailures,short lockState,Date lastFailureTime,Date lockTime) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("loginFailures", loginFailures);
		map.put("lockState", lockState);
		map.put("lastFailureTime", lastFailureTime);
		map.put("lockTime", lockTime);
		update("updateLock", map);
	}
	/**
	 * 更新状态(status:1=激活 0=禁用 2=离职)
	 * @param userId
	 * @param status
	 * @param delFlag
	 */
	public void updStatus(Long userId,Long currentUserId, Short status) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("currentUserId", currentUserId);
		map.put("currentTime", new Date());
		map.put("status", status);
		update("updStatus", map);
	}
	/**
	 * 更新错误信息
	 * @param userId
	 * @param status
	 * @param delFlag
	 */
	public void updFailure(Long userId, String loginFailures,Date LastFailureTime) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("loginFailures", loginFailures);
		map.put("LastFailureTime", LastFailureTime);
		update("updFailure", map);
	}
	/**
	 * 更新密级(security:0=内部 1=一般 2=重要)
	 * @param userId
	 * @param currentUserId
	 * @param security
	 */
	public void updSecurity(Long userId,Long currentUserId, String security) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("currentUserId", currentUserId);
		map.put("security", security);
		map.put("currentTime", new Date());
		update("updSecurity", map);
	}
	/**
	 * 通过orgid查询该组织直接领导人
	 * @param orgId
	 * @return
	 */
	public List<SysUser> getDirectLeaderByOrgId(Long orgId) {
		List users = getBySqlKey("getDirectLeaderByOrgId", orgId);
		return users;
	}
	/**
	 *下属用户id为underuserid的用户列表(即userid的上级用户列表)
	 * @param underuserid
	 * @return
	 */
	public List<SysUser> getUserByUnderUserId(Long underuserid) {
		List users = getBySqlKey("getUserByUnderUserId", underuserid);
		return users;
	}
	/**
	 * 获得userid的下属用户列表
	 * @param userId
	 * @return
	 */
	public  List<SysUser> getUnderUserByUserId(Long userId) {
		List users = getBySqlKey("getUnderUserByUserId", userId);
		return users;
	}

	/**
	 * 有组织的的所有用户列表
	 * @return
	 */
	public List<SysUser> getAllIncludeOrg() {
		return getBySqlKey("getAllIncludeOrg");
	}
	/**
	 * 某流程所有执行人的用户信息
	 * @param actInstId
	 * @return
	 */
	public List<SysUser> getExeUserByInstnceId(Long actInstId) {
		return getBySqlKey("getExeUserByInstnceId", actInstId);
	}
	/**
	 * 组织集合相关用户
	 * @param list
	 * @return
	 */
	public List<SysUser> getByOrgIds(List<Long> list) {
		return getBySqlKey("getByOrgIds", list);
	}
	/**
	 * 组织集合相关的组织直接领导人
	 * @param list
	 * @return
	 */
	public List<SysUser> getMgrByOrgIds(List<Long> list) {
		return getBySqlKey("getMgrByOrgIds", list);
	}
	/**
	 * 角色集合内相关用户
	 * @param list
	 * @return
	 */
	public List<SysUser> getByRoleIds(List<Long> list) {
		return getBySqlKey("getByRoleIds", list);
	}
	/**
	 * 担任某些职务的用户列表
	 * @param list
	 * @return
	 */
	public List<SysUser> getByJobIds(List<Long> list) {
		return getBySqlKey("getByJobIds", list);
	}
	/**
	 * 担任某些岗位主要负责人并作为主岗位的用户
	 * @param list
	 * @return
	 */
	public List<SysUser> getByPos(List<Long> list) {
		return getBySqlKey("getByPos", list);
	}

	public List<SysUser> getSuperiorByUserId(Long userId, Long orgId) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("orgId", orgId);
		return getBySqlKey("getSuperiorByUserId", map);
	}

	/**
	 * 担任主要岗位的相关组织的
	 * @param userId
	 * @return
	 */
	public List<SysUser> getOrgMainUser(Long userId) {
		Map map = new HashMap();
		map.put("userId", userId);
		return getBySqlKey("getOrgMainUser", map);
	}
	/**
	 * 担任某组织岗位和具有某角色身份的用户(包括之前担任的)
	 * @param roleId
	 * @param orgId
	 * @return
	 */
	public List<SysUser> getUserByRoleIdOrgId(Long roleId, Long orgId) {
		Map params = new HashMap();
		params.put("roleId", roleId);
		params.put("orgId", orgId);
		return getBySqlKey("getUserByRoleIdOrgId", params);
	}
	/**
	 * 同时在某组织下任职和担任某岗位的用户（包括之前担任的）
	 * @param orgId
	 * @param posId
	 * @return
	 */
	public List<SysUser> getByOrgIdPosId(Long orgId, Long posId) {
		Map params = new HashMap();
		params.put("posId", posId);
		params.put("orgId", orgId);
		return getBySqlKey("getByOrgIdPosId", params);
	}
	/**
	 * 根据orgid获得所有该组织岗位下的用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByOrgId(QueryFilter queryFilter) {
		return getBySqlKey("getDistinctUserByOrgId", queryFilter);
	}
	/**
	 * 已授权的岗位用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByPosId(QueryFilter queryFilter) {
		return getBySqlKey("getDistinctUserByPosId", queryFilter);
	}
	/**
	 * 与发起人相同岗位的用户
	 * @param userId
	 * @return
	 */
	public List<SysUser> getSamePositionUsersByUserId(Long userId) {
		return getBySqlKey("getSamePositionUsersByUserId", userId);
	}
	/**
	 * 与发起人相同职务的用户
	 * @param userId
	 * @return
	 */
	public List<SysUser> getSameJobUsersByUserId(Long userId) {
		return getBySqlKey("getSameJobUsersByUserId", userId);
	}
	/**
	 * 同时在某组织下任职和担任某岗位的用户
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<SysUser> getByOrgRole(Long orgId, Long roleId) {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("roleId", roleId);
		return getBySqlKey("getByOrgRole", params);
	}
	/**
	 * 对应公司组织主管用户
	 * @param companyId
	 * @param roleId
	 * @param isCharge
	 * @return
	 */
	public List<SysUser> getByCompanyRole(Long companyId, Long roleId,
			boolean isCharge) {
		Map params = new HashMap();
		params.put("companyId", companyId);
		params.put("roleId", roleId);
		if (isCharge) {
			params.put("isCharge", "1");
		}
		return getBySqlKey("getByCompanyRole", params);
	}
	


	public void updateCommon(SysUser sysUser) {
		update("updateCommon", sysUser);
	}
	
    /**
     * 查找 某个组织下某个职务的用户
     * @param orgId 组织ID
     * @param jobId 职务ID
     * @return
     */
    public List<? extends ISysUser> getSameOrgJobUsers(Long orgId, Long jobId)
    {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orgId", orgId);
        params.put("jobId", jobId);
        return getBySqlKey("getSameOrgJobUsers", params);
    }
    
    /**
     * 通过人的中文名来返回相应的人
     * @param fullname 中文名
     * @return
     */
    public List<? extends ISysUser> getByFullname(String fullname) {
		return getBySqlKey("getByFullname", fullname);
	}  
}
