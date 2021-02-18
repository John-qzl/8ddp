package com.cssrc.ibms.core.form.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * Description:将原先DataTemplateController、DataTemplateService中的部分方法移至此处
 * <p>
 * DataTemplateUtil.java
 * </p>
 * 
 * @author dengwenjie
 * @date 2017年7月29日
 */
public class DataTemplateUtil {
	
	
/**
 * 获取显示列-固定列
 * @param dataTemplate
 * @return
 */
public static JSONArray getFixFields(DataTemplate dataTemplate,Map<String,Boolean> map){
	JSONArray arr = new JSONArray();
	String displayFields_str = dataTemplate.getDisplayField();
	JSONArray dis_arr = JSONArray.fromObject(displayFields_str);
	for(Iterator it = dis_arr.iterator();it.hasNext();){
		JSONObject obj = new JSONObject();
		JSONObject field = JSONObject.fromObject(it.next());
		String name = field.getString("name");
		String desc = field.getString("desc");
		boolean isFix =  field.getBoolean("isFix");
		boolean right = map.containsKey(name)&&map.get(name);
		if(isFix&&right){
			obj.put("name", name);
			obj.put("desc", desc);
			arr.add(obj);
		}		
	}
	return arr;
}
	
		/**
		 * 获取显示字段json串
		 * @param bpmFormTable
		 * @param displayField：可为空，为空则取默认的
		 * @return 
		 */
		public static String getDisplayField(IFormTable bpmFormTable,
				String displayField) {
			Map<String, String> map = getDisplayFieldRight(displayField);
			Map<String, String> descMap = getDisplayFieldDesc(displayField);
			Map<String, JSONObject> extMap = getDisplayFieldExt(displayField);
			if (BeanUtils.isNotEmpty(bpmFormTable)) {
				List<IFormField> fieldList = (List<IFormField>)bpmFormTable.getFieldList();
				JSONArray jsonAry = new JSONArray();
				for (IFormField bpmFormField : fieldList) {
					JSONObject json = new JSONObject();
					json.accumulate("name", bpmFormField.getFieldName());
					String desc = bpmFormField.getFieldDesc();
					if ((BeanUtils.isNotEmpty(map))
							&& (map.containsKey(bpmFormField.getFieldName()))) {
						desc = (String) descMap.get(bpmFormField.getFieldName());
					}
					json.accumulate("desc", desc);
					json.accumulate("type", bpmFormField.getFieldType());
					json.accumulate("style", bpmFormField.getDatefmt());
					// 增加控制类型
					json.accumulate("controltype", bpmFormField.getControlType());
					String right = "";
					if (BeanUtils.isNotEmpty(map))
						right = map.get(bpmFormField.getFieldName());
					if (StringUtils.isEmpty(right))
						right = getDefaultDisplayFieldRight();

					json.accumulate("right", right);

					if (BeanUtils.isNotEmpty(extMap)) {
						JSONObject obj = extMap.get(bpmFormField.getFieldName());
						if (obj != null && obj.containsKey("displayType")
								&& obj.containsKey("action")
								&& obj.containsKey("onclick")
								&& obj.containsKey("urlParams")
								&& obj.containsKey("isFix")) {
							json.accumulate("displayType", obj.get("displayType"));
							json.accumulate("action", obj.get("action"));
							json.accumulate("onclick", obj.get("onclick"));
							json.accumulate("urlParams", obj.get("urlParams"));
							json.accumulate("isFix", obj.get("isFix"));
						} else {
							json.accumulate("displayType", "simple");
							json.accumulate("action", "");
							json.accumulate("onclick", "");
							json.accumulate("urlParams", "");
							json.accumulate("isFix", "");
						}
					}
					jsonAry.add(json);
				}

				// 显示列排序
				JSONArray sortArray = new JSONArray();
				if (BeanUtils.isEmpty(displayField)) {
					displayField = "[]";
				}
				JSONArray array = JSONArray.fromObject(displayField);
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = JSONObject.fromObject(array.get(i));
					for (int j = 0; j < jsonAry.size(); j++) {
						JSONObject json = (JSONObject) jsonAry.get(j);
						if (json.get("name").equals(obj.get("name"))) {
							sortArray.add(json);
							break;
						}
					}
				}
				// 新增字段，放置在后面
				jsonAry.removeAll(sortArray);
				sortArray.addAll(jsonAry);
				displayField = sortArray.toString();
			}
			return displayField;
		}
	/**
	 * 
	 * @param bpmFormTable
	 * @param field：可为空，，为空则取生成默认的
	 * @return:导出字段json串
	 */
	public static String getExportField(IFormTable bpmFormTable, String field) {
		Map<String, String> map = getExportFieldRight(field);
		Map<String, String> descMap = getExportFieldDesc(field);
		if (BeanUtils.isEmpty(bpmFormTable))
			return field;

		JSONArray jsonAry = new JSONArray();

		List<IFormField> fieldList = (List<IFormField>)bpmFormTable.getFieldList();
		jsonAry.add(getTableField(bpmFormTable, fieldList, map, descMap));
		List<IFormTable> subTableList = (List<IFormTable>)bpmFormTable.getSubTableList();
		for (IFormTable subTable : subTableList) {
			jsonAry.add(getTableField(subTable, (List<IFormField>)subTable.getFieldList(), map,
					descMap));
		}

		return jsonAry.toString();
	}
	private static String getDefaultDisplayFieldRight() {
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

	private static String getDefaultExportFieldRight() {
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

	private  static Map<String, String> getDisplayFieldDesc(String displayField) {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);

		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			String desc = (String) json.get("desc");
			map.put(name, desc);
		}
		return map;
	}

	private  static Map<String, String> getDisplayFieldRight(String displayField) {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);

		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			JSONArray right = (JSONArray) json.get("right");
			map.put(name, right.toString());
		}
		return map;
	}

	private  static Map<String, JSONObject> getDisplayFieldExt(
			String displayField) {
		if (StringUtil.isEmpty(displayField))
			return null;
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		JSONArray jsonAry = JSONArray.fromObject(displayField);

		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");

			map.put(name, json);
		}
		return map;
	}
	private static Map<String, String> getExportFieldDesc(String field) {
		if (StringUtil.isEmpty(field))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(field);
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			JSONArray fields = (JSONArray) json.get("fields");
			for (Object obj1 : fields) {
				JSONObject json1 = JSONObject.fromObject(obj1);
				String name = (String) json1.get("name");
				String tableName = (String) json1.get("tableName");
				String desc = (String) json1.get("desc");
				map.put(tableName + name, desc);
			}
		}
		return map;
	}

	private static Map<String, String> getExportFieldRight(String field) {
		if (StringUtil.isEmpty(field))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		JSONArray jsonAry = JSONArray.fromObject(field);

		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			JSONArray fields = (JSONArray) json.get("fields");
			for (Object obj1 : fields) {
				JSONObject json1 = JSONObject.fromObject(obj1);
				String name = (String) json1.get("name");
				String tableName = (String) json1.get("tableName");
				JSONArray right = (JSONArray) json1.get("right");
				map.put(tableName + name, right.toString());
			}
		}
		return map;
	}

	private static JSONObject getTableField(IFormTable bpmFormTable,
			List<IFormField> fieldList, Map<String, String> map,
			Map<String, String> descMap) {
		JSONObject tableJson = new JSONObject();
		tableJson.element("tableName", bpmFormTable.getTableName());
		tableJson.element("tableDesc", bpmFormTable.getTableDesc());
		tableJson.element("isMain", bpmFormTable.getIsMain());
		JSONArray jsonAry = new JSONArray();
		for (IFormField bpmFormField : fieldList) {
			JSONObject json = new JSONObject();
			String key = bpmFormTable.getTableName()
					+ bpmFormField.getFieldName();
			json.element("tableName", bpmFormTable.getTableName());
			json.element("isMain", bpmFormTable.getIsMain());
			json.element("name", bpmFormField.getFieldName());
			String desc = bpmFormField.getFieldDesc();
			if (BeanUtils.isNotEmpty(map))
				desc = descMap.get(key);
			json.element("desc", desc);
			json.element("type", bpmFormField.getFieldType());
			json.element("style", bpmFormField.getDatefmt());
			String right = "";
			if (BeanUtils.isNotEmpty(map))
				right = map.get(key);
			if (StringUtils.isEmpty(right))
				right = getDefaultExportFieldRight();
			json.element("right", right);
			jsonAry.add(json);
		}
		tableJson.element("fields", jsonAry.toArray());
		return tableJson;

	}
}
