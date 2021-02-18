package com.cssrc.ibms.api.sysuser.intf;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

public interface ISysOrgService{

	/**
	 * 获取组织信息
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgByOrgId(QueryFilter queryFilter);

	/**
	 * 获得维度为demid的组织架构
	 * @param demId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgsByDemIdOrAll(Long demId);

	/**
	 * 根据组织名获得组织信息
	 * @param orgName
	 * @return
	 */
	public abstract List<?extends ISysOrg> getByOrgName(String orgName);

	/**
	 * 根据path查出组织
	 * @param path
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgByPath(String path);

	/**
	 * 
	 * @param demId
	 * @return
	 */
	public abstract Map getOrgMapByDemId(Long demId);

	/**
	 * code查询组织
	 * @param code
	 * @return
	 */
	public abstract ISysOrg getByCode(String code);

	/**
	 * 判断该组织是否分配
	 * @param id
	 * @return
	 */
	public abstract boolean isUserExistFromOrg(Long id);

	/**
	 * 删除该组织所有相关联的数据
	 */
	public abstract void delById(Long id);

	/**
	 * 还原被逻辑删除的组织架构以及所有相关联的数据
	 */
	public abstract void restoreLogicByPath(Long id);
	
	/**
	 * 获得用户分配的组织
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgsByUserId(Long userId);

	/**
	 * 用户所在组织id集合
	 * @param userId
	 * @return
	 */
	public abstract String getOrgIdsByUserId(Long userId);

	/**
	 * 获取用户默认所属组织
	 * @param userId
	 * @return
	 */
	public abstract Long getOrgIdByUserId(Long userId);


	/**
	 * 获取用户指定维度的组织架构
	 * @param userId
	 * @param demId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getByUserIdAndDemId(Long userId, Long demId);

	public abstract void move(Long targetId, Long dragId, String moveType);
	/**
	 *用户主组织(只有一个) 
	 * @param userId
	 * @return
	 */
	public abstract ISysOrg getPrimaryOrgByUserId(Long userId);

	/**
	 * 获取用户负责的组织列表
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getChargeOrgByUserId(Long userId);

	/**
	 * 获取path下的所有父子组织树列表
	 * @param path
	 * @return
	 */
	public abstract List<?extends ISysOrg> getByOrgPath(String path);


	public abstract ISysOrg getDefaultOrgByUserId(Long userId);

	public abstract void updSn(Long orgId, long sn);

	/**
	 * 获得下级组织
	 * @param orgSupId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgByOrgSupId(Long orgSupId);

	public abstract List<?extends ISysOrg> getOrgByOrgSupIdAndLevel(Long orgSupId);

	public abstract List<?extends ISysOrg> getOrgByOrgSupIdAndLevel(Long orgSupId,
			int level);

	/**
	 * 分组组织信息
	 * @param groupId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getByOrgMonGroup(Long groupId);

	/**
	 * organem非空下可获得对应组织(父子)树
	 * @param filter
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgForMobile(QueryFilter filter);

	/**
	 * 获得组织类型orgType的组织
	 * @param orgType
	 * @return
	 */
	public abstract List<?extends ISysOrg> getByOrgType(Long orgType);

	/**
	 * 更加和添加组织分公司
	 */
	public abstract void updCompany();

	/**
	 * 获取有公司的组织
	 * @return
	 */
	public abstract List<Map<String, Object>> getCompany();

	/**
	 * 该组织是否存在code
	 * @param code
	 * @param id
	 * @return
	 */
	public abstract Integer getCountByCode(String code, Long id);

	/**
	 * 获得下级组织
	 * @param parentId
	 * @param params
	 * @return
	 */
	public abstract List getByParentId(Long parentId, Map<String, Object> params);

	/**
	 *重置所有组织code
	 */
	public abstract void syncAllOrg();

	/**
	 * 根据父组织id迭代更新子组织的父code
	 * @param parentId
	 * @param parentCode
	 */
	public abstract void syncOrgsByParentId(Long parentId, String parentCode);

	/**
	 * 分级组织节点数Json对象
	 * @param orgId
	 * @return
	 */
	public abstract String getOrgJsonByAuthOrgId(Long orgId);
	/**
	 * 级联获取该用户相关的组织含所有子组织列表
	 * @param userId
	 * @param path
	 * @return
	 */
	public abstract List<?extends ISysOrg> getOrgByUserIdPath(Long userId, String path);
	/**
	 * 用户所属的组织信息
	 * @param username
	 * @return
	 */
	public abstract ISysOrg getOrgByUsername(String account);
	/**
	 * 根据userid获取对应组织
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysOrg> getByUserId(Long userId);

	/**
	 * 根据ID查找 组织ID
	 * @param id
	 * @return
	 */
	public abstract ISysOrg getById(Long id);
	
    /**
     * 根据组织id获取组织分管领导
     * 
     * @param orgId 组织Id
     * @param type 领导类型，all、leader、viceLeader(所有领导、主领导、副领导)
     * @param upslope 如果当前组织分管领导为空，是否递归往上查找
     * @return
     */
    public abstract List<?extends ISysUser> getLeaderUserByOrgId(Long orgId, String type, boolean equals);
    /**
	 *获取父组织及有关子节点
	 * @param orgId
	 * @return
	 */
	public String getIdsBySupId(Long orgId);
    /**
     * 获取用户负责的组织
     * @param userId
     * @return
     */
    public abstract List<Long> getChargeOrg(Long userId);

    public abstract List<Long> getByAllLeader(Long userId);

    public abstract List<Long> getByLeader(Long userId);

    public abstract List<Long> getByViceLeader(Long userId);

    /**
     * 获取子组织
     * @param orgId
     * @param orgType
     * @return
     */
    public abstract List getByParentIdAndType(Long orgId, String orgType);
    
	/**
	 * 将所有组织放到redis中
	 */
	public void setAllSysOrgToRedis();

	public abstract List<?extends ISysOrg> getAll();

}