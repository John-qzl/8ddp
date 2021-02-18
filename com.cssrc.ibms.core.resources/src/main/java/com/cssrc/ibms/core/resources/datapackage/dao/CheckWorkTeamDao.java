package com.cssrc.ibms.core.resources.datapackage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.resources.datapackage.model.CheckDataPackageInfo;
import com.cssrc.ibms.core.resources.datapackage.model.CheckPackage;
import com.cssrc.ibms.core.resources.datapackage.model.CheckWorkTeam;
import com.cssrc.ibms.dp.form.model.CheckForm;

@Repository
public class CheckWorkTeamDao  extends BaseDao<CheckWorkTeam>{
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return CheckWorkTeam.class;
	}
	
	/**
	 * @param Id
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CheckWorkTeam> getByModelId(String Id){
		Map map = new HashMap();
		map.put("Id", Id);
		return (List<CheckWorkTeam>)this.getBySqlKey("getBymodelid", map);
	}
	
}
