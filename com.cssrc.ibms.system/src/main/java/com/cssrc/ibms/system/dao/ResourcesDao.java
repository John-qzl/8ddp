package com.cssrc.ibms.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.Resources;
/**
 * 功能点Dao层
 * <p>Title:ResourcesDao</p>
 * @author Yangbo 
 * @date 2016-8-22下午09:05:28
 */
@Repository
public class ResourcesDao extends BaseDao<Resources> {
	public Class getEntityClass() {
		return Resources.class;
	}
	/**
	 * 得出子节点列表
	 * @param parentId
	 * @return
	 */
	public List<Resources> getByParentId(long parentId) {
		return getBySqlKey("getByParentId", Long.valueOf(parentId));
	}

	/**
	 * role的status未做判断，被禁用的角色的功能点也可以查询
	 * @param userId
	 * @return
	 */
	public List<Resources> getNormMenu(Long userId) {
		Map p = new HashMap();
		p.put("userId", userId);
		return getBySqlKey("getNormMenu", p);
	}
	/**
	 * role.sratus=1
	 * @param userId
	 * @return
	 */
	public List<Resources> getNormMenuByUser(Long userId) {
		Map p = new HashMap();
		p.put("userId", userId);
		return getBySqlKey("getNormMenuByUser", p);
	}

	public List<Resources> getNormMenuByAllRole(String rolealias) {
		Map p = new HashMap();
		p.put("rolealias", rolealias);
		return getBySqlKey("getNormMenuByAllRole", p);
	}

	public List<Resources> getSuperMenu(Long userId) {
		return getBySqlKey("getSuperMenu", userId);
	}

	public Integer isAliasExists(String alias) {
		Map params = new HashMap();
		params.put("alias", alias);
		return (Integer) getOne("isAliasExists", params);
	}

	public Integer isAliasExistsForUpd(Long resId, String alias) {
		Map params = new HashMap();
		params.put("alias", alias);
		params.put("resId", resId);
		return (Integer) getOne("isAliasExistsForUpd", params);
	}

	public List<Resources> getByUrl(String url) {
		return getBySqlKey("getByUrl", url);
	}

	public void updSn(Long resId, long sn) {
		Map map = new HashMap();
		map.put("resId", resId);
		map.put("sn", Long.valueOf(sn));
		update("updSn", map);
	}


	public Resources getByAlias(String alias) {
		Map map = new HashMap();
		map.put("alias", alias);
		return (Resources) getUnique("getByAlias", map);
	}

	@Deprecated
	public List<Resources> getByParentUserId(Long resId, Long userId) {
		Map map = new HashMap();
		map.put("resId", resId);
		map.put("userId", userId);

		return getBySqlKey("getByParentUserId", map);
	}

	public List<Resources> getNormMenuByAllRoleParentId(Long resId,
			String rolealias) {
		Map map = new HashMap();
		map.put("resId", resId);
		map.put("rolealias", rolealias);

		return getBySqlKey("getNormMenuByAllRoleParentId", map);
	}
	public List<Resources> getDatatemplateRes(String roleIds){
		Map map = new HashMap();
		map.put("roleIds", roleIds);
		if(roleIds.contains(ISysUser.IMPLEMENT_USER+"")){
			map.put("showAll", true);
		}
		return getBySqlKey("getDatatemplateRes", map);
	}
}
