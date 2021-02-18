package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.IResourcesUrlService;
import com.cssrc.ibms.api.system.intf.IRoleResourcesService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.dao.SysRoleDao;
import com.cssrc.ibms.core.user.dao.UserRoleDao;
import com.cssrc.ibms.core.user.model.OrgAuth;
import com.cssrc.ibms.core.user.model.SysOrgRole;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.redis.RedisClient;
import com.cssrc.ibms.core.util.redis.RedisKey;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 角色管理
 * <p>Title:SysRoleService</p>
 * @author Yangbo 
 * @date 2016-8-23下午09:57:36
 */
@Service
public class SysRoleService extends BaseService<SysRole> implements ISysRoleService{
	public static JSONArray allSysRoleInf = new JSONArray();
	@Resource
	private IResourcesUrlService resourcesUrlService;
	@Resource
	UserRoleDao userRoleDao;
	@Resource
	private IRoleResourcesService roleResourcesService;
	@Resource
	private SysOrgRoleService sysOrgRoleService;
	@Resource
	private OrgAuthService orgAuthService;
	@Resource
	private SysRoleDao dao;

	protected IEntityDao<SysRole, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 角色别名校验
	 * @param alias
	 * @return
	 */
	public boolean isExistRoleAlias(String alias) {
		return this.dao.isExistRoleAlias(alias);
	}
	/**
	 * 该角色别名是否存在了
	 * @param alias
	 * @param roleId
	 * @return
	 */
	public boolean isExistRoleAliasForUpd(String alias, Long roleId) {
		return this.dao.isExistRoleAliasForUpd(alias, roleId);
	}
	/**
	 * 添加过滤的角色数组
	 * @param queryFilter
	 * @return
	 */
	public List<SysRole> getRoleList(QueryFilter queryFilter) {
		return this.dao.getRole(queryFilter);
	}
	/**
	 * 用户id获取角色信息
	 * @param userId
	 * @return
	 */
	public List<SysRole> getByUserId(Long userId) {
		return this.dao.getByUserId(userId);
	}
	/**
	 * 用户id获得授权的角色字符串
	 * @param userId
	 * @return
	 */
	public String getRoleIdsByUserId(Long userId) {
		StringBuffer sb = new StringBuffer("");
		List<SysRole> sysRoleList = getByUserId(userId);
		for (SysRole sysRole : sysRoleList) {
			sb.append(sysRole.getRoleId()).append(",");
		}
		if (sysRoleList.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	/**
	 * 用户id获得授权的角色Long数组
	 * @param userId
	 * @return
	 */
	public List<Long> getRoleIdsByUserIdLong(Long userId) {
		List<SysRole> sysRoleList = getByUserId(userId);
		List list = new ArrayList();
		for (SysRole role : sysRoleList) {
			list.add(role.getRoleId());
		}
		return list;
	}
	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public List<SysRole> getRoleTree(QueryFilter queryFilter) {
		return this.dao.getRole(queryFilter);
	}

	/**
	 * 复制角色方法(复制用户角色关联和角色资源关联)
	 * @param sysRole
	 * @param oldRoleId
	 * @throws Exception
	 */
	public void copyRole(SysRole sysRole, long oldRoleId) throws Exception {
		Long newRoleId = sysRole.getRoleId();
		this.dao.add(sysRole);
		List<UserRole> userRoleList = this.userRoleDao.getUserRoleByRoleId(Long
				.valueOf(oldRoleId));
		for (UserRole userRole : userRoleList) {
			UserRole ur = (UserRole) userRole.clone();
			ur.setUserRoleId(Long.valueOf(UniqueIdUtil.genId()));
			ur.setRoleId(newRoleId);
			this.userRoleDao.add(ur);
		}
		this.roleResourcesService.copyRoleResources(oldRoleId, newRoleId);
		
	}
	/**
	 * 获得组织和用户授权角色的别名数组的总和
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public List<String> getRolesByUserIdAndOrgId(Long userId, Long orgId) {
		List totalRoles = new ArrayList();

		List userRoles = getUserRoles(userId);

		if (BeanUtils.isNotEmpty(userRoles)) {
			totalRoles.addAll(userRoles);
		}

		if (orgId.longValue() > 0L) {
			List roleRoles = getOrgRoles(orgId);
			if (BeanUtils.isNotEmpty(roleRoles)) {
				totalRoles.addAll(roleRoles);
			}
		}
		return totalRoles;
	}
	/**
	 *获得userid用户被授权的角色名或别名
	 * @param userId
	 * @return
	 */
	private List<String> getUserRoles(Long userId) {
		List<SysRole> userRoleList = this.dao.getByUserId(userId);
		List userRoles = new ArrayList();
		if (BeanUtils.isNotEmpty(userRoleList)) {
			for (SysRole role : userRoleList) {
				if(BeanUtils.isNotEmpty(role.getAlias()))
					userRoles.add(role.getAlias());
				else
					userRoles.add(role.getRoleName());
					
			}
		}
		return userRoles;
	}
	/**
	 * 获取组织的授权角色别名
	 * @param orgId
	 * @return
	 */
	public List<String> getOrgRoles(Long orgId) {
		List<SysOrgRole> orgRoles = this.sysOrgRoleService
				.getRolesByOrgId(orgId);

		List roles = new ArrayList();
		if (BeanUtils.isNotEmpty(orgRoles)) {
			for (SysOrgRole sysOrgRole : orgRoles) {
				SysRole role = sysOrgRole.getRole();
				if (role != null) {
					roles.add(role.getAlias());
				}
			}
		}

		return roles;
	}
	/**
	 * 符合角色名的列表
	 * @param roleName
	 * @return
	 */
	public List<SysRole> getByRoleName(String roleName) {
		return this.dao.getByRoleName(roleName);
	}
	/**
	 * 符合角色别名的角色数组
	 * @param roleAlias
	 * @return
	 */
	public SysRole getByRoleAlias(String roleAlias) {
		return this.dao.getByRoleAlias(roleAlias);
	}
	
	/**
	 * 授权id查找授权的角色
	 * @param authId
	 * @return
	 */
	public List<SysRole> getByAuthId(Long authId) {
		return this.dao.getByAuthId(authId);
	}
	/**
	 * 用过用户(管理员id)获得角色
	 * @param userId
	 * @return
	 */
	public List<SysRole> getByUser(long userId) {
		HashMap roleMap = new HashMap();
		List<OrgAuth> orgAuthList = this.orgAuthService.getByUserId(userId);
		for (OrgAuth orgAuth : orgAuthList) {
			List<SysRole> roleList = getByAuthId(orgAuth.getId());
			for (SysRole role : roleList) {
				roleMap.put(role.getAlias(), role);
			}
		}
		return new ArrayList(roleMap.values());
	}
	/**
	 * 当前用户管理的分级组织授权的角色
	 * @param queryFilter
	 * @return
	 */
	public List<SysRole> getUserAssignRole(QueryFilter queryFilter) {
		Map params = queryFilter.getFilters();
		params.put("userId", UserContextUtil.getCurrentUserId());
		return this.dao.getUserAssignRole(params);
	}
	/**
	 * 获取角色的分类
	 * @return
	 */
	public List<String> getDistinctCategory() {
		return this.dao.getBySqlKeyGenericity("getDistinctCategory", null);
	}
	
	/**
	 * 将所有角色放到redis中
	 */
	public void setAllSysRoleToRedis(){
		//获取所有的角色
		List<SysRole> sysRoleList = getAll();
		if(BeanUtils.isEmpty(sysRoleList)){
        	return;
        }
		JSONArray roleArr = new JSONArray();
		for(SysRole sysRole:sysRoleList){
			
			JSONObject roleObj = new JSONObject();
			roleObj.put("roleId", sysRole.getRoleId());
			roleObj.put("roleName", sysRole.getRoleName());
			roleArr.add(roleObj);
		}
		
		try {
   		 	//RedisClient.set(RedisKey.ALL_SYSROLE_INF, roleArr.toString());
			allSysRoleInf=roleArr;
        } catch (Exception e) {
            logger.error("角色放到redis中初始化出错");
        }
	}
	
	/**
	 * 通过前台传过来的角色输入框的值进行模糊查询
	 * @param orgFuzzyName
	 * @return
	 */
	public JSONArray getFuzzySysRoleList(String roleFuzzyName,String fieldName,String relvalue,String type,String typeVal){
		//Object allSysRole = RedisClient.get(RedisKey.ALL_SYSROLE_INF);
		//JSONArray allSysRoleInf = JSONArray.fromObject(allSysRole);
		JSONArray roleArr = new JSONArray();
		
		//获取当前用户ID
		Long curentUserId = UserContextUtil.getCurrentUserId();
		//获取当前用户所在角色
		List<SysRole> roleList =  getByUserId(curentUserId);
		
		Iterator<Object> it = allSysRoleInf.iterator();
		
		//其次判断是否需要通过typeVal进行数据过滤
		if((StringUtil.isNotEmpty(type)) && (!"all".equals(typeVal))
				&& (BeanUtils.isNotEmpty(roleList))){
			//将当前用户所在角色id存在字符串中
			String roleIds = "";
			for(int i=0;i<roleList.size();i++){
				if(i>0)
					roleIds += "-";
				roleIds += roleList.get(i).getRoleId();
			}
			
			while(it.hasNext()){
				JSONObject roleObj = (JSONObject) it.next();
				Object fieldValue = roleObj.get(fieldName);
				Object roleId = roleObj.get("roleId");
				
				//当前角色
				if(typeVal.equals("self")){
					if(roleId!=null&&roleIds.contains(roleId.toString())){
						//判断是否需要模糊查询 .
						if(fieldValue!=null){
							if(StringUtil.isChinese(roleFuzzyName)){
								if(fieldValue.toString().contains(roleFuzzyName)){
									roleArr.add(roleObj);
									//限制查询时的数据数量
									if(roleArr.size()>20)
										break;
								}
							}else{
								if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(roleFuzzyName)){
									roleArr.add(roleObj);
									//限制查询时的数据数量
									if(roleArr.size()>20)
										break;
								}
							}
						}else{
							roleArr.add(roleObj);
							//限制查询时的数据数量
							if(roleArr.size()>20)
								break;
						}
					}
				}
			}
		}else{
			while(it.hasNext()){
				JSONObject roleObj = (JSONObject) it.next();
				Object fieldValue = roleObj.get(fieldName);
				
				//判断是否需要模糊查询 .
				if(fieldValue!=null){
					if(StringUtil.isChinese(roleFuzzyName)){
						if(fieldValue.toString().contains(roleFuzzyName)){
							roleArr.add(roleObj);
							//限制查询时的数据数量
							if(roleArr.size()>20)
								break;
						}
					}else{
						if(PinyinUtil.getPinyinToLowerCase(fieldValue.toString()).contains(roleFuzzyName)){
							roleArr.add(roleObj);
							//限制查询时的数据数量
							if(roleArr.size()>20)
								break;
						}
					}
				}else{
					roleArr.add(roleObj);
					//限制查询时的数据数量
					if(roleArr.size()>20)
						break;
				}
			}
		}
		
		return roleArr;
	}
}
