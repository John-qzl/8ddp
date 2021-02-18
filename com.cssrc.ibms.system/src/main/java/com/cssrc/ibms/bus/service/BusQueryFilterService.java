package com.cssrc.ibms.bus.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.bus.dao.BusQueryFilterDao;
import com.cssrc.ibms.bus.dao.BusQueryRuleDao;
import com.cssrc.ibms.bus.dao.BusQueryShareDao;
import com.cssrc.ibms.bus.model.BusQueryFilter;
import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.bus.model.BusQueryShare;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.scan.SearchCache;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableEntity;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableField;
import com.cssrc.ibms.core.util.bean.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:BusQueryFilterService</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:14:00
 */
@Service
public class BusQueryFilterService extends BaseService<BusQueryFilter> {

	@Resource
	private BusQueryFilterDao dao;

	@Resource
	private BusQueryRuleDao busQueryRuleDao;

	@Resource
	private BusQueryShareDao busQueryShareDao;

	protected IEntityDao<BusQueryFilter, Long> getEntityDao() {
		return this.dao;
	}

	public void saveFilter(BusQueryFilter busQueryFilter,
			Map<String, Object> queryMap, String sortParameter) {
		TableEntity tableEntity = (TableEntity) SearchCache.getTableEntityMap()
				.get(busQueryFilter.getTableName());
		BusQueryRule busQueryRule = this.busQueryRuleDao
				.getByTableName(busQueryFilter.getTableName());
		busQueryFilter.setId(Long.valueOf(UniqueIdUtil.genId()));
		busQueryFilter.setRuleId(busQueryRule.getId());
		busQueryFilter.setQueryParameter(getQueryParameter(tableEntity,
				queryMap));
		busQueryFilter.setSortParameter(sortParameter);
		this.dao.add(busQueryFilter);
	}

	private String getQueryParameter(TableEntity tableEntity,
			Map<String, Object> map) {
		if (BeanUtils.isEmpty(map))
			return null;
		JSONArray jsonAry = new JSONArray();

		Map fieldMap = getFieldMap(tableEntity.getTableFieldList());

		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			String key = (String) e.getKey();
			String paraName = key.substring(2, key.lastIndexOf("_"));
			String type = key.substring(key.lastIndexOf("_") + 1);
			Object value = e.getValue();
			JSONObject json = new JSONObject();
			json.accumulate("key", key);
			json.accumulate("paraName", paraName);
			json.accumulate("type", type);
			json.accumulate("value", value);
			json.accumulate("desc", fieldMap.get(paraName.toLowerCase()));
			jsonAry.add(json);
		}
		return jsonAry.toString();
	}

	private Map<String, String> getFieldMap(List<TableField> tableFieldList) {
		Map map = new HashMap();
		for (TableField tableField : tableFieldList) {
			map.put(tableField.getVar().toLowerCase(), tableField.getDesc());
		}
		return map;
	}

	public List<BusQueryFilter> getMyFilterList(String tableName, Long userId) {
		return this.dao.getMyFilterList(tableName, userId);
	}

	public List<BusQueryFilter> getShareFilterList(String tableName, Long userId) {
		List<BusQueryFilter> list1 = this.dao.getShareFilterList(tableName,
				userId);
		Map rightMap = QueryUtil.getRightMap();
		if (BeanUtils.isEmpty(rightMap))
			return list1;
		List list = new ArrayList();
		for (BusQueryFilter busQueryFilter : list1) {
			BusQueryShare busQueryShare = this.busQueryShareDao
					.getByFilterId(busQueryFilter.getId());
			if (BeanUtils.isEmpty(busQueryShare))
				continue;
			JSONObject permission = JSONObject.fromObject(busQueryShare
					.getShareRight());
			if (QueryUtil.hasRight(permission, rightMap)) {
				list.add(busQueryFilter);
			}
		}
		return list;
	}
}
