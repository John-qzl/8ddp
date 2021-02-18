package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.OrgAuth;
/**
 * 
 * 组织授权相关DAO
 * <p>Title:OrgAuthDao</p>
 * @author YangBo 
 * @date 2016-7-30下午02:20:41
 */
@Repository
public class OrgAuthDao extends BaseDao<OrgAuth> {
	public Class<OrgAuth> getEntityClass() {
		return OrgAuth.class;
	}
	/**
	 * 通过用户(管理员)获得组织授权信息
	 * @param userId
	 * @return
	 */
	public List<OrgAuth> getByUserId(long userId) {
		return getBySqlKey("getByUserId", Long.valueOf(userId));
	}
	/**
	 * 维度的不同
	 * @param dimId 维度
	 * @param userId
	 * @return
	 */
	public OrgAuth getUserIdDimId(Long dimId, Long userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("dimId", dimId);
		map.put("userId", userId);
		return (OrgAuth) getUnique("getUserIdDimId", map);
	}
	/**
	 * 统计具体组织该管理员的授权数量
	 * @param userId
	 * @param orgId 组织
	 * @return
	 */
	public boolean checkOrgAuthIsExist(Long userId, Long orgId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("userId", userId);
		int count = ((Integer) getOne("checkOrgAuthIsExist", map)).intValue();
		return count > 0;
	}
	/**
	 * 具体组织授权情况
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public OrgAuth getByUserIdAndOrgId(long userId, long orgId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", Long.valueOf(orgId));
		map.put("userId", Long.valueOf(userId));
		return (OrgAuth) getOne("getByUserIdAndOrgId", map);
	}
	/**
	 * 删除具体人员的授权
	 * @param userId
	 */
	public void delByUserId(Long userId) {
		delBySqlKey("delByUserId", userId);
	}
	/**
	 * 删除具体组织的授权
	 * @param orgId
	 */
	public void delByOrgId(Long orgId) {
		delBySqlKey("delByOrgId", orgId);
	}
}
