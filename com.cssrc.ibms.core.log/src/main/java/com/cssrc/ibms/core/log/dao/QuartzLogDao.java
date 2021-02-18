package com.cssrc.ibms.core.log.dao;

import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.log.model.QuartzLog;

@Repository
public class QuartzLogDao extends BaseDao<QuartzLog>
{


	@Override
	public Class getEntityClass() {
		return QuartzLog.class;
	}

}