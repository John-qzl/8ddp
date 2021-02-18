package com.cssrc.ibms.core.user.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.UserPosition;
/**
 * 
 * <p>Title:UserPositionDao</p>
 * @author Yangbo 
 * @date 2016-8-2下午03:47:27
 */
@Repository
public class UserPositionDao extends BaseDao<UserPosition> {
	public Class<?> getEntityClass() {
		return UserPosition.class;
	}
	/**
	 * 获得岗位为posid的对应用户id
	 * @param posId
	 * @return
	 */
	public List<Long> getUserIdsByPosId(Long posId) {
		List list = getBySqlKey("getUserIdsByPosId", posId);
		return list;
	}
	/**
	 * 删除某用户的岗位关系 isdelete=1
	 * @param userId
	 */
	public void delByUserId(Long userId) {
		delBySqlKey("delByUserId", userId);
	}
	/**
	 * 根据用户id获取组织和岗位信息
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getByUserId(Long userId) {
		return getBySqlKey("getByUserId", userId);
	}
	/**
	 * 通过posId获得相关组织岗位信息
	 * @param posId
	 * @return
	 */
	public List<UserPosition> getByPosId(Long posId) {
		return getBySqlKey("getByPosId", posId);
	}
	/**
	 * 根据用户id获取组织用户岗位分组列表
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getOrgListByUserId(Long userId) {
		return getBySqlKey("getOrgListByUserId", userId);
	}
	/**
	 * 设置该用户的组织岗位为非主要的(用于添加某个组织岗位为主时，先清除先前的主岗位)
	 * 一个人只有一个主组织或主岗位
	 * @param userId
	 */
	public void updNotPrimaryByUser(Long userId) {
		update("updNotPrimaryByUser", userId);
	}
	/**
	 * 获取该用户的主组织岗位
	 * @param userId
	 * @return
	 */
	public UserPosition getPrimaryUserPositionByUserId(Long userId) {
		return (UserPosition) getUnique("getPrimaryUserPositionByUserId",
				userId);
	}
	/**
	 * 获取组织集合的关联岗位的信息
	 * @param orgIds
	 * @return
	 */
	public List<UserPosition> getUserByOrgIds(String orgIds) {
		Map param = new HashMap();
		param.put("orgIds", orgIds);
		return getBySqlKey("getUserByOrgIds", param);
	}
	//根据组织id获取组织负责人
	public List<UserPosition> getChargeByOrgId(Long orgId) {
		return getBySqlKey("getChargeByOrgId", orgId);
	}
	/**
	 * 获得用户负责的组织岗位
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getChargeOrgByUserId(Long userId) {
		return getBySqlKey("getChargeOrgByUserId", userId);
	}
	/**
	 * 删除组织的岗位关系
	 * @param orgId
	 */
	public void delByOrgId(Long orgId) {
		update("delByOrgId", orgId);
	}
	/**
	 * 级联物理删除该组织下所有子组织
	 * @param path
	 */
	public void delByOrgPath(String path) {
		delBySqlKey("delByOrgPath", path);
	}
	/**
	 * 级联逻辑删除该组织下所有子组织
	 * @param path
	 */
	public void delLogicByOrgPath(String path) {
		update("delLogicByOrgPath", path);
	}
	/**
	 * 还原被逻辑删除的组织架构以及该组织下的所有子组织与岗位的关系
	 * @param path
	 */
	public void restoreLogicByOrgPath(String path) {
		update("restoreLogicByOrgPath", path);
	}
	/**
	 * 删除岗位id为posId的关联信息（Isdelet=1）
	 * @param posId
	 */
	public void delByPosId(Long posId) {
		update("delByPosId", posId);
	}
	/**
	 * 获取对应组织下的用户岗位关系信息
	 * */
	public List<UserPosition> getByOrgId(Long orgId) {
		return getBySqlKey("getByOrgId", orgId);
	}
	/**
	 * 删除orgId的负责人(isdelete=1)，不允许该组织被负责
	 * @param orgId
	 */
	public void delChargeByOrgId(Long orgId) {
		update("delChargeByOrgId", orgId);
	}
	/**
	 * 获取指定userid和posid的关联信息
	 * @param userId
	 * @param posId
	 * @return
	 */
	public UserPosition getUserPosModel(Long userId, Long posId) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("posId", posId);
		UserPosition userPosition = (UserPosition) getUnique("getUserPosModel",
				param);
		return userPosition;
	}
	/**
	 * 在设置用户主岗位时，先将其他所有岗位设置成非主岗位， 一个人只有一个主岗位
	 * @param userId
	 */
	public void updNotPrimaryByUserId(Long userId) {
		update("updNotPrimaryByUserId", userId);
	}
	/**
	 * 获取用户id为userId的岗位组织信息
	 * @param userId
	 * @return
	 */
	public List<UserPosition> getPosIdByUserId(Long userId) {
		List userPosition = getBySqlKey("getPosByUserId", userId);
		return userPosition;
	}
	/**
	 * 该用户岗位是posId的都删除(isdelete=1)
	 * @param userId
	 * @param posId
	 */
	public void delByUserIdAndPosId(Long userId, Long posId) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("posId", posId);
		update("delByUserIdAndPosId", param);
	}
	/**
	 * 批量删除该用户下posId集合内岗位
	 * @param posId
	 * @param userId
	 */
	public void delUserPositionByPosId(String posId, Long userId) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("posId", posId);
		update("delByUserIdAndPosId", param);
	}
	/**
	 * 删除该职务关系(isdelete=1)
	 * @param jobId
	 */
	public void delByJobId(Long jobId) {
		update("delByJobId", jobId);
	}
	/**
	 * 该组织是否被分配
	 * @param orgId
	 * @return
	 */
	public boolean isUserExistFromOrg(Long orgId) {
		int count = ((Integer) getOne("isUserExistFromOrg", orgId)).intValue();
		return count > 0;
	}
	
	/**
	 * 获取用户组织直接的关系数据
	 *@author YangBo @date 2016年11月10日下午4:52:20
	 *@param userId
	 *@param orgId
	 *@return
	 */
	public UserPosition getUserOrgModel(Long userId, Long orgId, Long posId) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("orgId", orgId);
		if(posId==null){
			param.put("posId", "");
		}
		UserPosition userPosition = (UserPosition) getUnique("getUserOrgModel",
				param);
		return userPosition;
	}


}
