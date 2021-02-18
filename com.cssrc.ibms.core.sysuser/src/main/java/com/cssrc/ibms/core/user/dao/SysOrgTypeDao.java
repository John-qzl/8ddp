package com.cssrc.ibms.core.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysOrgType;
/**
 * 组织类型DAO
 * <p>Title:SysOrgTypeDao</p>
 * @author Yangbo 
 * @date 2016-8-2上午08:57:16
 */
@Repository
public class SysOrgTypeDao extends BaseDao<SysOrgType> {
	public Class getEntityClass() {
		return SysOrgType.class;
	}

	public Integer getMaxLevel(long demId) {
		return (Integer) getOne("getMaxLevel", Long.valueOf(demId));
	}

	public List<SysOrgType> getByDemId(long demId) {
		return getBySqlKey("getByDemId", Long.valueOf(demId));
	}
}
