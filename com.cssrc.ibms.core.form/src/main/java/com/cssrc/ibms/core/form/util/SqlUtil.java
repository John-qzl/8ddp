package com.cssrc.ibms.core.form.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.form.model.DialogField;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

public class SqlUtil {
	/**
	 * 取得where 条件。
	 * 
	 * @param conditionMap
	 *            条件map。
	 * @param params
	 *            传入的参数
	 * @return
	 */
	public static String getWhere(List<DialogField> conditionList,
			Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();

		for (DialogField dialogField : conditionList) {
			getStringByDialogField(dialogField, params, sb);
			if (dialogField.getDefaultType() == 5) {
				// java脚本，只需循环一次便可得到sql语句
				break;
			}
		}
		if (sb.length() > 0) {
			return " where 1=1 " + sb.toString();
		}
		return "";

	}
	
	/**
	 * 根据参数配置，上下文参数计算SQL语句。
	 * 
	 * @param dialogField
	 * @param params
	 * @param sb
	 */
	private static void getStringByDialogField(DialogField dialogField,
			Map<String, Object> params, StringBuffer sb) {
		String field = dialogField.getFieldName();
		String condition = dialogField.getCondition();
		int conditionType = dialogField.getDefaultType();
		Object value = null;
		//判断数据库类型，这里是关于mysql数据库字段小写查询问题 by YangBo
		ISysDataSource dtaSource=(ISysDataSource)AppUtil.getBean("sysdatasource");
		String dbType = dtaSource.getDbType();
		if(!dbType.equals("mysql")){
			field = field.toUpperCase();
		}
		
		switch (conditionType) {
		case 1:
			if (params.containsKey(field)) {
				value = params.get(field).toString();
			}
			break;
		case 2:
			value = dialogField.getDefaultValue();
			break;
		case 3:
			String script = dialogField.getDefaultValue();
			if (StringUtil.isNotEmpty(script)) {
				GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
						.getBean(GroovyScriptEngine.class);
				value = groovyScriptEngine.executeObject(script, null);
			}
			break;
		case 4:
			// 向对话框传参数
			if (params.containsKey(field)) {
				value = params.get(field).toString();
			}
			break;
		case 5:
			// java脚本
			String java = dialogField.getDefaultValue();
			if (StringUtil.isNotEmpty(java)) {
				GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
						.getBean(GroovyScriptEngine.class);
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("map", params);
				value = groovyScriptEngine.executeObject(java, paramsMap);
			}
			break;

		}
		if (BeanUtils.isEmpty(value)) {
			// 解决时间类型between条件时查不到数据
			if (!(params.containsKey("start" + field) || params
					.containsKey("end" + field)))
				return;
		}

		if (value.toString().indexOf("#,") != -1) {
			String temp = value.toString().replaceAll("#,", ",");
			sb.append(" and " + field + " in (" + temp + ")");
			return;
		}

		if (dialogField.getFieldType().equals(ColumnModel.COLUMNTYPE_VARCHAR)) {
			if (condition.equals("=")) {
				sb.append(" and " + field + "='" + value.toString() + "' ");
			} else if (condition.equals("like")) {
				sb.append(" and " + field + " like '%" + value.toString()
						+ "%' ");
			} else if (condition.equals("in")) {
				sb.append(" and " + field + " in (" + value.toString() + ")");
			} else if (condition.equals("not in")) {
				sb.append(" and " + field + " not in (" + value.toString()
						+ ")");
			} else if (condition.equals("!=")) {
				sb.append(" and " + field + "!='" + value.toString() + "' ");
			}else {
				sb.append(" and " + field + " like '" + value.toString()
						+ "%' ");
			}
		} else if (dialogField.getFieldType().equals(
				ColumnModel.COLUMNTYPE_DATE)) {
			if (dialogField.getCondition().equals("=")) {
				sb.append(" and " + field + "=:" + field + " ");
				if (!params.containsKey(field)) {
					params.put(field, value);
				}
			} else if (dialogField.getCondition().equals(">=")) {
				if (conditionType == 1) {
					if (params.containsKey("start" + field)) {
						sb.append(" and " + field + ">=:start" + field + " ");
					}
				} else {
					sb.append(" and " + field + ">=:start" + field + " ");
					params.put("start" + field, value);
				}
			} else if (dialogField.getCondition().equals("<=")) {
				if (conditionType == 1) {
					if (params.containsKey("end" + field)) {
						sb.append(" and " + field + "<=:end" + field + " ");
					}
				} else {
					sb.append(" and " + field + "<=:end" + field + " ");
					params.put("end" + field, value);
				}
			}
			// 添加时间类型between条件
			else if (dialogField.getCondition().equals("between")
					&& conditionType == 1) {
				if (params.containsKey("start" + field)) {
					sb.append(" and " + field + ">=:start" + field + " ");
				}
				if (params.containsKey("end" + field)) {
					sb.append(" and " + field + "<=:end" + field + " ");
				}
			}
		} else {
			if (conditionType == 1) {
				if (params.containsKey(field)) {
					sb.append(" and " + field + dialogField.getCondition()
							+ ":" + field + " ");
				}
			} else if (conditionType == 5) {
				if (value.toString().trim().startsWith("and")) {
					sb.append(value.toString());
				} else
					sb.append(" and " + value.toString());
			} else if (conditionType == 2) {
				// 固定值不为varchar和date类型时
				sb.append(" and " + field + dialogField.getCondition() + value
						+ " ");
			} else {
				if (condition.equals("in")) {
					sb.append(" and " + field + " in (" + value.toString() + ")");
				}else{
					sb.append(" and " + field + " "+dialogField.getCondition() + " :"
							+ field + " ");
				}
				params.put(field, value);
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static String getSql(Map<String, Object> map,
			Map<String, Object> params) {
		String objectName = (String) map.get("objectName");
		List<DialogField> retrunList = (List<DialogField>) map
				.get("returnList");
		List<DialogField> displayList = (List<DialogField>) map
				.get("displayList");
		List<DialogField> conditionList = (List<DialogField>) map
				.get("conditionList");
		List<DialogField> sortList = (List<DialogField>) map.get("sortList");
		String sql = "select ";
		if (BeanUtils.isEmpty(retrunList)) {
			sql = "select a.* from " + objectName + " a";
		} else {
			String returnStr = "";
			for (DialogField dialogField : retrunList) {

				returnStr += "," + dialogField.getFieldName();
			}
			returnStr = returnStr.replaceFirst(",", "");
			sql += returnStr + " from " + objectName;
		}
		String where = getWhere(conditionList, params);

		String orderBy = " order by ";
		if (BeanUtils.isEmpty(displayList)) {
			for (int i = 0; i < sortList.size(); i++) {
				DialogField df = sortList.get(i);
				if (i != 0) {
					orderBy += ",";
				}
				orderBy += " " + df.getFieldName() + " " + df.getComment();
			}
			if (BeanUtils.isEmpty(sortList)) {
				return sql + where;
			}
			return sql + where + orderBy;
		}
		if (params.containsKey("sortField")) {
			orderBy += params.get("sortField");
		} else if (BeanUtils.isEmpty(sortList)) {
			orderBy += displayList.get(0).getFieldName();
		}
		// sortField有值或srotList为空
		if (!" order by ".equals(orderBy)) {
			if (params.containsKey("orderSeq")) {
				orderBy += " " + params.get("orderSeq");
			} else {
				orderBy += " ASC";
			}
			for (DialogField df : sortList) {
				// 添加一个判断 因为sqlserver不允许有重复的order by的字段。否则会报错
				if (!params.get("sortField").equals(df.getFieldName())) {
					orderBy += ", " + df.getFieldName() + " " + df.getComment();
				}
			}
		} else {
			// sortField无值以及说sortList不为空
			for (DialogField df : sortList) {
				orderBy += df.getFieldName() + " " + df.getComment() + ",";
			}
		}
		sql = sql + where + orderBy;
		if (sql.trim().endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		return sql;
	}
	
	/**
	 * 获取列表的sql语句
	 * 
	 * @param objectName
	 *            表名或视图名称。
	 * @param displayList
	 *            显示字段列表。
	 * @param conditionList
	 *            条件字段列表。
	 * @param params
	 *            params为只有查询条件的map，没有其他
	 * @return
	 */
	public static String getFKColumnShowDataSql(Map<String, Object> map, Map<String, Object> params) {
		String objectName=(String) map.get("objectName");
		String sql = "select a.* from " + objectName + " a where 1=1 ";
        
		Set<String> set = params.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			String key = it.next();
			if(StringUtil.isNotEmpty(key)){
		         sql+= " and "+key+"=:"+key;
			}
		}

		if(sql.trim().endsWith(",")){
			sql=sql.substring(0,sql.length()-1);
		}
		return sql;
	}

}
