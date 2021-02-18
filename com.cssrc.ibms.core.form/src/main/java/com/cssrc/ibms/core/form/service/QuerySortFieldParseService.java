package com.cssrc.ibms.core.form.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
@Service
public class QuerySortFieldParseService {
	public Map<String, String> getSortMap(Map<String, Object> params,
			String tableIdCode) {
		Map<String, String> sortMap = new HashMap<String, String>();

		String sortField = null;
		String orderSeq = "DESC";
		String newSortField = null;
		if (params.get(tableIdCode + "s") != null) {
			sortField = (String) params.get(tableIdCode + "s");
		}
		if (params.get(tableIdCode + "o") != null) {
			orderSeq = (String) params.get(tableIdCode + "o");
		}
		if (params.get(tableIdCode + "__ns__") != null)
			newSortField = (String) params.get(tableIdCode + "__ns__");
		if (StringUtil.isNotEmpty(newSortField)) {
			if (newSortField.equals(sortField)) {
				if (orderSeq.equals("ASC"))
					orderSeq = "DESC";
				else {
					orderSeq = "ASC";
				}
			}
			sortField = newSortField;
			params.put(tableIdCode + "s", sortField);
			params.put(tableIdCode + "o", orderSeq);

			sortMap.put("sortField", sortField);
			sortMap.put("orderSeq", orderSeq);
		}

		return sortMap;
	}

	public String getOrderBySql(String sortField, Map<String, String> sortMap) {
		StringBuffer orderBy = new StringBuffer();
		if (BeanUtils.isNotEmpty(sortMap)) {
			orderBy.append(" ORDER BY ").append(
					(String) sortMap.get("sortField")).append(" ").append(
					(String) sortMap.get("orderSeq"));
		} else if (StringUtils.isNotEmpty(sortField)) {
			String sortSql = getSortSQL(sortField);
			if (StringUtils.isNotEmpty(sortSql)) {
				orderBy.append(" ORDER BY ").append(sortSql);
			}

		}

		return orderBy.toString();
	}

	private String getSortSQL(String sortField) {
		StringBuffer sb = new StringBuffer();
		JSONArray jsonArray = JSONArray.fromObject(sortField);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArray.get(i);
			String name = (String) jsonObj.get("name");
			String sort = (String) jsonObj.get("sort");
			sb.append(name).append(" " + sort).append(",");
		}
		if (sb.length() > 0)
			return sb.substring(0, sb.length() - 1);
		return sb.toString();
	}
}
