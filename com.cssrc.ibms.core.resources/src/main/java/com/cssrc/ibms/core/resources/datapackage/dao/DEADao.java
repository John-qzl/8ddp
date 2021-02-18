package com.cssrc.ibms.core.resources.datapackage.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.util.IOConstans;

/**
 * @author user
 *
 */
@Repository
public class DEADao {
	@Resource
	private JdbcDao jdbcDao;
	
	public List<Map<String, Object>> query(String productId, String projectId, String ckName
			, String ckShortName, String ckDescription,String resultTableName) {
		StringBuilder sql = new StringBuilder();
		
		String ckNameSql = "";
		if (!ckName.equals("")) {
			ckNameSql = " AND B.F_NAME LIKE '%" +  ckName + "%' ";
		}
		
		String ckShortNameSql = "";
		if (!ckShortName.equals("")) {
			ckShortNameSql = " AND B.F_SHORTNAME LIKE '%" +  ckShortName + "%' ";
		}
		
		String ckDescriptionSql = "";
		if (!ckDescription.equals("")) {
			ckDescriptionSql = " AND B.F_DESCRIPTION LIKE '%" + ckDescription + "%' ESCAPE '\\' ";
		}
		
		/*String itemDefIdSql = "";
		if (itemDefId != "") {
			itemDefIdSql = " AND B.ID ='" + itemDefId + "'";
		}*/
		//根据projuctId 获取对应的型号类型


		sql.append(" SELECT AA.F_XHMC,AA.F_FCMC,AA.F_FCZT,AA.F_NAME,AA.F_SHORTNAME,AA.F_DESCRIPTION,BB.F_VALUE FROM ");
		sql.append(" (SELECT DISTINCT E.F_XHMC, F.F_FCMC, F.F_FCZT, B.F_NAME, B.F_SHORTNAME, B.F_DESCRIPTION,B.ID FROM ")
			.append(" W_ITEMDEF B, W_DATAPACKAGEINFO C, W_PACKAGE D, W_PRODUCT E, W_PROJECT F, W_TABLE_TEMP G ")
			.append(" WHERE E.ID IN (").append(productId).append(") ")
			.append(" AND F.ID IN (").append(projectId).append(") ")
			.append(" AND D.F_SSXH = E.ID AND D.F_SSFC = F.ID AND D.F_JDLX != '").append(IOConstans.SIMPLE_PACKAGE_NODE).append("' ")
			.append(" AND C.F_SSSJB = D.ID ")
			.append(" AND G.F_NAME = C.F_SSMBMC AND G.F_PROJECT_ID = F.ID ")
			.append(" AND B.F_TABLE_TEMP_ID = G.ID  AND B.F_TYPE = '1'").append(ckNameSql).append(ckShortNameSql)
			//.append(" AND B.F_DESCRIPTION LIKE '%").append(ckDescription).append("%' ESCAPE '\\' ")
			.append(ckDescriptionSql)/*.append(itemDefIdSql)*/
			.append(" ) AA , " + resultTableName+" BB ")
				.append(" WHERE BB.F_ITEMDEF_ID= AA.ID AND BB.F_VALUE is not null ")
				.append(" ORDER BY AA.F_FCMC,AA.ID ");
		
		return jdbcDao.queryForList(sql.toString(), null);
	}
	
	public List<Map<String, Object>> queryProjectNodeById(String id) {
		String sql = "SELECT A.*, B.F_XHMC FROM W_PROJECT A, W_PRODUCT B where A.F_SSXH in (" + id + ") AND B.ID = A.F_SSXH ORDER BY B.ID,A.ID";
		return jdbcDao.queryForList(sql, null);
	}
	
	/**
	 * 根据检查结果id获取检查项id
	 */
	public Map<String, Object> queryItemDefById(String id) {
		String sql = "SELECT F_ITEMDEF_ID FROM W_CK_RESULT where ID='" + id + "'";
		return jdbcDao.queryForMap(sql, null);
	}
	/**
	 * @Author  shenguoliang
	 * @Description: 根据检查结果id以及所在的检查结果表表名 来获取检查项ID
	 * @Params [id, resultTableName]
	 * @Date 2018/5/19 16:55
	 * @Return java.util.Map<java.lang.String,java.lang.Object>
	 */
	public Map<String, Object> queryItemDefByIdAndResultTableName(String id,String resultTableName) {

		String sql = "SELECT F_ITEMDEF_ID FROM "+resultTableName+" where ID='" + id + "'";
		return jdbcDao.queryForMap(sql, null);
	}
	/**
	 * 根据检查项id获取检查项信息
	 */
	public Map<String, Object> queryItemInfoById(String id) {
		String sql = "SELECT * FROM W_ITEMDEF where ID='" + id + "'";
		return jdbcDao.queryForMap(sql, null);
	}
	
	/**
	 * 根据模板实例获取所属数据包
	 */
	public Map<String, Object> queryDataPackageInfo(String id) {
		String sql = "SELECT F_SSXH, F_SSFC from W_PACKAGE where id = (select F_SSSJB from W_DATAPACKAGEINFO where F_SSMB = '" + id + "' )";
		return jdbcDao.queryForMap(sql, null);
	}
}
