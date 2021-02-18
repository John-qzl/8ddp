package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IQueryFieldService;
import com.cssrc.ibms.api.form.model.IQueryField;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.util.JdbcHelperUtil;
import com.cssrc.ibms.core.form.dao.QueryFieldDao;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class QueryFieldService extends BaseService<QueryField> implements
		IQueryFieldService {

	@Resource
	private QueryFieldDao dao;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private QuerySettingService sysQuerySettingService;

	protected IEntityDao<QueryField, Long> getEntityDao() {
		return this.dao;
	}

	public List<QueryField> getSysQueryFieldArr(String jsonArr) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));
		if (StringUtil.isEmpty(jsonArr))
			return null;
		List sysQueryFieldList = new ArrayList();
		new JSONArray();
		JSONArray jsonArray = JSONArray.fromObject(jsonArr);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			QueryField sysQueryField = (QueryField) JSONObject.toBean(obj,
					QueryField.class);
			sysQueryField.setControlType((short)obj.getInt("ct"));
			sysQueryFieldList.add(sysQueryField);
		}

		return sysQueryFieldList;
	}

	public List<QueryField> getListBySqlId(Long sqlId) {
		List list = this.dao.getListBySqlKey("getListBySqlId", sqlId);
		if ((list != null) && (list.size() > 0))
			return list;
		return null;
	}

	public void saveOrUpdate(List<QueryField> sysQueryFields) throws Exception {
		if ((sysQueryFields == null) || (sysQueryFields.size() == 0))
			return;
		for (QueryField sysQueryField : sysQueryFields)
			if ((sysQueryField.getId() == null)
					|| (sysQueryField.getId().longValue() == 0L)) {
				sysQueryField.setId(Long.valueOf(UniqueIdUtil.genId()));
				add(sysQueryField);
			} else {
				updateWithoutControlAndFormat(sysQueryField);
			}
	}

	public List<QueryField> getDisplayFieldListBySqlId(Long sqlId) {
		List list = this.dao.getDisplayFieldListBySqlId(sqlId);
		if ((list != null) && (list.size() > 0))
			return list;
		return null;
	}

	public List<QueryField> getConditionFieldListBySqlId(Long sqlId) {
		List list = this.dao.getConditionFieldListBySqlId(sqlId);
		if ((list != null) && (list.size() > 0))
			return list;
		return null;
	}

	public List<QueryField> getSqlField(Long sqlId, String dsname,
			String sysQuerySql) throws Exception {
		if (StringUtil.isEmpty(sysQuerySql))
			return null;
		List fields = new ArrayList();
		SqlRowSet srs = this.jdbcTemplate.queryForRowSet(sysQuerySql);
		SqlRowSetMetaData srsmd = srs.getMetaData();
		for (int i = 1; i < srsmd.getColumnCount() + 1; i++) {
			String cn = srsmd.getColumnName(i);
			String ctn = srsmd.getColumnTypeName(i);
			if(ctn.toUpperCase().equals("TIMESTAMP")){
				ctn = "DATE";
			}
			if(ctn.toUpperCase().equals("DECIMAL")||ctn.toUpperCase().equals("BIGINT")){
				ctn = "NUMBER";
			}
			QueryField field = new QueryField();
			field.setId(Long.valueOf(UniqueIdUtil.genId()));
			field.setSqlId(sqlId);
			field.setName(cn);
			field.setFieldDesc(cn);
			field.setType(ctn);
			field.setIsShow(QueryField.IS_SHOW);
			field.setIsSearch(QueryField.IS_NOT_SEARCH);
			field.setControlType(Short.valueOf((short) 1));
			fields.add(field);
		}
		return fields;
	}

	public int updateWithoutControlAndFormat(QueryField sysQueryField) {
		return this.dao.update("updateWithoutControlAndFormat", sysQueryField);
	}

	public void syncConditionControlAndField(QueryField sysQueryField,
			QuerySetting sysQuerySetting) {
		JSONArray jsonArray = new JSONArray();
		if ((sysQuerySetting.getConditionField() != null)
				&& (!sysQuerySetting.getConditionField().equals("null"))
				&& (!sysQuerySetting.getConditionField().equals(""))) {
			jsonArray = JSONArray.fromObject(sysQuerySetting
					.getConditionField());
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				//控件类型、格式修改后，需要同步到设置表中
				boolean ct_needSync = jsonObject.getInt("ct") != sysQueryField.getControlType().shortValue();
				boolean ftm_needSync = false;
				if(jsonObject.get("ftm")!=null){
					ftm_needSync = jsonObject.getString("ftm") != sysQueryField.getFormat();
				}
				boolean needSync = ct_needSync||ftm_needSync;
				if ((jsonObject.getLong("id") == sysQueryField.getId().longValue())
						&& (sysQueryField.getControlType() != null)
						&&needSync) {
					jsonObject.element("ct", sysQueryField.getControlType());
					jsonObject.element("ftm", sysQueryField.getFormat());
					jsonArray.set(i, jsonObject);
					sysQuerySetting.setConditionField(jsonArray.toString());
					this.sysQuerySettingService.update(sysQuerySetting);
					break;
				}
			}
		}
	}

	public void delBySqlIds(Long[] lAryId) {
		if ((lAryId != null) && (lAryId.length > 0))
			for (Long sqlId : lAryId)
				this.dao.delBySqlId(sqlId);
	}

}
