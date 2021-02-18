package com.cssrc.ibms.core.user.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 
 * <p>
 * Title:PositionDao
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-2上午10:17:36
 */
@Repository
public class PositionDao extends BaseDao<Position> {
	public Class<?> getEntityClass() {
		return Position.class;
	}

	public Position getPrimaryPositionByUserId(Long userId) {
		return (Position) getUnique("getPrimaryPositionByUserId", userId);
	}

	public Position getPosByUserId(Long userId) {
		return getPrimaryPositionByUserId(userId);
	}

	public List<Position> getByUserId(Long userId) {
		return getBySqlKey("getByUserId", userId);
	}

	/**
	 * 通过岗位名称查找岗位信息
	 * */
	public List<Position> getByPosName(String posName) {
		return getBySqlKey("getByPosName", posName);
	}

	/**
	 * 通过部门查找其下的岗位
	 * */
	public List<Position> getPositionByOrgId(Long orgId) {
		return getBySqlKey("getPositionByOrgId", orgId);
	}

	public List<Position> getOrgPosListByOrgIds(String orgIds) {
		if (StringUtil.isEmpty(orgIds))
			return null;
		Map map = new HashMap();
		map.put("orgIds", orgIds);
		return getBySqlKey("getOrgPosListByOrgIds", map);
	}

	public List<Position> getOrgListByOrgIds(String orgIds) {
		if (StringUtil.isEmpty(orgIds))
			return null;
		Map map = new HashMap();
		map.put("orgIds", orgIds);
		return getBySqlKey("getOrgListByOrgIds", map);
	}

	public Position getByPosCode(String posCode) {
		return (Position) getUnique("getByPosCode", posCode);
	}

	public List<Position> getBySupOrgId(QueryFilter queryFilter) {
		return getBySqlKey("getBySupOrgId", queryFilter);
	}

	public Position getByOrgJobId(Long orgId, Long jobId) {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("jobId", jobId);
		return (Position) getUnique("getByOrgJobId", params);
	}
	
	public Position getByOrgId(Long orgId,String posName) {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("posName", posName);
		return (Position) getUnique("getByOrgId", params);
	}

	public void deleteByUpdateFlag(Long posId,Long currentUserId) {
		Map params = new HashMap();
		params.put("posId", posId);
		params.put("currentUserId", currentUserId);
		params.put("updateTime", new Date());
		update("deleteByUpdateFlag", params);
	}
	
	public void deleByJobId(Long jobId,Long currentUserId) {
		Map params = new HashMap();
		params.put("jobId", jobId);
		params.put("currentUserId", currentUserId);
		params.put("updateTime", new Date());
		update("delByJobId", params);
	}

	//逻辑删除组织
	public void delLogicByOrgId(Long orgId,Long currentUserId) {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("currentUserId", currentUserId);
		params.put("updateTime", new Date());
		update("delLogicByOrgId", params);
	}
	
	//还原逻辑的删除组织
	public void restoreLogicByOrgId(Long orgId,Long currentUserId) {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("currentUserId", currentUserId);
		params.put("updateTime", new Date());
		update("restoreLogicByOrgId", params);
	}
	
	//物理删除组织
	public void delByOrgId(Long orgId) {
		delBySqlKey("delByOrgId", orgId);
	}

	public boolean isJobUsedByPos(Long jobId) {
		Integer count = (Integer) getOne("isJobUsedByPos", jobId);
		return count.intValue() > 0;
	}

	public String getPosCode(Long posId) {
		return ((Position) getBySqlKey("getPosCode", posId).get(0))
				.getPosCode();
	}

	public boolean isPoscodeUsed(String posCode) {
		Integer count = (Integer) getOne("isPoscodeUsed", posCode);
		return count.intValue() <= 0;
	}
}
