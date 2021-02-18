package com.cssrc.ibms.bus.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.bus.dao.BusQuerySettingDao;
import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.bus.model.BusQuerySetting;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldShow;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:BusQuerySettingService</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:14:25
 */
@Service
public class BusQuerySettingService extends BaseService<BusQuerySetting> {

	@Resource
	private BusQuerySettingDao dao;

	@Resource
	private BusQueryRuleService busQueryRuleService;

	protected IEntityDao<BusQuerySetting, Long> getEntityDao() {
		return this.dao;
	}

	public BusQuerySetting getByTableNameUserId(String tableName, Long userId) {
		return this.dao.getByTableNameUserId(tableName, userId);
	}

	public BusQuerySetting getShowList(String tableName, Long userId) {
		BusQueryRule busQueryRule = this.busQueryRuleService
				.getByTableName(tableName);

		Map rightMap = QueryUtil.getRightMap();

		BusQuerySetting busQuerySetting = this.dao.getByTableNameUserId(
				tableName, userId);

		Map permission = QueryUtil.getPermission(busQueryRule, rightMap);

		if (BeanUtils.isEmpty(busQuerySetting)) {
			busQuerySetting = new BusQuerySetting();
			busQuerySetting.setTableName(tableName);
			busQuerySetting.setFieldShowList(getFieldShowList(permission,
					busQueryRule, ""));
		} else {
			busQuerySetting.setFieldShowList(getFieldShowList(permission,
					busQueryRule, busQuerySetting.getDisplayField()));
		}
		return busQuerySetting;
	}

	private List<FieldShow> getFieldShowList(Map<String, Boolean> permission,
			BusQueryRule busQueryRule, String displayField) {
		Map map = getDisplayFieldShow(displayField);
		Map descMap = getDisplayFieldDesc(busQueryRule.getDisplayField());
		List fieldShowList = new ArrayList();
		for (Iterator it = permission.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			String key = (String) e.getKey();
			boolean val = ((Boolean) e.getValue()).booleanValue();
			if (!val)
				continue;
			FieldShow fieldShow = new FieldShow();
			fieldShow.setName(key);
			int show = 0;
			if (BeanUtils.isNotEmpty(map))
				show = ((Boolean) map.get(key)).booleanValue() ? 0 : BeanUtils
						.isEmpty(map.get(key)) ? 0 : 1;
			fieldShow.setShow(show);
			fieldShow.setDesc((String) descMap.get(key));
			fieldShowList.add(fieldShow);
		}
		return fieldShowList;
	}

	private Map<String, String> getDisplayFieldDesc(String displayField) {
		Map map = new HashMap();
		if (StringUtil.isEmpty(displayField))
			return map;
		JSONArray jsonAry = JSONArray.fromObject(displayField);

		for (Iterator localIterator = jsonAry.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("variable");
			String desc = (String) json.get("desc");
			map.put(name, desc);
		}
		return map;
	}

	private Map<String, Boolean> getDisplayFieldShow(String displayField) {
		Map map = new HashMap();
		if (StringUtil.isEmpty(displayField)) {
			return null;
		}
		JSONArray jsonAry = JSONArray.fromObject(displayField);

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
}
