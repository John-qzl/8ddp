package com.cssrc.ibms.system.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysBackUpRestore;
/**
 * 参数dao层
 * @see
 */
@Repository
public class SysBackUpRestoreDao extends BaseDao<SysBackUpRestore>{

	public Class getEntityClass() {
		return SysBackUpRestore.class;
	}
}

