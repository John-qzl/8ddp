 package com.cssrc.ibms.core.user.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.excel.Excel;
import com.cssrc.ibms.core.excel.editor.IFontEditor;
import com.cssrc.ibms.core.excel.style.Color;
import com.cssrc.ibms.core.excel.style.font.BoldWeight;
import com.cssrc.ibms.core.excel.style.font.Font;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.login.model.OnlineUser;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.event.UserEvent;
import com.cssrc.ibms.core.user.listener.UserSessionListener;
import com.cssrc.ibms.core.user.model.ParamSearch;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.ListUtil;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SysUserService extends BaseService<SysUser> implements ISysUserService{
	public static JSONArray allSysUserInf = new JSONArray();
	public static Map<String,JSONObject> allUserMap = new HashMap();
	@Resource
	private SysUserDao dao;
	@Resource
	private UserPositionService userPositionService;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private UserUnderService userUnderService;
	@Resource
	private SysRoleService sysRoleService;

	@Override
	protected IEntityDao<SysUser, Long> getEntityDao() { 
		return dao;
	}
	public SysUser findByUserId(Long userId) {
		return this.dao.getById(userId);
	}

	public SysUser getByUsername(String username) {
		return this.dao.getByUsername(username);
	}
	/**
	 * 未删除的用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserByQuery(QueryFilter queryFilter)
	{
		return this.dao.getUserByQuery(queryFilter);
	}
	/**
	 * 字段查询用户列表
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUsersByQuery(QueryFilter queryFilter)
	{
		return this.dao.getUsersByQuery(queryFilter);
	}
	/**
	 * roleId查找用户
	 * @param roleId
	 * @return
	 */
	public List<Long> getUserIdsByRoleId(Long roleId)
	{
		List ids = new ArrayList();
		List<SysUser> users = this.dao.getByRoleId(roleId);
		for (SysUser user : users) {
			ids.add(user.getUserId());
		}
		return ids;
	}
	/**
	 * 具有某角色下的所有用户
	 * @param roleId
	 * @return
	 */
	public List<SysUser> getByRoleId(Long roleId)
	{
		return this.dao.getByRoleId(roleId);
	}
	/**
	 * no主要岗位的用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserNoOrg(QueryFilter queryFilter)
	{
		return this.dao.getUserNoOrg(queryFilter);
	}
	/**
	 * 获得指定username的用户信息列表
	 * @param accounts
	 * @return
	 */
	public List<SysUser> getByUsernames(String usernames)
	{
		List users = new ArrayList();
		if (usernames != null) {
			String[] str = usernames.split(",");

			for (String a : str) {
				SysUser u = getByUsername(a);
				if (u != null) {
					users.add(u);
				}
			}
		}
		return users;
	}


	/**
	 * 某岗位下所有用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByPosPath(QueryFilter queryFilter)
	{
		return this.dao.getDistinctUserByPosPath(queryFilter);
	}
	/**
	 * 通过isprimary获得相关组织用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByOrgPath(QueryFilter queryFilter)
	{
		String path = (String)queryFilter.getFilters().get("path");
		String orgId = (String)queryFilter.getFilters().get("orgId");
		if ((StringUtil.isEmpty(path)) && (StringUtil.isNotEmpty(orgId))) {
			SysOrg org = this.sysOrgDao.getById(Long.valueOf(Long.parseLong(orgId)));
			if (org != null) {
				queryFilter.addFilterForIB("path", org.getPath());
			}
		}
		return this.dao.getDistinctUserByOrgPath(queryFilter);
	}
	/**
	 * 判断用户是否存在
	 * @param username
	 * @return
	 */
	public boolean isUsernameExist(String username)
	{
		return this.dao.isUsernameExist(username);
	}

	/**
	 * 是否存在一个用户多条数据
	 * @param userId
	 * @param account
	 * @return
	 */
	public boolean isUsernameExistForUpd(Long userId, String username)
	{
		return this.dao.isUsernameExistForUpd(userId, username);
	}
	/**
	 * 用户自定义参数查询
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> getByUserParam(String userParam)
	throws Exception
	{
		ParamSearch search = new ParamSearch<SysUser>()
		{
			public List<SysUser> getFromDataBase(Map<String, String> property)
			{
				return SysUserService.this.dao.getByUserOrParam(property);
			}
		};
		return search.getByParamCollect(userParam);
	}
	/**
	 * 组织自定义参数查询
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> getByOrgParam(String userParam)
	throws Exception
	{
		ParamSearch search = new ParamSearch<SysUser>()
		{
			public List<SysUser> getFromDataBase(Map<String, String> property)
			{
				return SysUserService.this.dao.getByOrgOrParam(property);
			}
		};
		return search.getByParamCollect(userParam);
	}

	/**
	 * 用户id获取其上下级信息
	 * @param userId
	 * @return
	 */
	public List<SysUser> getByUserIdAndUplow(long userId)
	{
		List list = new ArrayList();

		SysOrg ol = this.sysOrgDao.getPrimaryOrgByUserId(Long.valueOf(userId));
		if (ol != null) {
			String currentPath = ol.getPath();
			int currentDepth = currentPath.split("\\.").length;

			for (int depth = currentDepth; depth > 1; depth--) {
				Map param = handlerCondition(currentPath, depth);
				List<SysUser> users = this.dao.getUpLowOrg(param);
				for (SysUser user :users) {
					if (!list.contains(user)) {
						list.add(user);
					}
				}
			}
		}
		return list;
	}
	/**
	 * 处理上下级路径条件
	 * @param currentPath
	 * @param upLowType
	 * @param upLowLevel
	 * @param isCharge
	 * @return
	 */
	private static Map<String, Object> handlerCondition(String currentPath, short upLowType, int upLowLevel, int isCharge)
	{
		String[] pathArr = currentPath.split("\\.");
		int currentDepth = pathArr.length;
		String path = null;
		Integer depth = null;
		String pathCondition = null;
		int depthCondition = 0;

		switch (upLowType) {
		case 1:
			depth = Integer.valueOf(currentDepth - upLowLevel);
			pathCondition = "=";
			path = coverArray2Str(pathArr, depth.intValue());
			path = path + ".";
			break;
		case -1:
			depth = Integer.valueOf(currentDepth + upLowLevel);
			pathCondition = "like";
			depthCondition = depth.intValue();
			path = currentPath + "%";
			break;
		case 0:
			depth = Integer.valueOf(currentDepth);
			pathCondition = "like";
			path = coverArray2Str(pathArr, depth.intValue() - 1) + "._%";
			depthCondition = depth.intValue();
		}

		Map returnMap = new HashMap();

		returnMap.put("path", path);

		returnMap.put("depthCondition", Integer.valueOf(depthCondition));

		returnMap.put("isCharge", Integer.valueOf(isCharge));

		returnMap.put("pathCondition", pathCondition);
		return returnMap;
	}

	private static Map<String, Object> handlerCondition(String currentPath, int depth)
	{
		String[] pathArr = currentPath.split("\\.");
		String path = null;
		String pathCondition = "=";
		if (depth == pathArr.length) {
			path = coverArray2Str(pathArr, depth - 1) + "._%";
			pathCondition = "like";
		} else {
			path = coverArray2Str(pathArr, depth);
			path = path + ".";
		}
		Map returnMap = new HashMap();
		returnMap.put("path", path);

		returnMap.put("isCharge", Integer.valueOf(0));
		returnMap.put("pathCondition", pathCondition);

		returnMap.put("depthCondition", Integer.valueOf(0));
		return returnMap;
	}

	private static String coverArray2Str(String[] pathArr, int len)
	{
		if (len < 0)
			return pathArr[0];
		if (len > pathArr.length) {
			len = pathArr.length;
		}
		StringBuilder sb = new StringBuilder();
		if (pathArr.length > 1) {
			int i = 0;
			do {
				sb.append(pathArr[i]);
				sb.append(".");
				i++;
			}while (i < len);

			sb = sb.delete(sb.length() - 1, sb.length());
		} else if (pathArr.length > 0) {
			sb = sb.append(pathArr[0]);
		}return sb.toString();
	}
	/**
	 * 获得当前用户
	 * @param list
	 * @return
	 */
	public List<SysUser> getOnlineUser(List<SysUser> list)
	{
		List listOnl = new ArrayList();
		Map<Long,OnlineUser> onlineUsers = UserSessionListener.getOnLineUsers();
		List<OnlineUser> onlineList = new ArrayList();
		for (Long userId : onlineUsers.keySet()) {
			OnlineUser onlineUser = (OnlineUser)onlineUsers.get(userId);
			onlineList.add(onlineUser);
		}
		for (SysUser sysUser : list) {
			for (OnlineUser onlineUser : onlineList) {
				Long sysUserId = sysUser.getUserId();
				Long onlineUserId = onlineUser.getUserId();
				if (sysUserId.toString().equals(onlineUserId.toString())) {
					listOnl.add(sysUser);
				}
			}
		}
		return listOnl;
	}
	/**
	 * userid该集合范围内存在的用户
	 * @param idSet
	 * @returnsys
	 */
	public List<SysUser> getByIdSet(Set uIds)
	{
		return this.dao.getByIdSet(uIds);
	}

	/**
     * ids范围内存在的用户,ID 之间用， 分割
     * @param idSet
     * @return
     */
    public List<SysUser> getByIdSet(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ArrayList<SysUser>();
        }
        return this.dao.getByIdSet(ids);
    }
    
	public SysUser getByMail(String address) {
		return this.dao.getByMail(address);
	}

	public List<SysUser> getAllIncludeOrg()
	{
		return this.dao.getAll();
	}
	/**
	 * 修改用户密码
	 * @param userId
	 * @param pwd
	 */
	public void updPwd(Long userId, String pwd)
	{
		String enPassword = PasswordUtil.generatePassword(pwd);//MD5解码
		this.dao.updPwd(userId, enPassword);
	}
	/**
	 * 更新状态(status:1=激活 0=禁用 2=离职)
	 * @param userId
	 * @param status
	 */
	public void updStatus(Long userId,Long currentUserId, Short status)
	{
		this.dao.updStatus(userId,currentUserId, status);
	}
	/**
	 * 更新密级(security:3=非密， 6=一般， 9=重要， 12=核心)
	 * @param userId
	 * @param currentUserId
	 * @param security
	 */
	public void updSecurity(Long userId,Long currentUserId, String security)
	{
		this.dao.updSecurity(userId,currentUserId, security);
	}
	/**	
	 * 更新status状态
	 * (status:1=激活 0=禁用 2=离职)
	 * @param username
	 * @param status
	 */
	public void updStatus(String username,Long currentUserId, Short status)
	{
		SysUser sysUser = this.dao.getByUsername(username);
		if (sysUser != null)
			this.dao.updStatus(sysUser.getUserId(),currentUserId, status);
	}
	/**
	 * 添加或更新新用户信息（包括岗位和组织信息）
	 * @param bySelf
	 * @param sysUser
	 * @param posIdCharge
	 * @param posIds
	 * @param posIdPrimary
	 * @param roleIds
	 * @throws Exception
	 */
	public void saveUser(Integer bySelf, SysUser sysUser, Long[] posIdCharge, Long[] posIds, Long posIdPrimary, Long[] roleIds,Long[] aryOrgId)
	throws Exception
	{
		ISysUser currentUser = UserContextUtil.getCurrentUser();
		int event = UserEvent.ACTION_ADD;
		if (sysUser.getUserId() == null) {
			sysUser.setUser_createTime(new Date());
			sysUser.setUser_updateTime(new Date());
			sysUser.setUser_creatorId(UserContextUtil.getCurrentUserId());
			sysUser.setUser_updateId(UserContextUtil.getCurrentUserId());
			sysUser.setUserId(Long.valueOf(UniqueIdUtil.genId()));
			sysUser.setDelFlag(Short.valueOf((short) 0));
			sysUser.setLoginFailures("0");
			sysUser.setLockState(Short.valueOf((short) 0));
			this.dao.add(sysUser);
		} else if(!currentUser.getUserId().equals(ISysUser.RIGHT_USER)){//权限管理员不能保存人员角色之外的相关信息
			sysUser.setUser_updateTime(new Date());
			sysUser.setUser_updateId(UserContextUtil.getCurrentUserId());
			if(BeanUtils.isEmpty(sysUser.getDelFlag()))
				sysUser.setDelFlag(Short.valueOf((short) 0));
			event = UserEvent.ACTION_UPD;
			this.dao.update(sysUser);
		}
		if (bySelf.intValue() == 0) {
			Long userId = sysUser.getUserId();

			if(!currentUser.getUserId().equals(ISysUser.RIGHT_USER)){//权限管理员不能保存组织相关信息
				this.userPositionService.saveUserPos(userId, posIds, posIdPrimary, posIdCharge, aryOrgId);
			}

			if(!currentUser.getUserId().equals(ISysUser.SYSTEM_USER)){//系统管理员不能保存人员角色信息
				this.userRoleService.saveUserRole(userId, roleIds);
			}

			if(!currentUser.getUserId().equals(ISysUser.SYSTEM_USER)&&!currentUser.getUserId().equals(ISysUser.RIGHT_USER)){//权限管理员和系统管理员不能保存上级领导信息
				this.userUnderService.saveSuperior(userId, sysUser.getSuperiorIds());
			}
		}

		EventUtilService.publishUserEvent(sysUser.getUserId(), event, sysUser);
	}

	/**
	 * 查询某角色下的用户列表
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getUserByRoleId(QueryFilter queryFilter)
	{
		return this.dao.getBySqlKey("getUserByRoleId", queryFilter);
	}

	/**
	 * 担任某组织岗位和具有某角色身份的用户(包括之前担任的)
	 * @param roleId
	 * @param orgId
	 * @return
	 */
	public List<SysUser> getUserByRoleIdOrgId(Long roleId, Long orgId)
	{
		return this.dao.getUserByRoleIdOrgId(roleId, orgId);
	}
	/**
	 * 担任某岗位的用户(posid已经唯一)
	 * @param orgId
	 * @param posId
	 * @return
	 */
	public List<SysUser> getByOrgIdPosId(Long orgId, Long posId)
	{
		return this.dao.getByOrgIdPosId(orgId, posId);
	}
	/**
	 * 某组织下的用户列表
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByOrgId(QueryFilter queryFilter)
	{
		return this.dao.getDistinctUserByOrgId(queryFilter);
	}
	/**
	 * 根据岗位id查询用户
	 * @param queryFilter
	 * @return
	 */
	public List<SysUser> getDistinctUserByPosId(QueryFilter queryFilter)
	{
		return this.dao.getDistinctUserByPosId(queryFilter);
	}
	/**
	 * 职务id查询用户列表
	 * @param jobId
	 * @return
	 */
	public List<SysUser> getUserListByJobId(Long jobId)
	{
		List list = new ArrayList();
		list.add(jobId);
		return this.dao.getByJobIds(list);
	}
	/**
	 * posid的岗位下用户
	 * @param posId
	 * @return
	 */
	public List<SysUser> getUserListByPosId(Long posId)
	{
		return this.dao.getByPosId(posId);
	}
	/**
	 * 更新sysuser对象
	 * 如果传回来的密码是null,则不设置密码
	 * 如果传回来的密码不是空,则密码进行加密
	 * update by zmz 20200819
	 * @param sysUser
	 */
	public void updateCommon(SysUser sysUser)
	{
		if (sysUser.getPassword()==""||sysUser.getPassword()==null){
			//用户没有设置密码,直接保存
		}else {
			//用户设置了密码,对密码进行加密
			String enPassword = PasswordUtil.generatePassword(sysUser.getPassword());//MD5解码
			sysUser.setPassword(enPassword);
		}
		this.dao.updateCommon(sysUser);
	}

	/**
	 * email作为查询条件
	 * @param email
	 * @return
	 */
	public List<SysUser> findLinkMan(String email)
	{
		return this.dao.getBySqlKey("findLinkMan", email);
	}
	
	
	/**
	 * 根据组织id获取所有用户
	 * @param orgId
	 * @return
	 */
	public List<SysUser> getByOrgId(Long orgId) {
		return dao.getByOrgId(orgId);
	}
	@Override
	public List<? extends ISysUser> getByPosId(Long posId) {
		return this.dao.getByPosId(posId);
	}
    
    @Override
    public List<? extends ISysUser> getByJobId(Long jobId)
    {
        return this.dao.getByJobId(jobId);
    }
	@Override
	public List<? extends ISysUser> getExeUserByInstnceId(Long actInstId) {
		return this.dao.getExeUserByInstnceId(actInstId);
	}
	@Override
	public List<? extends ISysUser> getByRoleIds(List list) {
		return this.dao.getByRoleIds(list);
	}
	@Override
	public List<? extends ISysUser> getByPos(List list) {
		return this.dao.getByPos(list);
	}
	@Override
	public List<? extends ISysUser> getByOrgIds(List list) {
		return this.dao.getByOrgIds(list);
	}
	@Override
	public List<? extends ISysUser> getMgrByOrgIds(List list) {
		return dao.getMgrByOrgIds(list);
	}
	@Override
	public List<? extends ISysUser> getDirectLeaderByOrgId(Long orgId) {
		return dao.getDirectLeaderByOrgId(orgId);
	}
	@Override
	public List<? extends ISysUser> getUserByUnderUserId(Long userId) {
		return dao.getUserByUnderUserId(userId);
	}
	@Override
	public List<? extends ISysUser> getSameJobUsersByUserId(Long userId) {
		return dao.getSameJobUsersByUserId(userId);
	}
	@Override
	public List<? extends ISysUser> getSamePositionUsersByUserId(Long userId) {
		return dao.getSamePositionUsersByUserId(userId);
	}
	@Override
	public List<? extends ISysUser> getByUserIdAndUplow(Long userId,
			String cmpIds) {
		return null;
	}
	@Override
	public List getOrgMainUser(Long userId) {
		return this.dao.getOrgMainUser(userId);
	}
	@Override
	public List<? extends ISysUser> getByCompanyRole(Long companyId, Long roleId,
			boolean isCharge) {
		return dao.getByCompanyRole(companyId, roleId, isCharge);
	}
	@Override
	public List getByOrgRole(Long orgId, Long roleId) {
		return this.dao.getByOrgRole(orgId,roleId);
	}
	/**
	 * 获得上级用户列表
	 * @param userId
	 * @return
	 */
	public List<SysUser> getMyLeaders(Long userId) {
		return this.dao.getUserByUnderUserId(userId);
	}
	
	public void updateFailure(Long userId, String loginFailures,
			Date LastFailureTime) {
		// TODO Auto-generated method stub
		this.dao.updFailure(userId,loginFailures,LastFailureTime);
	}

    /**
     * 查找 某个组织下某个职务的用户
     * @param orgId 组织ID
     * @param jobId 职务ID
     * @return
     */
    @Override
    public List<? extends ISysUser> getSameOrgJobUsers(Long orgId, Long jobId)
    {
        return this.dao.getSameOrgJobUsers(orgId,jobId);

    }
    
	/**
	 * 锁定与解锁
	 * @param userId
	 * @param pwd
	 */
	public void updLock(Long userId, String loginFailures,short lockState,Date lastFailureTime,Date lockTime) {
		this.dao.updLock(userId, loginFailures, lockState, lastFailureTime, lockTime);
	}
	
    @Override
    public ISysUser newUser(Long userId)
    {
        return new SysUser(userId);
    }
    
    /**
     * 通过中文名来返回对应的记录
     * @param fullname 中文名
     * @return
     */
    public List<? extends ISysUser> getByFullname(String fullname) {
    	return this.dao.getByFullname(fullname);
	}
    
	/**
	 * 将所有用户放到redis中
	 */
	public void setAllSysUserToRedis(){
		//获取所有的用户
		List<SysUser> sysUserList = getAll();
		if(BeanUtils.isEmpty(sysUserList)){
        	return;
        }
		JSONArray userArr = new JSONArray();
		for(SysUser sysUser:sysUserList){
			
			//获取用户所属的组织id集合,以"-"分割开
			List<Long> idList = userPositionService.getOrgIdByUserAndJob(sysUser.getUserId(), null);
			String ids = "";
			String orgNames = "";
			if (idList != null && idList.size() > 0){
				StringBuffer idBuffer = new StringBuffer();
				StringBuffer orgNameBuffer = new StringBuffer();
				for (int i = 0; i < idList.size(); i++) {
					Long orgId = idList.get(i);
					SysOrg sysOrg = this.sysOrgDao.getById(orgId);
					if (i > 0)
						idBuffer.append("-");
					if(sysOrg!=null){
						idBuffer.append(orgId+":"+sysOrg.getPath());
						orgNameBuffer.append(sysOrg.getOrgPathname());
					}else{
						idBuffer.append(orgId+":"+"");
					}
				}
				ids = idBuffer.toString();
				orgNames = orgNameBuffer.toString();
			}
			//获取用户所属的角色id、角色名称集合,以","分割开
			String roleIds = "";
			String roleNames = "";
			List<SysRole> sysRoles = this.sysRoleService.getByUserId(sysUser.getUserId());
			for(SysRole role: sysRoles) {
				roleIds+=role.getRoleId()+",";
				roleNames+=role.getRoleName()+",";
			}
			if(roleIds.endsWith(",")) {
				roleIds = roleIds.substring(0,roleIds.length()-1);
				roleNames = roleNames.substring(0,roleNames.length()-1);
			}
			JSONObject userObj = new JSONObject();
			userObj.put("userId", sysUser.getUserId());
			userObj.put("fullname", sysUser.getFullname());
			userObj.put("username", sysUser.getUsername());
			userObj.put("org", ids);
			userObj.put("orgNames", orgNames);
			userObj.put("roleIds", roleIds);
			userObj.put("roleNames", roleNames);
			userArr.add(userObj);
		}
		
		try {
   		 	//RedisClient.set(RedisKey.ALL_SYSUSER_INF, userArr.toString());
   		 	allSysUserInf = userArr;
   		 	allUserMap = ListUtil.list2Map(userArr, "username");
        } catch (Exception e) {
            logger.error("用户放到redis中初始化出错");
        }
	}
	
	/**
	 * 通过前台传过来的用户输入框的值进行模糊查询
	 * @param userFuzzyName
	 * @return
	 */
	public JSONArray getFuzzySysUserList(String userFuzzyName,String fieldName,String relvalue,String type,String typeVal){
		//Object allSysUser = RedisClient.get(RedisKey.ALL_SYSUSER_INF);
		//JSONArray allSysUserInf = JSONArray.fromObject(allSysUser);
		JSONArray userArr = new JSONArray();
		
		//获取当前用户组织
		SysOrg sysCurrentOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		Iterator<Object> it = allSysUserInf.iterator();
			
		//首先判断是否存在级联查询 1.
		if(!relvalue.isEmpty()){
			while(it.hasNext()){
				JSONObject userObj = (JSONObject) it.next();
				Object fieldValue = userObj.get(fieldName);
				Object org = userObj.get("org");
				
				if(org!=null&&org.toString().contains(relvalue)){
					//其次判断是否需要模糊查询 2.
					if(fieldValue!=null){
						if(StringUtil.isChinese(userFuzzyName)){
							if(fieldValue.toString().contains(userFuzzyName)){
								userArr.add(userObj);
								//限制查询时的数据数量
								if(userArr.size()>20)
									break;
							}
						}else{
							if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(userFuzzyName)){
								userArr.add(userObj);
								//限制查询时的数据数量
								if(userArr.size()>20)
									break;
							}
						}
					}else{
						userArr.add(userObj);
						//限制查询时的数据数量
						if(userArr.size()>20)
							break;
					}
				}
			}
		}else if((StringUtil.isNotEmpty(type)) && (!"all".equals(typeVal))
				&& (BeanUtils.isNotEmpty(sysCurrentOrg))){
			
			//处理当前组织path
			String path = sysCurrentOrg.getPath();
			//上级组织path
			String upOrgPath = path.substring(0,path.length()-1);
			if(upOrgPath.indexOf(".")>0){
				upOrgPath = upOrgPath.substring(0,upOrgPath.lastIndexOf(".")+1);
			}else{
				upOrgPath = "";
			}
			
			//其次判断是否需要通过typeVal进行数据过滤
			while(it.hasNext()){
				JSONObject userObj = (JSONObject) it.next();
				Object fieldValue = userObj.get(fieldName);
				Object org = userObj.get("org");
				
				//当前组织
				if(typeVal.equals("self")){
					if(org!=null&&org.toString().contains(path)){
						//其次判断是否需要模糊查询 2.
						if(fieldValue!=null){
							if(StringUtil.isChinese(userFuzzyName)){
								if(fieldValue.toString().contains(userFuzzyName)){
									userArr.add(userObj);
									//限制查询时的数据数量
									if(userArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(userFuzzyName)){
									userArr.add(userObj);
									//限制查询时的数据数量
									if(userArr.size()>20)
										break;
								}
							}
						}else{
							userArr.add(userObj);
							//限制查询时的数据数量
							if(userArr.size()>20)
								break;
						}
					}
				}else if(typeVal.equals("up")&&StringUtil.isNotEmpty(upOrgPath)){
					//上级组织
					if(org!=null&&org.toString().contains(upOrgPath)){
						//其次判断是否需要模糊查询 2.
						if(fieldValue!=null){
							if(StringUtil.isChinese(userFuzzyName)){
								if(fieldValue.toString().contains(userFuzzyName)){
									userArr.add(userObj);
									//限制查询时的数据数量
									if(userArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(userFuzzyName)){
									userArr.add(userObj);
									//限制查询时的数据数量
									if(userArr.size()>20)
										break;
								}
							}
						}else{
							userArr.add(userObj);
							//限制查询时的数据数量
							if(userArr.size()>20)
								break;
						}
					}
				}
				
			}
			
		}else{
			//最后判断是否需要模糊查询 3.
			while(it.hasNext()){
				JSONObject userObj = (JSONObject) it.next();
				Object fieldValue = userObj.get(fieldName);
				
				if(fieldValue!=null){
					if(StringUtil.isChinese(userFuzzyName)){
						if(fieldValue.toString().contains(userFuzzyName)){
							userArr.add(userObj);
							//限制查询时的数据数量
							if(userArr.size()>20)
								break;
						}
					}else{
						if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(userFuzzyName)){
							userArr.add(userObj);
							//限制查询时的数据数量
							if(userArr.size()>20)
								break;
						}
					}
				}else{
					userArr.add(userObj);
					//限制查询时的数据数量
					if(userArr.size()>20)
						break;
				}
			}
		}
		return userArr;
	}
	  /**
     * 担任posid岗位的用户名数组
     * 
     * @param posId
     * @return
     */
    public String getUsernamesByPosId(Long posId) {
        StringBuffer userNames = new StringBuffer();
        List<UserPosition> userPositionList = userPositionService.getByPosId(posId);
        if (BeanUtils.isNotEmpty(userPositionList)) {
            for (UserPosition userPosition : userPositionList) {
                ISysUser sysUser = this.getById(userPosition
                        .getUserId());
                userNames.append(RequestUtil.getUserLinkOpenWindow(userPosition
                        .getUserId().toString(), sysUser.getFullname()));
            }
        }
        return userNames.toString();
    }
    public String exportExcel(QueryFilter queryFilter) throws Exception{
    	String fileName = "用户信息列表_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd")+".xls";
    	String filePath = SysConfConstant.UploadFileFolder+"\\temp"+File.separator+fileName;
    	//获取数据
    	//密级转换
    	List<SysUser> list = this.getAll(queryFilter);
    	this.addRoleNames(list);
    	this.addOrgNames(list);
    	String[] titles = {"序号","账户名","所属组织","涉密等级","姓名","所属角色"};
    	String[] dataKeys = {"no","username","orgName","security","fullname","roleNames"};
    	List<List<Object>> dataList = this.tranToExcelData(list,dataKeys);
    	//生成excel文件
    	Excel excel = new Excel();
    	excel.sheet().sheetName("用户信息列表");
    	excel.row(0, 0).value(titles).font(new IFontEditor() {
			@Override
			public void updateFont(Font font) {
				font.boldweight(BoldWeight.BOLD);// 粗体
			}
		}).bgColor(Color.GREY_25_PERCENT);
		if (BeanUtils.isNotEmpty(dataList)) {
			for (int i = 0; i < dataList.size(); i++) {
				List<Object> objectList = dataList.get(i);
				excel.row(i + 1).value(objectList.toArray())
						.dataFormat("@");
			}
		}
        ExcelUtil.writeExcel(filePath, excel.getWorkBook());
    	//返回文件生成地址
    	return filePath;
    }
    /**
     * 将Sysuser对象转换成是适合excel导出的数据类型
     * @param list
     * @param dataKeys
     * @return
     */
    private List<List<Object>> tranToExcelData(List<SysUser> list,String[] dataKeys){
    	List<List<Object>> dataList = new ArrayList();
    	int i =1;
    	for(SysUser user : list) {
    		JSONObject userObj = JSONObject.fromObject(user);
    		List<Object> data =  new ArrayList();
    		for(String key : dataKeys) {
    			if(key.equals("no")) {
    				data.add(i);
    			}else if(key.equals("security")){
    				data.add(user.getFormatSecurity());
    			}else {
    				data.add(userObj.get(key));
    			}
    		}
    		dataList.add(data);
    		i++;
    	}
    	return dataList;
    }
    /**
	 * 为每个用户添加角色信息（多个角色用逗号隔开）
	 * @param list
	 */
    public void addRoleNames(List<SysUser> list) {
    	for(SysUser user : list) {
    		String userName = user.getUsername();
    		JSONObject userObj = allUserMap.get(userName);
    		if(userObj!=null) {
    			String names = CommonTools.Obj2String(userObj.get("roleNames"));
    			user.setRoleNames(names);
    		}
    	}
    }
    /**
	 * 为每个用户添加组织信息（多个组织名用逗号隔开）
	 * @param list
	 */
    public void addOrgNames(List<SysUser> list) {
    	for(SysUser user : list) {
    		String userName = user.getUsername();
    		JSONObject userObj = allUserMap.get(userName);
    		String orgNames = CommonTools.Obj2String(userObj.get("orgNames"));
    		orgNames = orgNames.replaceAll("-", ",");
			user.setOrgName(orgNames);
    	}
    }
}