package com.cssrc.ibms.bus.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.bus.dao.BusQueryRuleDao;
import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableEntity;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableField;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:BusQueryRuleService</p>
 * @author Yangbo 
 * @date 2016-8-8下午03:25:28
 */
@Service
public class BusQueryRuleService extends BaseService<BusQueryRule> {

	@Resource
	private BusQueryRuleDao dao;

	protected IEntityDao<BusQueryRule, Long> getEntityDao() {
		return this.dao;
	}

	public BusQueryRule getByTableName(String tableName) {
		return this.dao.getByTableName(tableName);
	}

	public Integer getCountByTableName(String tableName) {
		return this.dao.getCountByTableName(tableName);
	}

	public BusQueryRule getByTableEntity(TableEntity tableEntity) {
		String tableName = tableEntity.getTableName();
		BusQueryRule busQueryRule = this.dao.getByTableName(tableName);
		if (BeanUtils.isEmpty(busQueryRule)) {
			busQueryRule = new BusQueryRule();
			busQueryRule.setTableName(tableName);
			busQueryRule.setDisplayField(getDisplayField(tableEntity, ""));
			busQueryRule.setExportField(getExportField(tableEntity, ""));
		} else {
			busQueryRule.setDisplayField(getDisplayField(tableEntity,
					busQueryRule.getDisplayField()));
			busQueryRule.setExportField(getExportField(tableEntity,
					busQueryRule.getExportField()));
		}
		return busQueryRule;
	}

	private String getDisplayField(TableEntity tableEntity, String displayField) {
		Map<String, String> map = getDisplayFieldRight(displayField);
		Map<String, String> descMap = getDisplayFieldDesc(displayField);
		if (BeanUtils.isEmpty(tableEntity))
			return displayField;
		List<TableField> fieldList = tableEntity.getTableFieldList();
		JSONArray jsonAry = new JSONArray();
		for (TableField tableField : fieldList) {
			if (tableField.getDataType().equals("clob"))
				continue;
			JSONObject json = new JSONObject();
			json.accumulate("name", tableField.getName());
			json.accumulate("variable", tableField.getVar());
			String desc = tableField.getDesc();
			if (BeanUtils.isNotEmpty(map))
				desc = (String) descMap.get(tableField.getName());
			json.accumulate("desc", desc);
			json.accumulate("type", tableField.getFieldType());
			json.accumulate("style", "");
			String right = "";
			if (BeanUtils.isNotEmpty(map))
				right = (String) map.get(tableField.getName());
			if (StringUtils.isEmpty(right)) {
				right = getDefaultDisplayFieldRight();
			}
			json.accumulate("right", right);
			jsonAry.add(json);
		}
		displayField = jsonAry.toString();

		return displayField;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> getDisplayFieldDesc(String displayField) {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);

		for (Iterator localIterator = jsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			String desc = (String) json.get("desc");
			map.put(name, desc);
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> getDisplayFieldRight(String displayField) {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);

		for (Iterator localIterator = jsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			JSONArray right = (JSONArray) json.get("right");
			map.put(name, right.toString());
		}
		return map;
	}

	private String getDefaultDisplayFieldRight() {
		JSONArray jsonAry = new JSONArray();
		for (int i = 0; i < 2; i++) {
			JSONObject json = new JSONObject();
			json.accumulate("s", i);
			json.accumulate("type", "none");
			json.accumulate("id", "");
			json.accumulate("name", "");
			json.accumulate("script", "");
			jsonAry.add(json);
		}
		return jsonAry.toString();
	}

	private String getExportField(TableEntity tableEntity, String field) {
		Map<String, String> map = getExportFieldRight(field);
		Map<String, String> descMap = getExportFieldDesc(field);
		if (BeanUtils.isEmpty(tableEntity)) {
			return field;
		}
		JSONArray jsonAry = new JSONArray();

		List<TableField> fieldList = tableEntity.getTableFieldList();
		jsonAry.add(getTableField(tableEntity, fieldList, map, descMap));
		List<TableEntity> subTableList = tableEntity.getSubTableList();
		for (TableEntity subTable : subTableList) {
			jsonAry.add(getTableField(subTable, subTable.getTableFieldList(),
					map, descMap));
		}

		return jsonAry.toString();
	}

	private JSONObject getTableField(TableEntity tableEntity,
			List<TableField> fieldList, Map<String, String> map,
			Map<String, String> descMap) {
		JSONObject tableJson = new JSONObject();
		tableJson.element("tableName", tableEntity.getTableName());
		tableJson.element("tableDesc", tableEntity.getComment());
		tableJson.element("isMain", 1);
		JSONArray jsonAry = new JSONArray();
		for (TableField tableField : fieldList) {
			JSONObject json = new JSONObject();
			String key = tableField.getName() + tableField.getName();
			json.element("tableName", tableField.getName());
			json.element("isMain", 1);
			json.element("name", tableField.getName());
			String desc = tableField.getDesc();
			if (BeanUtils.isNotEmpty(map))
				desc = (String) descMap.get(key);
			json.element("desc", desc);
			json.element("type", tableField.getFieldType());
			json.element("style", "");
			String right = "";
			if (BeanUtils.isNotEmpty(map))
				right = (String) map.get(key);
			if (StringUtils.isEmpty(right))
				right = getDefaultExportFieldRight();
			json.element("right", right);
			jsonAry.add(json);
		}
		tableJson.element("fields", jsonAry.toArray());
		return tableJson;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> getExportFieldDesc(String field) {
		if (StringUtil.isEmpty(field))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(field);
		for (Iterator localIterator1 = jsonAry.iterator(); localIterator1
				.hasNext();) {
			Object obj = localIterator1.next();
			JSONObject json = JSONObject.fromObject(obj);
			JSONArray fields = (JSONArray) json.get("fields");
			for (Iterator localIterator2 = fields.iterator(); localIterator2
					.hasNext();) {
				Object obj1 = localIterator2.next();
				JSONObject json1 = JSONObject.fromObject(obj1);
				String name = (String) json1.get("name");
				String tableName = (String) json1.get("tableName");
				String desc = (String) json1.get("desc");
				map.put(tableName + name, desc);
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, String> getExportFieldRight(String field) {
		if (StringUtil.isEmpty(field))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(field);

		for (Iterator localIterator1 = jsonAry.iterator(); localIterator1
				.hasNext();) {
			Object obj = localIterator1.next();
			JSONObject json = JSONObject.fromObject(obj);
			JSONArray fields = (JSONArray) json.get("fields");
			for (Iterator localIterator2 = fields.iterator(); localIterator2
					.hasNext();) {
				Object obj1 = localIterator2.next();
				JSONObject json1 = JSONObject.fromObject(obj1);
				String name = (String) json1.get("name");
				String tableName = (String) json1.get("tableName");
				JSONArray right = (JSONArray) json1.get("right");
				map.put(tableName + name, right.toString());
			}
		}
		return map;
	}

	private String getDefaultExportFieldRight() {
		JSONArray jsonAry = new JSONArray();
		JSONObject json = new JSONObject();
		json.accumulate("s", 2);
		json.accumulate("type", "none");
		json.accumulate("id", "");
		json.accumulate("name", "");
		json.accumulate("script", "");
		jsonAry.add(json);
		return jsonAry.toString();
	}
}
