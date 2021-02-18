package com.cssrc.ibms.core.resources.datapackage.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;

@Repository
public class DpComAnalysisDao {
	@Resource
	private JdbcDao jdbcDao;
	
	/**
	 * 根据发次Id,父节点（普通节点）统计
	 * @param projectId
	 * @param parentName
	 * @return
	 */
	public int getCount(String projectId, String parentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) FROM W_DATAPACKAGEINFO WHERE F_SSSJB IN ")
			.append("(select ID from W_PACKAGE where F_SSFC = '").append(projectId)
			.append("' AND F_JDLX != '").append(IOConstans.SIMPLE_PACKAGE_NODE).append("' ")
			.append("start with ID IN (").append(parentId).append(") connect by prior ID = F_PARENTID)");
		return jdbcDao.queryForInt(sql.toString(), null); 
	}
	
	/**
	 * 根据发次Id,执行状态统计
	 * @param projectId
	 * @param status
	 * @return
	 */
	public int getCountBystatus(String projectId, String status) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(*) FROM W_DATAPACKAGEINFO WHERE F_SSSJB IN ( ")
			.append(" SELECT ID FROM W_PACKAGE where F_SSFC = '").append(projectId)
			.append("' AND F_JDLX != '").append(IOConstans.SIMPLE_PACKAGE_NODE).append("') ")
			.append(" and F_ZXZT = '").append(status).append("'");
		return jdbcDao.queryForInt(sql.toString(), null); 
	}
	
	/** 查询数据包结构树非普通分类节点 (PARENTID=0) */
	public int getSpecNodeCount(String projectId){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(*) FROM W_DATAPACKAGEINFO WHERE F_SSSJB IN ( ");
		sql.append(" SELECT ID FROM W_PACKAGE START WITH ID IN ( ");
		sql.append(" SELECT ID FROM W_PACKAGE WHERE F_JDLX != '")
			.append(IOConstans.SIMPLE_PACKAGE_NODE).append("' AND F_PARENTID='0' AND F_SSFC = '")
			.append(projectId).append("') ")
			.append(" connect by prior ID = F_PARENTID)");
		return jdbcDao.queryForInt(sql.toString(),null);
	}
}
