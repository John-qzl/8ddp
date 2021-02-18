package com.cssrc.ibms.record.dao;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.record.model.RecFun;
import com.cssrc.ibms.system.model.RoleResources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * <p>RecFunDao.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
@Repository
public class RecFunDao  extends BaseDao<RecFun> {
	public Class getEntityClass() {
		return RecFun.class;
	}
	/**
	 * 得出子节点列表
	 * @param parentId
	 * @return
	 */
	public List<RecFun> getByParentId(long parentId) {
		return getBySqlKey("getByParentId", Long.valueOf(parentId));
	}
	/**
	 * 根据表单功能点类型返回功能点
	 * @param typeId
	 * @return
	 */
	public List<RecFun> getByTypeId(long typeId) {
		return getBySqlKey("getByTypeId", Long.valueOf(typeId));
	}
	/**
	 * 根据表单角色获取功能点集合
	 * @param alias
	 * @return
	 */
	public List<RecFun> getFunByRoles(String alias) {
		Map params = new HashMap();
		if(BeanUtils.isEmpty(alias)){
			alias = "''";
		}
		params.put("rolealias", alias);
		return getBySqlKey("getFunByRoles", params);
	}
	/**
	 * 根据记录角色获取功能点集合
	 * @param alias
	 * @return
	 */
	public List<RecFun> getFunByRoleSons(String alias,Long dataId) {
		Map params = new HashMap();
		params.put("roleSonAlias", alias);
		params.put("dataId", dataId);
		return getBySqlKey("getFunByRoleSons", params);
	}
	/**
	 * 别名是否存在
	 * @param alias
	 * @return
	 */
	public Integer isAliasExists(String alias) {
		Map params = new HashMap();
		params.put("alias", alias);
		return (Integer) getOne("isAliasExists", params);
	}
	/**
	 * @param funId
	 * @param alias
	 * @return
	 */
	public Integer isAliasExistsForUpd(Long funId, String alias) {
		Map params = new HashMap();
		params.put("alias", alias);
		params.put("funId", funId);
		return (Integer) getOne("isAliasExistsForUpd", params);
	}
	public void updSn(Long funId, long sn) {
		Map map = new HashMap();
		map.put("funId", funId);
		map.put("sn", Long.valueOf(sn));
		update("updSn", map);
	}
	public RecFun getByAlias(String alias) {
		return (RecFun)getUnique("getByAlias",alias);
	}
}
