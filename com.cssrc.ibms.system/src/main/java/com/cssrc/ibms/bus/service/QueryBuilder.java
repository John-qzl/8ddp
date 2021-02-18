package com.cssrc.ibms.bus.service;

import com.cssrc.ibms.core.db.mybatis.query.entity.FieldLogic;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldSort;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldTableUtil;
import com.cssrc.ibms.core.db.mybatis.query.scan.SearchCache;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableEntity;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableField;
import com.cssrc.ibms.core.db.mybatis.query.script.IScopeScript;
import com.cssrc.ibms.core.db.mybatis.query.script.ISingleScript;
import com.cssrc.ibms.core.db.mybatis.query.script.ISqlScript;
import com.cssrc.ibms.core.db.mybatis.query.script.impl.ScopeScript;
import com.cssrc.ibms.core.db.mybatis.query.script.impl.SingleScriptFactory;
import com.cssrc.ibms.core.db.mybatis.query.script.impl.SqlScript;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * <p>Title:QueryBuilder</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:14:46
 */
public class QueryBuilder {
	public static String buildSql(String mainTableName,
			List<FieldLogic> fieldLogics, List<FieldSort> fieldSorts) {
		TableEntity tableEntity = (TableEntity) SearchCache.getTableEntityMap()
				.get(mainTableName);

		Set tables = QueryUtil.getTables(fieldLogics);
		tables.add(mainTableName);

		StringBuilder sb = new StringBuilder();
		sb.append("select ").append(buildSelectFields(tableEntity, tables))
				.append(" from ").append(buildFromTables(tables)).append(
						" where 1=1 ");
		if (BeanUtils.isNotEmpty(fieldLogics)) {
			String whereSql = buildWhereSqls(fieldLogics);
			if (StringUtils.isNotEmpty(whereSql))
				sb.append(" and ").append(whereSql);
		}
		sb.append(buildTableRelations(tableEntity, tables));
		if (BeanUtils.isNotEmpty(fieldSorts))
			sb.append(" order by ").append(buildSorts(fieldSorts));
		return sb.toString();
	}

	public static String buildSelectFields(TableEntity tableEntity,
			Set<String> tables) {
		StringBuilder sb = new StringBuilder(" distinct ");
		for (TableField tableField : tableEntity.getTableFieldList()) {
			if (tableField.getDataType().equals("clob"))
				continue;
			String fieldName = tableField.getName();

			fieldName = FieldTableUtil.fixFieldName(fieldName, tableField
					.getVar(), tableEntity.getTableName(), true);
			sb.append(fieldName).append(",");
		}

		String result = sb.toString();
		if (result.endsWith(","))
			result = result.substring(0, result.length() - 1);
		return result;
	}

	public static String buildFromTables(Set<String> tableNames) {
		StringBuilder sb = new StringBuilder();
		for (Iterator it = tableNames.iterator(); it.hasNext();) {
			String table = (String) it.next();
			String key = table.toLowerCase();
			sb.append(table).append(" ").append(key).append(",");
		}
		String result = sb.toString();
		if (result.endsWith(","))
			result = result.substring(0, result.length() - 1);
		return result;
	}

	public static String buildWhereSqls(List<FieldLogic> fieldLogics) {
		StringBuilder sb = new StringBuilder();
		if (fieldLogics.size() == 0)
			return sb.toString();
		FieldLogic firstFieldLogic = (FieldLogic) fieldLogics.get(0);
		if (firstFieldLogic.isGroup()) {
			String script = buildWhereSqls(firstFieldLogic.getGroupLogics());
			sb.append(script);
		} else {
			sb.append(getSql(firstFieldLogic));
		}
		int len = fieldLogics.size();
		if (len == 1)
			return sb.toString();
		for (int i = 1; i < len; i++) {
			FieldLogic fieldLogic = (FieldLogic) fieldLogics.get(i);
			if (StringUtils.isNotEmpty(fieldLogic.getFieldRelation()))
				sb.append(" ").append(fieldLogic.getFieldRelation())
						.append(" ");
			if (fieldLogic.isGroup()) {
				String script = buildWhereSqls(fieldLogic.getGroupLogics());
				sb.append(" (").append(script).append(") ");
			} else {
				sb.append(getSql(fieldLogic));
			}
		}
		return sb.toString();
	}

	private static String buildTableRelations(TableEntity tableEntity,
			Set<String> tables) {
		String pk = tableEntity.getPk();
		Map subTableMap = TableEntity.getSubTableMap(tableEntity);
		StringBuilder sb = new StringBuilder();
		for (String table : tables) {
			TableEntity subTable = (TableEntity) subTableMap.get(table);
			String fk = "";
			if (BeanUtils.isNotEmpty(subTable))
				fk = subTable.getRelation();
			if (StringUtils.isEmpty(fk))
				continue;
			sb.append(" ").append("AND").append(" ").append(
					tableEntity.getTableName()).append(".").append(pk).append(
					"=").append(table).append(".").append(fk);
		}
		return sb.toString();
	}

	public static String buildSorts(List<FieldSort> fieldSorts) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldSorts.size(); i++) {
			FieldSort fieldSort = (FieldSort) fieldSorts.get(i);
			sb.append(fieldSort.getFixFieldName()).append(" ").append(
					fieldSort.getOrderBy());
			if (i != fieldSorts.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private static String getSql(FieldLogic fieldLogic) {
		String script = "";
		int judgeType = fieldLogic.getJudgeType();
		if (judgeType == 1) {
			ISingleScript singleScript = SingleScriptFactory
					.getQueryScript(fieldLogic.getDataType());
			script = singleScript.getSQL(fieldLogic.getJudgeSingle());
		} else if (judgeType == 2) {
			IScopeScript scopeScript = new ScopeScript();
			script = scopeScript.getSQL(fieldLogic.getJudgeScope());
		} else if (judgeType == 3) {
			ISqlScript sqlScript = new SqlScript();
			script = sqlScript.getSQL(fieldLogic.getJudgeScript());
		}
		if (StringUtils.isEmpty(script))
			return "";
		return "(" + script + ")";
	}
}
