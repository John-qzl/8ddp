package com.cssrc.ibms.bus.service;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.displaytag.util.ParamEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.bus.model.BusQueryFilter;
import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.bus.model.BusQuerySetting;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.Filter;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldLogic;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldSort;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldTableUtil;
import com.cssrc.ibms.core.db.mybatis.query.entity.FilterJsonStruct;
import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeScope;
import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeScript;
import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeSingle;
import com.cssrc.ibms.core.db.mybatis.query.scan.SearchCache;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableEntity;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableField;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.StringUtil;


public class QueryUtil {

	@Resource
	private static JdbcTemplate jdbcTemplate;
	public static Logger logger = LoggerFactory.getLogger(QueryUtil.class);

	public static List<FilterJsonStruct> buildFilterJsonStructs(
			JSONObject filterJson) {
		if (JSONUtil.isEmpty(filterJson))
			return new ArrayList();
		return buildFilterJsonStructsByCondition(filterJson.get("condition")
				.toString());
	}

	private static JSONObject getFilterJson(String filterField, String filterKey) {
		if (StringUtils.isEmpty(filterField))
			return null;
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(filterField);
		if (JSONUtil.isEmpty(jsonArray)) {
			return null;
		}
		if (StringUtils.isEmpty(filterKey))
			return jsonArray.getJSONObject(0);
		for (Iterator localIterator = jsonArray.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jObj = (JSONObject) obj;
			String key = (String) jObj.get("key");
			if (key.equals(filterKey)) {
				return jObj;
			}
		}
		return null;
	}

	public static List<FilterJsonStruct> buildFilterJsonStructsByCondition(
			String condition) {
		return com.alibaba.fastjson.JSONArray.parseArray(condition,
				FilterJsonStruct.class);
	}

	public static List<FieldLogic> buildFieldLogics(
			List<FilterJsonStruct> filterJsonStructs) {
		Map params = new HashMap();
		CommonVar.setCurrentVars(params);
		List fieldLogics = new ArrayList();
		for (Iterator iterator = filterJsonStructs.iterator(); iterator
				.hasNext();) {
			FilterJsonStruct filterJsonStruct = (FilterJsonStruct) iterator
					.next();
			FieldLogic fieldLogic = buildFieldLogic(filterJsonStruct, params);
			if (filterJsonStruct.getBranch().booleanValue()) {
				List groupLogics = buildFieldLogics(filterJsonStruct.getSub());
				fieldLogic.setGroupLogics(groupLogics);
			}
			fieldLogics.add(fieldLogic);
		}
		return fieldLogics;
	}

	public static FieldLogic buildFieldLogic(FilterJsonStruct filter,
			Map<String, Object> params) {
		FieldLogic fieldLogic = new FieldLogic();
		if (filter.getRuleType().intValue() == 2) {
			fieldLogic.setFieldRelation("AND");
			fieldLogic.setJudgeType(3);
			fieldLogic.setJudgeScript(buildJudgeScript(filter));
		} else {
			fieldLogic.setGroup(filter.getBranch().booleanValue());
			fieldLogic.setFieldName(filter.getFlowvarKey());
			fieldLogic.setDataType(filter.getOptType());
			fieldLogic.setTableName(filter.getTable());
			fieldLogic.setFieldRelation(filter.getCompType());
			if (StringUtils.isEmpty(filter.getJudgeVal2())) {
				fieldLogic.setJudgeType(1);
				fieldLogic.setJudgeSingle(buildJudgeSingle(filter, params));
			} else {
				fieldLogic.setJudgeType(2);
				fieldLogic.setJudgeScope(buildJudgeScope(filter));
			}
		}
		return fieldLogic;
	}

	public static Set<String> getTables(List<FieldLogic> fieldLogics) {
		Set tableSet = new HashSet();
		if ((fieldLogics == null) || (fieldLogics.size() == 0))
			return tableSet;
		for (Iterator iterator = fieldLogics.iterator(); iterator.hasNext();) {
			FieldLogic fieldLogic = (FieldLogic) iterator.next();
			if (StringUtils.isNotEmpty(fieldLogic.getTableName())) {
				tableSet.add(fieldLogic.getTableName());
			}
			if (fieldLogic.isGroup()) {
				Set groupSet = getTables(fieldLogic.getGroupLogics());
				tableSet.addAll(groupSet);
			}
		}
		return tableSet;
	}

	public static List<FieldSort> buildFieldSorts(String sortJson) {
		if (StringUtils.isEmpty(sortJson))
			return new ArrayList();
		List fieldSorts = com.alibaba.fastjson.JSONArray.parseArray(sortJson,
				FieldSort.class);
		return fieldSorts;
	}

	private static String getJudeValue(String judgeVal, Integer optType,
			String judgeCon, Map<String, Object> params) {
		String judgeValue = "";
		Object o = null;
		switch (optType.intValue()) {
		case 1:
		case 2:
		case 3:
			judgeValue = judgeVal;
			break;
		case 4:
			String[] vals = judgeVal.split("&&");
			for (String val : vals) {
				judgeValue = judgeValue + val + ",";
			}
			judgeValue = StringUtils.removeEnd(judgeValue, ",");
			break;
		case 5:
			String[] ids = judgeVal.split("&&");
			if (ids.length == 2) {
				judgeValue = ids[0];
			} else {
				if (("3".equalsIgnoreCase(judgeCon))
						|| ("4".equalsIgnoreCase(judgeCon))) {
					o = params.get(judgeVal);
				}
				if (o == null)
					break;
				judgeValue = String.valueOf(o);
			}

		}

		return judgeValue;
	}

	private static JudgeSingle buildJudgeSingle(
			FilterJsonStruct filterJsonStruct, Map<String, Object> params) {
		JudgeSingle judgeSingle = new JudgeSingle();
		judgeSingle.setTableName(filterJsonStruct.getTable());
		judgeSingle.setFieldName(filterJsonStruct.getFlowvarKey());
		judgeSingle.setCompare(filterJsonStruct.getJudgeCon1());
		judgeSingle.setValue(getJudeValue(filterJsonStruct.getJudgeVal1(),
				filterJsonStruct.getOptType(), filterJsonStruct.getJudgeCon1(),
				params));
		return judgeSingle;
	}

	private static JudgeScope buildJudgeScope(FilterJsonStruct filterJsonStruct) {
		JudgeScope judgeScope = new JudgeScope();
		judgeScope.setTableName(filterJsonStruct.getTable());
		judgeScope.setFieldName(filterJsonStruct.getFlowvarKey());
		judgeScope.setCompare(filterJsonStruct.getJudgeCon1());
		judgeScope.setValue(filterJsonStruct.getJudgeVal1());
		judgeScope.setCompareEnd(filterJsonStruct.getJudgeCon2());
		judgeScope.setValueEnd(getJudeValue(filterJsonStruct.getJudgeVal2(),
				filterJsonStruct.getOptType(), filterJsonStruct.getJudgeCon1(),
				null));

		judgeScope.setRelation("AND");
		judgeScope.setOptType(filterJsonStruct.getOptType());
		return judgeScope;
	}

	private static JudgeScript buildJudgeScript(
			FilterJsonStruct filterJsonStruct) {
		JudgeScript judgeScript = new JudgeScript();
		judgeScript.setValue(filterJsonStruct.getScript());
		return judgeScript;
	}

	private static List<FilterJsonStruct> getPostQueryFilter(
			HttpServletRequest request, TableEntity tableEntity) {
		List mainTableFieldList = tableEntity.getTableFieldList();
		if (BeanUtils.isEmpty(mainTableFieldList))
			return null;
		String mainTableName = tableEntity.getTableName();
		Map tableFieldMap = getTableFieldMap(mainTableFieldList);
		List list = new ArrayList();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement().toString();
			String[] values = request.getParameterValues(key);
			if ((values.length <= 0) || (!StringUtils.isNotEmpty(values[0]))
					|| (!key.startsWith("Q_")))
				continue;
			FilterJsonStruct filterJsonStruct = setFilterJsonStruct(key,
					values, mainTableName, tableFieldMap);
			if (BeanUtils.isEmpty(filterJsonStruct))
				continue;
			list.add(filterJsonStruct);
		}

		return list;
	}

	private static FilterJsonStruct setFilterJsonStruct(String key,
			String[] values, String mainTableName,
			Map<String, String> tableFieldMap) {
		String[] aryParaKey = key.split("_");
		if (aryParaKey.length < 3)
			return null;
		String paraName = key.substring(2, key.lastIndexOf("_"));
		String type = key.substring(key.lastIndexOf("_") + 1);
		String tableField = getTableField(tableFieldMap, paraName, type);
		String relation = getQueryRelation(type);
		if (StringUtils.isEmpty(tableField)) {
			return null;
		}
		FilterJsonStruct f = new FilterJsonStruct();
		if (values.length == 1) {
			String value = values[0].trim();
			if (StringUtil.isNotEmpty(value)) {
				try {
					if (value.indexOf("_") != -1) {
						value = value.replaceAll("_", "|_");
					}
					f = getFilterJsonStruct(mainTableName, tableField,
							paraName, relation);
				} catch (Exception e) {
					logger.debug(e.getMessage());
				}
			}
		}
		return f;
	}

	private static String getTableField(Map<String, String> tableFieldMap,
			String paraName, String type) {
		if ("DL".equals(type)) {
			paraName = StringUtils.removeStart(paraName, "begin");
		} else if ("DG".equals(type)) {
			paraName = StringUtils.removeStart(paraName, "end");
		}
		return (String) tableFieldMap.get(paraName);
	}

	private static String getQueryRelation(String type) {
		String relation = "=";

		if (("S".equals(type)) || ("L".equals(type)) || ("N".equals(type))
				|| ("DB".equals(type)) || ("BD".equals(type))
				|| ("FT".equals(type)) || ("SN".equals(type))) {
			relation = "=";
		} else if (("SL".equals(type)) || ("SLR".equals(type))
				|| ("SLL".equals(type)) || ("SUPL".equals(type))
				|| ("SUPLR".equals(type)) || ("SUPLL".equals(type))
				|| ("SLOL".equals(type)) || ("SLOLR".equals(type))
				|| ("SLOLL".equals(type))) {
			relation = " LIKE ";
		} else if ("DL".equals(type)) {
			relation = ">=";
		} else if ("DG".equals(type)) {
			relation = "<=";
		}

		return relation;
	}

	private static FilterJsonStruct getFilterJsonStruct(String tableName,
			String fieldName, String paraName, String relation) {
		FilterJsonStruct f = new FilterJsonStruct();
		f.setRuleType(Integer.valueOf(2));
		StringBuffer script = new StringBuffer();

		script.append(FieldTableUtil.fixFieldName(fieldName, tableName))
				.append(" ").append(relation).append(" :").append(paraName);

		f.setScript(script.toString());
		return f;
	}

	private static Map<String, String> getTableFieldMap(
			List<TableField> mainTableFieldList) {
		Map map = new HashMap();
		for (TableField tableField : mainTableFieldList) {
			map.put(tableField.getVar(), tableField.getName());
		}
		return map;
	}

	private static FieldSort getPostFieldSort(QueryFilter queryFilter,
			String tableName) {
		if (queryFilter == null)
			return null;
		Map filters = queryFilter.getFilters();
		Object orderField = filters.get("orderField");
		if (BeanUtils.isEmpty(orderField))
			return null;
		Object orderSeq = filters.get("orderSeq");
		return new FieldSort(tableName, String.valueOf(orderField), String
				.valueOf(orderSeq));
	}

	private static List<FieldSort> buildFieldSort(String sortField,
			String tableName) {
		if ((StringUtils.isEmpty(sortField))
				|| (StringUtils.isEmpty(tableName)))
			return null;
		List list = new ArrayList();
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(sortField);
		if (JSONUtil.isEmpty(jsonArray))
			return null;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArray.get(i);
			String name = (String) jsonObj.get("name");
			String sort = (String) jsonObj.get("sort");
			list.add(new FieldSort(tableName, name, sort));
		}
		return list;
	}

	public static String getQuerySQL(BusQueryRule busQueryRule,
			TableEntity tableEntity, QueryFilter queryFilter,
			Map<String, Object> params, BusQueryFilter busQueryFilter) {
		List postFilterJsonStructs = null;
		FieldSort fieldSort = null;
		HttpServletRequest request = queryFilter.getRequest();

		String filterKey = RequestUtil.getString(request, "__FILTERKEY__");
		if (BeanUtils.isNotEmpty(busQueryFilter)) {
			filterKey = busQueryFilter.getFilterKey();
			postFilterJsonStructs = getBusQueryFilter(busQueryFilter,
					tableEntity);
			fieldSort = getQuerySort(busQueryFilter, tableEntity);
		} else {
			postFilterJsonStructs = getPostQueryFilter(request, tableEntity);

			fieldSort = getPostFieldSort(queryFilter, tableEntity
					.getTableName());
		}
		JSONObject filterJson = getFilterJson(busQueryRule.getFilterField(),
				filterKey);

		if (JSONUtil.isNotEmpty(filterJson)) {
			String type = (String) filterJson.get("type");
			if ("2".equals(type)) {
				String condition = (String) filterJson.get("condition");
				Map paramsMap = new HashMap();

				CommonVar.setCurrentVars(params);
				paramsMap.put("map", params);
				paramsMap.put("sort", getFieldSortMap(fieldSort));
				GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
						.getBean(GroovyScriptEngine.class);
				String querySQL = groovyScriptEngine.executeString(condition,
						paramsMap);
				logger.info(querySQL);
				return querySQL;
			}
		}

		List filterJsonStructs = buildFilterJsonStructs(filterJson);

		if (BeanUtils.isNotEmpty(postFilterJsonStructs)) {
			filterJsonStructs.addAll(postFilterJsonStructs);
		}

		List fieldLogics = buildFieldLogics(filterJsonStructs);

		List fieldSorts = new ArrayList();

		if (fieldSort != null) {
			fieldSorts.add(fieldSort);
		} else {
			fieldSorts = buildFieldSort(busQueryRule.getSortField(),
					tableEntity.getTableName());
		}

		String querySQL = QueryBuilder.buildSql(tableEntity.getTableName(),
				fieldLogics, fieldSorts);

		logger.info(querySQL);
		return querySQL;
	}

	private static Map<String, Object> getFieldSortMap(FieldSort fieldSort) {
		Map map = new HashMap();
		if (BeanUtils.isEmpty(fieldSort)) {
			return map;
		}
		map.put("orderField", fieldSort.getFieldName());
		map.put("orderSeq", fieldSort.getOrderBy());
		map.put("table", fieldSort.getTableName());
		return map;
	}

	private static FieldSort getQuerySort(BusQueryFilter busQueryFilter,
			TableEntity tableEntity) {
		String sortParameter = busQueryFilter.getSortParameter();
		if (StringUtils.isEmpty(sortParameter))
			return null;
		JSONObject json = JSONObject.fromObject(sortParameter);
		if (JSONUtil.isEmpty(json))
			return null;
		Object orderField = json.get("orderField");
		if (BeanUtils.isEmpty(orderField))
			return null;
		Object orderSeq = json.get("orderSeq");
		return new FieldSort(tableEntity.getTableName(), String
				.valueOf(orderField), String.valueOf(orderSeq));
	}

	private static List<FilterJsonStruct> getBusQueryFilter(
			BusQueryFilter busQueryFilter, TableEntity tableEntity) {
		String queryParameter = busQueryFilter.getQueryParameter();
		if (StringUtils.isEmpty(queryParameter))
			return null;
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(queryParameter);
		if (JSONUtil.isEmpty(jsonArray)) {
			return null;
		}
		List mainTableFieldList = tableEntity.getTableFieldList();
		if (BeanUtils.isEmpty(mainTableFieldList))
			return null;
		String mainTableName = tableEntity.getTableName();
		Map tableFieldMap = getTableFieldMap(mainTableFieldList);

		List list = new ArrayList();
		for (Iterator localIterator = jsonArray.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jObj = (JSONObject) obj;
			String paraName = (String) jObj.get("paraName");
			String type = (String) jObj.get("type");
			String tableField = getTableField(tableFieldMap, paraName, type);
			String relation = getQueryRelation(type);
			if (StringUtils.isEmpty(tableField))
				return null;
			FilterJsonStruct filterJsonStruct = getFilterJsonStruct(
					mainTableName, tableField, paraName, relation);

			list.add(filterJsonStruct);
		}
		return list;
	}

	public static List<?> getPageList(BusQueryRule busQueryRule,
			QueryFilter queryFilter) {
		if ((StringUtils.isEmpty(busQueryRule.getFilterKey()))
				&& (busQueryRule.getIsFilter().shortValue() == 1)) {
			queryFilter.setForWeb();
			return null;
		}

		List list = null;
		HttpServletRequest request = queryFilter.getRequest();
		short isQuery = getIsQuery(request, busQueryRule);

		if (isQuery == 0) {
			TableEntity tableEntity = getTableEntity(busQueryRule
					.getTableName());
			BusQueryFilter busQueryFilter = getBusQueryFilter(busQueryRule
					.getFilterFlag());
			Map paraMap = getFilters(queryFilter.getFilters(), busQueryFilter);

			String querySQL = getQuerySQL(busQueryRule, tableEntity,
					queryFilter, paraMap, busQueryFilter);

			if (busQueryRule.getNeedPage().intValue() == 1) {
				ParamEncoder paramEncoder = queryFilter.getParamEncoder();
				String tableIdCode = paramEncoder.encodeParameterName("");
				PagingBean page = queryFilter.getPagingBean();
				int currentPage = page.getCurrentPage();
				int pageSize = page.getPageSize();
				int oldPageSize = RequestUtil.getInt(request, tableIdCode
						+ "oz", -1);
				if (oldPageSize < 0)
					pageSize = page.getPageSize() != busQueryRule.getPageSize()
							.intValue() ? busQueryRule.getPageSize().intValue()
							: page.getPageSize();
				PagingBean pageBean = new PagingBean(currentPage, pageSize);
				queryFilter.setPagingBean(pageBean);

				list = JdbcTemplateUtil.getPage("dataSource_Default", querySQL,
						paraMap, pageBean);
			} else {
				list = jdbcTemplate.queryForList(querySQL,
						new Object[] { paraMap });
			}
		}

		queryFilter.setForWeb();
		return list;
	}

	private static short getIsQuery(HttpServletRequest request,
			BusQueryRule busQueryRule) {
		if (BeanUtils.isEmpty(busQueryRule))
			return 0;
		Short isQuery = RequestUtil.getShort(request, "__IS_QUERY__", null);
		if (BeanUtils.isEmpty(isQuery))
			return busQueryRule.getIsQuery().shortValue();
		return isQuery.shortValue();
	}

	private static Map<String, Object> getFilters(Map<String, Object> filters,
			BusQueryFilter busQueryFilter) {
		if (BeanUtils.isEmpty(busQueryFilter))
			return filters;
		String queryParameter = busQueryFilter.getQueryParameter();
		if (StringUtils.isEmpty(queryParameter))
			return null;
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(queryParameter);
		if (JSONUtil.isEmpty(jsonArray))
			return null;
		filters = new HashMap();
		for (Iterator localIterator = jsonArray.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jObj = (JSONObject) obj;
			String paraName = (String) jObj.get("paraName");
			String value = (String) jObj.get("value");
			filters.put(paraName, value);
		}
		return filters;
	}

	private static BusQueryFilter getBusQueryFilter(Long id) {
		if ((BeanUtils.isNotEmpty(id)) && (id.longValue() > 0L)) {
			BusQueryFilterService busQueryFilterService = (BusQueryFilterService) AppUtil
					.getBean(BusQueryFilterService.class);
			return (BusQueryFilter) busQueryFilterService.getById(id);
		}
		return null;
	}

	public static TableEntity getTableEntity(HttpServletRequest request) {
		String uri = request.getRequestURI();

		String var = StringUtils.substring(uri, 0, uri.lastIndexOf("/"));
		var = StringUtils.substring(var, var.lastIndexOf("/") + 1);

		return (TableEntity) SearchCache.getTableVarMap().get(var);
	}

	public static TableEntity getTableEntity(String displayTagId,
			HttpServletRequest request) {
		TableEntity tableEntity = (TableEntity) SearchCache
				.getDisplayTagIdMap().get(displayTagId);
		if (tableEntity != null)
			return tableEntity;
		return getTableEntity(request);
	}

	public static TableEntity getTableEntity(String tableName) {
		return (TableEntity) SearchCache.getTableEntityMap().get(tableName);
	}

	public static BusQueryRule getBusQueryRule(String displayTagId,
			HttpServletRequest request) {
		String url = request.getRequestURI();
		TableEntity tableEntity = getTableEntity(displayTagId, request);

		if (BeanUtils.isEmpty(tableEntity))
			return null;
		String tableName = tableEntity.getTableName();

		/*if (UserContextUtil.isSuperAdmin()) {
			BusQueryRule busQueryRule = new BusQueryRule();
			busQueryRule.setUrl(url);
			busQueryRule.setTableName(tableName);
			return busQueryRule;
		}*/

		BusQueryRuleService busQueryRuleService = (BusQueryRuleService) AppUtil
				.getBean(BusQueryRuleService.class);

		BusQueryRule busQueryRule = busQueryRuleService
				.getByTableName(tableName);

		if (BeanUtils.isEmpty(busQueryRule)) {
			busQueryRule = new BusQueryRule();
			busQueryRule.setUrl(url);
			busQueryRule.setTableName(tableName);
			return busQueryRule;
		}

		Long filterFlag = RequestUtil.getLong(request, "__FILTER_FLAG__", null);

		Map rightMap = getRightMap();

		busQueryRule = getRightFilter(busQueryRule, rightMap);

		String filterKey = getFilterKey(busQueryRule, request);

		Map permission = getPermission(busQueryRule, rightMap);

		BusQuerySettingService busQuerySettingService = (BusQuerySettingService) AppUtil
				.getBean(BusQuerySettingService.class);
		BusQuerySetting busQuerySetting = busQuerySettingService
				.getByTableNameUserId(tableName, UserContextUtil.getCurrentUserId());
		permission = getPermissionSetting(permission, busQuerySetting);

		List filterList = getFilter(busQueryRule, rightMap, filterKey);
		busQueryRule.setFilterKey(filterKey);
		busQueryRule.setFilterList(filterList);
		busQueryRule.setPermission(permission);
		busQueryRule.setUrl(url);
		busQueryRule.setFilterFlag(filterFlag);
		return busQueryRule;
	}

	private static Map<String, Boolean> getPermissionSetting(
			Map<String, Boolean> permission, BusQuerySetting busQuerySetting) {
		if (BeanUtils.isEmpty(busQuerySetting))
			return permission;
		if (BeanUtils.isEmpty(busQuerySetting.getDisplayField())) {
			return permission;
		}
		Map right = getDisplayFieldShow(busQuerySetting.getDisplayField());
		return getPermissionSetting(permission, right);
	}

	private static Map<String, Boolean> getPermissionSetting(
			Map<String, Boolean> permission, Map<String, Boolean> right) {
		Map map = new HashMap();
		for (Iterator it = permission.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			String key = (String) e.getKey();
			boolean val = ((Boolean) e.getValue()).booleanValue();
			if (val)
				val = BeanUtils.isEmpty(right.get(key)) ? val
						: ((Boolean) right.get(key)).booleanValue();
			map.put(key, Boolean.valueOf(val));
		}
		return map;
	}

	private static Map<String, Boolean> getDisplayFieldShow(String displayField) {
		Map map = new HashMap();
		if (StringUtil.isEmpty(displayField)) {
			return null;
		}
		net.sf.json.JSONArray jsonAry = net.sf.json.JSONArray
				.fromObject(displayField);

		for (Iterator localIterator = jsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			String show = (String) json.get("show");
			map.put(name, Boolean.valueOf(!"1".equals(show)));
		}
		return map;
	}

	private static BusQueryRule getRightFilter(BusQueryRule busQueryRule,
			Map<String, Object> rightMap) {
		String filterField = busQueryRule.getFilterField();
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(filterField);
		String destFilterField = new net.sf.json.JSONArray().toString();
		if (JSONUtil.isEmpty(jsonArray)) {
			busQueryRule.setFilterField(destFilterField);
			return busQueryRule;
		}

		net.sf.json.JSONArray jsonAry = new net.sf.json.JSONArray();
		for (Iterator localIterator1 = jsonArray.iterator(); localIterator1
				.hasNext();) {
			Object obj = localIterator1.next();
			JSONObject jObj = (JSONObject) obj;
			net.sf.json.JSONArray rightAry = net.sf.json.JSONArray
					.fromObject(jObj.get("right"));
			Iterator iterator = rightAry.iterator();
			while (iterator.hasNext()) {
				JSONObject permission = (JSONObject) iterator.next();
				if ((permission.containsKey("type"))
						&& (hasRight(permission, rightMap))) {
					jsonAry.add(obj);
					break;
				}
			}
		}

		if (JSONUtil.isEmpty(jsonAry)) {
			busQueryRule.setFilterField(destFilterField);
			return busQueryRule;
		}

		busQueryRule.setFilterField(jsonAry.toString());
		return busQueryRule;
	}

	public static Map<String, Boolean> getPermission(BusQueryRule busQueryRule,
			Map<String, Object> rightMap) {
		if (BeanUtils.isEmpty(busQueryRule))
			return null;
		return getPermission(busQueryRule.getDisplayField(), rightMap);
	}

	private static Map<String, Boolean> getPermission(String displayField,
			Map<String, Object> rightMap) {
		net.sf.json.JSONArray jsonAry = net.sf.json.JSONArray
				.fromObject(displayField);
		return getPermissionMap(jsonAry, rightMap);
	}

	public static String getRightsName(JSONObject json) {
		String var = (String) json.get("variable");
		if (StringUtil.isEmpty(var))
			var = json.getString("name");
		return var;
	}

	public static Map<String, Boolean> getPermissionMap(
			net.sf.json.JSONArray jsonAry, Map<String, Object> rightMap) {
		Map map = new HashMap();
		if (JSONUtil.isEmpty(jsonAry))
			return map;
		for (Iterator localIterator1 = jsonAry.iterator(); localIterator1
				.hasNext();) {
			Object obj = localIterator1.next();
			JSONObject json = JSONObject.fromObject(obj);
			String name = getRightsName(json);
			net.sf.json.JSONArray rights = (net.sf.json.JSONArray) json
					.get("right");
			Iterator iterator = rights.iterator();
			boolean hasRight = false;
			while (iterator.hasNext()) {
				JSONObject permission = (JSONObject) iterator.next();

				hasRight = hasRight(permission, rightMap);
				if (hasRight) {
					map.put(name, Boolean.valueOf(hasRight));
					break;
				}
			}
			map.put(name, Boolean.valueOf(hasRight));
		}
		return map;
	}

	public static boolean hasRight(JSONObject permission,
			Map<String, Object> rightMap) {
		String type = permission.get("type").toString();
		Object script = permission.get("script");
		if ("none".equals(type)) {
			return false;
		}
		if ("everyone".equals(type)) {
			return true;
		}
		String id = permission.get("id").toString();
		String[] userId = (String[]) null;
		String[] curOrgId = (String[]) null;
		if (BeanUtils.isNotEmpty(rightMap.get("userId"))) {
			userId = rightMap.get("userId").toString().split(",");
		}
		if (BeanUtils.isNotEmpty(rightMap.get("curOrgId"))) {
			curOrgId = rightMap.get("curOrgId").toString().split(",");
		}

		List roles = (List) rightMap.get("roles");
		List positions = (List) rightMap.get("positions");
		List orgs = (List) rightMap.get("orgs");
		List ownOrgs = (List) rightMap.get("ownOrgs");
		Object localObject1;
		/*if ("user".equals(type)) {
			if ((BeanUtils.isNotEmpty(userId))
					&& ((localObject1 = userId).length != 0)) {
				String ui = localObject1[0];
				return StringUtil.contain(id, ui);
			}

		}*/// yangbo
		if ("role".equals(type)) {
			if (BeanUtils.isNotEmpty(roles)) {
				for (localObject1 = roles.iterator(); ((Iterator) localObject1)
						.hasNext();) {
					ISysRole role = (ISysRole) ((Iterator) localObject1).next();
					if (StringUtil.contain(id, role.getRoleId().toString())) {
						return true;
					}
				}
			}
		} else if ("org".equals(type)) {
			if (BeanUtils.isNotEmpty(orgs)) {
				for (localObject1 = orgs.iterator(); ((Iterator) localObject1)
						.hasNext();) {
					ISysOrg org = (ISysOrg) ((Iterator) localObject1).next();
					if (StringUtil.contain(id, org.getOrgId().toString())) {
						return true;
					}
				}
			}
		} else if ("orgMgr".equals(type)) {
			if (BeanUtils.isNotEmpty(ownOrgs)) {
				for (localObject1 = ownOrgs.iterator(); ((Iterator) localObject1)
						.hasNext();) {
					IUserPosition userPosition = (IUserPosition) ((Iterator) localObject1)
							.next();
					if (StringUtil
							.contain(id, userPosition.getOrgId().toString())) {
						return true;
					}
				}
			}
		} else if ("pos".equals(type)) {
			if (BeanUtils.isNotEmpty(positions))
				for (localObject1 = positions.iterator(); ((Iterator) localObject1)
						.hasNext();) {
					IPosition position = (IPosition) ((Iterator) localObject1)
							.next();
					if (StringUtil.contain(id, position.getPosId().toString()))
						return true;
				}
		} else if ("script".equals(type)) {
			if (BeanUtils.isEmpty(script))
				return false;
			Map map = new HashMap();
			if ((userId != null) && (curOrgId != null))
				CommonVar.setCurrentVars(map);
			GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil
					.getBean(GroovyScriptEngine.class);
			return groovyScriptEngine.executeBoolean(script.toString(), map);
		}
		return false;
	}

	public static Map<String, Object> getRightMap() {
		/*if (UserContextUtil.isSuperAdmin()) {
			return null;
		}*/
		Long userId = UserContextUtil.getCurrentUserId();
		Long curOrgId = UserContextUtil.getCurrentOrgId();

		ISysRoleService sysRoleService = (ISysRoleService) AppUtil
				.getBean(ISysRoleService.class);
		IPositionService positionService = (IPositionService) AppUtil
				.getBean(IPositionService.class);
		ISysOrgService sysOrgService = (ISysOrgService) AppUtil
				.getBean(ISysOrgService.class);
		IUserPositionService userPositionService = (IUserPositionService) AppUtil
				.getBean(IUserPositionService.class);

		Map map = new HashMap();
		List roles = sysRoleService.getByUserId(userId);
		List positions = positionService.getByUserId(userId);
		List orgs = sysOrgService.getOrgsByUserId(userId);

		List ownOrgs = userPositionService.getChargeOrgByUserId(userId);
		if(ownOrgs.size()==0){
			ownOrgs=null;		
		}
		map.put("userId", userId);
		map.put("curOrgId", curOrgId);
		map.put("roles", roles);
		map.put("positions", positions);
		map.put("orgs", orgs);	
		map.put("ownOrgs", ownOrgs);
		return map;
	}

	public static List<Filter> getFilter(BusQueryRule busQueryRule,
			Map<String, Object> rightMap, String filterKey) {
		if ((BeanUtils.isEmpty(busQueryRule))
				|| (StringUtils.isEmpty(busQueryRule.getFilterField())))
			return null;
		net.sf.json.JSONArray jsonAry = net.sf.json.JSONArray
				.fromObject(busQueryRule.getFilterField());
		if (JSONUtil.isEmpty(jsonAry))
			return null;
		List list = new ArrayList();
		for (Iterator localIterator = jsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject jObj = (JSONObject) obj;
			String name = (String) jObj.get("name");
			String key = (String) jObj.get("key");
			net.sf.json.JSONArray rightAry = net.sf.json.JSONArray
					.fromObject(jObj.get("right"));
			JSONObject permission = rightAry.getJSONObject(0);
			if (hasRight(permission, rightMap)) {
				Filter filter = new Filter();
				filter.setName(name);
				filter.setDesc(StringUtil.subString(name, 5, "..."));
				filter.setKey(key);
				list.add(filter);
			}
		}

		return list;
	}

	public static String getFilterKey(BusQueryRule busQueryRule,
			HttpServletRequest request) {
		String filterKey = RequestUtil.getString(request, "__FILTERKEY__");
		if ((StringUtils.isNotEmpty(filterKey))
				|| (BeanUtils.isEmpty(busQueryRule)))
			return filterKey;
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray
				.fromObject(busQueryRule.getFilterField());
		if (JSONUtil.isEmpty(jsonArray))
			return filterKey;
		return jsonArray.getJSONObject(0).getString("key");
	}
}
