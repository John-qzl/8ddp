package com.cssrc.ibms.record.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.rec.intf.IRecRoleService;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.record.dao.RecRoleDao;
import com.cssrc.ibms.record.dao.RecRoleFunDao;
import com.cssrc.ibms.record.dao.RecTypeDao;
import com.cssrc.ibms.record.model.RecRole;
import com.cssrc.ibms.record.model.RecType;

/**
 * Description:
 * <p>RecRoleService.java</p>
 * @author dengwenjie 
 * @date 2017年3月13日
 */
@Service
public class RecRoleService  extends BaseService<RecRole> implements IRecRoleService{
	
	@Resource
	private RecRoleFunService recRoleFunService;
	@Resource
	private RecRoleDao dao;
	@Resource
	private RecRoleFunDao recRoleFunDao;
	@Resource
	private RecTypeDao recTypeDao;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private IPositionService positionService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private ISysRoleService sysRoleService;
	
	protected IEntityDao<RecRole, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 复制角色方法(复制用户角色关联和角色资源关联)
	 * @param recRole
	 * @param oldRoleId
	 * @throws Exception
	 *//*
	public void copyRole(RecRole recRole, long oldRoleId) throws Exception {
		Long newRoleId = recRole.getRoleId();
		this.dao.add(recRole);
		
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.addFilterForIB("roleId", Long.valueOf(oldRoleId));
		List<RecRoleMeta> roleMetaList = this.recRoleMetaDao.getAll(queryFilter);
		for (RecRoleMeta recRoleMeta : roleMetaList) {
			RecRoleMeta rrm = (RecRoleMeta) recRoleMeta.clone();
			rrm.setRoleMetaId(Long.valueOf(UniqueIdUtil.genId()));
			rrm.setRoleId(newRoleId);
			this.recRoleMetaDao.add(rrm);
		}
		this.recRoleFunService.copyRecRoleFun(oldRoleId, newRoleId);	
	}*/
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
	public boolean isExistRoleAliasForUpd(String alias, Long recRoleId) {
		return this.dao.isExistRoleAliasForUpd(alias, recRoleId);
	}
	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public List<RecRole> getRoleTree(QueryFilter queryFilter) {
		return this.dao.getRole(queryFilter);
	}
	/**
	 * 根据角色类别查询出所有角色
	 * @param queryFilter
	 * @return
	 */
	public List<RecRole> getByTypeId(Long typeId) {
		return this.dao.getByTypeId(typeId);
	}
	/**
	 * 根据角色类别查询出当前用户，是否可以进行记录权限设置
	 * 0:不允许  ，1：允许
	 * @return
	 */
	public boolean canRecordSet(Long userId,String typeAlias){
		boolean canSet = false;
		List<RecRole>  roles = getByTypeAlias(typeAlias);
		for(RecRole role : roles){
			if(isRecRole(userId,typeAlias,role.getAlias())){
				short set = role.getAllowSet()==null?0:role.getAllowSet();
				if(set==1){
					canSet = true;
					break;
				}
			}
		}
		return canSet;
	}
	
	/**
	 * 获取此类型别名 的 所有角色信息
	 * @param typeAlias : 类型别名
	 * @return
	 */
	public List<RecRole> getByTypeAlias(String typeAlias) {
		RecType type = recTypeDao.getByAlias(typeAlias);
		return getByTypeId(type.getTypeId());
	}
	/**
	 * 根据类别、及用户ID，得到用户的默认项目角色
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @return
	 */
	public List<RecRole> getRolesByUT(Long userId, String typeAlias){
		List<RecRole> rs = new ArrayList();
		List<RecRole>  roles = getByTypeAlias(typeAlias);
		for(RecRole role : roles){
			if(isRecRole(userId,typeAlias,role.getAlias())){
				rs.add(role);
			}
		}

		return rs;
	}
	/**
	 * 根据类别、及用户ID，得到用户的所属 项目角色别名
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @return
	 */
	public String getAllRoleAliasByType(Long userId, String typeAlias){
		String alias = "";
		List<RecRole>  roles = getByTypeAlias(typeAlias);
		for(RecRole role : roles){
			if(isRecRole(userId,typeAlias,role.getAlias())){
				alias +=  "'" + role.getAlias() + "',";
			}
		}
		if(alias.contains(",")){
			alias = alias.substring(0, alias.length()-1);
		}
		return alias;
	}
	/**
	 * 根据类别、及用户ID、项目角色别名，得到是否属于该项目角色
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @param recRoleAlias ： 项目角色别名
	 * @return
	 */
	public boolean isRecRole(Long userId,String typeAlias,String recRoleAlias){		
		boolean flag = false;
		Long curOrgId = 0L;
		ISysOrg org = UserContextUtil.getCurrentOrg();
		if(org!=null){
			curOrgId = UserContextUtil.getCurrentOrg().getOrgId();
		}
		//获取过滤条件
		RecRole role = dao.getRoleByAlias(recRoleAlias);
		String filter = role.getFilter();
		if(StringUtil.isNotEmpty(filter)){
			// 获取权限map
			Map<String, Object> rightMap = this.getRightMap(userId, curOrgId);
			
			JSONArray jsonAry = JSONArray.fromObject(filter);
			for (Object obj : jsonAry) {
				JSONObject c = JSONObject.fromObject(obj);
				flag = hasRight(c,rightMap);
				if(flag){
					break;
				}
			}
		}
		return flag;
	}
	/**
	 * 根据类别、及用户ID、项目角色ID，得到是否属于该项目角色
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @param recRoleId ： 项目角色ID
	 * @return
	 */
	public boolean isRecRole(Long userId,String typeAlias,Long recRoleId){
		RecRole role = dao.getById(recRoleId);
		return isRecRole(userId,typeAlias,role.getAlias());
	}
	private boolean hasRight(JSONObject permission, Map<String, Object> rightMap) {
		boolean hasRight = false;
		String type = permission.get("type").toString();
		String id = permission.get("id").toString();
		Object script = permission.get("script");
		if ("none".equals(type)) // 无
			return false;

		if ("everyone".equals(type))// 所有人
			return true;

		Long userId = (Long) rightMap.get("userId");
		// Long curOrgId = (Long) rightMap.get("curOrgId");
		List<ISysRole> roles = (List<ISysRole>) rightMap.get("roles");
		List<?extends IPosition> positions = (List<?extends IPosition>) rightMap.get("positions");
		List<ISysOrg> orgs = (List<ISysOrg>) rightMap.get("orgs");
		List<ISysOrg> ownOrgs = (List<ISysOrg>) rightMap.get("ownOrgs");
		// 指定用户
		if ("user".equals(type)) {
			hasRight = StringUtil.contain(id, userId.toString());
			return hasRight;
		}
		// 指定角色
		else if ("role".equals(type)) {
			for (ISysRole role : roles) {
				if (StringUtil.contain(id, role.getRoleId().toString())) {
					return true;
				}
			}
		}
		// 指定组织
		else if ("org".equals(type)) {
			for (ISysOrg org : orgs) {
				if (StringUtil.contain(id, org.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 组织负责人
		else if ("orgMgr".equals(type)) {
			for (ISysOrg ownOrg : ownOrgs) {
				if (StringUtil.contain(id, ownOrg.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 岗位
		else if ("pos".equals(type)) {
			for (IPosition position : positions) {
				if (StringUtil.contain(id, position.getPosId().toString())) {
					return true;
				}
			}
		} else if ("script".equals(type)) {
			if (BeanUtils.isEmpty(script))
				return false;
			Map<String, Object> map = new HashMap<String, Object>();
			CommonVar.setCurrentVars(map);
			return groovyScriptEngine.executeBoolean(script.toString(), map);
		}
		return false;
	}
	/**
	 * 获取当前用户的权限Map
	 * 
	 * @param userId
	 * @param curOrgId
	 * @return
	 */
	public Map<String, Object> getRightMap(Long userId, Long curOrgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ISysUser user = sysUserService.getById(userId);
		List<?extends ISysRole> roles = sysRoleService.getByUserId(userId);
		List<?extends IPosition> positions = positionService.getByUserId(userId);
		List<?extends ISysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
		// 获取可以管理的组织列表。
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);

		map.put("userId", userId);
		map.put("curOrgId", curOrgId);
		map.put("roles", roles);
		map.put("positions", positions);
		map.put("orgs", orgs);
		map.put("ownOrgs", ownOrgs);
		return map;
	}
	
	public RecRole getRoleByAlias(String alias) {
		return dao.getRoleByAlias(alias);
	}
}
