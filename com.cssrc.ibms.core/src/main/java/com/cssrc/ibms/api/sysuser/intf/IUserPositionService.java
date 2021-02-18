package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IUserPositionService{

	/**
	 * 担任posid岗位的用户id集合
	 * @param posId
	 * @return
	 */
	public abstract List<Long> getUserIdsByPosId(Long posId);

	/**
	 * 获得该用户组织岗位信息
	 * @param userId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getByUserId(Long userId);

	/**
	 * 获取该用户组织岗位分组列表（orgId,userId）
	 * @param userId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getOrgListByUserId(Long userId);

	/**
	 * 获取该岗位的组织岗位列表
	 * @param posId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getByPosId(Long posId);

	/**
	 * 删除某用户的岗位关系
	 * @param userId
	 */
	public abstract void delByUserId(Long userId);

	/**
	 * 删除posid关联信息
	 * @param posId
	 */
	public abstract void delByPosId(Long posId);

	/**
	 * 获取用户的主组织岗位
	 * @param userId
	 * @return
	 */
	public abstract IUserPosition getPrimaryUserPositionByUserId(Long userId);

	/**
	 * 组织获取用户信息
	 * @param orgIds
	 * @return
	 */
	public abstract List<?extends IUserPosition> getUserByOrgIds(String orgIds);

	/**
	 * 获取用户负责的组织岗位
	 * @param userId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getChargeOrgByUserId(Long userId);

	/**
	 * 保存用户组织岗位结构
	 * @param userId
	 * @param posIds
	 * @param posIdPrimary
	 * @param posIdCharge
	 * @throws Exception
	 */
	public abstract void saveUserPos(Long userId, Long[] posIds,
			Long posIdPrimary, Long[] posIdCharge, Long[] aryOrgId)
			throws Exception;

	/**
	 *获取用户所属的组织名集合
	 * @param userId
	 * @return
	 */
	public abstract String getOrgnamesByUserId(Long userId);

	/**
	 * 给岗位批量添加用户
	 * @param userIds
	 * @param posId
	 * @throws Exception
	 */
	public abstract void addPosUser(Long[] userIds, Long posId)
			throws Exception;

	/**
	 * 没有岗位直接添加用户和组织关系
	 * @param userIds
	 * @param orgId
	 * @throws Exception
	 */
	public abstract void addOrgUser(Long[] userIds, Long orgId)
			throws Exception;

	/**
	 * 批量删除用户与岗位关联关系记录
	 * */
	public abstract void delUserPositionByPosId(String posId, Long userId);

	/**
	 * 删除指定用户指定岗位的信息
	 * @param userId
	 * @param posId
	 */
	public abstract void delByUserIdAndPosId(Long userId, Long posId);

	/**
	 * 批量删除职务id相关记录（例如将“组长”相关所有用户的岗位停用）
	 * @param jobId
	 */
	public abstract void delByJobId(Long jobId);

	/**
	 * 设置组织岗位主管负责人
	 * @param userPosId
	 */
	public abstract void setIsCharge(Long userPosId);




	/**
	 * 根据组织id获取组织负责人
	 * @param orgId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getChargeByOrgId(Long orgId);

	/**
	 * 删除该组织下的所有子组织与岗位的关系
	 * @author Yangbo 2016-8-5
	 * @param path
	 */
	public abstract void delByOrgPath(String path);

	/**
	 * 删除某组织关联的关系
	 * @param orgId
	 */
	public abstract void delByOrgId(Long orgId);

	/**
	 * 获取用户主组织岗位信息
	 * @param userId
	 * @return
	 */
	public abstract IUserPosition getPrimaryByUserId(Long userId);

	/**
	 * 获取指定组织用户岗位列表
	 * @author Yangbo 2016-8-5
	 * @param orgId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getByOrgId(Long orgId);

	/**
	 * 获取用户组织信息
	 * @param userId
	 * @return
	 */
	public abstract List<?extends IUserPosition> getOrgByUserId(Long userId);

	/**
	 * 通过userPosIdS设置该岗位为主岗位或去除主岗位，对应的用户其他主岗位消除
	 * @param userPosId
	 */
	public abstract void setIsPrimary(Long userPosId);
	/**
	 * 根据组织id获取组织负责人
	 * @param orgId
	 * @param upslope
	 * @return
	 */
	public abstract List<?extends ISysUser> getLeaderUserByOrgId(Long orgId, boolean b);

	public abstract IUserPosition getById(Long valueOf);

	public abstract List<?extends IUserPosition> getAll(QueryFilter filter);
	
	public abstract List<?extends IUserPosition> getByOrgIds(String orgIds,Long isCharge);
    /**
     * 获取用户组织ID，可指定职务
     * @param userId
     * @param jobId
     * @return
     */
    public abstract List<Long> getOrgIdByUserAndJob(Long userId, String jobId);
}