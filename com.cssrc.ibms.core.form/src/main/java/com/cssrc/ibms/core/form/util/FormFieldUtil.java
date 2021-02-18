package com.cssrc.ibms.core.form.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.TeamModel;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

public class FormFieldUtil {
	/**
	 * 移除关联关系的现实字段，关联关系显示字段已经在控件中存在了
	 * @param table
	 * @param fields
	 */
	public static void removeRelField(FormTable table, List<FormField> fields) {
		List<String> relation = new ArrayList<String>();
		for (FormField f : fields) {
			String rel = f.getRelFormDialogStripCData();
			if (StringUtil.isNotEmpty(rel)) {
				relation.add(rel);
			}
		}
		for (Iterator<FormField> it = fields.iterator(); it.hasNext();) {
			FormField field = it.next();
			if (field.getControlType() == IFieldPool.RELATION_COLUMN_CONTROL) {
				continue;
			}
			for (String rel : relation) {
				JSONObject _rel = JSONObject.fromObject(rel);
				JSONArray relfs = _rel.getJSONArray("fields");
				for (Object relf : relfs) {
					JSONObject _f = (JSONObject) relf;
					try {
						boolean isShow = _f.getBoolean(IFieldPool.FK_LISTSHOW);
						if (isShow
								&& _f.getString(IFieldPool.FK_TABLEFIELD)
										.equals(field.getFieldName())) {
							it.remove();
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}

				}
				break;
			}
		}
	}
	/**
	 * 设置分组字段
	 * 
	 * @param fieldsMap
	 * @param table
	 * @param fields
	 * @param fieldList
	 * @return
	 */
	public static List<TeamModel> setTeamFields(Map<String, Object> fieldsMap,
			FormTable table, List<FormField> fields, String nodeType) {
		// 判断表是否有分组
		if (StringUtil.isEmpty(table.getTeam()))
			return null;
		List<TeamModel> list = new ArrayList<TeamModel>();
		JSONObject json = JSONObject.fromObject(table.getTeam());
		fieldsMap.put("isShow", json.get("isShow"));
		fieldsMap.put("showPosition", json.get("showPosition"));

		JSONArray teamJson = JSONArray.fromObject(json.get("team"));
		for (Object obj : teamJson) {
			TeamModel teamModel = new TeamModel();
			JSONObject jsonObj = (JSONObject) obj;
			String teamName = jsonObj.get("teamName").toString();
			String teamNameKey = jsonObj.get("teamNameKey").toString();
			teamModel.setTeamName(teamName);
			teamModel.setTeamNameKey(teamNameKey);
			// 获取字段
			JSONArray jArray = (JSONArray) jsonObj.get("teamField");
			List<FormField> teamFields = new ArrayList<FormField>();
			for (Object object : jArray) {
				JSONObject fieldObj = (JSONObject) object;
				String fieldName = (String) fieldObj.get("fieldName");
				FormField bpmFormField = getTeamField(fields, fieldName);
				if (BeanUtils.isNotEmpty(bpmFormField)) {
					fields.remove(bpmFormField);
					teamFields.add(bpmFormField);
				}
			}
			setFormFieldName(table, teamFields, nodeType);
			teamModel.setTeamFields(teamFields);
			list.add(teamModel);
		}
		fieldsMap.put("teamFields", list);
		return list;
	}

	/**
	 * 设置字段名字【关系表字段前缀（r），主表字段前缀（m），子表字段前缀（s）】:【表名】:【字段名】
	 * 
	 * @param table
	 * @param fields
	 */
	public static void setFormFieldName(FormTable table, List<FormField> fields,
			String nodeType) {
		for (FormField field : fields) {
			field.setFormTable(table);
			field.setFieldName(setFormFieldName(table.getIsMain(), nodeType)
					+ table.getTableName() + ":" + field.getFieldName());
		}

	}

	// 取得字段前缀 字段前缀：关系表字段前缀（r:），主表字段前缀（m:），子表字段前缀（s:）
	private static String setFormFieldName(Short isMain, String nodeType) {
		String extStr = "";
		if (StringUtils.isNotEmpty(nodeType) && nodeType.equals("rel")) {
			extStr = "r:";
		} else {
			extStr = (isMain.shortValue() == FormTable.IS_MAIN.shortValue()) ? "m:"
					: "s:";
		}

		return extStr;
	}

	/**
	 * 获取分组字段
	 * 
	 * @param fields
	 * @param fieldName
	 * @return
	 */
	private static  FormField getTeamField(List<FormField> fields, String fieldName) {
		for (FormField bpmFormField : fields) {
			if (bpmFormField.getFieldName().equals(fieldName))
				return bpmFormField;
		}
		return null;
	}
}
