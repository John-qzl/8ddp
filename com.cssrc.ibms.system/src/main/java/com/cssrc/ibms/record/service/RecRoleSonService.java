package com.cssrc.ibms.record.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.rec.intf.IRecRoleSonService;
import com.cssrc.ibms.api.rec.model.IRecRoleSon;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.record.dao.RecRoleDao;
import com.cssrc.ibms.record.dao.RecRoleSonDao;
import com.cssrc.ibms.record.dao.RecRoleSonUserDao;
import com.cssrc.ibms.record.dao.RecTypeDao;
import com.cssrc.ibms.record.model.RecRole;
import com.cssrc.ibms.record.model.RecRoleSon;
import com.cssrc.ibms.record.model.RecRoleSonUser;
import com.cssrc.ibms.record.model.RecType;


/**
 * Description:
 * <p>RecRoleSonService.java</p>
 * @author dengwenjie 
 * @date 2017年4月5日
 */
@Service
public class RecRoleSonService  extends BaseService<RecRoleSon> implements IRecRoleSonService{
	
	@Resource
	private RecRoleFunService recRoleFunService;
	@Resource
	private RecRoleSonFunService recRoleSonFunService;
	@Resource
	private IUserPositionService userPositionService;
	@Resource
	private RecRoleSonDao dao;
	@Resource
	public RecTypeDao recTypeDao;
	@Resource
	private RecRoleDao recRoleDao;
	@Resource
	private RecRoleSonUserDao recRoleUserDao;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private IPositionService positionService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;

	protected IEntityDao<RecRoleSon, Long> getEntityDao() {
		return this.dao;
	}	
	public List<RecRoleSonUser> getByRoleSonId(Long roleSonId){
		List<RecRoleSonUser> rrsuList;
		RecRoleSon rrs = dao.getById(roleSonId);
		if(BeanUtils.isEmpty(rrs)){
			return null;
		}
		String filter = rrs.getFilter();
		String def_filter = rrs.getDef_filter();
		String sql = this.getUserSql(JSONArray.fromObject(filter));
		if(BeanUtils.isEmpty(def_filter)){
			String defSql = this.getDefUserSql(JSONObject.fromObject(def_filter));
			rrsuList =recRoleUserDao.getRecUser(sql,defSql, rrs.getUserAdd(), rrs.getUserDel(),rrs.getRoleSonId());
		}else{
			String defSql = this.getDefUserSql(JSONObject.fromObject(def_filter));
			rrsuList =recRoleUserDao
					.getRecUser(sql,defSql, rrs.getUserAdd(), rrs.getUserDel(),rrs.getRoleSonId());
		}
		return rrsuList;
	}
	public List<? extends IRecRoleSon> getRecRoleSonInfo(Long dataTemplateId,Long dataId,String typeAlias)throws Exception{
		List<? extends IRecRoleSon> list = dao.getRecRoleSonInfo(dataTemplateId, dataId);
		if(BeanUtils.isEmpty(list)){
			this.initRecRoleSon(dataId,dataTemplateId,typeAlias);
			list = dao.getRecRoleSonInfo(dataTemplateId, dataId);
		}
		for(RecRoleSon rrs : (List<RecRoleSon>)list){
			String filter = rrs.getFilter();
			String def_filter = rrs.getDef_filter();
			String userAdd = rrs.getUserAdd();
			String userDel = rrs.getUserDel();
			String sql = this.getUserSql(JSONArray.fromObject(filter));
			if(BeanUtils.isEmpty(def_filter)){
				List<RecRoleSonUser> rrsu =recRoleUserDao
						.getRecUser(sql, userAdd, userDel,rrs.getRoleSonId());
				rrs.setUserList(rrsu);
			}else{
				String defSql = this.getDefUserSql(JSONObject.fromObject(def_filter));
				List<RecRoleSonUser> rrsu =recRoleUserDao
						.getRecUser(sql,defSql, userAdd, userDel,rrs.getRoleSonId());
				rrs.setUserList(rrsu);
			}
		}
		return list;
	}
	
	public List<RecRoleSon> getAll(QueryFilter queryFilter){
		List<RecRoleSon> list = super.getAll(queryFilter);
		for(RecRoleSon rrs : list){
			String filter = rrs.getFilter();
			String def_filter = rrs.getDef_filter();
			String userAdd = rrs.getUserAdd();
			String userDel = rrs.getUserDel();
			String sql = this.getUserSql(JSONArray.fromObject(filter));
			if(BeanUtils.isEmpty(def_filter)){
				List<RecRoleSonUser> rrsu =recRoleUserDao
						.getRecUser(sql, userAdd, userDel,rrs.getRoleSonId());
				rrs.setUserList(rrsu);
			}else{
				String defSql = this.getDefUserSql(JSONObject.fromObject(def_filter));
				List<RecRoleSonUser> rrsu =recRoleUserDao
						.getRecUser(sql,defSql, userAdd, userDel,rrs.getRoleSonId());
				rrs.setUserList(rrsu);
			}
		}
		return list;
	}
	private String getDefUserSql(JSONObject permission){
		if(BeanUtils.isEmpty(permission)){
			return " where 1=0 ";
		}
		String filterSql = "";
		String type = permission.get("type").toString();
		String id = permission.get("id").toString();
		if ("none".equals(type)){
			filterSql = " where 1=0 ";// 无
			return filterSql;
		}

		if ("everyone".equals(type)){// 所有人
			filterSql = " where 1=1 ";
			return filterSql;
		}		
		// 指定用户
		if ("user".equals(type)) {
			String ids = "'"+id.replace(",", "','")+"'";
			filterSql = " where USERID in ("+ids+") ";
		}
		// 指定角色
		else if ("role".equals(type)) {
			String ids = "'"+id.replace(",", "','")+"'";
			filterSql = " where userId in ( "+
						" select  userid   from  cwm_sys_role_user  where  roleid in ("+ids+")"+  
						" ) ";
		}
		// 指定组织
		else if ("org".equals(type)) {
			String ids = "";
			String  orgIds= sysOrgService.getIdsBySupId(Long.valueOf(id));
			List<? extends IUserPosition> list = this.userPositionService.getByOrgIds(orgIds,null);
			for(IUserPosition p : list){
				ids +=p.getUserId()+",";
			}
			if(ids.equals("")){
				ids = "''";
			}else{
				ids = ids.substring(0, ids.length()-1);
				ids = "'" + ids.replace(",", "','") + "'";
			}
			filterSql = " where USERID in ("+ids+") ";
		}
		// 组织负责人
		else if ("orgMgr".equals(type)) {
			String ids = "";
			String  orgIds= sysOrgService.getIdsBySupId(Long.valueOf(id));
			List<? extends IUserPosition> list = this.userPositionService.getByOrgIds(orgIds,1L);
			for(IUserPosition p : list){
				ids +=p.getUserId()+",";
			}
			if(ids.equals("")){
				ids = "''";
			}else{
				ids = ids.substring(0, ids.length()-1);
				ids = "'" + ids.replace(",", "','") + "'";
			}
			filterSql = " where USERID in ("+ids+") ";
		}
		// 岗位
		else if ("pos".equals(type)) {
			String ids = "'"+id.replace(",", "','")+"'";
			filterSql = " where userId in ( "+
					" select  userid   from  CWM_SYS_USER_POSITION  where  posid in ("+ids+")"+  
					" ) ";
		}else{
			filterSql = " where 1=0 ";
		}
		return filterSql;
	}
	private String getUserSql(JSONArray jsonArr){
		if(BeanUtils.isEmpty(jsonArr)||BeanUtils.isEmpty(jsonArr.get(0))){
			return " where 1=0 ";
		}
		JSONObject permission = (JSONObject)jsonArr.get(0);
		String filterSql = "";
		String type = permission.get("type").toString();
		String id = permission.get("id").toString();
		if ("none".equals(type)){
			filterSql = " where 1=0 ";// 无
			return filterSql;
		}

		if ("everyone".equals(type)){// 所有人
			filterSql = " where 1=1 ";
			return filterSql;
		}		
		// 指定用户
		if ("user".equals(type)) {
			String ids = "'"+id.replace(",", "','")+"'";
			filterSql = " where USERID in ("+ids+") ";
		}
		// 指定角色
		else if ("role".equals(type)) {
			String ids = "'"+id.replace(",", "','")+"'";
			filterSql = " where userId in ( "+
						" select  userid   from  cwm_sys_role_user  where  roleid in ("+ids+")"+  
						" ) ";
		}
		// 指定组织
		else if ("org".equals(type)) {
			String ids = "";
			String  orgIds= sysOrgService.getIdsBySupId(Long.valueOf(id));
			List<? extends IUserPosition> list = this.userPositionService.getByOrgIds(orgIds,null);
			for(IUserPosition p : list){
				ids +=p.getUserId()+",";
			}
			if(ids.equals("")){
				ids = "''";
			}else{
				ids = ids.substring(0, ids.length()-1);
				ids = "'" + ids.replace(",", "','") + "'";
			}
			filterSql = " where USERID in ("+ids+") ";
		}
		// 组织负责人
		else if ("orgMgr".equals(type)) {
			String ids = "";
			String  orgIds= sysOrgService.getIdsBySupId(Long.valueOf(id));
			List<? extends IUserPosition> list = this.userPositionService.getByOrgIds(orgIds,1L);
			for(IUserPosition p : list){
				ids +=p.getUserId()+",";
			}
			if(ids.equals("")){
				ids = "''";
			}else{
				ids = ids.substring(0, ids.length()-1);
				ids = "'" + ids.replace(",", "','") + "'";
			}
			filterSql = " where USERID in ("+ids+") ";
		}
		// 岗位
		else if ("pos".equals(type)) {
			String ids = "'"+id.replace(",", "','")+"'";
			filterSql = " where userId in ( "+
					" select  userid   from  CWM_SYS_USER_POSITION  where  posid in ("+ids+")"+  
					" ) ";
		}else{
			filterSql = " where 1=0 ";
		}
		return filterSql;
	}
	
	/**
	 * def_filter的格式：{"type":"role","id":"10000000290007,-1,-2"}
	 * type对应的类型为：[none\\everyone\\user\\role\\org\\orgMgr\\pos]
	 * id:对应的主键集合，逗号分隔
	 * 增加自定义过滤
	 * @param def_filter
	 * @param roleSonId
	 */
	public Object[] addDefFilter(String def_filter,Long roleSonId){
		RecRoleSon rrs = this.dao.getById(roleSonId);
		//校验def_filter的格式
		if(BeanUtils.isEmpty(def_filter)){
			rrs.setDef_filter(def_filter);
			this.update(rrs);
			return new Object[]{true,""};
		}else{
			JSONObject obj = JSONObject.fromObject(def_filter);
			if(obj.containsKey("type")&&obj.containsKey("id")){
				String value = obj.getString("type");
				String ss = ",none,everyone,user,role,org,orgMgr,pos,";
				if(!ss.contains(","+value+",")){
					return new Object[]{false,value+"必须是none,everyone,user,role,org,orgMgr,pos中的一个！"};
				}
				rrs.setDef_filter(def_filter);
				this.update(rrs);	
				return new Object[]{true,""};
			}else{
				return new Object[]{false,"def_filter中应包含type和id两个key值！"};
			}
		}
	}
	
	/**
	 * 新增用户
	 * @param userIds
	 * @param roleSonId
	 */
	public void addUser(Long[] userIds,Long roleSonId){
		RecRoleSon rrs = this.dao.getById(roleSonId);
		String[] addArr = rrs.getUserAdd().split(",");
		String[] delArr = rrs.getUserDel().split(",");
		String userAdd = rrs.getUserAdd();
		String userDel = rrs.getUserDel();
		Arrays.sort(addArr);
		Arrays.sort(delArr);
		for(Long userId : userIds){
			String id = String.valueOf(userId);
			int i_add = Arrays.binarySearch(addArr, id);
			int i_del = Arrays.binarySearch(delArr, id);
			if(i_add<0){
				userAdd+= "," + id;
			}
			if(i_del>=0){
				userDel = userDel.replaceAll(","+id, "");
			}
		}
		rrs.setUserAdd(userAdd);
		rrs.setUserDel(userDel);
		this.update(rrs);
	}
	
	/**
	 * 删除用户
	 * @param userIds
	 * @param roleSonId
	 */
	public void delUser(Long[] userIds,Long roleSonId){
		RecRoleSon rrs = this.dao.getById(roleSonId);
		String[] addArr = rrs.getUserAdd().split(",");
		String[] delArr = rrs.getUserDel().split(",");
		String userAdd = rrs.getUserAdd();
		String userDel = rrs.getUserDel();
		Arrays.sort(addArr);
		Arrays.sort(delArr);
		for(Long userId : userIds){
			String id = String.valueOf(userId);
			int i_add = Arrays.binarySearch(addArr, id);
			int i_del = Arrays.binarySearch(delArr, id);
			if(i_add>=0){
				userAdd = userAdd.replaceAll(","+id, "");
			}
			if(i_del<0){
				userDel+= "," + id;
			}
		}
		rrs.setUserAdd(userAdd);
		rrs.setUserDel(userDel);
		this.update(rrs);
	}
	/**
	 * 将表ibms_rec_role 表中的数据复制到 ibms_rec_roleson
	 * @param dataId
	 * @param dataTemplateId
	 */
	public void initRecRoleSon(Long dataId,Long dataTemplateId,String typeAlias) throws Exception{
		RecType rt = recTypeDao.getByAlias(typeAlias);
		List<RecRole> list = recRoleDao.getByTypeId(rt.getTypeId());
		String roleIds = "0";
		for(RecRole rr : list){
			roleIds += ","+rr.getRoleId();
			RecRoleSon rrs = dao.getByParentRoleId(rr.getRoleId(),dataId);
			if(rrs==null){
				rrs = new RecRoleSon();
				rrs.setRoleSonId(Long.valueOf(UniqueIdUtil.genId()));
				rrs.setRoleId(rr.getRoleId());
				rrs.setTypeName(rt.getTypeName());
				rrs.setTypeId(rt.getTypeId());
				rrs.setDataId(dataId);
				rrs.setDataTemplateId(dataTemplateId);
				rrs.setRoleSonName(rr.getRoleName());
				rrs.setAlias(rr.getAlias());
				rrs.setRoleSonDesc(rr.getRoleDesc());
				rrs.setFilter(rr.getFilter());
				rrs.setIsHide(rr.getIsHide());
				rrs.setAllowDel(rr.getAllowDel());
				add(rrs);
			}else{
				rrs.setTypeName(rt.getTypeName());
				rrs.setTypeId(rt.getTypeId());
				rrs.setDataId(dataId);
				rrs.setDataTemplateId(dataTemplateId);
				rrs.setRoleSonName(rr.getRoleName());
				rrs.setAlias(rr.getAlias());
				rrs.setRoleSonDesc(rr.getRoleDesc());
				rrs.setFilter(rr.getFilter());		
				rrs.setIsHide(rr.getIsHide());
				rrs.setAllowDel(rrs.getAllowDel());
				//复制角色
				update(rrs);
			}
			//删除之前分配的
			recRoleSonFunService.delByRoleSonId(rrs.getRoleSonId());
			//复制角色已分配的功能权限
			recRoleSonFunService.synRecRoleSonFun(rr.getRoleId(),rrs.getRoleSonId());
		}
	}
	/**
	 * @param typeAlias
	 * @return
	 */
	public RecType getRecType(String typeAlias){
		return recTypeDao.getByAlias(typeAlias);
	}
	/**
	 * 角色别名校验
	 * @param alias
	 * @return
	 */
	public boolean isExistRoleAlias(String alias,Long dataId) {
		return this.dao.isExistRoleAlias(alias,dataId);
	}
	/**
	 * 该角色别名是否存在了
	 * @param alias
	 * @param roleId
	 * @return
	 */
	public boolean isExistRoleAliasForUpd(String alias, Long recRoleId,Long dataId) {
		return this.dao.isExistRoleAliasForUpd(alias, recRoleId,dataId);
	}
	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public List<RecRoleSon> getRoleTree(QueryFilter queryFilter) {
		return this.dao.getRole(queryFilter);
	}
	/**
	 * 根据记录ID查询出角色
	 * @param dataId
	 * @return
	 */
	public List<RecRoleSon> getByDataId(Long dataId) {
		return this.dao.getByDataId(dataId);
	}
	/**
	 *判断该记录是否进行了记录角色的设置
	 * @param dataId
	 * @return
	 */
	public boolean isExistRoleSon(Long dataId) {
		return this.dao.isExistRoleSon(dataId);
	}
	
	/**
	 * 根据类别、及用户ID，得到用户的所属 记录项目角色别名
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @return
	 */
	public String getAllRoleSonAliasByType(Long userId,Long dataId){
		String alias = "";
		List<RecRoleSon>  roles =  dao.getByDataId(dataId);
		for(RecRoleSon role : roles){
			if(isRecRoleSon(userId,role)){
				alias +=  "'" + role.getAlias() + "',";
			}
		}
		if(alias.contains(",")){
			alias = alias.substring(0, alias.length()-1);
		}
		if(alias.equals("")){
			alias = "''";
		}
		return alias;
	}
	/**
	 * 根据类别、及用户ID，得到用户的记录项目角色
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @return
	 */
	public List<RecRoleSon> getRoleSonsByUT(Long userId,Long dataId){
		List<RecRoleSon> rs = new ArrayList();
		List<RecRoleSon>  roles =  dao.getByDataId(dataId);
		for(RecRoleSon role : roles){
			if(isRecRoleSon(userId,role)){
				rs.add(role);
			}
		}

		return rs;
	}
	/**
	 * 根据类别、及用户ID、项目角色别名，得到是否属于该项目角色
	 * @param userId ： 用户ID
	 * @param typeAlias : 类别别名
	 * @param recRoleAlias ： 项目角色别名
	 * @return
	 */
	public boolean isRecRoleSon(Long userId,RecRoleSon role){		
		boolean flag = false;
		Long curOrgId = 0L;
		ISysOrg org = UserContextUtil.getCurrentOrg();
		if(org!=null){
			curOrgId = UserContextUtil.getCurrentOrg().getOrgId();
		}
		/*1.用户已经删除的情况:false*/
		String userDel = role.getUserDel();
		if(userDel.contains(","+userId)){
			return false;
		}
		/*2.用户已经添加的情况:true*/
		String userAdd = role.getUserAdd();
		if(userAdd.contains(","+userId)){
			return true;
		}
		/*3.用户已经添加的情况:true*/
		String defFilter = role.getDef_filter();
		if(BeanUtils.isNotEmpty(defFilter)){
			// 获取权限map
			Map<String, Object> rightMap = this.getRightMap(userId, curOrgId);		
			JSONObject c = JSONObject.fromObject(defFilter);
			flag = hasDefRight(c,rightMap);
			if(flag){
				return flag;
			}
		}
		/*4.filter不为空：系统定义*/
		String filter = role.getFilter();
		if(BeanUtils.isNotEmpty(filter)){
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
	private boolean hasDefRight(JSONObject permission, Map<String, Object> rightMap){
		boolean hasRight = false;
		String type = permission.get("type").toString();
		String id = permission.get("id").toString();
		if ("none".equals(type)) // 无
			return false;

		if ("everyone".equals(type))// 所有人
			return true;

		Long userId = (Long) rightMap.get("userId");
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
		}
		return false;
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
}
