package com.cssrc.ibms.core.user.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysOrgTactic;
/**
 * 组织策略Dao
 * <p>Title:SysOrgTacticDao</p>
 * @author Yangbo 
 * @date 2016-8-4下午08:46:01
 */
@Repository
public class SysOrgTacticDao extends BaseDao<SysOrgTactic> {
	public Class<?> getEntityClass() {
		return SysOrgTactic.class;
	}
}
