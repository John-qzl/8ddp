package com.cssrc.ibms.core.form.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.common.CommonTools;

@Repository
public class RelTableDao extends BaseDao<JdbcTemplate> {
	@Override
	public Class<JdbcTemplate> getEntityClass() {
		// TODO Auto-generated method stub
		// return null;
		return JdbcTemplate.class;
	}

	/**
	 * 从系统IBMS_FORM_FIELD中查出 关联表中存放主表Id的字段
	 * @param mainTableId
	 * @param relTableId
	 * @return
	 */
	public String getRelSaveColumn(Long mainTableId, Long relTableId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from IBMS_FORM_FIELD");
		sql.append(" where");
		sql.append(" TABLEID='" + relTableId + "' ");
		sql.append("and RELTABLEID='" + mainTableId + "'");
		Map<String, Object> readMap = jdbcTemplate.queryForMap(sql.toString());
		String relSaveColumn = CommonTools.Obj2String(readMap.get("FIELDNAME"));
		return relSaveColumn;
	}

	/**
	 * 获取主表关联表记录
	 * @param mainTableName
	 * @param mainTableFieldsName
	 * @param relTableName
	 * @param relTableFieldsName
	 * @param relSaveColumn
	 * @return
	 */
	public List<Map<String, Object>> getAllRecords(String mainTableName,
			ArrayList<String> mainTableFieldsName, String relTableName,
			ArrayList<String> relTableFieldsName, String relSaveColumn) {
		// 主表
		StringBuffer mainSon = new StringBuffer();
		mainSon.append("w_" + mainTableName + ".id as m_id,");
		for (int i = 1; i < mainTableFieldsName.size(); i++) {
			String mainFieldName = mainTableFieldsName.get(i);
			mainSon.append("w_" + mainTableName + ".f_" + mainFieldName
					+ " as m_" + mainFieldName + ",");
		}
		// 关联表
		StringBuffer relSon = new StringBuffer();
		relSon.append("w_" + relTableName + ".id as r_id,");
		for (int i = 1; i < relTableFieldsName.size(); i++) {
			String relFieldName = relTableFieldsName.get(i);
			relSon.append("w_" + relTableName + ".f_" + relFieldName + " as r_"
					+ relFieldName + ",");
		}
		//去除多余的一个","
		StringBuffer rightRelSon = relSon.deleteCharAt(relSon.length() - 1);
		//最终执行的sql
		StringBuffer sql = new StringBuffer();
		sql.append("select " + mainSon + " " + rightRelSon + " from w_"
				+ mainTableName + "");
		sql.append(" left join w_" + relTableName + "");
		sql.append(" on w_" + mainTableName + ".id = w_" + relTableName + ".f_"
				+ relSaveColumn + "");
		sql.append(" order by w_" + mainTableName + ".id");
		List<Map<String, Object>> allRecords = jdbcTemplate.queryForList(sql
				.toString());
		return allRecords;
	}

}
