package com.cssrc.ibms.core.resources.ioOld2New.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.model.CheckCondition;
import com.cssrc.ibms.dp.sync.bean.ConditionBean;
import com.cssrc.ibms.dp.sync.bean.Project;
import com.cssrc.ibms.dp.sync.bean.SignBean;
import com.cssrc.ibms.dp.sync.bean.TaskList;
import com.cssrc.ibms.dp.sync.bean.TasksBean;
import com.cssrc.ibms.dp.sync.dao.DataSyncDao;
import com.cssrc.ibms.dp.sync.model.SyncUser;
import com.cssrc.ibms.dp.sync.model.SyncUserXML;
import com.cssrc.ibms.dp.sync.util.SyncBaseDao;
import com.cssrc.ibms.dp.sync.util.SyncBaseFilter;
import com.cssrc.ibms.statistics.model.Address;

@Repository
public class ImportFromOldSystemDao extends DataSyncDao {
	@Resource
	private JdbcDao jdbcDao;
}
