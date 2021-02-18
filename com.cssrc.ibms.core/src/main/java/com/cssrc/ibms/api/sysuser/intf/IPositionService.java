package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.api.sysuser.model.IPosition;

public interface IPositionService{

	/**
	 * 通过岗位名称查找岗位信息
	 * */
	public abstract List<?extends IPosition> getByPosName(String posName);

	/**
	 * 删除岗位信息
	 * */
	public abstract void deleteByUpdateFlag(Long posId,Long currentUserId);

	public abstract List<?extends IPosition> getByUserId(Long userId);

	public abstract List<Long> getPositonIdsByUserId(Long userId);

	public abstract List<?extends IPosition> getOrgPosListByOrgIds(String orgIds);

	public abstract List<?extends IPosition> getOrgListByOrgIds(String orgIds);

	/**
	 * 获取用户默认的组织岗位关系  
	 * 有主组织获取主组织，没有主组织获取查询后的第一个组织。
	 * @param userId
	 * @return
	 */
	public abstract IPosition getDefaultPosByUserId(Long userId);

	public abstract IPosition getByPosCode(String posCode);

	public abstract List<?extends IPosition> getBySupOrgId(QueryFilter filter);

	public abstract void deleByJobId(Long jobId,Long currentUserId);

	public abstract boolean isJobUsedByPos(Long jobId);

	public abstract String getPosCode(Long posId);

	public abstract boolean isPoscodeUsed(String posCode);

	public abstract IPosition getPosByUserId(Long userId);

	public abstract IPosition getByOrgJobId(Long orgId, Long jobId);

	public abstract IPosition getById(Long id);

	/**
	 * 将所有岗位放到redis中
	 */
	public void setAllPositionToRedis();
	
	public List<?extends IPosition> getPositionByOrgId(Long orgId);
}