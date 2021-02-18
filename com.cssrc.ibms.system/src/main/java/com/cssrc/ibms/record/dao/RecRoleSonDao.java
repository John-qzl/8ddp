package com.cssrc.ibms.record.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.record.model.RecRoleSon;
import com.cssrc.ibms.record.model.RecRoleSonUser;

import org.springframework.stereotype.Repository;


/**
 * Description:
 * <p>RecRoleSonDao.java</p>
 * @author dengwenjie 
 * @date 2017年4月6日
 */
@Repository
public class RecRoleSonDao  extends BaseDao<RecRoleSon> {
	public Class getEntityClass() {
		return RecRoleSon.class;
	}
	/**
	 * 根据记录ID返回记录角色
	 * @param recType
	 * @return
	 */
	public List<RecRoleSon> getByDataId(Long dataId) {
		return getBySqlKey("getByDataId", Long.valueOf(dataId));
	}
	
	/**
	 * roleId与dataId组成唯一key（ibms_rec_roleson），等同于ROLESONID
	 * @param roleId：父id
	 * @param dataId：记录id
	 * @return
	 */
	public RecRoleSon getByParentRoleId(Long roleId,Long dataId) {
		Map map = new HashMap();
		map.put("roleId", roleId);
		map.put("dataId", dataId);
		return getUnique("getByParentRoleId",map);
	}
	/**
	 * 同步默认角色时，对 多余的记录角色（非用户添加，新的默认角色已删除的）进行删除
	 * @param roleIds ： 不删除的 默认角色id集合
	 */
	public void delByRoleIds(String roleIds){
		Map map = new HashMap();
		map.put("roleIds", roleIds);
		delBySqlKey("delByRoleIds", map);
	}
	/**
	 * 判断角色别名是否存在
	 * @param alias
	 * @return
	 */
	public boolean isExistRoleAlias(String alias,Long dataId) {
		Map map = new HashMap();
		map.put("alias", alias);
		map.put("dataId", dataId);
		Integer count = (Integer) getOne("isExistRoleAlias", map);
		return count.intValue() > 0;
	}
	/**
	 * 该角色别名是否更新过（多个相同别名角色）
	 * @param alias
	 * @param roleId
	 * @return
	 */
	public boolean isExistRoleAliasForUpd(String alias, Long roleSonId,Long dataId) {
		Map map = new HashMap();
		map.put("alias", alias);
		map.put("roleSonId", roleSonId);
		map.put("dataId", dataId);
		Integer count = (Integer) getOne("isExistRoleAliasForUpd", map);
		return count.intValue() > 0;
	}
	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public List<RecRoleSon> getRole(QueryFilter queryFilter) {
		return getBySqlKey("getRole", queryFilter);
	}
	
	/**
	 * 判断该记录是否进行了记录角色的设置
	 * @param pk
	 * @return
	 */
	public boolean isExistRoleSon(Long dataId){
		Map map = new HashMap();
		map.put("dataId", dataId);
		Integer count = (Integer) getOne("isExistRoleSon", map);
		return count.intValue() > 0;
	}
	public List<RecRoleSon> getRecRoleSonInfo(Long dataTemplateId,Long dataId){
		Map map = new HashMap();
		map.put("dataTemplateId", dataTemplateId);
		map.put("dataId", dataId);
		return getBySqlKey("getRecRoleSonInfo",map);
	}
}
