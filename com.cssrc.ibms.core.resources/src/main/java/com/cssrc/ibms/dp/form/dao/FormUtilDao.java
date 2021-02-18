package com.cssrc.ibms.dp.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.sync.dao.DataSyncDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FormUtilDao extends DataSyncDao {
	@Resource
	private JdbcDao jdbcDao;
}
