package com.cssrc.ibms.core.form.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.dao.FormDefTreeDao;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormDefTree;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class FormDefTreeService extends BaseService<FormDefTree> {

	@Resource
	private FormDefTreeDao dao;

	@Resource
	private IFormFieldService formFieldService;

	@Resource
	private IFormTableService formTableService;

	@Resource
	private IFormDefService formDefService;

	protected IEntityDao<FormDefTree, Long> getEntityDao() {
		return this.dao;
	}

	public void save(FormDefTree formDefTree) {
		Long id = formDefTree.getId();
		if ((id == null) || (id.longValue() == 0L)) {
			id = Long.valueOf(UniqueIdUtil.genId());
			formDefTree.setId(id);
			add(formDefTree);
		} else {
			update(formDefTree);
		}
	}

	public FormDefTree getByFormKey(Long formKey) {
		return this.dao.getByFormKey(formKey);
	}

	public List<JSONObject> treeListJson(Long formKey,
			Map<String, Object> params) throws Exception {
		FormDefTree formDefTree = getByFormKey(formKey);
		IFormDef bpmFormDef = formDefService
				.getDefaultPublishedByFormKey(Long.valueOf(formKey));
		IFormTable bpmFormTable = formTableService.getTableById(bpmFormDef
				.getTableId());

		String sql = getSql(formDefTree, bpmFormTable, params);

		JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getNewJdbcTemplate(bpmFormTable.getDsAlias());
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

		List jsonObjectList = new ArrayList();
		for (Map map : mapList) {
			JSONObject jo = getJsonByMap(map);
			jsonObjectList.add(jo);
		}
		return jsonObjectList;
	}

	private JSONObject getJsonByMap(Map<String, Object> map) {
		JSONObject jo = new JSONObject();
		for (String key : map.keySet()) {
			Object val = map.get(key) == null ? "" : map.get(key);

			if (key.equals("PARENTID")) {
				jo.put("parentId", val);
			} else if (key.equals("ID")) {
				jo.put("id", val);
			} else if (key.equals("NAME")) {
				jo.put("name", val);
			} else if (key.equals("ISPARENT")) {
				jo.put("isParent", val);
			} else
				jo.put(key, val);
		}
		return jo;
	}

	private String getSql(FormDefTree formDefTree, IFormTable bpmFormTable,
			Map<String, Object> params) {
		String treeIdField = getFieldNameByFieldId(formDefTree.getTreeId(),
				bpmFormTable.isExtTable());
		String parentIdField = getFieldNameByFieldId(formDefTree.getParentId(),
				bpmFormTable.isExtTable());
		String displayField = getFieldNameByFieldId(formDefTree
				.getDisplayField(), bpmFormTable.isExtTable());
		StringBuffer sb = new StringBuffer();

		sb.append("select " + treeIdField + " id ");
		sb.append("," + parentIdField + " parentId ");
		sb.append("," + displayField + " name ");

		if (formDefTree.getLoadType().equals(FormDefTree.LOADTYPE_ASYNC)) {
			StringBuffer isParentSql = new StringBuffer();
			isParentSql.append("( case (select count(*)  from "
					+ bpmFormTable.getDbTableName() + " p ");
			isParentSql.append("where p." + parentIdField + " = o."
					+ treeIdField + " and p." + treeIdField + " != p."
					+ parentIdField + ")");
			isParentSql
					.append("when 0 then 'false' else 'true' end )isParent ");
			sb.append("," + isParentSql);
		}

		sb.append("from " + bpmFormTable.getDbTableName() + " o ");

		if (formDefTree.getLoadType().equals(FormDefTree.LOADTYPE_ASYNC)) {
			String id = params.get("id").toString();
			if (StringUtil.isNotEmpty(id))
				sb.append("where " + parentIdField + " = " + id);
			else {
				sb.append("where " + parentIdField + " = "
						+ formDefTree.getRootId());
			}
		}
		return sb.toString();
	}

	private String getFieldNameByFieldId(String fid, boolean isExtTable) {
		if (fid.toString().equals("ID")) {
			return "ID";
		}
		if (isExtTable) {
			return fid;
		}
		return TableModel.CUSTOMER_COLUMN_PREFIX + fid;
	}
}