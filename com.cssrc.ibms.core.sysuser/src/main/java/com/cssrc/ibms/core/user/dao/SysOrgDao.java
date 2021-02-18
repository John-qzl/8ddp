package com.cssrc.ibms.core.user.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 组织架构DAO
 * <p>Title:SysOrgDao</p>
 * @author Yangbo 
 * @date 2016-8-1下午03:56:10
 */
@Repository
public class SysOrgDao extends BaseDao<SysOrg> {
	public Class getEntityClass() {
		return SysOrg.class;
	}
	/**
	 * 获取组织信息
	 * @param queryFilter
	 * @return
	 */
	public List<SysOrg> getOrgByOrgId(QueryFilter queryFilter) {
		return getBySqlKey("getOrgByOrgId", queryFilter);
	}
	/**
	 * 获取指定维度的组织
	 * @param demId
	 * @return
	 */
	public List<SysOrg> getOrgByDemId(Long demId) {
		return getBySqlKey("getOrgByDemId", demId);
	}
	/**
	 * 组织名为orgname的信息
	 * @param orgName
	 * @return
	 */
	public List<SysOrg> getByOrgName(String orgName) {
		return getBySqlKey("getByOrgName", orgName);
	}
	
	/**
	 * 用户所属的组织信息
	 * @param username
	 * @return
	 */
	public SysOrg getOrgByUsername(String username) {
		List<SysOrg> list = getBySqlKey("getOrgByUsername", username);
		if (list.size() == 0) {
			return new SysOrg();
		}
		return (SysOrg) list.get(0);
	}
	
	/**
	 * 更新组织节点顺序
	 * @param orgId
	 * @param sn
	 */
	public void updSn(Long orgId, long sn) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("sn", Long.valueOf(sn));
		update("updSn", params);
	}

	/**
	 * 获得用户所在组织
	 * @param userId
	 * @return
	 */
	public List<SysOrg> getOrgsByUserId(Long userId) {
		return getBySqlKey("getOrgsByUserId", userId);
	}
	
	/**
	 * 组织id集合详细列表
	 * @param orgIds
	 * @return
	 */
	public List<SysOrg> getByOrgIds(List<Long> orgIds) {
		List<SysOrg> list = getBySqlKey("getByOrgIds", orgIds);
		return list;
	}
	
	/**
	 * 分组组织角色分配列表
	 * @param groupId
	 * @return
	 */
	public List<SysOrg> getByOrgMonGroup(Long groupId) {
		return getBySqlKey("getByOrgMonGroup", groupId);
	}
	
	/**
	 * 取得所有组织列表(维度可为空)
	 * @param demId
	 * @return
	 */
	public List<SysOrg> getOrgsByDemIdOrAll(Long demId) {
		Map<String,Object> params = new HashMap<String,Object>();
		if (demId.longValue() != 0L) {
			params.put("demId", demId);
		}
		return getBySqlKey("getOrgsByDemIdOrAll", params);
	}
	/**
	 * 获取指定维度下的组织
	 * @param userId
	 * @param demId
	 * @return
	 */
	public List<SysOrg> getByUserIdAndDemId(Long userId, Long demId) {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("userId", userId);
		m.put("demId", demId);
		return getBySqlKey("getByUserIdAndDemId", m);
	}
	/**
	 * 获得固定层级的对应组织
	 * @param depth
	 * @return
	 */
	public List<SysOrg> getByDepth(Integer depth) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("depth", depth);
		return getBySqlKey("getByDepth", params);
	}
	/**
	 * 获取根路径为path的组织树
	 * @param path
	 * @return
	 */
	public List<SysOrg> getByOrgPath(String path) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", StringUtil.isNotEmpty(path) ? path + "%" : "");
		return getBySqlKey("getByOrgPath", params);
	}
	/**
	 * 根据数据来源获得组织列表
	 * @param type
	 * @return
	 */
	public List<SysOrg> getByFromType(short type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fromType", Short.valueOf((short) type));
		return getBySqlKey("getByFromType", params);
	}
	/**
	 * 级联删除父子组织列表
	 * @param path
	 */
	public void delByPath(String path) {
		delBySqlKey("delByPath", path);
	}
	/**
	 * 级联逻辑删除父子组织列表
	 * @param path
	 */
	public void delLogicByPath(String path) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", path);
		params.put("updateTime", new Date());
		update("delLogicByPath", params);
	}
	/**
	 * 还原被逻辑删除的组织架构
	 * @param path
	 */
	public void restoreLogicOrg(String path) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", path);
		params.put("updateTime", new Date());
		update("restoreLogicOrg", params);
	}
	/**
	 * 根据用户userid获取主组织
	 * 
	 * @param userId
	 * @return
	 */
	public SysOrg getPrimaryOrgByUserId(Long userId) {
		return (SysOrg) getUnique("getPrimaryOrgByUserId", userId);
	}
	/**
	 * 获取用户负责的组织信息
	 * @param userId
	 * @return
	 */
	public List<SysOrg> getChargeOrgByUserId(Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		List<SysOrg> list=getBySqlKey("getChargeOrgByUserId", params);
		return list;
	}
	
	/**
	 * 获取orgids对应组织信息
	 * @param orgIds
	 * @return
	 */
	public List<SysOrg> getOrgByIds(String orgIds) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgIds", orgIds);
		List<SysOrg> list = getBySqlKey("getOrgByIds", params);
		return list;
	}
	/**
	 * 获得下级组织
	 * @param orgSupId
	 * @return
	 */
	public List<SysOrg> getOrgByOrgSupId(Long orgSupId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgSupId", orgSupId);
		List<SysOrg> list = getBySqlKey("getOrgByOrgSupId", params);
		return list;
	}
	/**
	 * 获得组织管理界面的下级组织
	 * @param orgSupId
	 * @return
	 */
	public List<SysOrg> getOrgManageByOrgSupId(Long orgSupId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgSupId", orgSupId);
		List<SysOrg> list = getBySqlKey("getOrgManageByOrgSupId", params);
		return list;
	}
	/**
	 * 获得包含已逻辑删除的下级组织
	 * @param orgSupId
	 * @return
	 */
	public List<SysOrg> getLogicOrgByOrgSupId(Long orgSupId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgSupId", orgSupId);
		List<SysOrg> list = getBySqlKey("getLogicOrgByOrgSupId", params);
		return list;
	}
	/**
	 * 组织名为orgname的信息
	 * @param orgName
	 * @return
	 */
	public SysOrg getOrgByOrgName(String orgName) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgName", orgName);
		SysOrg sysOrg = (SysOrg) getUnique("getOrgByOrgName", params);
		return sysOrg;
	}
	/**
	 * 级联获取该用户相关的组织含所有子组织列表
	 * @param userId
	 * @param path
	 * @return
	 */
	public List<SysOrg> getOrgByUserIdPath(Long userId, String path) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("path", path);
		List<SysOrg> list = getBySqlKey("getOrgByUserIdPath", params);
		return list;
	}
	/**
	 * 获得组织类型为orgType的组织列表
	 * @param orgType
	 * @return
	 */
	public List<SysOrg> getByOrgType(Long orgType) {
		return getBySqlKey("getByOrgType", orgType);
	}
	/**
	 * 存在公司的组织列表
	 * @return
	 */
	public List<Map<String, Object>> getCompany() {
		return getBySqlKeyGenericity("getCompany", null);
	}
	/**
	 * 判断id的组织是否包含code代码
	 * @param code
	 * @param id
	 * @return
	 */
	public Integer getCountByCode(String code, Long id) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", code);
		params.put("id", id);

		Integer rtn = (Integer) getOne("getCountByCode", params);

		return rtn;
	}
	/**
	 * 根据path查出组织
	 * @param path
	 * @return
	 */
	public List<SysOrg> getOrgByPath(String path) {
		return getBySqlKey("getOrgByPath", path);
	}
	/**
	 * 根据code获取
	 * @param code
	 * @return
	 */
	public List<SysOrg> getByCode(String code) {
		return getBySqlKey("getByCode", code);
	}
	/**
	 * 根据userId获取对应组织
	 * @param userId
	 * @return
	 */
	public List<SysOrg> getByUserId(Long userId) {
		return getBySqlKey("getByUserId", userId);
	}
	
    /**
     * 获取用户负责的组织
     * @param userId
     * @return
     */
    public List getChargeOrg(Long userId)
    {
        return this.getListBySqlKey("getChargeOrg", userId);

    }
	/**
	 * 是否存在该组织
	 * @param id
	 * @return
	 */
	public boolean isOrgExist(String orgId) {
		Integer rtn = (Integer) getOne("isOrgExist", orgId);
		return rtn.intValue() > 0;
	}
	
	
	/**
	 * 是否存在该组织
	 * @param id
	 * @return
	 */
	public boolean isOrgExistByCode(String code) {
		Integer rtn = (Integer) getOne("isOrgExistByCode", code);
		return rtn.intValue() > 0;
	}
    /**
     * 查询用户作为分管领导的组织
     * @param userId
     * @return
     */
    public List<Long> getByAllLeader(Long userId)
    {
        return this.getListBySqlKey("getByAllLeader", userId);
    }
    
    
    /**
     * 查询用户作为分管主领导的组织
     * @param userId
     * @return
     */
    public List<Long> getByLeader(Long userId)
    {
        return this.getListBySqlKey("getByLeader", userId);
    }
    
    /**
     * 查询用户作为分管副领导的组织
     * @param userId
     * @return
     */
    public List<Long> getByViceLeader(Long userId)
    {
        return this.getListBySqlKey("getByViceLeader", userId);
    }
    
    public List<SysOrg> getByParentIdAndType(Long orgId, String orgType)
    {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orgId", orgId);
        params.put("orgType", orgType);
        return this.getBySqlKey("getByParentIdAndType", params);
    }
}
