package com.cssrc.ibms.core.resources.io.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.io.bean.FileData;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;


@Repository
public class FileDataDao {
	@Resource
	private JdbcDao jdbcDao;
	
	public FileData getById(String id) {
		String sql = " select * from W_FILE_DATA where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new FileData(map);
		}
	}
	
	public List<FileData> getByPlanId(String planId) {
		String sql = " select * from W_FILE_DATA where F_PLANID='"+planId+"'";	
		List<Map<String, Object>>  list= jdbcDao.queryForList(sql, null);
		if(list==null) {
			return null;
		}else {
			List<FileData> fileDataList=new ArrayList<>();
			for (Map<String, Object> map : list) {
				fileDataList.add(new FileData(map));
			}
			return fileDataList;
		}
	}
	
	public void insert(FileData fileData) {
		String  sql = SqlHelp.getInsertSql(FileData.class, "W_FILE_DATA");
		Map<String, Object> map = MapUtil.transBean2Map(fileData);
		jdbcDao.exesql(sql, map);
	}
	
	

	public void update(FileData fileData) {
		String  sql = SqlHelp.getUpdateSql(FileData.class, "W_FILE_DATA");
		Map<String, Object> map = MapUtil.transBean2Map(fileData);
		jdbcDao.exesql(sql, map);
	}

	public List<FileData> getByDataId(String dataId) {
		String sql = " select * from W_FILE_DATA where F_SJID='"+dataId+"'";	
		List<Map<String, Object>>  list= jdbcDao.queryForList(sql, null);
		if(list==null) {
			return null;
		}else {
			List<FileData> fileDataList=new ArrayList<>();
			for (Map<String, Object> map : list) {
				fileDataList.add(new FileData(map));
			}
			return fileDataList;
		}
	}
}
